
// mutations으로만 Vuex STORE에 저장된 상태를 변경한다.
export default {
  updateMyData (state, data) {
    console.log(data.user.name)
    state.user.name = data.user.name
    state.teams = data.teams
    state.boards = data.boards

    console.log(state.user.name)
  },
  addTeam (state, team) {
    state.teams.push(team)
  },
  addBoard (state, board) {
    state.boards.push(board)
  }
}
