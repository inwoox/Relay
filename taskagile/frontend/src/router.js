import Vue from 'vue'
import Router from 'vue-router'
import LoginPage from '@/views/LoginPage'
import RegisterPage from '@/views/RegisterPage'
import HomePage from '@/views/HomePage'
import BoardPage from '@/views/BoardPage'

Vue.use(Router)

export default new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: [
  {
    path: '/login',
    name: 'LoginPage',
    component: LoginPage
  },
  {
    path: '/register',
    name: 'RegisterPage',
    component: RegisterPage
  },
  {
    path: '/',
    name: 'HomePage',
    component: HomePage
  },
  // 두 경로 모두 동작하게 하기 위해 BoardPage를 리팩토링한다.
  {
    path: '/board/:boardId',
    name: 'board',
    component: BoardPage
  },
  {
    path: '/card/:cardId/:cardTitle',
    name: 'card',
    component: BoardPage
  }]
})
