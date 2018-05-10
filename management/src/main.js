// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import VueI18n from 'vue-i18n'
import lang from './assets/common/lang/lang'
import MuseUI from 'muse-ui'
import 'muse-ui/dist/muse-ui.css'
import 'muse-ui/dist/theme-light.css'
import 'animate.css/animate.css'
import axios from 'axios'
import VueScroller from 'vue-scroller'
import VueAwesomeSwiper from 'vue-awesome-swiper'
import 'swiper/dist/css/swiper.css'

Vue.config.productionTip = false

// 添加muse-ui
Vue.use(MuseUI)
// 添加多国语言支持
Vue.use(VueI18n)
const i18n = lang.createI18n()
// 添加axios
Vue.prototype.$http = axios
// 添加下拉刷新回弹
Vue.use(VueScroller)
// 添加翻页轮播插件
Vue.use(VueAwesomeSwiper)

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  i18n,
  components: { App },
  template: '<App/>'
})
