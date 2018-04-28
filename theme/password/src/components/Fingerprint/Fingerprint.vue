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
  <div class="fingerprint">
    <mu-icon v-if="useFingerprint" value="fingerprint" :size="36" color="white"/>
  </div>
</template>

<script>
  export default {
    name: "Fingerprint",
    // 数据
    data() {
      return {
        // 是否使用指纹
        useFingerprint: true
      }
    }
  }
</script>

<style lang="scss">
  .fingerprint {

  }
</style>
