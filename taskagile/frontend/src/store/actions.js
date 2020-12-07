
// Action으로만 뮤테이션을 commit할 수 있다.

import meService from '@/services/me'

export default {
  getMyData = ({ commit }) => {
    meService.getMyData().then(data => {
      commit('updateMyData', data)
    })
  },
  addTeam = ({commit}, team) => {
    commit('addTeam', team)
  },
  addBoard = ({commit}, board) => {
    commit('addBoard', board)
  }
}
