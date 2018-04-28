<!--
 @Title: 应用信息
 @Description: 应用信息(图标和名字)
 @author XueLong xuelongqy@foxmail.com
 @date 2018/4/28 10:50
 @update_author
 @update_time
 @version V1.0
-->
<template>
  <div class="app_info">
    <!--应用图标-->
    <img class="ai_app_icon"
         :src="appInfo.icon"
         onerror="src='./static/image/transparent.png'">
    <!--应用名字-->
    <p class="ai_app_name">{{appInfo.name}}</p>
  </div>
</template>

<script>
  export default {
    name: "AppIcon",
    // 数据
    data() {
      return {
        appInfo: {
          name: "",
          icon: ""
        }
      }
    }
  }
</script>

<style lang="scss">
  .app_info{
    height: 50px;
    line-height: 50px;
    // 应用图标
    .ai_app_icon {
      float: left;
      display: inline-block;
      height: 50px;
      width: 50px;
    }
    // 应用名字
    .ai_app_name {
      display: inline-block;
      margin: 0;
      padding: 0 10px;
      color: white;
      font-size: 1.2rem;
    }
  }
</style>
