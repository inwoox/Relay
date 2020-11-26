import { shallowMount } from '@vue/test-utils'
import LoginPage from '@/views/LoginPage'

// Jest를 사용하는 단위 테스트 코드

describe('LoginPage.vue', () => {
  it('h1의 내용이 TaskAgile인지 검사', () => {
    const msg = 'TaskAgile'
    const wrapper = shallowMount(LoginPage, {
      props: { msg }
    })

    expect(wrapper.find("div>h1").text()).toBe(msg)
  })
})
