import axios from 'axios'
import errorParser from '@/utils/error-parser'

export default {
  create (detail) {
    return new Promise((resolve, reject) => {
      axios.post('/boards', detail).then(({data}) => {
        resolve(data)
      }).catch((error) => {
        reject(errorParser.parse(error))
      })
    })
  },
  addMember (boardId, usernameOrEmailAddress) {
    return new Promise((resolve, reject) => {
      axios.post('/boards/' + boardId + '/members', { usernameOrEmailAddress }).then(({data}) => {
        resolve(data)
      }).catch((error) => {
        reject(errorParser.parse(error))
      })
    })
  },
  getBoard (boardId) {
    return new Promise((resolve, reject) => {
      axios.get('/boards/' + boardId).then(({data}) => {
        resolve(data)
      }).catch((error) => {
        reject(errorParser.parse(error))
      })
    })
  }
}
