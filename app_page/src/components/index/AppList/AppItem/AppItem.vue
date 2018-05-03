<template>
  <!--App条目-->
  <mu-flat-button class="al_app_item" @click="onAppItem">
    <div class="al_app_item_box">
      <!--应用图标-->
      <img :src="appInfo.appIcon" :alt="appInfo.appName">
      <!--应用名称-->
      <p class="al_app_item_name">{{appInfo.appName}}</p>
      <!--开关盒子-->
      <div class="al_app_item_switch_box">
        <mu-switch v-model="isLock"/>
      </div>
      <!--设置盒子-->
      <div v-if="isLock" class="al_app_item_settings_box">
        <mu-icon-button icon="settings" class="al_app_item_settings_icon"
                        iconClass="al_app_item_settings_icon_style"
                        @click.stop="onAppLockSettings"/>
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
          "themes":[],
          "versionCode": 0,
          "versionName": ""
        }
      },
      // 设置按钮事件
      onSettings: {
        type: Function,
        default: function () {}
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
        LockAppsUtil.openApp(this.appInfo.packageName)
      },
      // App锁设置
      onAppLockSettings() {
        // 获取应用下标
        var appIndex = -1
        for (var index in this.$store.state.LockAppsConfig.allAppsInfo) {
          if (this.$store.state.LockAppsConfig.allAppsInfo[index].packageName == this.appInfo.packageName) {
            appIndex = index
            break
          }
        }
        // 调用设置回调
        this.onSettings(appIndex, this.appInfo)
      }
    },
  }
</script>

<style lang="scss">
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
        margin: 1px 10px;

        .al_app_item_settings_icon_style {
          width: 24px;
          height: 26px;
        }
      }
    }
  }
</style>
