<template>
  <div id="index">
    <!--主干区域-->
    <div class="index_main">
      <!--左边区域盒子-->
      <div class="index_main_left_box animated bounceInLeft">
        <p class="index_main_text">{{this.$t('index.hithertoUnknown')}}</p>
        <p class="index_main_text">{{this.$t('index.lockerApp')}}</p>
        <p class="index_main_label">{{this.$t('index.upToYou')}}</p>
        <mu-raised-button class="index_main_download_btn" :label="$t('index.downloadSoft')" primary/>
        <mu-raised-button class="index_main_more_theme_btn" :label="$t('index.moreTheme')" @click="$router.push('/theme')"/>
      </div>
      <div class="index_main_right_box animated pulse">
        <!--App截图-->
        <img class="index_main_app_ss" src="../../assets/app_screenshots.png">
      </div>
    </div>
    <!--底部栏-->
    <mu-flexbox class="index_footer_box animated bounceInUp">
      <mu-flexbox-item class="index_footer_item">
        <!--图标-->
        <mu-icon value="security" :size="40" color="white"/>
        <!--文字-->
        <div class="index_footer_item_text_box">
          <p class="index_footer_item_text_name">{{this.$t('index.safe')}}</p>
          <p class="index_footer_item_text_info">{{this.$t('index.safeLabel')}}</p>
        </div>
      </mu-flexbox-item>
      <mu-flexbox-item class="index_footer_item">
        <!--图标-->
        <mu-icon value="flash_on" :size="40" color="white"/>
        <!--文字-->
        <div class="index_footer_item_text_box">
          <p class="index_footer_item_text_name">{{this.$t('index.efficient')}}</p>
          <p class="index_footer_item_text_info">{{this.$t('index.efficientLabel')}}</p>
        </div>
      </mu-flexbox-item>
      <mu-flexbox-item class="index_footer_item">
        <!--图标-->
        <mu-icon value="layers" :size="40" color="white"/>
        <!--文字-->
        <div class="index_footer_item_text_box">
          <p class="index_footer_item_text_name">{{this.$t('index.custom')}}</p>
          <p class="index_footer_item_text_info">{{this.$t('index.customLabel')}}</p>
        </div>
      </mu-flexbox-item>
    </mu-flexbox>
  </div>
</template>

<script>
  export default {
    name: 'HelloWorld',
    // 数据
    data() {
      return {
      }
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss">
  // 主页
  #index {
    height: 100%;
    width: 100%;
    text-align: left;
    // 主干区域
    .index_main {
      position: absolute;
      width: 100%;
      padding-top: 80px;
      height: calc(100% - 80px);
      overflow: auto;
      text-align: center;
      // 左侧区域
      .index_main_left_box {
        display: inline-block;
        vertical-align: center;
        margin: 20px 50px;
        color: white;
        width: 250px;
        text-align: center;
        .index_main_text {
          font-size: 1.5rem;
          margin: 0;
          padding: 0;
        }
        .index_main_label {
          margin: 50px 0;
        }
        .index_main_download_btn {
          margin-right: 20px;
        }
        .index_main_more_theme_btn {
          background: transparent;
        }
      }
      // 右侧区域
      .index_main_right_box {
        display: inline-block;
        vertical-align: top;
        // App截图
        .index_main_app_ss {
          margin: 0 50px;
          width: 200px;
        }
      }
    }
    // 底部栏
    .index_footer_box {
      position: absolute;
      bottom: 0;
      left: 0;
      right: 0;
      width: 100%;
      color: white;
      background: rgba(225,225,225,0.2);
      // 底部栏条目
      .index_footer_item {
        padding: 20px 0;
        text-align: center;
        // 文字盒子
        .index_footer_item_text_box {
          margin-left: 5px;
          display: inline-block;
          height: 40px;
          p {
            margin: 0;
            padding: 0;
            line-height: normal;
            text-align: left;
          }
          .index_footer_item_text_name {
            font-size: 1rem;
            margin-bottom: 5px;
          }
        }
      }
    }
  }
</style>
