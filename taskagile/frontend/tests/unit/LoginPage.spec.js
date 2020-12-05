import { mount, createLocalVue } from '@vue/test-utils'
import Vuelidate from 'vuelidate'
import VueRouter from 'vue-router'
import LoginPage from '@/views/LoginPage'
import authenticationService from '@/services/authentication'

// Setup local Vue with Vuelidate
const localVue = createLocalVue()
localVue.use(Vuelidate)
localVue.use(VueRouter)
const router = new VueRouter()

// Mock dependency registratioService
jest.mock('@/services/authentication')

describe('LoginPage.vue', () => {
  let wrapper
  let fieldUsername
  let fieldPassword
  let buttonSubmit
  let authenticateSpy

  beforeEach(() => {
    wrapper = mount(LoginPage, {
      localVue,
      router
    })
    fieldUsername = wrapper.find('#username')
    fieldPassword = wrapper.find('#password')
    buttonSubmit = wrapper.find('form button[type="submit"]')
    // Create spy for registration service
    authenticateSpy = jest.spyOn(authenticationService, 'authenticate')
  })

  afterEach(() => {
    authenticateSpy.mockReset()
    authenticateSpy.mockRestore()
  })

  afterAll(() => {
    jest.restoreAllMocks()
  })

  // 로그인 폼을 렌더링 해야한다
  it('should render login form', () => {
    expect(wrapper.find('.logo').attributes().src)
      .toEqual('/static/images/logo.png')
    expect(wrapper.find('.tagline').text())
      .toEqual('Open source task management tool')
    expect(fieldUsername.element.value).toEqual('')
    expect(fieldPassword.element.value).toEqual('')
    expect(buttonSubmit.text()).toEqual('Sign in')
    expect(wrapper.find('.link-sign-up').attributes().href)
      .toEqual('/register')
    expect(wrapper.find('.link-forgot-password'))
      .toBeTruthy()
  })

  // 초기값을 갖는 데이터 모델을 포함해야한다.
  it('should contain data model with initial values', () => {
    expect(wrapper.vm.form.username).toEqual('')
    expect(wrapper.vm.form.password).toEqual('')
  })

  // 데이터 모델과 연결된 폼 입력을 가져야한다.
  it('should have form inputs bound with data model', () => {
    const username = 'sunny'
    const password = 'VueJsRocks!'

    wrapper.vm.form.username = username
    wrapper.vm.form.password = password
    expect(fieldUsername.element.value).toEqual(username)
    expect(fieldPassword.element.value).toEqual(password)
  })

  // submitForm 폼 제출 핸들러를 가져야한다.
  it('should have form submit event handler `submitForm`', () => {
    const stub = jest.fn()
    wrapper.setMethods({submitForm: stub})
    buttonSubmit.trigger('submit')
    expect(stub).toBeCalled()
  })

  // 자격이 유효하면 성공해야한다.
  it('should succeed when credentials are valid', async () => {
    expect.assertions(2)
    const stub = jest.fn()
    wrapper.vm.$router.push = stub
    wrapper.vm.form.username = 'sunny'
    wrapper.vm.form.password = 'JestRocks!'
    wrapper.vm.submitForm()
    expect(authenticateSpy).toBeCalled()
    await wrapper.vm.$nextTick()
    expect(stub).toHaveBeenCalledWith({name: 'HomePage'})
  })

  // 자격이 유효하지 않으면 실패해야한다. 
  it('should fail when credentials are invalid', async () => {
    expect.assertions(3)
    // In the mock, only password `JestRocks!` combined with
    // username `sunny` or `sunny@taskagile.com` is valid
    wrapper.vm.form.username = 'sunny'
    wrapper.vm.form.password = 'MyPassword!'
    expect(wrapper.find('.failed').isVisible()).toBe(false)
    wrapper.vm.submitForm()
    expect(authenticateSpy).toBeCalled()
    await wrapper.vm.$nextTick()
    expect(wrapper.find('.failed').isVisible()).toBe(true)
  })

  // 유효하지 않은 데이터 제출을 방지하기 위한 검증이 존재해야한다.
  it('should have validation to prevent invalid data submit', () => {
    // Empty form
    wrapper.vm.submitForm()
    expect(authenticateSpy).not.toHaveBeenCalled()

    // Only username is valid
    wrapper.vm.form.username = 'sunny'
    wrapper.vm.submitForm()
    expect(authenticateSpy).not.toHaveBeenCalled()

    // Only email is valid
    wrapper.vm.form.username = 'sunny@taskagile.com'
    wrapper.vm.submitForm()
    expect(authenticateSpy).not.toHaveBeenCalled()

    // Only password is valid
    wrapper.vm.form.password = 'MyPassword!'
    wrapper.vm.form.username = ''
    wrapper.vm.submitForm()
    expect(authenticateSpy).not.toHaveBeenCalled()
  })
})
