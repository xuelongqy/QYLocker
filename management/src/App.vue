<template>
  <div id="app">
    <!--背景-->
    <div id="app_bg"></div>
    <router-view/>
  </div>
</template>

<script>
  require('./assets/view/Voronoi')

  export default {
    name: 'App',
    // 页面初始化
    created() {
      // 修改标题
      document.title = this.$t('comm.appName')+this.$t('comm.management')
      // jquery准备完毕,初始化jquery相关
      $(document).ready(() => {
        // 头部背景
        $('#app_bg').voronoi(false)
      })
    }
  }
</script>

<style lang="scss">
  #app {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    position: fixed;
    height: 100%;
    width: 100%;
    #app_bg {
      position: absolute;
      top: 0;
      bottom: 0;
      left: 0;
      right: 0;
      height: 100%;
      width: 100%;
      z-index: -1;
    }
  }

  body {
    padding: 0;
    margin: 0;
    height: 100%;
    width: 100%;
  }
</style>
