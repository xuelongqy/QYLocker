<template>
  <div id="theme">
    <!--主题列表-->
    <div class="theme_list_box">
      <div class="theme_list">
        <!--主题搜索框-->
        <div class="theme_search_box">
          <mu-text-field :hintText="$t('theme.searchTheme')"/>
          <mu-icon-button icon="search" style="color: white"/>
        </div>
        <!--主题条目-->
        <mu-grid-tile class="theme_item" @click.native="openThemeInfoDialog(1)">
          <img src="../../assets/app_screenshots.png"/>
          <span slot="title">标题</span>
          <span slot="subTitle">by <b>作者</b></span>
          <mu-icon-button icon="file_download" slot="action"/>
        </mu-grid-tile>
        <!--主题分页-->
        <div class="theme_pagination_box">
          <mu-pagination class="theme_pagination" :total="totalPage" :current="currentPage" @pageChange="onPageChange">
          </mu-pagination>
        </div>
      </div>
    </div>
    <!--主题信息弹窗-->
    <mu-dialog :open="theme_info_dialog_open" dialogClass="theme_info_dialog" title="主题名字" @close="closeThemeInfoDialog">
      <mu-row>
        <!--图片-->
        <mu-col width="100" tablet="60" desktop="60">
          <swiper :options="themeInfoSwiperOption">
            <swiper-slide>
              <img class="theme_info_image" src="../../assets/app_screenshots.png"/>
            </swiper-slide>
            <swiper-slide>
              <img class="theme_info_image" src="../../assets/app_screenshots.png"/>
            </swiper-slide>
            <swiper-slide>
              <img class="theme_info_image" src="../../assets/app_screenshots.png"/>
            </swiper-slide>
            <swiper-slide>
              <img class="theme_info_image" src="../../assets/app_screenshots.png"/>
            </swiper-slide>
            <swiper-slide>
              <img class="theme_info_image" src="../../assets/app_screenshots.png"/>
            </swiper-slide>
            <swiper-slide>
              <img class="theme_info_image" src="../../assets/app_screenshots.png"/>
            </swiper-slide>
            <swiper-slide>
              <img class="theme_info_image" src="../../assets/app_screenshots.png"/>
            </swiper-slide>
            <swiper-slide>
              <img class="theme_info_image" src="../../assets/app_screenshots.png"/>
            </swiper-slide>
            <div class="swiper-pagination" slot="pagination"></div>
          </swiper>
        </mu-col>
        <!--信息-->
        <mu-col width="100" tablet="40" desktop="40">
          <!--作者-->
          <div class="theme_info_item">
            <div class="theme_info_name">{{$t('theme.author')}}：</div>
            <div class="theme_info_label"> </div>
          </div>
          <!--版本-->
          <div class="theme_info_item">
            <div class="theme_info_name">{{$t('theme.version')}}：</div>
            <div class="theme_info_label"> </div>
          </div>
          <!--日期-->
          <div class="theme_info_item">
            <div class="theme_info_name">{{$t('theme.date')}}：</div>
            <div class="theme_info_label"> </div>
          </div>
          <!--介绍-->
          <div class="theme_info_item">
            <div class="theme_info_name">{{$t('theme.describe')}}：</div>
            <div class="theme_info_label"> </div>
          </div>
        </mu-col>
      </mu-row>
      <!--下载按钮-->
      <mu-float-button icon="file_download" class="theme_info_dialog_download_btn"/>
    </mu-dialog>
  </div>
</template>

<script>
  export default {
    name: 'theme',
    // 数据
    data() {
      return {
        // 分页相关
        totalPage: 1,
        currentPage: 1,
        // 主题信息弹窗
        theme_info_dialog_open: false,
        // 图片滑动参数
        themeInfoSwiperOption: {
          pagination: {
            el: '.swiper-pagination',
            dynamicBullets: true
          }
        }
      }
    },
    // 方法
    methods: {
      // 页面改变
      onPageChange(newIndex) {
        alert(newIndex)
      },
      // 打开主题信息弹窗
      openThemeInfoDialog(index) {
        this.theme_info_dialog_open = true
      },
      // 关闭主题信息弹窗
      closeThemeInfoDialog() {
        this.theme_info_dialog_open = false
      }
    }
  }
</script>

<style lang="scss">
  #theme {
    height: 100%;
    overflow: auto;
    // 主题列表盒子
    .theme_list_box {
      width: 100%;
      padding: 20px 10%;
      text-align: center;
      // 主题列表
      .theme_list {
        width: 100%;
        max-width: 1000px;
        display: inline-block;
        // 搜索框
        .theme_search_box {
          width: 100%;
          text-align: left;
        }
        // 主题条目
        .theme_item {
          display: inline-block;
          width: 150px;
          margin: 10px;
          text-align: left;
          img {
            width: 150px;
          }
        }
        // 主题分页
        .theme_pagination_box {
          width: 100%;
          .theme_pagination {
            margin-top: 20px;
            float: right;
          }
        }
      }
    }
    @media screen and (max-width: 600px) {
      .theme_list_box {
        padding: 20px 0;
        .theme_list {
          .theme_item {
            margin: 0;
          }
        }
      }
    }
  }
  // 主题信息弹窗
  .theme_info_dialog {
    position: fixed;
    .swiper-slide {
      text-align: center;
    }
    // 图片
    .theme_info_image {
      height: 300px;
    }
    // 信息
    .theme_info_item {
      .theme_info_name {
        float: left;
        color: gray;
      }
      .theme_info_label {
        color: black;
      }
    }
    // 下载按钮
    .theme_info_dialog_download_btn {
      position: absolute;
      top: 10px;
      right: 10px;
    }
  }
</style>
