import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'index',
      component: require('@/components/Index/Index').default
    },
    {
      path: '/theme',
      name: 'theme',
      component: require('@/components/Theme/Theme').default
    },
    {
      path: '/my-theme',
      name: 'my-theme',
      component: require('@/components/MyTheme/MyTheme').default
    }
  ]
})
