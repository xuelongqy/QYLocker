<template>
  <div id="index">
    <!--底部栏-->
    <mu-flexbox class="index_footer_box">
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
    data() {
      return {
        msg: 'Welcome to Your Vue.js App'
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
