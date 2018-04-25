import Vue from 'vue'
import settingsUtil from '../../assets/util/cordova/settingsUtil'

/**
 * @Title: Settings.js 文件
 * @File: Settings.js
 * @Description: 设置相关全局管理
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/2/5 9:41
 * @update_author
 * @update_time
 * @version V1.0
*/
export default {
  // 数据源
  state: {
    // 设置配置信息
    settingsConfig: {
      bgImageUrl: "",
      lockModel: "",
      resetLockModel:  "",
      useFingerprint: false,
      advancedMode: false,
      lockNewApp: false,
      preventUninstall: false
    }
  },
  // 同步方法
  mutations: {
    // 设置基本加锁配置信息
    setSettingsConfig(state, conf) {
      if (typeof conf == 'undefined' || conf == null) {
        return
      }
      // 判断是否为Json格式
      if (conf instanceof Object) {
        state.settingsConfig = conf
      }else {
        state.settingsConfig = JSON.parse(conf)
      }
    },
    // 通过UI更新设置配置中的值
    updateSettingConfigByView(state, {key, value}) {
      Vue.set(state.settingsConfig, key, value)
    }
  },
  // 异步方法
  actions: {
    // 获取基本加锁配置信息
    getSettingsConfig({commit}) {
      settingsUtil.getSettingsConfig((settingsConfig) => {
        commit('setSettingsConfig', settingsConfig)
      })
    },
  }
}
