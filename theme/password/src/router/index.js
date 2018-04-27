import Vue from 'vue'
import Router from 'vue-router'
import LockPage from '@/components/LockPage/LockPage'
import SetPwdPage from '@/components/SetPwdPage/SetPwdPage'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'lock-page',
      component: LockPage
    },
    {
      path: '/set-pwd',
      component:SetPwdPage
    }
  ]
})
