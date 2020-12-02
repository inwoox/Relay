// 목이 어떻게 동작해야하는지 설정을 해두고, 이 목을 테스트에서 활용한다.
export default {
  authenticate (detail) {
    return new Promise((resolve, reject) => {
      (detail.username === 'sunny' || detail.username === 'sunny@taskagile.com') &&
      detail.password === 'JestRocks!'
        ? resolve({result: 'success'})
        : reject(new Error('Invalid credentials'))
    })
  }
}
