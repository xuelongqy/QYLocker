<!--
 @Title: 主题卡片
 @Description: 主题卡片页面
 @author XueLong xuelongqy@foxmail.com
 @date 2018/4/12 10:03
 @update_author
 @update_time
 @version V1.0
-->
<template>
  <mu-flat-button class="theme_card" @click="onThemeClick">
    <!--主题卡片盒子-->
    <div class="theme_card_box">
      <!--主题图片-->
      <img class="theme_img" :src="themeInfo.imageUrl" :alt="themeInfo.name" onerror="src='./static/image/ic_broken_image_grey.png'"/>
      <!--已使用标签-->
      <mu-badge v-if="themeInfo.name == $store.state.LockAppsConfig.lockAppsConfig.theme"
        :content="this.$t('theme.used')" class="theme_badge" primary slot="right"/>
      <!--已下载标签-->
      <mu-badge v-if="isDownloaded"
        :content="$t('theme.haveDownloaded')" class="theme_badge" secondary slot="right"/>
      <!--主题信息盒子-->
      <div class="theme_info_box">
        <!--主题名字-->
        <p class="theme_name">
          {{this.themeInfo.name}}
        </p>
        <!--主题作者-->
        <p class="theme_author">
          {{this.$t("theme.author")}}: {{this.themeInfo.author}}
        </p>
        <!--主题发布日期-->
        <p class="theme_date">
          {{this.$t("theme.date")}}: {{this.themeInfo.date}}
        </p>
      </div>
    </div>
  </mu-flat-button>
</template>

<script>
    export default {
      name: "ThemeCard",
      // 参数
      props: {
        // 主题信息
        themeInfo: {
          type: Object,
          default() {
            return {
              id:-1,
              imageUrl: "",
              name: "",
              author: "",
              date: ""
            }
          }
        },
        // 是否下载
        isDownloaded: {
          type: Boolean,
          default: false
        }
      },
      // 方法
      methods: {
        // 主题点击事件
        onThemeClick() {
          this.$router.push("/index/theme-info/" + this.themeInfo.id)
        }
      }
    }
</script>

<style lang="scss">
  // 主题卡片
  .theme_card {
    width: 100%;
    height: 150px;
    text-align: center;
    border-bottom: 1px gainsboro solid;

    // 主题卡片盒子
    .theme_card_box {
      width: 100%;
      height: 150px;
      padding: 5px 20px;
      float: left;

      // 主题图片
      .theme_img {
        display: block;
        width: 100px;
        height: 140px;
        float: left;
      }
      // 悬浮标签
      .theme_badge {
        float: right;
        margin-left: 5px;
      }
      // 主题信息盒子
      .theme_info_box {
        position: absolute;
        bottom: 0;
        right: 0;
        padding: 5px 20px;
        float: right;
        text-align: right;
        p {
          margin: 2px 0;
          line-height: 30px;
        }
        // 主题名字
        .theme_name {
          font-size: 1.2rem;
          font-weight: bold;
        }
      }
    }
  }
</style>
