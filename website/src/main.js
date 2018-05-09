// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import store from './store'
import VueI18n from 'vue-i18n'
import lang from './assets/common/lang/lang'
import MuseUI from 'muse-ui'
import 'muse-ui/dist/muse-ui.css'
import 'muse-ui/dist/theme-light.css'
import axios from 'axios'

Vue.config.productionTip = false

// 添加muse-ui
Vue.use(MuseUI)
// 添加多国语言支持
Vue.use(VueI18n)
const i18n = lang.createI18n()
// 添加axios
Vue.prototype.$http = axios

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  i18n,
  components: { App },
  template: '<App/>'
})
