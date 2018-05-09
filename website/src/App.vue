<template>
  <div id="app">
    <!--头部盒子-->
    <div class="app_header_box">

    </div>
    <!--路由-->
    <router-view/>
  </div>
</template>

<script>
  export default {
    name: 'App',
    // 页面初始化
    created() {
      
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
    // 头部盒子
    .app_header_box {
      height: 100px;
      width: 100%;
    }
  }
  body {
    padding: 0;
    margin: 0;
    height: 100%;
    width: 100%;
  }
</style>
