<template>
  <div id="login">
    <!--名字-->
    <h2 class="login_website_name">{{$t('comm.appName')}}{{$t('comm.management')}}</h2>
    <!--登陆盒子-->
    <mu-paper class="login_box" :zDepth="2" >
      <!--登陆头像-->
      <img class="login_head_img" src="../../assets/logo.png">
      <!--用户登陆文字-->
      <p class="login_user_label">用户登陆</p>
      <!--用户名-->
      <mu-text-field v-model="userName" :label="$t('login.userName')" :errorText="userNameError" :maxLength="15" labelFloat/><br/>
      <!--密码-->
      <mu-text-field v-model="pwd" :label="$t('login.password')" :errorText="pwdError" :maxLength="16" :hintText="$t('login.inputPassword')" type="password" labelFloat/>
      <!--登陆按钮-->
      <mu-float-button icon="arrow_forward" class="login_submit_btn" @click="onLogin"/>
    </mu-paper>
  </div>
</template>

<script>
  export default {
    name: 'Login',
    // 数据
    data () {
      return {
        // 输入数据
        userName: "",
        pwd: "",
        // 错误提示
        userNameError: "",
        pwdError: ""
      }
    },
    // 方法
    methods: {
      // 登陆
      onLogin() {
        // 清空错误提示
        this.userNameError = ""
        this.pwdError = ""
        // 输入验证
        if (this.inputValidation()) {
          alert("ok")
        }
      },
      // 输入验证
      inputValidation() {
        // 用户名
        if (this.userName.length < 1 || this.userName.length > 15 || this.userName.indexOf(" ") > -1) {
          this.userNameError = this.$t('login.userNameError')
          return false
        }else {
          this.userNameError = ""
        }
        // 密码
        if ((/^[A-Za-z0-9]{6,20}$/).test(this.pwd)) {
          this.pwdError = ""
        }else {
          this.pwdError = this.$t('login.pwdError')
          return false
        }
        return true
      },
    },
  }
</script>

<style lang="scss">
  #login {
    text-align: center;
    // 背景
    .login_bg {
      position: absolute;
      top: 0;
      bottom: 0;
      left: 0;
      right: 0;
      height: 100%;
      width: 100%;
      z-index: -1;
    }
    // 网站名字
    .login_website_name {
      margin-top: 50px;
      font-size: 2rem;
      font-weight: normal;
      color: white;
    }
    // 登陆盒子
    .login_box {
      position: relative;
      display: inline-block;
      margin-top: 50px;
      width: 320px;
      height: 400px;
      text-align: center;
      // 用户登陆头像
      .login_head_img {
        margin: 20px;
        width: 100px;
        height: 100px;
        border-radius: 50%;
      }
      // 用户登陆文字
      .login_user_label {
        margin: 0;
        padding: 0;
        font-size: 1.6rem;
        color: #03a9f4;
      }
      // 登陆按钮
      .login_submit_btn {
        position: absolute;
        width: 60px;
        height: 60px;
        right: -30px;
      }
    }
  }
</style>
