<template>
  <!--App条目-->
  <mu-flat-button class="al_app_item" @click="onAppItem">
    <div class="al_app_item_box">
      <img :src="appInfo.appIcon" :alt="appInfo.appName">
      <p class="al_app_item_name">{{appInfo.appName}}</p>
      <div class="al_app_item_switch_box">
        <mu-switch/>
      </div>
      <div class="al_app_item_settings_box">
        <mu-icon-button icon="settings" class="al_app_item_settings_icon" @click.stop="onAppLockSettings"/>
      </div>
    </div>
  </mu-flat-button>
</template>

<script>
  export default {
    name: "app-item",
    // 参数
    props: {
      // App信息
      appInfo: {
        type: Object,
        default: {
          "appIcon": "",
          "appName": "",
          "isIndependent": false,
          "isLock": false,
          "isSystemAPP": true,
          "packageName": "",
          "password": "",
          "theme": "",
          "versionCode": 0,
          "versionName": ""
        }
      }
    },
    // 方法
    methods: {
      // App条目点击事件
      onAppItem() {
        alert(this.appName)
      },
      // App锁设置
      onAppLockSettings() {
        alert('appLockSettings')
      }
    },
  }
</script>

<style lang="scss" scoped>
  // App条目
  .al_app_item {
    width: 100%;
    height: 50px;
    text-align: center;
    // App条目盒子
    .al_app_item_box {
      float: left;
      width: 100%;
      height: 60px;
      line-height: 50px;
      padding: 5px 20px;
      text-align: center;
      /*图标*/
      img {
        display: inline-block;
        float: left;
        height: 40px;
        width: 40px;
        margin: 5px 0;
      }
      /*App名称*/
      .al_app_item_name {
        /*width: calc(100% - 15px - 50px - 48px - 47px - 20px);*/
        width: calc(100% - 180px);
        line-height: 50px;
        float: left;
        margin: 0 15px;
        font-size: 1rem;
        text-align: left;
        white-space:nowrap;
        text-overflow:ellipsis;
        overflow:hidden;
      }
      /*按钮盒子*/
      .al_app_item_switch_box, .al_app_item_settings_box {
        float: right;
        height: 50px;
        line-height: 50px;
        text-align: center;
      }
      .al_app_item_switch_box {
        padding-top: 3px;
      }
      /*设置按钮*/
      .al_app_item_settings_icon {
        color: grey;
        margin: 0 10px;
      }
    }
  }
</style>
