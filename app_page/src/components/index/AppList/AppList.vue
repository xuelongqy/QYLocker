<template>
  <div id="app_list">
    <!--环形加载条-->
    <div v-if="$store.state.AppList.loadAppsInfo" class="al_load_box">
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
        <app-item v-for="appInfo in appsInfoList"
                  :key = 'appInfo.packageName'
                  :appInfo = "appInfo">
        </app-item>
      </scroller>
    </div>
    <!--App搜索框-->
    <app-search :onSearchKey="onSearchKey">
    </app-search>
  </div>
</template>

<script>
  import AppItem from './AppItem/AppItem'
  import AppSearch from './AppSearch/AppSearch'

  export default {
    name: "AppList",
    // 页面启动时
    created() {
      // 获取应用信息
      if (this.$store.state.AppList.allAppsInfo.length === 0) {
        this.$store.dispatch('getAllAppsInfo')
      }
    },
    // 数据
    data() {
      return {
        // 当前激活的选项卡
        activeTab: 'tabAll',
        // 搜索关键字
        searchKey: ""
      }
    },
    // 计算方法
    computed: {
      // 全部应用
      appsInfoList() {
        // 判断应用类型
        var appsInfo = this.$store.state.AppList.allAppsInfo.filter((appInfo) => {
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
      }
    },
    // 监听器
    watch: {
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
        console.log(key)
        this.searchKey = key
      }
    },
    // 组件
    components: {
      'app-item': AppItem,
      'app-search': AppSearch
    }
  }
</script>

<style lang="scss" scoped>
  #app_list {
    // 加载盒子,加载时覆盖
    .al_load_box {
      position: absolute;
      top: 0;
      bottom: 0;
      left: 0;
      right: 0;
      z-index: 1000;
    }
    // 环形加载条
    .al_load_bar_box {
      margin-top: 60px;
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
</style>
