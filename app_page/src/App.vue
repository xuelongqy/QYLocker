<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
  import PlatformUtil from "./assets/util/platform/platform-util"

  export default {
    name: 'App',
    // 页面启动时
    created() {
      //判断是否为Android平台
      if (PlatformUtil.isPlatform("Android")) {
        // 绑定设备初始化完成事件
        document.addEventListener('deviceready', () => {
          this.$store.dispatch('deviceIsReady')
          // 注册键盘抬起关闭事件
          window.addEventListener('keyboardWillShow', (ev) => {
            this.$store.commit('setKeyboardVisible', true)
            this.$store.commit('setKeyboardHeight', event.keyboardHeight)
          })
          window.addEventListener('keyboardDidShow', (ev) => {
            // Describe your logic which will be run each time when keyboard is about to be shown.
            // console.log(event.keyboardHeight);
          })
          window.addEventListener('keyboardWillHide', () => {
            this.$store.commit('setKeyboardVisible', false)
          })
          window.addEventListener('keyboardDidHide', () => {
            // Describe your logic which will be run each time keyboard is closed.
          })
        }, false)
      }else {
        this.$store.commit('setDeviceReady')
      }
      // 如果设备准备完成跳转主页
      if (this.deviceReady) {
        this.$router.push("/index")
      }
    },
    // 计算方法
    computed: {
      // 获取设备准备状态
      deviceReady() {
        return this.$store.state.App.deviceReady;
      }
    },
    // 监听器
    watch: {
      // 监听设备是否准备完毕
      deviceReady(newValue, oldValue) {
        if (newValue) {
          // 跳转主页
          this.$router.push("/index")
        }
      }
    }
  }
</script>

<style>
  #app {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
  }
</style>
