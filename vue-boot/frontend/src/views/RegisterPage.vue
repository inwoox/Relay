

<template>
  <div class="container">
    <div class="row justify-content-center"> <!-- .row 요소 안의 내용을 중앙에 위치시키는 부트스트랩 유틸리티 클래스 -->
      <div class="register-form">
        <div class="logo-wrapper">
          <img class="logo" src="/images/logo.png">
          <div class="tagline">Open source task management tool</div>
        </div>
        <form @submit.prevent="submitForm">
          <div v-show="errorMessage" class="failed">{{ errorMessage }}</div>
          <div class="form-group"> <!-- 부트 스트랩은 .form-group이 적용된 필드의 패딩과 마진을 멋지게 정리 -->
            <label for="username"> Username </label>
            <input type="text" class="form-control" id="username" v-model="form.username">
            <div class="field-error" v-if="$v.form.username.$dirty">
              <div class="error" v-if="!$v.form.username.required">이름을 입력해야합니다.</div>
              <div class="error" v-if="!$v.form.username.alphaNum">이름은 글자와 숫자만 가능합니다.</div>
              <div class="error" v-if="!$v.form.username.minLength">이름은 {{$v.form.username.$params.minLength.min}}자 이상이어야합니다.</div>
              <div class="error" v-if="!$v.form.username.maxLength">이름은 {{$v.form.username.$params.maxLength.max}}자 이하여야합니다.</div>
            </div>
          </div>
          <div class="form-group">
            <label for="emailAddress">Email address</label>
            <input type="email" class="form-control" id="emailAddress" v-model="form.emailAddress">
            <div class="field-error" v-if="$v.form.emailAddress.$dirty">
              <div class="error" v-if="!$v.form.emailAddress.required">이메일을 입력해야합니다.</div>
              <div class="error" v-if="!$v.form.emailAddress.email">유효한 이메일 주소가 아닙니다.</div>
              <div class="error" v-if="!$v.form.emailAddress.maxLength">이메일 주소는 {{$v.form.emailAddress.$params.maxLength.max}}자 이하여야합니다.</div>
            </div>
          </div>
          <div class="form-group">
            <label for="firstName">{{ $t('registerPage.form.firstName.label') }}</label>
            <input type="text" class="form-control" id="firstName" v-model="form.firstName">
            <div class="field-error" v-if="$v.form.firstName.$dirty">
              <div class="error" v-if="!$v.form.firstName.required">{{ $t('registerPage.form.firstName.required') }}</div>
              <div class="error" v-if="!$v.form.firstName.alpha">{{ $t('registerPage.form.firstName.alpha') }}</div>
              <div class="error" v-if="!$v.form.firstName.minLength">{{ $t('registerPage.form.firstName.minLength', {minLength: $v.form.firstName.$params.minLength.min}) }}</div>
              <div class="error" v-if="!$v.form.firstName.maxLength">{{ $t('registerPage.form.firstName.maxLength', {maxLength: $v.form.firstName.$params.maxLength.max}) }}</div>
            </div>
          </div>
          <div class="form-group">
            <label for="lastName">{{ $t('registerPage.form.lastName.label') }}</label>
            <input type="text" class="form-control" id="lastName" v-model="form.lastName">
            <div class="field-error" v-if="$v.form.lastName.$dirty">
              <div class="error" v-if="!$v.form.lastName.required">{{ $t('registerPage.form.lastName.required') }}</div>
              <div class="error" v-if="!$v.form.lastName.alpha">{{ $t('registerPage.form.lastName.alpha') }}</div>
              <div class="error" v-if="!$v.form.lastName.minLength">{{ $t('registerPage.form.lastName.minLength', {minLength: $v.form.lastName.$params.minLength.min}) }}</div>
              <div class="error" v-if="!$v.form.lastName.maxLength">{{ $t('registerPage.form.lastName.maxLength', {maxLength: $v.form.lastName.$params.maxLength.max}) }}</div>
            </div>
          </div>
          <div class="form-group">
            <label for="password">Password</label>
            <input type="password" class="form-control" id="password" v-model="form.password">
            <div class="field-error" v-if="$v.form.password.$dirty">
              <div class="error" v-if="!$v.form.password.required">패스워드를 입력해야합니다.</div>
              <div class="error" v-if="!$v.form.password.minLength">패스워드는 {{$v.form.password.$params.minLength.min}}자 이상이어야합니다.</div>
              <div class="error" v-if="!$v.form.password.maxLength">패스워드는 {{$v.form.password.$params.maxLength.min}}자 이하여야합니다.</div>
            </div>
          </div>
          <button type="submit" class="btn btn-primary btn-block">Create account</button>
          <p class="accept-terms text-muted">Create account를 클릭하면, <a href="#">서비스 약관</a> 및 <a href="#">개인정보 보호정책</a>에 동의하는 것입니다.</p>
          <p class="text-center text-muted">계정이 있으시면 <a href="/login">로그인</a>하세요.</p>
        </form>
      </div>
    </div>
    <footer class="footer">
      <span class="copyright">&copy; 2020 TaskAgile.com</span>
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
import { required, email, minLength, maxLength, alphaNum, alpha } from 'vuelidate/lib/validators'
import registrationService from '@/services/registration'
export default {
  name: 'RegisterPage',
  data () {
    return {
      form: {
        username: '',
        emailAddress: '',
        password: ''
      },
      errorMessage: ''
    }
  },
  // 여러개의 Vuelidate 내장 검증기를 가져와서, 유효성 검증 조건을 설정하고, 이것을 통해 필드 오류 표시 부분을 작성한다.
  validations: {
    form: {
      username: {
        required,
        minLength: minLength(2),
        maxLength: maxLength(50),
        alphaNum
      },
      emailAddress: {
        required,
        email,
        maxLength: maxLength(100)
      },
      firstName: {
        required,
        minLength: minLength(1),
        maxLength: maxLength(45),
        alpha
      },
      lastName: {
        required,
        minLength: minLength(1),
        maxLength: maxLength(45),
        alpha
      },
      password: {
        required,
        minLength: minLength(6),
        maxLength: maxLength(30)
      }
    }
  },
  methods: {
    submitForm () {
      // vuelidate가 생성하여 Vue 인스턴스에 추가한 $v 객체로 Vuelidate API에 접근 / $v 객체는 검증에 대한 현재 상태를 가진다
      // $v.$touch() 메서드를 호출해 데이터 검증 시작 / 그 다음 $v.$invalid 속성으로 결과 확인 , 검증이 실패시 $invalid 값은 true가 된다.
      this.$v.$touch()
      if (this.$v.$invalid) {
        return
      }
      registrationService.register(this.form).then(() => {
        this.$router.push({ name: 'LoginPage' })
      }).catch((error) => {
        this.errorMessage = '유저 등록에 실패하였습니다.' + error.message
      })
    }
  }
}
</script>

// 태그의 구조대로 스타일의 태그도 작성한다.
<style lang="scss" scoped> // scoped 속성을 가지고있을 때, CSS는 현재 컴포넌트의 엘리먼트에만 적용
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
