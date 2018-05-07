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
                    :settingInfo="lockModelSetInfo"
                    :settingIcon="'dashboard'"
                    :settingIconColor="'red'"
                    @click.native="lockModelDialogShow = true"/>
      <!--应用锁模式选择弹出框-->
      <mu-dialog :open="lockModelDialogShow" :title="$t('settings.lockModelSet')" @close="lockModelDialogShow = false">
        <mu-menu>
          <mu-radio :label="$t('settings.lockModelSetInfo.stackPolling')" v-model="lockModel" @change="lockModelChange" labelLeft name="lockModelGroup" :disabled="lockModel == 'xposed'" nativeValue="stackPolling" class="lock_model_radio"/>
          <mu-radio :label="$t('settings.lockModelSetInfo.listenApps')" v-model="lockModel" @change="lockModelChange" labelLeft name="lockModelGroup" :disabled="lockModel == 'xposed'" nativeValue="listenApps" class="lock_model_radio"/>
          <mu-radio :label="$t('settings.lockModelSetInfo.xposed')" v-model="lockModel" @change="lockModelChange" labelLeft name="lockModelGroup" disabled nativeValue="xposed" class="lock_model_radio"/>
        </mu-menu>
      </mu-dialog>
      <!--重新锁定-->
      <setting-item :settingName="$t('settings.resetLockSet')"
                    :settingInfo="resetLockSetInfo"
                    :settingIcon="'restore'"
                    :settingIconColor="'green'"
                    @click.native="resetLockDialogShow = true"/>
      <!--重新锁定模式选择弹出框-->
      <mu-dialog :open="resetLockDialogShow" :title="$t('settings.resetLockSet')" @close="resetLockDialogShow = false">
        <mu-menu>
          <mu-radio :label="$t('settings.resetLockSetInfo.oneToOne')" :value="resetLock" @change="resetLockModelChange" labelLeft name="resetLockGroup" nativeValue="oneToOne" class="lock_model_radio"/>
          <mu-radio :label="$t('settings.resetLockSetInfo.oneToAll')" :value="resetLock" @change="resetLockModelChange" labelLeft name="resetLockGroup" nativeValue="oneToAll" class="lock_model_radio"/>
          <mu-radio :label="$t('settings.resetLockSetInfo.allToAll')" :value="resetLock" @change="resetLockModelChange" labelLeft name="resetLockGroup" nativeValue="allToAll" class="lock_model_radio"/>
        </mu-menu>
      </mu-dialog>
      <!--指纹设置-->
      <setting-item :settingName="$t('settings.fingerprintSet')"
                    :settingInfo="$t('settings.fingerprintSetInfo')"
                    :settingIcon="'fingerprint'"
                    :settingIconColor="'coral'"
                    :showSettingSwitch="true"
                    :settingsSwitchValue="$store.state.Settings.settingsConfig.useFingerprint"
                    :onSettingsSwitch="fingerprintChange"/>
      <!--高级模式-->
      <setting-item :settingName="$t('settings.advancedModeSet')"
                    :settingInfo="$t('settings.advancedModeSetInfo')"
                    :settingIcon="'build'"
                    :settingIconColor="'chocolate'"
                    :showSettingSwitch="true"
                    :settingsSwitchValue="$store.state.Settings.settingsConfig.advancedMode"
                    :onSettingsSwitch="advancedModeChange"/>
      <!--锁定新应用-->
      <setting-item :settingName="$t('settings.lockNewAppSet')"
                    :settingInfo="$t('settings.lockNewAppSetInfo')"
                    :settingIcon="'fiber_new'"
                    :settingIconColor="'blueviolet'"
                    :showSettingSwitch="true"
                    :settingsSwitchValue="$store.state.Settings.settingsConfig.lockNewApp"
                    :onSettingsSwitch="lockNewAppChange"/>
      <!--防止卸载-->
      <setting-item :settingName="$t('settings.preventUninstallSet')"
                    :settingInfo="$t('settings.preventUninstallSetInfo')"
                    :settingIcon="'pan_tool'"
                    :settingIconColor="'brown'"
                    :showSettingSwitch="true"
                    :settingsSwitchValue="$store.state.Settings.settingsConfig.preventUninstall"
                    :onSettingsSwitch="preventUninstallChange"/>
    </div>
</template>

<script>
  import SettingItem from './SettingsItem/SettingsItem'
  import settingsUtil from '../../../assets/util/cordova/settingsUtil'
  import FilePicker from "../../../assets/util/cordova/FilePicker"

  export default {
    name: "Settings",
    // 数据
    data() {
      return {
        // 应用锁模式弹窗状态
        lockModelDialogShow: false,
        // 重新锁定模式弹窗状态
        resetLockDialogShow: false
      }
    },
    // 计算方法
    computed: {
      // 应用锁模式
      lockModel() {
        return this.$store.state.Settings.settingsConfig.lockModel
      },
      lockModelSetInfo() {
        switch (this.lockModel) {
          case "stackPolling":
            return this.$t('settings.lockModelSetInfo.stackPolling')
          case "listenApps":
            return this.$t('settings.lockModelSetInfo.listenApps')
          case "xposed":
            return this.$t('settings.lockModelSetInfo.xposed')
          default:
            return this.$t('settings.unselected')
        }
      },
      // 重新锁定模式
      resetLock() {
        return this.$store.state.Settings.settingsConfig.resetLockModel
      },
      resetLockSetInfo() {
        switch (this.resetLock) {
          case "oneToOne":
            return this.$t('settings.resetLockSetInfo.oneToOne')
          case "oneToAll":
            return this.$t('settings.resetLockSetInfo.oneToAll')
          case "allToAll":
            return this.$t('settings.resetLockSetInfo.allToAll')
          default:
            return this.$t('settings.unselected')
        }
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
        FilePicker.chooseFile(this.bgImageChange,this.$t("settings.chooseImage"),"image/*")
      },
      // 背景图片改变
      bgImageChange(imgUrl) {
        alert(imgUrl)
        settingsUtil.setBgImageUrl(imgUrl)
        this.$store.commit('updateSettingConfigByView', {
          key: 'bgImageUrl',
          value: imgUrl
        })
      },
      // 应用锁模式改变
      lockModelChange(model) {
        settingsUtil.setLockModel(model)
        this.$store.commit('updateSettingConfigByView', {
          key: 'lockModel',
          value: model
        })
      },
      // 重新锁定模式改变
      resetLockModelChange(model) {
        settingsUtil.setResetLockModel(model)
        this.$store.commit('updateSettingConfigByView', {
          key: 'resetLockModel',
          value: model
        })
      },
      // 指纹设置改变
      fingerprintChange(value) {
        settingsUtil.setUseFingerprint(value)
        this.$store.commit('updateSettingConfigByView', {
          key: 'useFingerprint',
          value: value
        })
      },
      // 高级模式设置
      advancedModeChange(value) {
        settingsUtil.setAdvancedMode(value)
        this.$store.commit('updateSettingConfigByView', {
          key: 'advancedMode',
          value: value
        })
      },
      // 锁定新应用改变
      lockNewAppChange(value) {
        settingsUtil.setLockNewApp(value)
        this.$store.commit('updateSettingConfigByView', {
          key: 'lockNewApp',
          value: value
        })
      },
      // 防止卸载改变
      preventUninstallChange(value) {
        settingsUtil.setPreventUninstall(value)
        this.$store.commit('updateSettingConfigByView', {
          key: 'preventUninstall',
          value: value
        })
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
    position: absolute;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    overflow:auto;
  }
  // 应用锁模式单选按钮
  .lock_model_radio {
    display: block;
    width: calc(100% - 30px);
    margin: 15px;
  }
</style>
