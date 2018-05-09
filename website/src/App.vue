<template>
  <div id="app">
    <h2 style="position: absolute; top: -9999px;">{{this.$t('comm.appName')}}</h2>
    <!--背景-->
    <div id="app_bg"></div>
    <!--头部-->
    <mu-appbar :title="$t('comm.appName')">
      <!--logo-->
      <img class="app_logo" src="./assets/logo.png" slot="left"/>
      <mu-flat-button :label="$t('app.home')" slot="right"/>
      <mu-flat-button :label="$t('app.lockScreen')" slot="right"/>
      <mu-flat-button :label="$t('app.download')" slot="right"/>
      <mu-icon-button icon="expand_more" slot="right"/>
    </mu-appbar>
    <!--主干区域-->
    <div class="app_main">
      <!--路由-->
      <router-view/>
    </div>
  </div>
</template>

<script>
  require('./assets/view/Voronoi')

  export default {
    name: 'App',
    // 页面初始化
    created() {
      // 修改标题
      document.title = this.$t('comm.appName')
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
    // 图标
    .app_logo {
      width: 50px;
      height: 50px;
    }
    // 主干区域
    .app_main {
      position: relative;
      height: calc(100% - 64px);
      bottom: 0;
    }
  }
  body {
    padding: 0;
    margin: 0;
    height: 100%;
    width: 100%;
  }
</style>
