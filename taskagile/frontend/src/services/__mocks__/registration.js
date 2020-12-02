// 목이 어떻게 동작해야하는지 설정을 해두고, 이 목을 테스트에서 활용한다.
export default {
  register (detail) {
    return new Promise((resolve, reject) => {
      detail.emailAddress === 'sunny@taskagile.com'
        ? resolve({result: 'success'})
        : reject(new Error('User already exist'))
    })
  }
}
