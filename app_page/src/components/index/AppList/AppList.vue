<template>
  <div id="app_list">
    <!--环形加载条-->
    <div v-if="$store.state.LockAppsConfig.loadAppsInfo" class="al_load_box">
      <div class="al_load_bar_box">
        <mu-paper class="al_load_bar" circle :zDepth="3">
          <mu-circular-progress class="al_load_bar_progress" :size="30"/>
        </mu-paper>
      </div>
    </div>
    <!--选项卡-->
    <mu-tabs :value="activeTab" @change="handleTabChange">
      <mu-tab value="tabAll" :title="$t('appList.allApps')"/>
      <mu-tab value="tabNonSys" :title="$t('appList.nonSysApps')"/>
      <mu-tab value="tabSys" :title="$t('appList.sysApps')"/>
    </mu-tabs>
    <!--应用列表-->
    <div class="al_apps_tab">
      <scroller
          :on-refresh="refreshAppInfo">
        <!--App条目-->
        <app-item v-for="(appInfo,index) in appsInfoList"
                  :key = 'appInfo.packageName'
                  :index = 'index'
                  :appInfo = "appInfo"
                  :onSettings = "onSettings">
        </app-item>
      </scroller>
    </div>
    <!--App搜索框-->
    <app-search :onSearchKey="onSearchKey"/>
    <!--设置弹出框-->
    <mu-dialog dialogClass="settings_dialog" :open="settingsDialogState" @close="onSettingsClose" :title="isSettingsFilterPage?settingsDialogAppInfo.appName+'-'+$t('appList.filterPage'):settingsDialogAppInfo.appName" scrollable>
      <!--过滤页面-->
      <mu-menu v-if="isSettingsFilterPage">
        <mu-switch labelClass="filter_page_switch_label" :label="' '" labelLeft class="filter_page_switch"/>
      </mu-menu>
      <!--设置项-->
      <mu-menu v-else>
        <!--独立设置-->
        <mu-switch :label="$t('appList.independentSetting')" labelLeft class="independent_setting_switch" v-model="settingsDialogAppInfo.isIndependent"/>
        <!--添加密码-->
        <mu-menu-item :title="$t('appList.addPwd')"/>
        <!--过滤页面-->
        <mu-menu-item :title="$t('appList.filterPage')" @click="isSettingsFilterPage = true"/>
      </mu-menu>
    </mu-dialog>
  </div>
</template>

<script>
  import AppItem from './AppItem/AppItem'
  import AppSearch from './AppSearch/AppSearch'
  import LockAppsUtil from '../../../assets/util/cordova/lockAppsUtil'
  import toastUtil from '../../../assets/util/cordova/toastUtil'

  export default {
    name: "AppList",
    // 页面启动时
    created() {
      // 获取应用信息
      if (this.$store.state.LockAppsConfig.allAppsInfo.length === 0) {
        this.$store.dispatch('getAllAppsInfo')
      }
    },
    // 数据
    data() {
      return {
        // 当前激活的选项卡
        activeTab: 'tabAll',
        // 搜索关键字
        searchKey: "",
        // 设置框显示状态
        settingsDialogState: false,
        // 设置框应用下标
        settingsDialogAppIndex: -1,
        // 设置框显示信息
        settingsDialogAppInfo: {
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
        },
        // 设置框过滤页面
        isSettingsFilterPage: false
      }
    },
    // 计算方法
    computed: {
      // 全部应用
      appsInfoList() {
        // 判断应用类型
        var appsInfo = this.$store.state.LockAppsConfig.allAppsInfo.filter((appInfo) => {
          return (this.activeTab === 'tabAll' ||
            (this.activeTab === 'tabNonSys' && !appInfo.isSystemAPP) ||
            (this.activeTab === 'tabSys' && appInfo.isSystemAPP))
        })
        // 匹配关键字搜索
        if (this.searchKey == "" || this.searchKey == null)
          return appsInfo
        else {
          return appsInfo.filter((appsInfo) => {
              return appsInfo.appName.indexOf(this.searchKey) > -1
            }
          )
        }
      },
      // 是否独立设置
      isIndependentSetting() {
        return this.settingsDialogAppInfo.isIndependent
      }
    },
    // 监听器
    watch: {
      // 监听独立设置变化
      isIndependentSetting(newValue, oldValue) {
        LockAppsUtil.setIndependentSettingIState((isSuccess)=>{
          if (isSuccess) {
            this.$store.commit('updateAppsInfoByView', {
              index: this.settingsDialogAppIndex,
              key: 'isIndependent',
              value: newValue
            })
          }else {
            toastUtil.showLongToast(this.$t('appList.setIndependentFail'))
            this.settingsDialogAppInfo.isIndependent = oldValue
          }
        },this.settingsDialogAppInfo.packageName,newValue)
      }
    },
    // 方法
    methods: {
      // 更换选项卡
      handleTabChange: function(val) {
        this.activeTab = val
      },
      // 刷新App列表信息
      refreshAppInfo: function (done) {
        done()
        this.$store.dispatch('getAllAppsInfo')
      },
      // 搜索关键字改变
      onSearchKey: function (key) {
        this.searchKey = key
      },
      // 设置框事件
      onSettings: function (index, appInfo) {
        this.settingsDialogState = true
        this.settingsDialogAppIndex = index
        this.settingsDialogAppInfo = appInfo
      },
      // 设置框关闭
      onSettingsClose: function () {
        this.settingsDialogState = false
        this.isSettingsFilterPage = false
      }
    },
    // 组件
    components: {
      'app-item': AppItem,
      'app-search': AppSearch
    }
  }
</script>

<style lang="scss">
  #app_list {
    // 加载盒子,加载时覆盖
    .al_load_box {
      position: fixed;
      top: 0;
      bottom: 0;
      left: 0;
      right: 0;
      z-index: 1000;
    }
    // 环形加载条
    .al_load_bar_box {
      margin-top: 140px;
      width: 100%;
      text-align: center;
      .al_load_bar {
        display: inline-block;
        width: 40px;
        height: 40px;
        text-align: center;
        .al_load_bar_progress {
          margin: 5px;
        }
      }
    }
    // 选项卡
    .al_apps_tab {
      position: absolute;
      top: 48px;
      bottom: 0;
      left: 0;
      right: 0;
      padding: 5px 0;
      overflow: auto;
      -webkit-overflow-scrolling: touch;
      z-index: -1;
    }
  }
  // 设置弹出窗口
  .settings_dialog {
    width: 90%;
  }
  // 独立开关按钮
  .independent_setting_switch {
    height: 48px;
    line-height: 48px;
    width: 100%;
    padding: 12px 16px;
    text-align: center;
  }
  // 过滤页面开关按钮
  .filter_page_switch {
    height: 48px;
    line-height: 48px;
    width: 100%;
    padding-right: 12px;
  }
  .filter_page_switch_label {
    word-break:break-all;
    width: 80%;
    font-size: 1rem;
    line-height: 16px;
  }
</style>
