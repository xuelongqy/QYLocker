<template>
  <div id="theme_info">
    <!--主题头部-->
    <mu-paper class="tf_head" :zDepth="2">
      <!--主题名称-->
      <div class="tf_name">{{themeInfo.name}}</div>
      <div style="float: right">
        <!--主题作者-->
        <div class="tf_author">{{themeInfo.author}}</div>
        <!--主题日期-->
        <div class="tf_date">{{themeInfo.date}}</div>
      </div>
    </mu-paper>
    <!--主题主干区域-->
    <div class="tf_main">
      <!--主题图片盒子-->
      <div class="tf_images_box">
        <swiper :options="swiperOption" class="images_swiper">
          <swiper-slide v-for="(image,index) in themeInfo.images" :key="index">
            <img class="tf_images" :src="image" onerror="src='./static/image/ic_broken_image_grey.png'">
          </swiper-slide>
          <div class="swiper-pagination" slot="pagination"></div>
        </swiper>
      </div>
      <!--主题介绍盒子-->
      <mu-paper class="tf_introduced_box" :zDepth="1">
        <p class="tf_introduced_text">
          {{this.$t("theme.introduced")}}
        </p>
        <p class="tf_introduced">
          {{themeInfo.describe}}
        </p>
      </mu-paper>
    </div>
    <!--主题底部栏-->
    <mu-paper class="tf_footer" :zDepth="2">
      <!--下载按钮-->
      <mu-raised-button v-if="canDownload" :label="$t('theme.download')" class="tf_operation_btn" primary @click="downloadTheme"/>
      <!--使用按钮-->
      <mu-raised-button v-if="canUse" :label="$t('theme.use')" class="tf_operation_btn" secondary @click="useTheme"/>
      <!--修改密码按钮-->
      <mu-raised-button v-if="canChange" :label="$t('theme.changePwd')" class="tf_operation_btn" backgroundColor="#a4c639" @click="useTheme"/>
      <!--删除按钮-->
      <mu-icon-button v-if="canDelete" class="tf_delete_btn" icon="delete" @click="delete_dialog = true"/>
    </mu-paper>
    <mu-dialog :open="delete_dialog" :title="$t('theme.delete')" @close="delete_dialog = false">
      {{$t('theme.delete')}} - themeInfo.name ?
      <mu-flat-button slot="actions" @click="delete_dialog = false" primary :label="$t('theme.cancel')"/>
      <mu-flat-button slot="actions" primary @click="deleteTheme" :label="$t('theme.ok')"/>
    </mu-dialog>
  </div>
</template>

<script>
  import ThemeUtil from "../../../assets/util/cordova/ThemeUtil"
  import ToastUtil from "../../../assets/util/cordova/toastUtil"

  export default {
    name: "ThemeInfo",
    // 数据
    data() {
      return {
        // 轮播参数
        swiperOption: {
          slidesPerView: 'auto',
          spaceBetween: 30,
          pagination: {
            el: '.swiper-pagination',
            clickable: true,
            hide: true
          }
        },
        // 删除提示框
        delete_dialog: false
      }
    },
    // 计算变量
    computed: {
      // 主题信息
      themeInfo() {
        if (this.$route.params.themeTab == "tabDownload") {
          return this.$store.state.Theme.downloadedThemes[this.$route.params.index]
        }else if (this.$route.params.themeTab == "tabStore") {
          return this.$store.state.Theme.storeThemes[this.$route.params.index]
        }
        return {
          id:-1,
          images: [],
          name: "",
          author: "",
          date: "",
          describe: ""
        }
      },
      // 是否为应用添加密码
      isAppAddPwd() {
        return eval(this.$route.params.isAppAddPwd)
      },
      // 应用包名,当应用添加密码时有效
      appIndex() {
        return parseInt(this.$route.params.appIndex)
      },
      // 判断是否未下载过的主题
      isDownloaded() {
        if (this.$route.params.themeTab== 'tabDownload') return true
        for (var index in this.$store.state.Theme.downloadedThemes) {
          if (this.$store.state.Theme.downloadedThemes[index].name == this.themeInfo.name) return true
        }
        return false
      },
      // 可以下载
      canDownload() {
        return !this.isDownloaded;
      },
      // 可以使用
      canUse() {
        return this.isDownloaded && !this.canChange
      },
      // 可以修改
      canChange() {
        return (this.themeInfo.name == this.$store.state.LockAppsConfig.lockAppsConfig.theme) && !this.isAppAddPwd
      },
      // 可以删除
      canDelete() {
        return this.isDownloaded && (this.themeInfo.name != this.$store.state.LockAppsConfig.lockAppsConfig.theme)
      }
    },
    // 方法
    methods: {
      // 删除主题
      deleteTheme() {
        ThemeUtil.deleteTheme(()=>{
          window.history.back()
          this.$store.dispatch('getDownloadedThemesInfo')
        } ,this.themeInfo.name)
      },
      // 使用主题
      useTheme() {
        ThemeUtil.setThemePwd((data)=> {
          // 判断主题是否设置成功
          if (data.state) {
            // 判断是否为应用添加密码,修改Vuex-Store仓库中的数据
            if (this.isAppAddPwd) {
              this.$store.commit('addAppPwd', {
                appIndex: this.appIndex,
                "name": data.name,
                "theme": this.themeInfo.name
              })
            }else {
              this.$store.commit('setTheme', this.themeInfo.name)
            }
            ToastUtil.showLongToast(this.$t('theme.themeSetSuccess'))
          }else {
            // 主题设置失败
            ToastUtil.showLongToast(this.$t('theme.themeNoSet'))
          }
        },this.themeInfo.name, this.isAppAddPwd, this.isAppAddPwd?this.$store.state.LockAppsConfig.allAppsInfo[this.appIndex].packageName:"")
      },
      // 下载主题
      downloadTheme() {
        ToastUtil.showLongToast(this.$t('theme.startDownload'))
        ThemeUtil.downloadTheme((status) => {
          // 判断是否下载成功
          if (status) {
            ToastUtil.showLongToast(this.$t('theme.downloadSuccess'))
            // 更新下载的主题列表
            this.$store.dispatch('getDownloadedThemesInfo')
          }else {
            ToastUtil.showLongToast(this.$t('theme.downloadFailure'))
          }
        },this.themeInfo.file)
      }
    },
  }
</script>

<style lang="scss">
  .swiper-slide {
    text-align: center;
    background: white;
  }
  #theme_info {
    height: 100%;
    width: 100%;
    // 主题头部
    .tf_head {
      position: absolute;
      top: 0;
      width: 100%;
      height: 60px;
      padding: 5px 50px;
      z-index: 2;
      // 主题名字
      .tf_name {
        float: left;
        line-height: 50px;
        font-size: 1.4rem;
        font-weight: bold;
      }
      // 主题作者和日期
      .tf_author {
        font-size: 1rem;
      }
      .tf_author,.tf_date {
        line-height: 25px;
      }
    }
    // 主题主干区域
    .tf_main {
      position: absolute;
      top: 60px;
      left: 0;
      right: 0;
      bottom: 60px;
      overflow:auto;
      // 主题图片盒子
      .tf_images_box {
        width: 100%;
        height: 300px;
        margin-top: 10px;
        // 轮播器
        .images_swiper {
          height: 100%;
          // 图片
          .tf_images {
            height: 300px;
            width: auto;
          }
        }
      }
      // 主题介绍盒子
      .tf_introduced_box {
        width: 94%;
        margin: 10px 3%;
        padding: 10px;
        .tf_introduced_text {
          margin: 0;
          font-size: 1rem;
          font-weight: bold;
        }
        .tf_introduced {
          margin: 0;
          text-indent:2em
        }
      }
    }
    // 主题底部栏
    .tf_footer {
      position: absolute;
      bottom: 0;
      width: 100%;
      height: 60px;
      z-index: 2;
      text-align: center;
      line-height: 60px;
      // 主题操作按钮
      .tf_operation_btn {
        width: 60%;
      }
      // 删除按钮
      .tf_delete_btn {
        position: absolute;
        top: 6px;
        right: 6px;
        color: darkslategray;
      }
    }
  }
</style>
