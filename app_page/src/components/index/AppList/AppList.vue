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
    <mu-dialog dialogClass="settings_dialog" :open="settingsDialogState" @close="onSettingsClose" :title="settingsDialogTitle" scrollable>
      <!--过滤页面-->
      <mu-menu v-if="settingsDialogType === 'filterPage'">
        <!--页面列表-->
        <mu-switch
          v-for="(activity,index) in settingsFilterPagesActivities" :key="index"
          class="filter_page_switch"
          labelClass="filter_page_switch_label"
          :label="activity"
          @change="onFilterPageSwitch(activity, $event)"
          :value="arrContains(settingsDialogAppInfo.filterActivity, activity)"
          labelLeft />
      </mu-menu>
      <!--设置项-->
      <mu-menu v-if="settingsDialogType === ''">
        <!--独立设置-->
        <mu-switch :label="$t('appList.independentSetting')" labelLeft class="independent_setting_switch" v-model="settingsDialogAppInfo.isIndependent"/>
        <!--密码管理-->
        <mu-menu-item :title="$t('appList.pwdManagement')" @click="onPwdManagement"/>
        <!--过滤页面-->
        <mu-menu-item :title="$t('appList.filterPage')" @click="onFilterPage"/>
      </mu-menu>
      <!--密码管理-->
      <mu-menu v-if="settingsDialogType === 'pwdManagement'" style="text-align: center">
        <!--密码列表-->
        <div class="sd_pwd_box" v-for="(theme,index) in settingsDialogAppInfo.themes" :key="index">
          <p class="sd_pwd_name">{{theme.name}}</p>
          <!--删除按钮-->
          <mu-icon-button icon="delete" class="sd_pwd_remove" @click="removeAppPwd(theme.name, index)"/>
        </div>
        <!--添加按钮-->
        <mu-float-button icon="add" mini @click="addAppPwd"/>
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
          "themes": [],
          "versionCode": 0,
          "versionName": "",
          filterActivity: []
        },
        // 弹窗类型
        settingsDialogType: "",
        // 过滤页面数据
        settingsFilterPagesActivities: []
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
      },
      // 弹窗标题名字
      settingsDialogTitle() {
        if (this.settingsDialogType === "") {
          return this.settingsDialogAppInfo.appName
        }else if (this.settingsDialogType === "filterPage") {
          return this.settingsDialogAppInfo.appName+'-'+this.$t('appList.filterPage')
        }else if (this.settingsDialogType === "pwdManagement") {
          return this.settingsDialogAppInfo.appName+'-'+this.$t('appList.pwdManagement')
        }
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
        this.settingsDialogType = ""
        this.settingsFilterPagesActivities = []
      },
      // 打开过滤页面
      onFilterPage: function () {
        this.settingsDialogType = "filterPage"
        LockAppsUtil.getActivities((activities)=>{
          this.settingsFilterPagesActivities = activities
        },this.settingsDialogAppInfo.packageName)
      },
      // 打开密码管理页面
      onPwdManagement: function () {
        this.settingsDialogType = "pwdManagement"
      },
      // 修改过滤页面
      onFilterPageSwitch: function (activity, event) {
        // alert(event)
        // 判断添加还是删除
        if (event) {
          LockAppsUtil.addFilterActivity(this.settingsDialogAppInfo.packageName, activity)
          this.$store.commit('updateAppsInfoByView', {
            index: this.settingsDialogAppIndex,
            key: "filterActivity",
            value: this.settingsDialogAppInfo.filterActivity.push(activity)
          })
        }else {
          LockAppsUtil.removeFilterActivity(this.settingsDialogAppInfo.packageName, activity)
          this.$store.commit('updateAppsInfoByView', {
            index: this.settingsDialogAppIndex,
            key: "filterActivity",
            value: this.settingsDialogAppInfo.filterActivity.splice(this.settingsDialogAppInfo.filterActivity.indexOf(activity),1)
          })
        }
      },
      // 添加应用密码
      addAppPwd() {
        this.$router.push("/index/theme/" + this.settingsDialogAppIndex)
        this.onSettingsClose()
      },
      // 删除应用密码
      removeAppPwd(name, index) {
        LockAppsUtil.removeAppPwd(this.settingsDialogAppInfo.packageName, name)
        this.$store.commit('removeAppPwd', {
          appIndex: this.settingsDialogAppIndex,
          themeIndex: index
        })
      },
      // 判断字符串是否包含
      arrContains: function (arr, obj) {
        var i = arr.length
        while(i--) {
          if(arr[i] === obj) {
            return true
          }
        }
        return false
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
    // 密码盒子
    .sd_pwd_box {
      width: 100%;
      height: 50px;
      line-height: 50px;
      margin-bottom: 10px;
      .sd_pwd_name {
        float: left;
        padding: 0;
        margin: 0 0 0 10px;
        font-size: 1.2rem;
      }
      .sd_pwd_remove {
        margin: 0 10px 0 0;
        float: right;
      }
    }
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
