import axios from 'axios'
import errorParser from '@/utils/error-parser'
import eventBus from '@/event-bus'

export default {
  getMyData () {
    return new Promise((resolve, reject) => {
      axios.get('/me').then(({data}) => {
        resolve(data)
        
        // 최초에 자신의 데이터를 가져올 때, 전역으로 등록한 이벤트 버스의 myDataFetched 이벤트를 발생시켜, 웹소켓을 초기화
        // 이 이벤트를 App.vue의 created 훅에서 등록한 이벤트 핸들러에서 받아서 웹 소켓을 초기화한다.
        eventBus.$emit('myDataFetched', data)
      }).catch((error) => {
        reject(errorParser.parse(error))
      })
    })
  },
  signOut () {
    return new Promise((resolve, reject) => {
      axios.post('/me/logout').then(({data}) => {
        resolve(data)
      }).catch((error) => {
        reject(errorParser.parse(error))
      })
    })
  }
}
