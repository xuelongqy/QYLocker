<template>
  <div id="theme">
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
        <theme-card></theme-card>
        <theme-card></theme-card>
        <theme-card></theme-card>
        <theme-card></theme-card>
        <theme-card></theme-card>
        <theme-card></theme-card>
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

  export default {
    name: "Theme",
    // 数据模型
    data() {
      return {
        // 当前Tab选项卡
        activeTab: "tabDownload",
        // 搜索关键字
        searchKey: "",
        // 缓存刷新完成回调
        refreshDoneCallback: null,
        // 缓存加载完成回调
        infiniteDoneCallback: null
      }
    },
    // 方法
    methods: {
      // Tab切换
      handleTabChange(val) {
        if (this.refreshDoneCallback != null) {
          this.refreshDoneCallback()
        }
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
        this.refreshDoneCallback = done
        // 如果是下载的主题则不进行操作
        if (this.activeTab == "tabDownload") {
          done()
          this.refreshDoneCallback = null
        }
      },
      // 底部加载
      infinite(done) {
        this.infiniteDoneCallback = done
        // 如果是下载的主题则不进行操作
        if (this.activeTab == "tabDownload") {
          done()
          this.infiniteDoneCallback = null
        }
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
    }
  }
</style>
