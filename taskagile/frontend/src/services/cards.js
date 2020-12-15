import axios from 'axios'
import errorParser from '@/utils/error-parser'

export default {
  add (detail) {
    return new Promise((resolve, reject) => {
      axios.post('/cards', detail).then(({data}) => {
        resolve(data)
      }).catch((error) => {
        reject(errorParser.parse(error))
      })
    })
  },
  changePositions (positionChanges) {
    return new Promise((resolve, reject) => {
      axios.post('/cards/positions', positionChanges).then(({data}) => {
        resolve(data)
      }).catch((error) => {
        reject(errorParser.parse(error))
      })
    })
  }
}
