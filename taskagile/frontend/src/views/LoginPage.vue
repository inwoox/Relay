// container
  // row justify-content-center / Header
    // form
      // Logo
      // 입력 폼
        // alert alert-danger failed
        // form-group
          // label
          // form-control
          // field-error
            // error
        // btn btn-outline-warning btn-lg btn-block
        // links
          // sign-up
          // get-password
  // Footer
    // copyright
    // footer-links list-inline float-right
      // About
      // 서비스 약관
      // 개인정보보호정책
      // Github

<template>
  <!-- 폭 자동 조정 유틸리티 클래스 -->
  <div class="container">
    <!-- 센터 정렬 유틸리티 클래스 / 헤더 시작 -->
    <div class="row justify-content-center">
      <!-- 전체 form -->
      <div class="form">
        <!-- 로고 / 입력 폼 -->
        <Logo/>
        <form @submit.prevent="submitForm">
          <!-- 경고 메시지 스타일 유틸리티 클래스 -->
          <div v-show="errorMessage" class="alert alert-danger failed">{{ errorMessage }} </div>
          <!-- 그룹핑 유틸리티 클래스 -->
          <div class="form-group">
            <!-- 입력 부분 -->
            <label for="username">Username</label>
            <input type="text" class="form-control" id="username" v-model="form.username">
            <!-- 유효성 검사 에러 표시 -->
            <div class="field-error" v-if="$v.form.username.$dirty">
              <div class="error" v-if="!$v.form.username.required">유저 이름을 입력해야합니다.</div>
            </div>
          </div>
          <!-- 그룹핑 유틸리티 클래스 -->
          <div class="form-group">
            <!-- 입력 부분 -->
            <label for="password">Password</label>
            <input type="password" class="form-control" id="password" v-model="form.password">
            <!-- 유효성 검사 에러 표시 -->
            <div class="field-error" v-if="$v.form.password.$dirty">
              <div class="error" v-if="!$v.form.password.required">패스워드를 입력해야합니다.</div>
            </div>
          </div>
          <!-- 폼 제출 버튼 -->
          <button type="submit" class="btn btn-outline-warning btn-lg btn-block">Sign in</button>
          <!-- 회원가입 / 패스워드 찾기 링크 -->
          <div class="links">
            <p class="sign-up text-muted"> 계정이 없으십니까? <a href="/register" class="link-sign-up">클릭하세요!</a></p>
            <p><a href="#">패스워드를 잊어버리셨습니까?</a></p>
          </div>
        </form>
      </div>
    </div>
    <PageFooter/>
  </div>
</template>

<script>
import { required } from 'vuelidate/lib/validators'
import authenticationService from '@/services/authentication'
import Logo from '@/components/Logo.vue'
import PageFooter from '@/components/PageFooter.vue'

export default {
  name: 'LoginPage',
  data () {
    return {
      form: {
        username: '',
        password: ''
      },
      errorMessage: ''
    }
  },
  components: {
    Logo,
    PageFooter
  },
  validations: {
    form: {
      username: { required },
      password: { required }
    }
  },
  methods: {
    submitForm () {
      this.$v.$touch()
      if (this.$v.$invalid) {
        return
      }

      authenticationService.authenticate(this.form).then(() => {
        this.$router.push({name: 'HomePage'})
      }).catch((error) => {
        this.errorMessage = error.message
      })
    }
  }
}

</script>
<style lang="scss" scoped>
  .links {
    margin: 30px 0 50px 0;
    text-align: center;
  }
  a { color:teal;}
  button { margin-top: 10%; }
</style>
