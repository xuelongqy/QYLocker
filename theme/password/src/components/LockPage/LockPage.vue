<!--
 @Title: 解锁页面
 @Description: 页面解锁
 @author XueLong xuelongqy@foxmail.com
 @date 2018/4/28 9:02
 @update_author
 @update_time
 @version V1.0
-->
<template>
  <div id="lock_page">
    <!--背景图片盒子-->
    <div class="lp_bg_box">
      <img class="lp_bg_img" :src="bgImageUrl" onerror="src='./static/image/transparent.png'">
    </div>
    <!--软件图标盒子-->
    <div class="lp_app_icon_box">
      <app-info/>
    </div>
    <!--密码输入盒子-->
    <div class="pwd_input_box">
      <mu-text-field class="pwd_input"
                     labelClass="pwd_input_label"
                     labelFocusClass="pwd_input_label"
                     hintTextClass="pwd_input_label"
                     underlineClass="pwd_input_line"
                     underlineFocusClass="pwd_input_line"
                     iconClass="pwd_input_label"
                     inputClass="pwd_input_label"
                     helpTextClass="pwd_input_label"
                     :label="$t('lockPage.password')"
                     :hintText="$t('lockPage.inputPwd')"
                     v-model="password"
                     type="password"
                     icon="lock"
                     @keyup.enter.native="onUnlock"
                     :maxLength="16"
                     labelFloat/>
    </div>
    <!--密码错误弹窗-->
    <mu-dialog :open="pwdWrongDialog" :title="$t('setPwdPage.pwdWrong')">
      <mu-flat-button :label="$t('setPwdPage.ok')" slot="actions" primary @click="pwdWrongDialog = false"/>
    </mu-dialog>
    <!--指纹盒子-->
    <div class="lp_fingerprint_box">
      <v-fingerprint ref="fingerprint" v-if="isFingerprint"/>
    </div>
  </div>
</template>

<script>
  import Fingerprint from "../Fingerprint/Fingerprint"
  import AppInfo from "../AppInfo/AppInfo"
  import ThemeUtil from "../../assets/cordova/plugin/Theme"

  export default {
    name: "LockPage",
    // 数据
    data() {
      return {
        // 密码
        password: "",
        // 密码错误弹窗
        pwdWrongDialog: false,
        // 背景图片
        bgImageUrl: "",
        // 是否支持指纹
        isFingerprint: false
      }
    },
    // 页面创建时
    created() {
      // 绑定设备初始化完成事件
      document.addEventListener('deviceready', () => {
        // 获取背景图片
        ThemeUtil.getBgImg((img) => {
          this.bgImageUrl = img
        })
        // 获取是否支持指纹
        ThemeUtil.isFingerprint((is) => {
          this.isFingerprint = is
        })
        // 设置指纹监听
        ThemeUtil.setFingerprintListener(this.fingerprintListener)
      })
    },
    // 组件
    components: {
      'v-fingerprint': Fingerprint,
      'app-info': AppInfo
    },
    // 方法
    methods: {
      // 解锁
      onUnlock() {
        ThemeUtil.unlock((isUnlock) => {
          // 判断是否解锁成功
          if (!isUnlock) {
            this.pwdWrongDialog = true
          }
        },this.password)
      },
      // 指纹监听事件
      fingerprintListener(data) {
        if (data instanceof Object) {
          if (!data.success) {
            if (data.msg == "") {
              // 密码错误
              this.$refs.fingerprint.failed()
            }else {
              // 密码异常
              this.$refs.fingerprint.error(data.msg)
            }
          }
        }else {
          console.log(data)
        }
      }
    }
  }
</script>

<style lang="scss">
  #lock_page {
    height: 100%;
    width: 100%;
    // 软件图标盒子
    .lp_app_icon_box {
      position: absolute;
      top: 70px;
      left: 50px;
    }
    // 背景盒子
    .lp_bg_box {
      display: block;
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      height: 100%;
      width: 100%;
      background: #2962ff;
      // 背景图片
      .lp_bg_img {
        height: 100%;
        width: 100%;
      }
    }
    // 密码输入盒子
    .pwd_input_box {
      position: absolute;
      top: 35%;
      left: 0;
      right: 0;
      text-align: center;
      // 密码框样式
      .pwd_input {
        text-align: left;
        .pwd_input_label {
          color: white;
        }
        .pwd_input_line {
          background-color: white;
        }
      }
    }
    // 指纹盒子
    .lp_fingerprint_box {
      position: absolute;
      top: 80%;
      left: 0;
      right: 0;
      width: 100%;
      text-align: center;
    }
  }
</style>
