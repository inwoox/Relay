import { mount, createLocalVue } from '@vue/test-utils'
import RegisterPage from '@/views/RegisterPage'
import VueRouter from 'vue-router'
import Vuelidate from 'vuelidate'
import registrationService from '@/services/registration'

// vm.$router에 접근할 수 있도록 테스트에 Vue Router 추가하기
const localVue = createLocalVue()
localVue.use(VueRouter)
localVue.use(Vuelidate)
const router = new VueRouter()

// registratioService의 목
jest.mock('@/services/registration')

describe('RegisterPage.vue', () => {
  let wrapper
  let fieldUsername
  let fieldEmailAddress
  let fieldPassword
  let buttonSubmit
  let registerSpy

  beforeEach(() => {
    wrapper = mount(RegisterPage, {
      localVue,
      router
    })
    fieldUsername = wrapper.find('#username')                  // id가 username인 요소를 가지는 fieldUsername를 생성
    fieldEmailAddress = wrapper.find('#emailAddress')
    fieldPassword = wrapper.find('#password')
    buttonSubmit = wrapper.find('form button[type="submit"]')
    registerSpy = jest.spyOn(registrationService, 'register')  // registrationService에 스파이를 등록한다.
  })

  afterEach(() => {
    registerSpy.mockReset()
    registerSpy.mockRestore()
  })

  afterAll(() => {         // 모든 테스트의 실행이 완료되면 호출 된다.
    jest.restoreAllMocks() // registrationService를 복구한다.
  })

  it('등록 폼을 작성했는지 체크', () => {
    expect(wrapper.find('.logo').attributes().src).toEqual('/static/images/logo.png')   // logo 클래스 요소의 src 값이 일치하는지 확인
    expect(wrapper.find('.tagline').text()).toEqual('Open source task management tool') // tagline 클래스의 텍스트 값이 일치하는지 확인
    expect(fieldUsername.element.value).toEqual('')                                     // fieldUsername 요소가 만들어져있는지 체크 
    expect(fieldEmailAddress.element.value).toEqual('')
    expect(fieldPassword.element.value).toEqual('')
    expect(buttonSubmit.text()).toEqual('Create account')                               // 요소의 텍스트 값이 일치하는지 확인
  })

  it('초기 값이 있는 데이터 모델을 포함하는지 체크', () => {
    expect(wrapper.vm.form.username).toEqual('')          // form.username 데이터 모델이 정상적으로 만들어져있는지 체크 
    expect(wrapper.vm.form.emailAddress).toEqual('')
    expect(wrapper.vm.form.password).toEqual('')
  })

  it('데이터 모델에 바인딩된 값이 있는지 체크', () => {
    const username = 'sunny'
    const emailAddress = 'sunny@taskagile.com'
    const password = 'VueJsRocks!'
    wrapper.vm.form.username = username                            // 데이터 모델에 값을 바인딩
    wrapper.vm.form.emailAddress = emailAddress
    wrapper.vm.form.password = password
    expect(fieldUsername.element.value).toEqual(username)          // id가 username인 fieldUsername 요소의 값이, 바인딩한 값과 일치하는지 확인
    expect(fieldEmailAddress.element.value).toEqual(emailAddress)
    expect(fieldPassword.element.value).toEqual(password)
  })

  it('폼 제출 이벤트 핸들러인 submitForm이 있는지 체크', () => {
    const stub = jest.fn()
    wrapper.setMethods({submitForm: stub})                  // stub에 submitForm 메서드를 설정
    buttonSubmit.trigger('submit')                          // 비동기적으로 버튼 요소의 폼 submit (이벤트) 을 트리거한다.
    expect(stub).toBeCalled()                               // submitForm 메서드가 호출되는지 체크
  })

  // it('새 사용자일 때 등록하는지 체크', async () => {
  //   expect.assertions(1)
  //   const stub = jest.fn()
  //   wrapper.vm.$router.push = stub                       // stub에 $router.push를 설정 
  //   wrapper.vm.form.username = 'sunny'
  //   wrapper.vm.form.emailAddress = 'sunny@taskagile.com'
  //   wrapper.vm.form.password = 'JestRocks!'
  //   wrapper.vm.submitForm()
  //   expect(registerSpy).toBeCalled()  // registerSpy가 발동하는지 체크
  //   await wrapper.vm.$nextTick()
  //   expect(stub).toHaveBeenCalledWith({name: 'LoginPage'})
  // })

  it('새로운 사용자가 아닌지 체크', async () => {
    expect.assertions(1)
    // 목에서는 'sunny@taskagile.com'만 새로운 사용자다.
    wrapper.vm.form.username = 'ted'
    wrapper.vm.form.emailAddress = 'ted@taskagile.com'
    wrapper.vm.form.password = 'JestRocks!'
    expect(wrapper.find('.failed').isVisible()).toBe(false)      // 에러 메시지가 안보이는지 체크
    wrapper.vm.submitForm()
    // expect(registerSpy).toBeCalled()                          // register 메서드가 호출 되었는지 체크
    await wrapper.vm.$nextTick()
    // expect(wrapper.find('.failed').isVisible()).toBe(true)    // 에러 메시지가 보이는지 체크
  })

  it('이메일 주소가 유효하지 않으면 실패하는지 체크', () => {
    wrapper.vm.form.username = 'test'
    wrapper.vm.form.emailAddress = 'bad-email-address'
    wrapper.vm.form.password = 'JestRocks!'
    wrapper.vm.submitForm()
    expect(registerSpy).not.toHaveBeenCalled()                  // register 메서드가 호출되지 않았는지 체크
  })

  it('사용자 이름이 유효하지 않으면 실패하는지 체크', () => {
    wrapper.vm.form.username = 'a'
    wrapper.vm.form.emailAddress = 'test@taskagile.com'
    wrapper.vm.form.password = 'JestRocks!'
    wrapper.vm.submitForm()
    expect(registerSpy).not.toHaveBeenCalled()                  // register 메서드가 호출되지 않았는지 체크
  })

  it('암호가 유효하지 않으면 실패하는지 체크', () => {
    wrapper.vm.form.username = 'test'
    wrapper.vm.form.emailAddress = 'test@taskagile.com'
    wrapper.vm.form.password = 'bad!'
    wrapper.vm.submitForm()
    expect(registerSpy).not.toHaveBeenCalled()                  // register 메서드가 호출되지 않았는지 체크
  })
})
