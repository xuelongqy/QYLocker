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
        },
        {
          path: 'theme',
          name: 'theme',
          component: require('@/components/index/Theme/Theme.vue').default
        },
        {
          path: 'theme/:appIndex',
          name: 'theme-app-add-pwd',
          component: require('@/components/index/Theme/Theme.vue').default
        },
        {
          path: 'theme-info/:themeTab/:index/:isAppAddPwd/:appIndex',
          name: 'theme-info',
          component: require('@/components/index/ThemeInfo/ThemeInfo.vue').default
        },
        {
          path: 'settings',
          name: 'settings',
          component: require('@/components/index/Settings/Settings.vue').default
        }
      ]
    }
  ]
})
