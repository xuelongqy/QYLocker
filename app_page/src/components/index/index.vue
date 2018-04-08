<template>
  <div id="index">
    <!--侧边栏-->
    <mu-drawer width="300" :open="sidebarOpen" :docked="false" @close="sidebarToggle()">
      <!--侧边栏头部-->
      <div class="index_drawer_header">
        <!--头部背景-->
        <div id="index_drawer_header_bg"></div>
        <!--头部内容容器-->
        <div class="index_drawer_header_content_box">
          <a class="index_drawer_header_img" href="javascript:void(0)"><img class="circle" src="./assets/img/side-nev_head.jpg"></a>
          <!--侧边栏信息盒子-->
          <div class="index_drawer_header_info_box">
            <a class="index_drawer_header_name" href="javascript:void(0)"><span class="white-text name">{{ this.$t('comm.appName') }}</span></a>
            <a class="index_drawer_header_email" href="javascript:void(0)"><span class="white-text email">{{ this.$t('comm.email') }}</span></a>
          </div>
        </div>
      </div>
      <!--侧边栏菜单-->
      <mu-list open="false">
        <mu-list-item v-for="menu_item in this.$t('index.menu_list')" :toggleNested="typeof menu_item.sub_menu_list != 'undefined'"
                      :key="menu_item.flag" :title="menu_item.name" @click.native="!menu_item.sub_menu_list && onSidebarMenuItem(menu_item.flag)">
          <mu-icon slot="left" :value="menu_item.icon"/>
          <mu-list-item v-if="typeof menu_item.sub_menu_list != 'undefined'" v-for="sub_menu_item in menu_item.sub_menu_list" slot="nested"
                        :key="sub_menu_item.flag" :title="sub_menu_item.name" @click.native="onSidebarMenuItem(sub_menu_item.flag)">
            <mu-icon slot="left" :value="sub_menu_item.icon"/>
          </mu-list-item>
        </mu-list-item>
      </mu-list>
    </mu-drawer>
    <!--顶部栏-->
    <div id="index_app_bar_box" class="animated fadeInDown mu-badge-primary" :style="'padding-top: ' + topHeight + 'px'">
      <mu-appbar :title="this.$t('index.menu')" class="index_header_box" :zDepth="0">
        <mu-icon-button icon="menu" slot="left" @click="sidebarToggle()"/>
        <mu-icon-button :icon="lockState?'lock_outline':'lock_open'" @click="$store.dispatch('modLockState', !lockState)" slot="right"/>
        <mu-icon-button icon="more_vert" slot="right"/>
      </mu-appbar>
    </div>
    <!--主干区域-->
    <div class="index_main animated fadeInRight" :style="'top:'+(56+topHeight)+'px;'">
      <router-view/>
    </div>
  </div>
</template>

<script>
  // window.$ = window.jQuery = require('jquery')
  require('../../assets/view/Voronoi')

  export default {
    name: "index",
    data() {
      return {
        // 侧边开开关状态
        sidebarOpen: false
      }
    },
    // 页面创建时
    created() {
      //设置标题
      document.title = this.$t('comm.appName')
      // jquery准备完毕,初始化jquery相关
      $(document).ready(() => {
        // 头部背景
        $('#index_drawer_header_bg').voronoi(false)
      })
    },
    // 计算方法
    computed: {
      // 获取顶部高度
      topHeight: function () {
        return this.$store.state.App.topHeight
      },
      // 获取上锁状态
      lockState: function () {
        return this.$store.state.LockAppsConfig.lockAppsConfig.isLock
      }
    },
    // 方法
    methods: {
      // 侧边栏开关
      sidebarToggle() {
        this.sidebarOpen = !this.sidebarOpen
      },
      // 侧边栏菜单选项
      onSidebarMenuItem() {
        this.sidebarToggle()
      }
    }
  }
</script>

<style lang="scss">
  @import "../../assets/lib/responsive/responsive.css";
  @import "../../../node_modules/animate.css/animate.css";

  #index {
    /*侧边栏头部*/
    .index_drawer_header {
      width: 100%;
      height: 180px;
      /*侧边栏背景*/
      #index_drawer_header_bg {
        position: fixed;
        width: 100%;
        height: 180px;
        z-index: -1;
      }
      /*头部内容容器*/
      .index_drawer_header_content_box {
        position: relative;
        padding: 24px;
        height: 100%;
        a {
          display: block;
          color: white;
        }
        /*侧边栏头像*/
        .index_drawer_header_img {
          img {
            width: 60px;
            height: 60px;
            border-radius: 50%;
          }
        }
        /*侧边栏信息盒子*/
        .index_drawer_header_info_box {
          position: absolute;
          left:0;
          bottom:0;
          padding: 24px;
          /*侧边栏名字*/
          .index_drawer_header_name {
            font-size: 1.2rem;
            font-weight: bold;
          }
        }
      }

    }
    /*主干区域*/
    .index_main {
      position: absolute;
      bottom: 0;
      left: 0;
      right: 0;
    }
  }
</style>
