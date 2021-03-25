
import Vue from 'vue'
import SockJS from 'sockjs-client'
import globalBus from '@/event-bus'

// 클라이언트를 구현하는데 SockJS를 활용하고, 서버 쪽에서는 스프링의 웹소켓 구현체를 이용한다.
// 내부적으로 SockJS 클라이언트는 브라우저가 제공하는 네이티브 웹소켓을 사용 (차선으로 XHR-Streaming, XHR-Polling과 같은 다른 전송 프로토콜 사용)


// 애플리케이션에서 여러 사용자가 같은 보드를 열었을 때, 보드에서 발생하는 모든 일에 대해 새로고침 없이 자동 업데이트 필요
// 이를 위해 발행 / 구독 패턴 방식의 통신

// 새로운 카드 추가 외 다양한 종류의 메시지가 있을 수 있어, 메세지 별로 구독하면, 중복 코드 다수 발생
// 그렇기 때문에 보드(채널)에 접근한 사용자는 보드(채널)에 관련된 모든 메시지를 받도록 한다.

// 특정 메시지 채널을 생성하고, 다양한 메시지를 이 채널에 전송한다.
// 이 채널을 구독한 클라이언트는 채널에 관련된 메시지를 받는다. (채널은 예를 들어 /board/1 형태의 보드 채널, 여기서 1은 보드의 ID)

// 보드 채널을 사용하면, 클라이언트가 보드를 열때 메시지를 보내, 보드를 여는 동안 보드 채널을 구독하고, 
// 보드를 닫으면 메세지를 서버에 전송하여 구독을 해지한다


class RealTimeClient {
  constructor () {
    this.serverUrl = null                 // 웹소켓 서버의 URL
    this.token = null                     // 실시간 토큰, 클라이언트가 서버 쪽에서 인증을 수행하는데 사용한다. (JWT 토큰 문자열)
    this.socket = null                    // SockJS의 인스턴스 , connect 메서드에서 생성
    this.authenticated = false            // 클라이언트가 서버에서 인증되었는지 여부
    this.loggedOut = false                // 사용자 로그아웃 및 실시간 클라이언트 종료 여부
    this.$bus = new Vue()                 // $bus 프로퍼티, 싫시간 클라이언트의 내부 이벤트 버스 역할


    // 아래 두 큐는 각각 구독하기 / 구독 해제하기 액션을 가진다 
    // 서버와 연결 되면, 큐에 있는 모든 구독 / 구독 해제 액션을 수행하고, 큐를 비운다.
    this.subscribeQueue = {
      /* channel: [handler1, handler2] */
    }
    this.unsubscribeQueue = {
      /* channel: [handler1, handler2] */
    }
  }
  // 웹소켓 초기화 / 인증 상태이면 리턴 / 그렇지 않으면 웹소켓 서버, 토큰 초기화 및 웹소켓 초기화
  // 이 init 메서드를 통한 소켓 초기화는 App.vue에서 수행한다.
  init (serverUrl, token) {
    if (this.authenticated) {
      console.warn('[RealTimeClient] WS connection already authenticated.')
      return
    }
    console.log('[RealTimeClient] Initializing')
    this.serverUrl = serverUrl
    this.token = token
    this.connect()
  }

  // 웹소켓 생성 및 이벤트 핸들러 등록
  connect () {
    console.log('[RealTimeClient] Connecting to ' + this.serverUrl)

    // SockJS의 인스턴스 (소켓) 생성하여, 연결을 수립한다.
    this.socket = new SockJS(this.serverUrl + '?token=' + this.token)
    
    // 아래와 같이 연결시, 메시지 받을 때, 에러 발생시, 연결 종료시 등 4개의 이벤트 핸들러를 소켓에 등록한다.
    // 소켓 연결 성립
    this.socket.onopen = () => {
      this.authenticated = true
      this._onConnected()
    }

    // 웹소켓을 통해 메시지를 수신
    this.socket.onmessage = (event) => {
      this._onMessageReceived(event)
    }

    // 에러 발생
    this.socket.onerror = (error) => {
      this._onSocketError(error)
    }

    // 연결 종료
    this.socket.onclose = (event) => {
      this._onClosed(event)
    }
  }

  // 연결이 수립되면 실행하도록, 이벤트 핸들러에서 실행되는 메서드
  _onConnected () {
    globalBus.$emit('RealTimeClient.connected')
    console.log('[RealTimeClient] Connected')

    // Handle subscribe and unsubscribe queue
    this._processQueues()
  }

  // 채널을 구독     // 채널은 /board/1 등의 각각의 보드
  subscribe (channel, handler) {
    // 연결이 수립되어 있지 않으면, 나중에 처리하기 위해 구독 큐에, 구독 액션(채널과 이벤트 핸들러)를 추가해놓고 리턴
    if (!this._isConnected()) {
      this._addToSubscribeQueue(channel, handler)
      return
    }

    // 연결이 수립되어 있으면,
    const message = { action: 'subscribe', channel }

    // 액션을 subscribe로 하고, 채널을 담은 메시지를 서버로 보낸다.  /  서버에서는 클라이언트를 관련 채널의 구독자로 추가한다.
    this._send(message)

    // 채널 이름을 가진 이벤트 핸들러를 내부 이벤트 버스에 바인딩
    // 따라서 서버로부터 메시지를 받으면, 클라이언트는 내부 이벤트 버스의 $emit() 메서드를 호출하여, 채널의 모든 핸들러들에게 알려준다.
    this.$bus.$on(this._channelEvent(channel), handler)
    console.log('[RealTimeClient] Subscribed to channel ' + channel)
  }

  // 채널을 구독 해제
  unsubscribe (channel, handler) {

    // 이미 로그아웃 상태일 경우, 구독 해제할 필요 없이 리턴
    if (this.loggedOut) {
      return
    }

    // 연결이 수립되어 있지 않으면 나중에 처리하기 위해 구독 해제 큐에 채널과 핸들러를 추가해놓고 리턴
    if (!this._isConnected()) {
      this._addToUnsubscribeQueue(channel, handler)
      return
    }

    // 연결이 수립되어 있으면
    const message = { action: 'unsubscribe', channel 
  }
    // 메시지 발송하고, 이벤트 핸들러 제거
    this._send(message)
    this.$bus.$off(this._channelEvent(channel), handler)
    console.log('[RealTimeClient] Unsubscribed from channel ' + channel)
  }

  // 연결이 수립되어있는지 여부
  _isConnected () {
    return this.socket && this.socket.readyState === SockJS.OPEN
  }

  // 메시지가 수신되면 실행되도록, 이벤트 핸들러에서 실행되는 메서드 
  _onMessageReceived (event) {
    const message = JSON.parse(event.data)
    console.log('[RealTimeClient] Received message', message)

    if (message.channel) {
      this.$bus.$emit(this._channelEvent(message.channel), JSON.parse(message.payload))
    }
  }
  // 해당 채널 이름을 가지고, 발생시킬 이벤트의 이름을 리턴
  _channelEvent (channel) {
    return 'channel:' + channel
  }

  // 메시지를 JSON 형태로, 클라이언트에서 서버쪽으로 전송한다.
  _send (message) {
    this.socket.send(JSON.stringify(message))
  }

  // 소켓 에러 발생시, 실행되는 메서드
  _onSocketError (error) {
    console.error('[RealTimeClient] Socket error', error)
  }
  
  // 연결 종료시, 실행되는 메서드
  _onClosed (event) {
    console.log('[RealTimeClient] Received close event', event)
    if (this.loggedOut) {
      // Manually logged out, no need to reconnect
      console.log('[RealTimeClient] Logged out')
      globalBus.$emit('RealTimeClient.loggedOut')
    } else {
      // Temporarily disconnected, attempt reconnect
      console.log('[RealTimeClient] Disconnected')
      globalBus.$emit('RealTimeClient.disconnected')

      setTimeout(() => {
        console.log('[RealTimeClient] Reconnecting')
        globalBus.$emit('RealTimeClient.reconnecting')
        this.connect()
      }, 1000)
    }
  }

  // 로그아웃 / 다른 이벤트 핸들러에서 사용되는 메서드
  logout () {
    console.log('[RealTimeClient] Logging out')
    this.subscribeQueue = {}
    this.unsubscribeQueue = {}
    this.authenticated = false
    this.loggedOut = true
    this.socket && this.socket.close()
  }

  _processQueues () {
    console.log('[RealTimeClient] Processing subscribe/unsubscribe queues')

    // 구독 큐 처리
    const subscribeChannels = Object.keys(this.subscribeQueue)
    subscribeChannels.forEach(channel => {
      const handlers = this.subscribeQueue[channel]
      handlers.forEach(handler => {
        this.subscribe(channel, handler)
        this._removeFromQueue(this.subscribeQueue, channel, handler)
      })
    })

    // 구독 해제 큐 처리
    const unsubscribeChannels = Object.keys(this.unsubscribeQueue)
    unsubscribeChannels.forEach(channel => {
      const handlers = this.unsubscribeQueue[channel]
      handlers.forEach(handler => {
        this.unsubscribe(channel, handler)
        this._removeFromQueue(this.unsubscribeQueue, channel, handler)
      })
    })
  }

  // 채널 구독을 큐에 추가
  _addToSubscribeQueue (channel, handler) {
    console.log('[RealTimeClient] Adding channel subscribe to queue. Channel: ' + channel)
    // 구독 취소가 서버로 전송되지 않도록 하려면
    // 구독 해제 큐에서 채널과 핸들러를 찾아 삭제
    this._removeFromQueue(this.unsubscribeQueue, channel, handler)
    const handlers = this.subscribeQueue[channel]

    // 구독 큐에서 채널을 찾지 못하면, 채널 위치에 핸들러를 대입
    // 구독 큐에서 채널을 찾으면, 채널에 핸들러를 push
    // 결론적으로 구독 큐에 채널과 이벤트 핸들러를 추가
    if (!handlers) {
      this.subscribeQueue[channel] = [handler]
    } 
    else {
      handlers.push(handler)
    }
  }

  // 채널 구독 해제를 큐에 추가
  _addToUnsubscribeQueue (channel, handler) {
    console.log('[RealTimeClient] Adding channel unsubscribe to queue. Channel: ' + channel)
    // To make sure the subscribe won't be sent out to the server
    this._removeFromQueue(this.subscribeQueue, channel, handler)
    const handlers = this.unsubscribeQueue[channel]
    if (!handlers) {
      this.unsubscribeQueue[channel] = [handler]
    } else {
      handlers.push(handlers)
    }
  }

  // 큐에서 채널을 찾아 핸들러를 삭제
  _removeFromQueue (queue, channel, handler) {
    const handlers = queue[channel]
    if (handlers) {
      let index = handlers.indexOf(handler)
      if (index > -1) {
        handlers.splice(index, 1)
      }
    }
  }
}

// RealTimeClient의 인스턴스는 real-time-client.js에서 기본적으로 내보내기 때문에,
// 싱글턴으로, 애플리케이션에는 오직 하나의 RealTimeClient 인스턴스가 존재한다.
export default new RealTimeClient()
