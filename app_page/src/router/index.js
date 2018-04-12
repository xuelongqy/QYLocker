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
          meta: {
            keepAlive: true // 需要被缓存
          },
          component: require('@/components/index/AppList/AppList.vue').default
        },
        {
          path: 'theme',
          name: 'theme',
          meta: {
            keepAlive: false // 需要被缓存
          },
          component: require('@/components/index/Theme/Theme.vue').default
        },

        {
          path: 'settings',
          name: 'settings',
          meta: {
            keepAlive: false // 需要被缓存
          },
          component: require('@/components/index/Settings/Settings.vue').default
        }
      ]
    }
  ]
})
