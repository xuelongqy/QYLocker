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
        infiniteDoneCallback: null,
        // 下载的主题
        downloadedThemes: [
          {
            id:0,
            imageUrl: "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523524052725&di=bcbaa3e03290bb1d6edc942134418305&imgtype=0&src=http%3A%2F%2Fs11.sinaimg.cn%2Flarge%2F001ndUTJgy6P7a6eyJY9a%26690",
            name: "图案",
            author: "KnoYo",
            date: "2018-01-05"
          },
          {
            id:1,
            imageUrl: "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523524114090&di=7629190fb3369220de024271e798cce0&imgtype=0&src=http%3A%2F%2Fc11.eoemarket.com%2Fapp0%2F699%2F699717%2Fscreen%2F3454157.jpg",
            name: "PIN",
            author: "KnoYo",
            date: "2018-01-06"
          },
          {
            id:2,
            imageUrl: "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523524647758&di=b4648a10795cecc06ab927838075de3a&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F01f55357877f0b0000018c1b4dfe99.jpg",
            name: "滑动",
            author: "KnoYo",
            date: "2018-01-07"
          },
          {
            id:3,
            imageUrl: "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1524119808&di=64972412a8bdbc1cc8a93e498c28daa5&imgtype=jpg&er=1&src=http%3A%2F%2Fattach.bbs.miui.com%2Fforum%2F201610%2F04%2F155701dnntm2cctynnhyx2.png",
            name: "数字",
            author: "KnoYo",
            date: "2018-01-07"
          }
        ],
        // 商店的主题
        storeThemes: [
          {
            id:4,
            imageUrl: "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523524796163&di=53da2bbaf09e91007a428308baaf7b0f&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F01cdb5563095366ac72548789738c4.png%401280w_1l_2o_100sh.png",
            name: "起航",
            author: "KnoYo",
            date: "2018-01-05"
          },
          {
            id:5,
            imageUrl: "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523524825809&di=5c42d16aa287be919493768894512f5b&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F016689591c02abb5b3086ed4131abe.jpg%401280w_1l_2o_100sh.png",
            name: "足球",
            author: "KnoYo",
            date: "2018-01-06"
          },
          {
            id:6,
            imageUrl: "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523524950217&di=4f2691a543b92cbed7d5c0280474203e&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F01ea105568a7da00000127160b24f3.jpg%40900w_1l_2o_100sh.jpg",
            name: "下拉",
            author: "KnoYo",
            date: "2018-01-07"
          },
          {
            id:7,
            imageUrl: "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1524119808&di=64972412a8bdbc1cc8a93e498c28daa5&imgtype=jpg&er=1&src=http%3A%2F%2Fattach.bbs.miui.com%2Fforum%2F201610%2F04%2F155701dnntm2cctynnhyx2.png",
            name: "数字",
            author: "KnoYo",
            date: "2018-01-07"
          }
        ]
      }
    },
    // 计算方法
    computed: {
      themeList() {
        // 判断Tab的模式
        var themes = []
        if (this.activeTab == "tabDownload") {
          themes = this.downloadedThemes
        }else if (this.activeTab == "tabStore") {
          themes = this.storeThemes
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
      },
      // 导入主题
      onImportTheme() {
        FilePicker.chooseFile(this.importThemeChange,this.$t("theme.chooseTheme"),"application/zip")
      },
      // 主题文件选择
      importThemeChange(themeUrl) {
        ThemeUtil.importTheme((state)=>{
          alert(state)
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
