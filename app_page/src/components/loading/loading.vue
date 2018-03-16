<!--
 @Title: 加载页面
 @Description: 加载页面
 @author XueLong xuelongqy@foxmail.com
 @date 2018/1/29 15:52
 @update_author
 @update_time
 @version V1.0
-->
<template>
    <div id="loading">
      <mu-linear-progress />
    </div>
</template>

<script>
    export default {
        name: "loading"
    }
</script>

<style lang="scss">
  #loading {
    position: absolute;
    top: 40%;
    width: 100%;
    padding: 20px;
    text-align: center;
  }
</style>
