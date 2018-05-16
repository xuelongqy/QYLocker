<!--
 @Title: 指纹控件
 @Description: 指纹控件布局
 @author XueLong xuelongqy@foxmail.com
 @date 2018/4/28 10:19
 @update_author
 @update_time
 @version V1.0
-->
<template>
  <div id="fingerprint" :class="fingerprintStyle">
    <mu-icon v-if="useFingerprint" value="fingerprint" :size="36" color="white"/>
    <p class="fingerprint_msg">{{fingerprintMsg}}</p>
  </div>
</template>

<script>
  export default {
    name: "Fingerprint",
    // 数据
    data() {
      return {
        // 是否使用指纹
        useFingerprint: true,
        // 指纹样式
        fingerprintStyle: "",
        // 指纹提示
        fingerprintMsg: ""
      }
    },
    // 方法
    methods: {
      // 指纹错误
      failed() {
        // 设置动画
        this.fingerprintStyle = "animated shake"
        // 设置提示文字
        this.fingerprintMsg = this.$t("lockPage.fingerprintFailed")
        // 监听动画结束
        var element = document.getElementById('fingerprint')
        element.addEventListener('animationend', ()=>{
          this.fingerprintStyle = ""
          this.fingerprintMsg = ""
        }, false)
      },
      // 指纹异常
      error(msg) {
        // 设置动画
        this.fingerprintStyle = "animated shake"
        // 设置提示文字
        this.fingerprintMsg = msg
        // 监听动画结束
        var element = document.getElementById('fingerprint')
        element.addEventListener('animationend', ()=>{
          this.fingerprintStyle = ""
          this.fingerprintMsg = ""
          this.useFingerprint = false
        }, false)
      }
    }
  }
</script>

<style lang="scss">
  #fingerprint {
    text-align: center;
    .fingerprint_msg {
      color: white;
      padding: 0;
      margin: 0;
    }
  }
</style>
