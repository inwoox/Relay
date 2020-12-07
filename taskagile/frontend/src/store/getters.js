
export default {
  user = state => state.user,
  hasBoards = state => {
  return state.boards.length > 0
  },
  
  personalBoards = state => {
    return state.boards.filter(board => board.teamId === 0)
  },

  teamBoards = state => {
  const teams = []

  state.teams.forEach(team => {
    teams.push({
      id: team.id,
      name: team.name,
      boards: state.boards.filter(board => board.teamId === team.id)
      })
    })
    return teams
  }
}
