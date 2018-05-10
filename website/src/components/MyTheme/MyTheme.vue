<template>
  <div id="my_theme">
    <!--主题列表-->
    <div class="my_theme_list_box">
      <div class="my_theme_list">
        <!--主题条目-->
        <mu-paper :zDepth="3" class="my_theme_item_box">
          <mu-row class="my_theme_item">
            <!--主题图片-->
            <mu-col width="50" tablet="50" desktop="50">
              <mu-grid-tile class="my_theme_item_img">
                <img src="../../assets/app_screenshots.png"/>
                <span slot="title">标题</span>
              </mu-grid-tile>
            </mu-col>
            <!--主题信息-->
            <mu-col width="50" tablet="50" desktop="50" class="my_theme_info_box">
              <mu-paper :zDepth="2" class="my_theme_info">
                <div class="my_theme_info_item">
                  <div class="my_theme_info_name">主题状态：</div>
                  <div class="my_theme_info_label">通过</div>
                </div>
                <div class="my_theme_info_item">
                  <div class="my_theme_info_name">信息：</div>
                  <div class="my_theme_info_label">无</div>
                </div>
              </mu-paper>
              <!--按钮-->
              <div class="my_theme_item_btn_box">
                <!--下载-->
                <mu-float-button icon="file_download" class="my_theme_item_btn"/>
                <!--更新-->
                <mu-float-button icon="update" class="my_theme_item_btn" @click="update_theme_dialog_open = true"/>
                <!--删除-->
                <mu-float-button icon="delete" class="my_theme_item_btn"/>
              </div>
            </mu-col>
          </mu-row>
        </mu-paper>
        <!--更新主题-->
        <mu-dialog :open="update_theme_dialog_open" :title="$t('myTheme.updateTheme')" @close="share_theme_dialog_open = false">
          <mu-text-field :hintText="$t('app.selectFile')" :value="updateThemeFile" fullWidth @click.native="selectUpdateTheme"/>
          <input style="display: none" ref="updateThemeInput" @change="onUpdateThemeFileChange" type="file" accept="application/zip"/>
          <!--上传按钮-->
          <mu-raised-button :label="$t('app.upload')" style="float: right" primary :disabled="shareThemeFile == ''" @click="onUploadUpdateTheme"/>
        </mu-dialog>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: "MyTheme",
    // 数据
    data() {
      return {
        // 更新主题弹窗
        update_theme_dialog_open: false,
        // 更新文件名
        updateThemeFile: ""
      }
    },
    methods: {
      // 选择上传主题
      selectUpdateTheme() {
        this.$refs.updateThemeInput.click()
      },
      // 更新主题文件改变
      onUpdateThemeFileChange(el) {
        this.updateThemeFile = el.target.files[0].name
      },
      // 上传更新主题
      onUploadUpdateTheme() {
      }
    }
  }
</script>

<style lang="scss">
  #my_theme {
    height: 100%;
    /*主题盒子*/
    .my_theme_list_box {
      width: 100%;
      height: 100%;
      padding: 20px 10%;
      text-align: center;
      overflow: auto;
      // 主题列表
      .my_theme_list {
        width: 100%;
        max-width: 1000px;
        display: inline-block;
        .my_theme_item_box {
          width: 300px;
          margin: 10px;
          display: inline-block;
          // 主题卡片
          .my_theme_item {
            text-align: center;
            // 主题图片
            .my_theme_item_img {
              img {
                width: 150px;
              }
            }
            // 主题信息
            .my_theme_info_box {
              padding: 10px 5px;
              text-align: center;
              text-align: center;
              // 信息
              .my_theme_info {
                padding: 10px;
                .my_theme_info_item {
                  text-align: left;
                  .my_theme_info_name {
                    float: left;
                    color: gray;
                  }
                  .my_theme_info_label {
                    color: black;
                  }
                }
              }
              // 按钮
              .my_theme_item_btn_box {
                margin: 10px;
                display: inline-block;
                .my_theme_item_btn {
                  display: block;
                  margin: 10px;
                }
              }
            }
          }
        }
      }
    }
  }
  @media screen and (max-width: 600px) {
    .my_theme_list_box {
      padding: 20px 0;
      text-align: center;
    }
  }
</style>
