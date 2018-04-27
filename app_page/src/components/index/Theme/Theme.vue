<template>
  <div id="theme">
    <!--环形加载条-->
    <div class="theme_load_box" v-if="$store.state.Theme.loadThemesInfo">
      <div class="theme_load_bar_box">
        <mu-paper class="al_load_bar" circle :zDepth="3">
          <mu-circular-progress class="al_load_bar_progress" :size="30"/>
        </mu-paper>
      </div>
    </div>
    <!--选项卡-->
    <mu-tabs :value="activeTab" @change="handleTabChange">
      <mu-tab value="tabDownload" :title="$t('theme.downloaded')"/>
      <mu-tab value="tabStore" :title="$t('theme.store')"/>
    </mu-tabs>
    <!--主题列表-->
    <div class="theme_apps_tab">
      <scroller
        :on-refresh="refresh"
        :on-infinite="infinite">
        <!--导入主题-->
        <mu-flat-button class="theme_import" v-if="activeTab == 'tabDownload'" @click="onImportTheme">
          {{this.$t('theme.importTheme')}}
        </mu-flat-button>
        <!--主题卡片-->
        <theme-card
          v-for="(themeInfo,index) in themeList"
          :key = 'themeInfo.name'
          :index = 'index'
          :themeTab = 'activeTab'
          :isDownloaded = 'isDownloadedTheme(themeInfo.name)'
          :themeInfo = 'themeInfo'/>
      </scroller>
    </div>
    <!--主题搜索框-->
    <theme-search
      :onSearchKey="onSearchKey"
      :onSearch="onSearch" />
  </div>
</template>

<script>
  import ThemeCard from "./ThemeCard/ThemeCard"
  import ThemeSearch from "../AppList/AppSearch/AppSearch"
  import FilePicker from "../../../assets/util/cordova/FilePicker"
  import ThemeUtil from "../../../assets/util/cordova/ThemeUtil"
  import toastUtil from "../../../assets/util/cordova/toastUtil"

  export default {
    name: "Theme",
    // 数据模型
    data() {
      return {
        // 当前Tab选项卡
        activeTab: "tabDownload",
        // 搜索关键字
        searchKey: "",
        // 缓存加载完成回调
        infiniteDoneCallback: null
      }
    },
    // 页面创建时
    created() {
      // 获取应用信息
      if (this.$store.state.Theme.downloadedThemes.length === 0) {
        this.$store.dispatch('getDownloadedThemesInfo')
      }
    },
    // 计算方法
    computed: {
      // 主题列表
      themeList() {
        // 判断Tab的模式
        var themes = []
        if (this.activeTab == "tabDownload") {
          themes = this.$store.state.Theme.downloadedThemes
        }else if (this.activeTab == "tabStore") {
          themes = this.$store.state.Theme.storeThemes
        }else {
          return []
        }
        // 匹配关键字搜索
        if (this.searchKey == "" || this.searchKey == null)
          return themes
        else {
          return themes.filter((themeInfo) => {
              return themeInfo.name.indexOf(this.searchKey) > -1
            }
          )
        }
      }
    },
    // 方法
    methods: {
      // Tab切换
      handleTabChange(val) {
        if (this.infiniteDoneCallback != null) {
          this.infiniteDoneCallback()
        }
        this.activeTab = val
      },
      // 搜索关键字改变时
      onSearchKey(searchKey) {
        this.searchKey = searchKey
      },
      // 搜索按钮确定事件
      onSearch(searchKey) {
      },
      // 下拉刷新
      refresh(done) {
        done()
        // 如果是下载的主题则不进行操作
        if (this.activeTab == "tabDownload") {
          this.$store.dispatch('getDownloadedThemesInfo')
        }else if (this.activeTab == "tabStore") {
          this.$store.dispatch('getOnlineThemesInfo')
        }
      },
      // 底部加载
      infinite(done) {
        this.infiniteDoneCallback = done
        // 如果是下载的主题则不进行操作
        if (this.activeTab == "tabDownload") {
          done()
          this.infiniteDoneCallback = null
        }else if (this.activeTab == "tabStore") {
          this.$store.dispatch('getOnlineThemesInfoMore')
        }
      },
      // 判断是否为下载过的主题
      isDownloadedTheme(themeName) {
        if (this.activeTab == 'tabDownload') return false
        for (var index in this.downloadedThemes) {
          if (this.downloadedThemes[index].name == themeName) return true
        }
        return false
      },
      // 导入主题
      onImportTheme() {
        FilePicker.chooseFile(this.importThemeChange,this.$t("theme.chooseTheme"),"application/zip")
      },
      // 主题文件选择
      importThemeChange(themeUrl) {
        ThemeUtil.importTheme((state)=>{
          if (state) {
            this.$store.dispatch('getDownloadedThemesInfo')
          }else {
            toastUtil.showLongToast(this.$t("theme.importFail"))
          }
        },themeUrl)
      }
    },
    // 组件
    components: {
      ThemeSearch,
      ThemeCard,
      'theme-card': ThemeCard,
      'theme-search': ThemeSearch
    }
  }
</script>

<style lang="scss">
  #theme {
    // 加载盒子,加载时覆盖
    .theme_load_box {
      position: absolute;
      top: 0;
      bottom: 0;
      left: 0;
      right: 0;
      z-index: 1000;
    }
    // 环形加载条
    .theme_load_bar_box {
      margin-top: 80px;
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
    .theme_apps_tab {
      position: absolute;
      top: 48px;
      bottom: 0;
      left: 0;
      right: 0;
      padding: 5px 0;
      overflow: auto;
      -webkit-overflow-scrolling: touch;
      z-index: -1;

      // 导入主题
      .theme_import {
        width: 100%;
        height: 50px;
        border-bottom: 1px gainsboro solid;
      }
    }
  }
</style>
