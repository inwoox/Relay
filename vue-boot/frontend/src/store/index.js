import Vue from 'vue'
import Vuex from 'vuex'
import * as getters from './getters'
import * as actions from './actions'
import mutations from './mutations'
//import createLogger from 'vuex/dist/logger'

Vue.use(Vuex)

// 상태는 여기에 있다
const state = {
  user: {
    name: null
  },
  teams: [/* {id, name} */],
  boards: [/* {id, name, description, teamId} */]
}

export default new Vuex.Store({
  state,
  getters,
  actions,
  mutations,
  // plugins: process.env.NODE_ENV !== 'production'
  //   ? [createLogger()]
  //   : []
})
