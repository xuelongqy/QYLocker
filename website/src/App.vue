<template>
  <div id="app">
    <h2 style="position: absolute; top: -9999px;">{{this.$t('comm.appName')}}</h2>
    <!--背景-->
    <div id="app_bg"></div>
    <!--头部-->
    <mu-appbar :title="$t('comm.appName')">
      <!--logo-->
      <img class="app_logo" src="./assets/logo.png" slot="left"/>
      <mu-flat-button :label="$t('app.home')" slot="right"/>
      <mu-flat-button :label="$t('app.lockScreen')" slot="right"/>
      <mu-flat-button :label="$t('app.download')" slot="right"/>
      <mu-icon-button icon="expand_more" slot="right" ref="moreBtn" @click="user_popover_toggle"/>
      <!--用户弹出组件-->
      <mu-popover :trigger="user_popover_trigger" :open="user_popover_open" @close="user_popover_open = false">
        <!--用户弹出组件盒子-->
        <div class="user_popover_box">
          <!--用户信息盒子-->
          <div class="user_info_box">
            <!--用户头像-->
            <img class="user_head" src="./assets/head_img.png">
            <!--用户名-->
            <p class="user_name">未登录</p>
            <!--登陆注销-->
            <a href="javascript:void(0)" class="user_login_btn" @click="user_login_dialog_open = true">登陆</a>
          </div>
          <!--更多菜单-->
          <mu-menu>
            <mu-divider />
            <mu-menu-item title="我的主题" leftIcon="color_lens"/>
            <mu-menu-item title="分享主题" leftIcon="share"/>
            <mu-menu-item title="关于" leftIcon="info"/>
          </mu-menu>
        </div>
      </mu-popover>
      <!--用户登陆窗口-->
      <mu-dialog :open="user_login_dialog_open" dialogClass="user_login_dialog" @close="user_login_dialog_open = false">
        <!--登陆头像-->
        <img class="user_login_head_img" src="./assets/head_img.png">
        <!--用户登陆文字-->
        <p class="user_login_label">用户登陆</p>
        <!--用户名-->
        <mu-text-field label="用户名" labelFloat/><br/>
        <!--密码-->
        <mu-text-field label="密码" hintText="请输入密码" type="password" labelFloat/>
        <!--登陆按钮-->
        <mu-float-button icon="arrow_forward" class="user_login_submit_btn"/>
      </mu-dialog>
    </mu-appbar>
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
        user_login_dialog_open: false
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
  }
</style>
