<template>
  <div id="management">
    <mu-appbar :title="$t('comm.appName')+$t('comm.management')">
      <!--logo-->
      <img class="app_logo" src="../../assets/logo.png" slot="left"/>
      <mu-text-field class="appbar-search-field"  slot="right" :hintText="$t('management.inputContent')"/>
      <mu-icon-button icon="search" slot="right"/>
    </mu-appbar>
    <!--主干区域-->
    <div class="management_main">
      <!--主题管理盒子-->
      <mu-paper class="management_themes_box" :zDepth="2">
        <!--主题类型选项卡-->
        <mu-tabs :value="themeType" @change="themeTypeChange">
          <mu-tab value="all" :title="$t('management.all')"/>
          <mu-tab value="waitCheck" :title="$t('management.waitCheck')"/>
          <mu-tab value="putOff" :title="$t('management.putOffed')"/>
        </mu-tabs>
        <!--主题列表-->
        <div class="management_theme_list">
          <scroller>
            <!--主题条目-->
            <mu-row class="management_theme_item" gutter>
              <!--名字-->
              <mu-col width="33" tablet="33" desktop="33">
                <a class="management_theme_name" href="javascript:void(0)" @click="openThemeInfoDialog(1)">主题名称</a>
              </mu-col>
              <!--状态-->
              <mu-col width="33" tablet="33" desktop="33">上架</mu-col>
              <!--操作-->
              <mu-col width="33" tablet="33" desktop="33">
                <a class="management_theme_btn" href="javascript:void(0)" @click="check_dialog_open = true">{{$t('management.check')}}</a>
                <a class="management_theme_btn" href="javascript:void(0)" @click="openConfirmDialog($t('management.putOn', 1))">{{$t('management.putOn')}}</a>
                <a class="management_theme_btn" href="javascript:void(0)" @click="openConfirmDialog($t('management.putOff', 1))">{{$t('management.putOff')}}</a>
                <a class="management_theme_btn" href="javascript:void(0)" @click="openConfirmDialog($t('management.delete', 1))">{{$t('management.delete')}}</a>
              </mu-col>
            </mu-row>
          </scroller>
        </div>
      </mu-paper>
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
    <!--审核弹窗-->
    <mu-dialog :open="check_dialog_open" :title="$t('management.check')+' - '+'主题名字'" @close="check_dialog_open = false">
      <mu-text-field v-model="rejectedMsg" :label="$t('management.rejectedMsg')" :hintText="$t('management.inputContent')" multiLine :rows="3" :rowsMax="6" labelFloat fullWidth/>
      <mu-flat-button slot="actions" primary @click="checkRejected" :label="$t('management.rejected')" :disabled="rejectedMsg.length == 0"/>
      <mu-flat-button slot="actions" @click="checkPass" primary :label="$t('management.pass')" :disabled="rejectedMsg.length > 0"/>
    </mu-dialog>
    <!--确定删除弹窗-->
    <mu-dialog :open="confirm_dialog_open" :title="confirm_type" @close="closeConfirmDialog">
      {{$t('management.confirm')}}{{confirm_type}}-主题名字?
      <mu-flat-button slot="actions" primary @click="closeConfirmDialog" :label="$t('management.cancel')"/>
      <mu-flat-button slot="actions" @click="onConfirm" primary :label="$t('management.ok')"/>
    </mu-dialog>
  </div>
</template>

<script>
  export default {
    name: "Management",
    // 数据
    data() {
      return {
        // 主题类型
        themeType: "all",
        // 主题信息弹窗
        theme_info_dialog_open: false,
        // 图片滑动参数
        themeInfoSwiperOption: {
          pagination: {
            el: '.swiper-pagination',
            dynamicBullets: true
          }
        },
        // 审核弹窗
        check_dialog_open: false,
        // 驳回信息
        rejectedMsg: "",
        // 确认弹窗
        confirm_dialog_open: false,
        confirm_type: ""
      }
    },
    // 方法
    methods: {
      // 主题类型改变
      themeTypeChange(val) {
        this.themeType = val
      },
      // 打开主题信息弹窗
      openThemeInfoDialog(index) {
        this.theme_info_dialog_open = true
      },
      // 关闭主题信息弹窗
      closeThemeInfoDialog() {
        this.theme_info_dialog_open = false
      },
      // 审核通过
      checkPass() {
        this.check_dialog_open = false
      },
      // 审核失败(驳回)
      checkRejected() {
        this.check_dialog_open = false
      },
      // 开启确认弹窗
      openConfirmDialog(type, index) {
        this.confirm_type = type
        this.confirm_dialog_open = true
      },
      // 关闭确认弹窗
      closeConfirmDialog() {
        this.confirm_dialog_open = false
        this.confirm_type = ""
      },
      // 确认按钮
      onConfirm() {
        alert(this.confirm_type)
        this.closeConfirmDialog()
      }
    },
  }
</script>

<style lang="scss">
  #management {
    width: 100%;
    height: 100%;
    // 图标
    .app_logo {
      width: 50px;
      height: 50px;
    }
    // 主干区域
    .management_main {
      width: 100%;
      height: calc(100% - 64px);
      text-align: center;
      // 主题管理盒子
      .management_themes_box {
        display: inline-block;
        margin: 10px 0;
        width: 100%;
        max-width: 1000px;
        height: calc(100% - 50px);
        // 主题列表
        .management_theme_list {
          position: relative;
          height: calc(100% - 50px);
          width: 100% ;
          // 主题条目
          .management_theme_item {
            height: 60px;
            line-height: 60px;
            text-align: center;
            // 名字
            .management_theme_name {
              font-size: 1.2rem;
            }
            // 操作按钮
            .management_theme_btn {
              margin: 0 5px;
            }
          }
        }
      }
    }
  }
  .appbar-search-field{
    color: #FFF;
    margin-bottom: 0;
    &.focus-state {
      color: #FFF;
    }
    .mu-text-field-hint {
      color: rgba(225,225,225,54%);
    }
    .mu-text-field-input {
      color: #FFF;
    }
    .mu-text-field-focus-line {
      background-color: #FFF;
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
