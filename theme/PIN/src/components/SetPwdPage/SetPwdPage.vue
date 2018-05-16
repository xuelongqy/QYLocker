<!--
 @Title: 设置密码页面
 @Description: 设置密码页面布局
 @author XueLong xuelongqy@foxmail.com
 @date 2018/4/28 11:10
 @update_author
 @update_time
 @version V1.0
-->
<template>
  <div id="set_pwd_page">
    <!--头部背景-->
    <div class="swp_header_bg"></div>
    <!--设置密码盒子-->
    <div v-if="!isVerifyPassword" class="set_pwd_box">
      <!--应用锁图标-->
      <mu-icon value="lock" :size="36" color="blue"/>
      <p class="set_pwd_text">{{$t('setPwdPage.setPwd')}}</p>
      <p class="set_pwd_label">{{$t('setPwdPage.setPwdLabel')}}</p>
      <!--密码设置输入框盒子-->
      <div class="pwd_input_box">
        <!--密码输入框-->
        <mu-text-field class="pwd_input" inputClass="pwd_input_style" :maxLength="16" v-model="password" :hintText="$t('setPwdPage.inputPwd')" type="number" @keyup.enter.native="onOkBtn"/>
        <!--密码提示文字-->
        <p v-if="password.length < 4" class="pwd_label">{{$t('setPwdPage.moreFour')}}</p>
        <p v-if="password.length > 16" class="pwd_label">{{$t('setPwdPage.lessSeventeen')}}</p>
      </div>
    </div>
    <!--验证密码盒子-->
    <div v-else class="verify_pwd_box">
      <!--应用锁图标-->
      <mu-icon value="lock" :size="36" color="blue"/>
      <p class="set_pwd_text">{{$t('setPwdPage.reInputPwd')}}</p>
      <!--密码设置输入框盒子-->
      <div class="pwd_input_box">
        <!--密码输入框-->
        <mu-text-field class="pwd_input" inputClass="pwd_input_style" :maxLength="16" v-model="verifyPassword" :hintText="$t('setPwdPage.inputPwd')" type="number" @keyup.enter.native="onOkBtn"/>
      </div>
    </div>
    <!--两次密码验证错误提示框-->
    <mu-dialog :open="verifyFailureDialog" :title="$t('setPwdPage.pwdWrong')">
      <mu-flat-button :label="$t('setPwdPage.ok')" slot="actions" primary @click="verifyFailureDialog = false"/>
    </mu-dialog>
    <!--底部按钮盒子-->
    <div class="swp_footer_btn_box">
      <!--取消按钮-->
      <mu-flat-button :label="$t('setPwdPage.cancel')" class="swp_cancel_btn" primary @click="onCancelBtn"/>
      <!--确认按钮-->
      <mu-raised-button :label="isVerifyPassword?$t('setPwdPage.ok'):$t('setPwdPage.next')" class="swp_ok_btn" primary :disabled="password.length < 4 || password.length > 16" @click="onOkBtn"/>
    </div>
  </div>
</template>

<script>
  import ThemeUtil from "../../assets/cordova/plugin/Theme"

  export default {
    name: "SetPwdPage",
    // 数据
    data() {
      return {
        // 密码
        password: "",
        // 验证密码
        verifyPassword: "",
        // 是否验证密码
        isVerifyPassword: false,
        // 密码验证弹框
        verifyFailureDialog: false
      }
    },
    // 方法
    methods: {
      // 取消按钮
      onCancelBtn() {
        ThemeUtil.cancelSetPwd()
      },
      // 确定按钮
      onOkBtn() {
        if (this.isVerifyPassword) {
          // 验证两次输入的密码
          if (this.password === this.verifyPassword) {
            // 设置密码
            ThemeUtil.setPassword(this.password)
          }else {
            this.verifyFailureDialog = true
          }
        }else {
          // 验证密码长度
          if (this.password.length < 4 || this.password.length > 16) {
            return
          }
          this.isVerifyPassword = true
        }
      }
    },
    // 观察器
    watch: {
      // 只能输入数字
      password(newValue, oldValue) {
        if (!/^\d{n}$ /.test(newValue)) {
          this.password = newValue.replace(/[^\d]/g,'')
        }
      }
    }
  }
</script>

<style lang="scss">
  #set_pwd_page {
    height: 100%;
    width: 100%;
    padding: 50px 20px 20px 20px;
    // 头部背景
    .swp_header_bg {
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      height: 50px;
      background: linear-gradient(top,gainsboro,rgba(255,255,255,0));
    }
    p {
      margin: 0;
      padding: 0;
    }
    // 设置密码文本
    .set_pwd_text {
      margin-top: 10px;
      font-size: 1.5rem;
    }
    .set_pwd_label {
      font-size: 1rem;
    }
    // 密码输入框盒子
    .pwd_input_box {
      position: absolute;
      top: 35%;
      width: calc(100% - 40px);
      // 密码提示文字
      .pwd_label {
        font-size: 0.8rem;
        color: gray;
      }
    }
    // 密码输入框
    .pwd_input {
      width: 100%;
      padding: 0;
      margin: 0;
      text-align: center;
      .pwd_input_style {
        text-align: center;
      }
    }
    // 底部按钮盒子
    .swp_footer_btn_box {
      position: absolute;
      bottom: 20px;
      width: calc(100% - 40px);
      // 取消按钮
      .swp_cancel_btn {
        margin-left: -10px;
        float: left;
        .mu-flat-button {
          min-width: 0;
        }
      }
      // 确认按钮
      .swp_ok_btn {
        float: right;
      }
    }
  }
</style>
