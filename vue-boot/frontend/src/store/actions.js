
// Action으로만 뮤테이션을 commit할 수 있다.

import meService from '@/services/me'

export const getMyData = ({ commit }) => {
  meService.getMyData().then(data => {
    commit('updateMyData', data)
  })
}

export const addTeam = ({commit}, team) => {
  commit('addTeam', team)
}

export const addBoard = ({commit}, board) => {
  commit('addBoard', board)
}
