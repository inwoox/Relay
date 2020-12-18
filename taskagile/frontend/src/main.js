import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import axios from 'axios'
import Vuelidate from 'vuelidate'

// 아이콘 추가 / 국제화
// npm install --save @fortawesome/fontawesome-svg-core @fortawesome/free-solid-svg-icons @fortawesome/vue-fontawesome vue-i18n 설치
import { library as faLibrary } from '@fortawesome/fontawesome-svg-core'
import { faHome, faSearch, faPlus } from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { i18n } from './i18n'


// 웹소켓을 사용하기 위해 이벤트 버스와 웹소켓 클라이언트를 참조
import eventBus from './event-bus'
import realTimeClient from '@/real-time-client'


// Bootstrap axios
// 모든 요청에 자동으로 /api 경로가 추가되게 한다. / 응답을 JSON 형식으로만 받는다 / 에러를 전파하기 위해 인터셉터를 응답에 추가
axios.defaults.baseURL = '/api'
axios.defaults.headers.common.Accept = 'application/json'
axios.interceptors.response.use(
  response => response,
  (error) => {
    return Promise.reject(error)
  }
)

Vue.use(Vuelidate)

// Set up FontAwesome
faLibrary.add(faHome, faSearch, faPlus)
Vue.component('font-awesome-icon', FontAwesomeIcon)

Vue.config.productionTip = false

// 웹소켓을 사용하기 위한 이벤트 버스와 웹소켓 클라이언트 역할을 하는 RealTimeClient 클래스
// 모든 Vue 컴포넌트가 인스턴스의 $rt 프로퍼티로 실시간 클라이언트에 접근할 수 있도록, RealTimeClient 인스턴스를 Vue.prototype.$rt에 바인드
Vue.prototype.$bus = eventBus
Vue.prototype.$rt = realTimeClient

new Vue({
  router,
  store,
  i18n,
  render: h => h(App)
}).$mount('#app')
