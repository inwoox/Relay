
<template>
  <div class="container">
    <div class="row justify-content-center">
      <div class="form">
        <Logo/>
        <form @submit.prevent="submitForm">
          <div v-show="errorMessage" class="alert alert-danger failed">{{ errorMessage }} </div>
          <div class="form-group">
            <label for="username">Username</label>
            <input type="text" class="form-control" id="username" v-model="form.username">
            <div class="field-error" v-if="$v.form.username.$dirty">
              <div class="error" v-if="!$v.form.username.required">유저 이름을 입력해야합니다.</div>
            </div>
          </div>
          <div class="form-group">
            <label for="password">Password</label>
            <input type="password" class="form-control" id="password" v-model="form.password">
            <div class="field-error" v-if="$v.form.password.$dirty">
              <div class="error" v-if="!$v.form.password.required">패스워드를 입력해야합니다.</div>
            </div>
          </div>
          <button type="submit" class="btn btn-outline-warning btn-lg btn-block">Sign in</button>
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
        this.$router.push({name: 'home'})
        this.$bus.$emit('authenticated')
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
