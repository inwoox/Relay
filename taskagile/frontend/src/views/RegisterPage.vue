// Header
  // 전체 폼
    // 로고 / 설명
    // 회원 가입 폼
      // 이름
        // 이름 입력
        // 이름 필드 오류 표시
      // 이메일
        // 이메일 입력
        // 이메일 필드 오류 표시
      // 패스워드
        // 패스워드 입력
        // 패스워드 필드 오류 표시
      // submit
        // submit 버튼
        // 동의 안내 / 로그인 링크
// Footer
  // copyright
  // About, 서비스 약관, 개인정보보호 정책, SNS, GitHub 등

<template>
  <div class="container">
    <!-- Header -->
    <div class="row justify-content-center"> <!-- .row 요소 안의 내용을 중앙에 위치시키는 부트스트랩 유틸리티 클래스 -->
      <!-- 전체 폼 -->
      <div class="register-form">
        <!-- 로고 / 태그 라인 -->
        <div class="logo-wrapper">
          <img class="logo" src="/static/images/logo.png">
          <div class="tagline">Open source task management tool</div>
        </div>
        <!-- 회원가입 폼 -->
        <form @submit.prevent="submitForm">
          <div v-show="errorMessage" class="failed">{{ errorMessage }}</div>

          <!-- 이름 부분 -->
          <div class="form-group"> <!-- 부트 스트랩은 .form-group이 적용된 필드의 패딩과 마진을 멋지게 정리 -->
            <!-- 이름 입력 부분 -->
            <label for="username"> Username </label>
            <input type="text" class="form-control" id="username" v-model="form.username">
            <!-- 이름 필드 오류 표시 -->
            <div class="field-error" v-if="$v.username.$dirty">
              <div class="error" v-if="!$v.username.required">이름을 입력해야합니다.</div>
              <div class="error" v-if="!$v.username.alphaNum">이름은 글자와 숫자만 가능합니다.</div>
              <div class="error" v-if="!$v.username.minLength">이름은 {{$v.username.$params.minLength.min}}자 이상이어야합니다.</div>
              <div class="error" v-if="!$v.username.maxLength">이름은 {{$v.username.$params.maxLength.max}}자 이하여야합니다.</div>
            </div>
          </div>

          <!-- 이메일 부분 -->
          <div class="form-group">
            <!-- 이메일 입력 부분 -->
            <label for="emailAddress">Email address</label>
            <input type="email" class="form-control" id="emailAddress" v-model="form.emailAddress">
            <!-- 이메일 필드 오류 표시 -->
            <div class="field-error" v-if="$v.emailAddress.$dirty">
              <div class="error" v-if="!$v.emailAddress.required">이메일을 입력해야합니다.</div>
              <div class="error" v-if="!$v.emailAddress.email">유효한 이메일 주소가 아닙니다.</div>
              <div class="error" v-if="!$v.emailAddress.maxLength">이메일 주소는 {{$v.emailAddress.$params.maxLength.max}}자 이하여야합니다.</div>
            </div>
          </div>

          <!-- 패스워드 부분 -->
          <div class="form-group">
            <!-- 패스워드 입력 부분 -->
            <label for="password">Password</label>
            <input type="password" class="form-control" id="password" v-model="form.password">
            <!-- 패스워드 필드 오류 표시 -->
            <div class="field-error" v-if="$v.password.$dirty">
              <div class="error" v-if="!$v.password.required">패스워드를 입력해야합니다.</div>
              <div class="error" v-if="!$v.password.minLength">패스워드는 {{$v.password.$params.minLength.min}}자 이상이어야합니다.</div>
              <div class="error" v-if="!$v.password.maxLength">패스워드는 {{$v.password.$params.maxLength.min}}자 이하여야합니다.</div>
            </div>
          </div>

          <!-- submit 부분 -->
          <button type="submit" class="btn btn-primary btn-block">Create account</button>
          <!-- 계정 만들기 설명 및 로그인 링크 -->
          <p class="accept-terms text-muted">Create account를 클릭하면, <a href="#">서비스 약관</a> 및 <a href="#">개인정보 보호정책</a>에 동의하는 것입니다.</p>
          <p class="text-center text-muted">계정이 있으시면 <a href="/login">로그인</a>하세요.</p>
        </form>
      </div>
    </div>
    <!-- Footer -->
    <footer class="footer">
      <!-- copyright -->
      <span class="copyright">&copy; 2020 TaskAgile.com</span>
      <!-- 약관, 정책, SNS, 관련 링크 -->
      <ul class="footer-links list-inline float-right"> <!-- 부트스트랩의 유틸리티 클래스들 -->
        <li class="list-inline-item"><a href="#">About</a></li>
        <li class="list-inline-item"><a href="#">Terms of Service</a></li>
        <li class="list-inline-item"><a href="#">Privacy Policy</a></li>
        <li class="list-inline-item"><a href="https://github.com/inwoox/Vue-Spring-TaskManager" target="_blank">GitHub</a></li>
      </ul>
    </footer>
  </div>
</template>

<script>
import { required, email, minLength, maxLength, alphaNum } from 'vuelidate/lib/validators'
import registrationService from '@/services/registration'
export default {
  name: 'RegisterPage',
  data () {
    return {
      form: {
        username: '', emailAddress: '', password: ''
      },
      errorMessage: ''
    }
  },
  validations: {
    username: { required, minLength: minLength(2), maxLength: maxLength(50), alphaNum },
    emailAddress: { required, email, maxLength: maxLength(100) },
    password: { required, minLength: minLength(6), maxLength: maxLength(30) }
  },
  methods: {
    submitForm () {
      this.$v.$touch()
      if (this.$v.$invalid) return
      registrationService.register(this.form).then(() => {
        this.$router.push({ name: 'LoginPage' })
      }).catch((error) => {
        this.errorMessage = '유저 등록에 실패하였습니다.' + error.message
      })
    }
  }
}
</script>

<style lang="scss" scoped>
  .container { max-width: 900px; }
  // Header
  .register-form { margin-top: 50px; max-width: 320px;}
  .register-form .logo-wrapper { text-align: center; margin-bottom: 40px; }
  .register-form .logo-wrapper .tagline { line-height: 180%; color: #666; }
  .register-form .logo-wrapper .logo { max-width: 150px; margin: 0 auto; }
  .register-form .form-group label { font-weight: bold; color: #555; }
  .register-form .accept-terms { margin: 20px 0 40px 0; }
  // Footer
  .footer { width: 100%; font-size: 13px; color: #666; line-height: 40px; border-top: 1px solid #ddd; margin-top:50px; }
  .footer .list-inline-item { margin-right: 10px; }
  .footer a { color: #666; }
</style>
