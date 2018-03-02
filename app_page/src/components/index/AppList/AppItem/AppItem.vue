<template>
  <!--App条目-->
  <mu-flat-button class="al_app_item" @click="onAppItem">
    <div class="al_app_item_box">
      <img :src="appInfo.appIcon" :alt="appInfo.appName">
      <p class="al_app_item_name">{{appInfo.appName}}</p>
      <div class="al_app_item_switch_box">
        <mu-switch v-model="isLock"/>
      </div>
      <div class="al_app_item_settings_box">
        <mu-icon-button icon="settings" class="al_app_item_settings_icon" @click.stop="onAppLockSettings"/>
      </div>
    </div>
  </mu-flat-button>
</template>

<script>
  import LockAppsUtil from '../../../../assets/util/cordova/lockAppsUtil'

  export default {
    name: "app-item",
    // 数据
    data() {
      return {
        isLock: this.appInfo.isLock
      }
    },
    // 参数
    props: {
      // 下标
      index: {
        type: Number,
        default: -1
      },
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
    // 监听器
    watch: {
      // 设置应用加锁状态
      isLock(newValue, oldValue) {
        if (newValue) {
          LockAppsUtil.addLockApp(this.appInfo.packageName)
        }else {
          LockAppsUtil.removeLockApp(this.appInfo.packageName)
        }
        this.$store.commit('updateAppsInfoByView', {
          index: this.index,
          key: 'isLock',
          value: newValue
        })
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
