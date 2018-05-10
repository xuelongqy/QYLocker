<template>
  <div id="app">
    <h2 style="position: absolute; top: -9999px;">{{this.$t('comm.appName')}}</h2>
    <!--背景-->
    <div id="app_bg"></div>
    <!--头部-->
    <mu-appbar :title="$t('comm.appName')" class="animated fadeInDown">
      <!--logo-->
      <img class="app_logo" src="./assets/logo.png" slot="left"/>
      <mu-flat-button :label="$t('app.home')" slot="right" @click="$router.push('/')"/>
      <mu-flat-button :label="$t('app.lockScreen')" slot="right" @click="$router.push('/theme')"/>
      <mu-flat-button :label="$t('app.download')" slot="right"/>
      <mu-icon-button icon="expand_more" slot="right" ref="moreBtn" @click="user_popover_toggle"/>
    </mu-appbar>
    <!--用户弹出组件-->
    <mu-popover :trigger="user_popover_trigger" :open="user_popover_open" @close="user_popover_open = false">
      <!--用户弹出组件盒子-->
      <div class="user_popover_box">
        <!--用户信息盒子-->
        <div class="user_info_box">
          <!--用户头像-->
          <img class="user_head" src="./assets/head_img.png">
          <!--用户名-->
          <p class="user_name">{{$t('app.noLogin')}}</p>
          <!--登陆注销-->
          <a href="javascript:void(0)" class="user_login_btn" @click="user_login_dialog_open = true">{{$t('app.login')}}</a>
        </div>
        <!--更多菜单-->
        <mu-menu>
          <mu-divider />
          <mu-menu-item :title="$t('app.myTheme')" leftIcon="color_lens"/>
          <mu-menu-item :title="$t('app.shareTheme')" leftIcon="share" @click="share_theme_dialog_open = true"/>
          <mu-menu-item :title="$t('app.about')" leftIcon="info"/>
        </mu-menu>
      </div>
    </mu-popover>
    <!--用户登陆窗口-->
    <mu-dialog :open="user_login_dialog_open" dialogClass="user_login_dialog" @close="closeUserLoginDialog">
      <!--登陆头像-->
      <img v-if="!is_user_register" class="user_login_head_img" src="./assets/head_img.png">
      <!--用户登陆文字-->
      <p class="user_login_label">{{is_user_register?$t('app.userRegistration'):$t('app.userLogin')}}</p>
      <!--用户名-->
      <mu-text-field :label="$t('app.userName')" v-model="userName" :errorText="userNameError" :maxLength="15" labelFloat/><br/>
      <!--邮箱-->
      <mu-text-field v-if="is_user_register" :label="$t('app.email')" v-model="email" :errorText="emailError" labelFloat/><br/>
      <!--手机-->
      <mu-text-field v-if="is_user_register" :label="$t('app.phone')" type="number" v-model="phone" :errorText="phoneError" labelFloat/><br/>
      <!--密码-->
      <mu-text-field :label="$t('app.password')" :hintText="$t('app.inputPassword')" type="password" v-model="pwd" :errorText="pwdError" :maxLength="16" labelFloat/>
      <!--确认密码-->
      <mu-text-field v-if="is_user_register" :label="$t('app.confirmPassword')" :hintText="$t('app.inputPassword')" v-model="confirmPwd" type="password" :errorText="confirmPwdError" :maxLength="16" labelFloat/>
      <!--登陆按钮-->
      <mu-float-button icon="arrow_forward" class="user_login_submit_btn" @click="onLogin"/>
      <!--注册账号-->
      <a v-if="!is_user_register" class="user_register" href="javascript:void(0)" @click="is_user_register = true">{{$t('app.registeredAccount')}}</a>
    </mu-dialog>
    <!--分享主题-->
    <mu-dialog :open="share_theme_dialog_open" :title="$t('app.shareTheme')" @close="share_theme_dialog_open = false">
      <mu-text-field :hintText="$t('app.selectFile')" :value="shareThemeFile" fullWidth @click.native="selectShareTheme"/>
      <input style="display: none" ref="shareThemeInput" @change="onShareThemeFileChange" id="share_theme_input" type="file" accept="application/zip"/>
      <!--上传按钮-->
      <mu-raised-button :label="$t('app.anonymousUpload')" style="float: right" primary :disabled="shareThemeFile == ''" @click="onUploadShareTheme"/>
    </mu-dialog>
    <!--主干区域-->
    <div class="app_main">
      <!--路由-->
      <router-view/>
    </div>
  </div>
</template>

<script>
  require('./assets/view/Voronoi')

  export default {
    name: 'App',
    // 数据
    data() {
      return {
        // 用户弹出组件触发器
        user_popover_trigger: null,
        user_popover_open: false,
        // 用户登陆窗口开启
        user_login_dialog_open: false,
        // 分享主题窗口
        share_theme_dialog_open: false,
        // 是否为用户注册
        is_user_register: false,
        // 输入数据
        userName: "",
        email: "",
        phone: "",
        pwd: "",
        confirmPwd: "",
        // 错误提示
        userNameError: "",
        emailError: "",
        phoneError: "",
        pwdError: "",
        confirmPwdError: "",
        // 分享主题文件
        shareThemeFile: ""
      }
    },
    // 页面初始化
    created() {
      // 修改标题
      document.title = this.$t('comm.appName')
      // jquery准备完毕,初始化jquery相关
      $(document).ready(() => {
        // 头部背景
        $('#app_bg').voronoi(false)
      })
    },
    // 钩子
    mounted () {
      this.user_popover_trigger = this.$refs.moreBtn.$el
    },
    // 方法
    methods: {
      // 用户弹出组件开关
      user_popover_toggle() {
        this.user_popover_open = !this.user_popover_open
      },
      // 登陆框关闭
      closeUserLoginDialog() {
        this.user_login_dialog_open = false
        this.is_user_register = false
        // 清除错误提示
        this.userNameError = ""
        this.emailError = ""
        this.phoneError = ""
        this.pwdError = ""
        this.confirmPwdError = ""
      },
      // 登陆或者注册
      onLogin() {
        // 输入验证
        if (this.inputValidation()) {
          alert("ok")
        }
      },
      // 输入验证
      inputValidation() {
        // 用户名
        if (this.userName.length < 1 || this.userName.length > 15 || this.userName.indexOf(" ") > -1) {
          this.userNameError = this.$t('app.userNameError')
          return false
        }else {
          this.userNameError = ""
        }
        if (this.is_user_register) {
          // 邮箱
          if ((/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/).test(this.email)) {
            this.emailError = ""
          }else {
            this.emailError = this.$t('app.emailError')
            return false
          }
          // 手机
          if ((/^1\d{10}$/).test(this.phone)) {
            this.phoneError = ""
          }else {
            this.phoneError = this.$t('app.phoneError')
            return false
          }
        }
        // 密码
        if ((/^[A-Za-z0-9]{6,20}$/).test(this.pwd)) {
          this.pwdError = ""
        }else {
          this.pwdError = this.$t('app.pwdError')
          return false
        }
        if (this.is_user_register) {
          // 验证密码
          if (this.pwd !== this.confirmPwd) {
            this.confirmPwdError = this.$t('app.confirmPwdError')
            return false
          }else {
            this.confirmPwdError = ""
          }
        }
        return true
      },
      // 选择分享主题
      selectShareTheme() {
        this.$refs.shareThemeInput.click()
      },
      // 选择分享主题文件
      onShareThemeFileChange(el) {
        this.shareThemeFile = el.target.files[0].name
      },
      // 上传分享主题
      onUploadShareTheme() {
        // let file = this.$refs.shareThemeInput.files[0]
        // let param = new FormData(); //创建form对象
        // param.append('file',file);//通过append向form对象添加数据
        // console.log(param.get('file')); //FormData私有类对象，访问不到，可以通过get判断值是否传进去
        // let config = {
        //   headers:{'Content-Type':'multipart/form-data'}
        // }; //添加请求头
        // this.$http.post('http://127.0.0.1:8081/upload',param,config)
        //   .then(response=>{
        //     console.log(response.data);
        //   })
      }
    }
  }
</script>

<style lang="scss">
  #app {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    position: fixed;
    height: 100%;
    width: 100%;
    #app_bg {
      position: absolute;
      top: 0;
      bottom: 0;
      left: 0;
      right: 0;
      height: 100%;
      width: 100%;
      z-index: -1;
    }
    // 图标
    .app_logo {
      width: 50px;
      height: 50px;
    }
    // 主干区域
    .app_main {
      position: relative;
      height: calc(100% - 64px);
      bottom: 0;
    }
  }
  body {
    padding: 0;
    margin: 0;
    height: 100%;
    width: 100%;
  }
  // 用户弹窗
  .user_popover_box {
    width: 200px;
    text-align: center;
    // 用户信息
    .user_info_box {
      padding: 20px 20px 0 20px;
      // 用户头像
      .user_head {
        height: 50px;
        width: 50px;
      }
      // 用户名
      .user_name {
        margin: 0;
        padding: 0;
      }
      // 登陆注销按钮
      .user_login_btn {
        float: right;
      }
    }
  }
  // 用户登陆弹窗
  .user_login_dialog {
    position: relative;
    width: 350px;
    text-align: center;
    // 用户登陆头像
    .user_login_head_img {
      margin: 20px;
      width: 100px;
      height: 100px;
      border-radius: 50%;
    }
    // 用户登陆文字
    .user_login_label {
      margin: 0;
      padding: 0;
      font-size: 1.6rem;
      color: #03a9f4;
    }
    // 登陆按钮
    .user_login_submit_btn {
      position: absolute;
      width: 60px;
      height: 60px;
      right: -30px;
    }
    // 注册账号
    .user_register {
      display: block;
      margin-top: 10px;
      float: right;
    }
  }
</style>
