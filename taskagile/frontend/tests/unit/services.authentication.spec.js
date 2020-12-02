import moxios from 'moxios'
import authenticationService from '@/services/authentication'

describe('services/authentication', () => {
  beforeEach(() => {
    moxios.install()
  })

  afterEach(() => {
    moxios.uninstall()
  })

  // API를 호출해야한다
  it('should call `/authentications` API', () => {
    expect.assertions(1)
    moxios.wait(() => {
      let request = moxios.requests.mostRecent()
      expect(request.url).toEqual('/authentications')
      request.respondWith({
        status: 200,
        response: {result: 'success'}
      })
    })
    return authenticationService.authenticate()
  })

  // 요청이 성공하면 호출한 곳에 응답해야한다
  it('should pass the response to caller when request succeeded', () => {
    expect.assertions(2)
    moxios.wait(() => {
      let request = moxios.requests.mostRecent()
      expect(request).toBeTruthy()
      request.respondWith({
        status: 200,
        response: {result: 'success'}
      })
    })
    return authenticationService.authenticate().then(data => {
      expect(data.result).toEqual('success')
    })
  })

  // 요청이 실패하면 호출한 곳에 에러를 전파해야한다
  it('should propagate the error to caller when request failed', () => {
    expect.assertions(2)
    moxios.wait(() => {
      let request = moxios.requests.mostRecent()
      expect(request).toBeTruthy()                  // 값이 true인지 확인
      request.reject({
        response: {
          status: 400,
          data: {message: 'Bad request'}
        }
      })
    })
    return authenticationService.authenticate().catch(error => {
      expect(error.message).toEqual('Bad request')
    })
  })
})
