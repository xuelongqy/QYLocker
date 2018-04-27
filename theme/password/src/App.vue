<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'App'
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
    background: blue;
  }
  body {
    padding: 0;
    margin: 0;
    height: 100%;
    width: 100%;
  }
</style>
