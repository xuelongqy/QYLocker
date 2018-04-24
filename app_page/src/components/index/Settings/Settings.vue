<!--
 @Title: 设置页面
 @Description: 设置页面布局
 @author XueLong xuelongqy@foxmail.com
 @date 2018/4/24 14:43
 @update_author
 @update_time
 @version V1.0
-->
<template>
    <div id="settings">
      <!--主题设置-->
      <setting-item :settingName="$t('settings.themeSet')"
                    :settingInfo="$t('settings.themeSetInfo')"
                    :settingIcon="'color_lens'"
                    :settingIconColor="'blue'"
                    @click.native="onThemeSet"/>
      <!--背景图片-->
      <setting-item :settingName="$t('settings.bgImageSet')"
                    :settingInfo="$t('settings.bgImageSetInfo')"
                    :settingIcon="'image'"
                    :settingIconColor="'orange'"
                    @click.native="onBgImageSet"/>
      <!--应用锁模式-->
      <setting-item :settingName="$t('settings.lockModelSet')"
                    :settingInfo="$t('')"
                    :settingIcon="'dashboard'"
                    :settingIconColor="'red'"
                    @click.native="onBgImageSet"/>
      <!--应用模式选择弹出框-->
      <mu-dialog :open="lockModelDialogShow" :title="$t('settings.lockModelSet')" @close="lockModelDialogShow = false">
        <mu-menu>
          <mu-radio :label="$t('settings.lockModelSetInfo.stackPolling')" labelLeft name="lockModelGroup" nativeValue="stackPolling" class="lock_model_radio"/>
          <mu-radio :label="$t('settings.lockModelSetInfo.listenApps')" labelLeft name="lockModelGroup" nativeValue="listenApps" class="lock_model_radio"/>
          <mu-radio :label="$t('settings.lockModelSetInfo.xposed')" labelLeft name="lockModelGroup" nativeValue="xposed" class="lock_model_radio"/>
        </mu-menu>
      </mu-dialog>
      <!--指纹设置-->
      <setting-item :settingName="$t('settings.fingerprintSet')"
                    :settingInfo="$t('settings.fingerprintSetInfo')"
                    :settingIcon="'fingerprint'"
                    :settingIconColor="'coral'"
                    @click.native="onBgImageSet"
                    :showSettingSwitch="true"/>
    </div>
</template>

<script>
  import SettingItem from './SettingsItem/SettingsItem'

  export default {
    name: "Settings",
    // 数据
    data() {
      return {
        lockModelDialogShow: false
      }
    },
    // 方法
    methods: {
      // 主题设置
      onThemeSet() {
        this.$router.push("/index/theme")
      },
      // 背景图片
      onBgImageSet() {
        this.lockModelDialogShow = true
      }
    },
    // 组件
    components: {
      SettingItem,
      'setting-item': SettingItem
    }
  }
</script>

<style lang="scss">
  #settings {
    width: 100%;
  }
  // 应用锁模式单选按钮
  .lock_model_radio {
    display: block;
    width: calc(100% - 30px);
    margin: 15px;
    /*padding: 0 10px;*/
  }
</style>
