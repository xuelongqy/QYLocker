import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      redirect: 'loading'
    },
    {
      path: '/loading',
      name: 'loading',
      component: require('@/components/loading/loading').default
    },
    {
      path: '/index',
      name: 'index',
      component: require('@/components/index/index').default,
      children: [
        {
          path: '/index/',
          redirect: 'app-list'
        },
        {
          path: 'app-list',
          name: 'app-list',
          component: require('@/components/index/AppList/AppList.vue').default
        }
      ]
    }
  ]
})
