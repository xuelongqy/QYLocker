// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import store from './store'
import PlatformUtil from "./assets/util/platform/platform-util"
import VueI18n from 'vue-i18n'
import lang from './assets/common/lang/lang'
import MuseUI from 'muse-ui'
import 'muse-ui/dist/muse-ui.css'
import 'muse-ui/dist/theme-light.css'

Vue.config.productionTip = false

// 判断平台
if (PlatformUtil.isPlatform("Android")){//安卓
  // 添加cordova支持
  var cordovaScript = document.createElement('script')
  cordovaScript.setAttribute('type', 'text/javascript')
  cordovaScript.setAttribute('src', './static/cordova/android/cordova.js')
  document.body.appendChild(cordovaScript)
}

// 添加muse-ui
Vue.use(MuseUI)

// 添加多国语言支持
Vue.use(VueI18n)
const i18n = lang.createI18n()

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  i18n,
  components: { App },
  template: '<App/>'
})
