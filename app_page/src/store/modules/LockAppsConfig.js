/**
 * @Title: AppList.js 文件
 * @File: AppList.js
 * @Description: 应用列表相关全局管理
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/2/27 17:56
 * @update_author
 * @update_time
 * @version V1.0
*/
import Vue from 'vue'
import LockAppsUtil from '../../assets/util/cordova/lockAppsUtil'

export default {
  // 数据源
  state: {
    // 基本加锁配置信息
    lockAppsConfig: {
      isLock: false,
      theme: ""
    },
    // 所有应用信息
    allAppsInfo: [],
    // 是否在加载应用信息
    loadAppsInfo: false
  },
  // 同步方法
  mutations: {
    // 设置基本加锁配置信息
    setLockAppsConfig(state, conf) {
      if (typeof conf == 'undefined' || conf == null) {
        return
      }
      // 判断是否为Json格式
      if (conf instanceof Object) {
        state.lockAppsConfig = conf
      }else {
        state.lockAppsConfig = JSON.parse(conf)
      }
    },
    // 所有应用信息
    setAllAppsInfo(state, allAppsInfo) {
      // 判断是否为数组格式
      if (allAppsInfo instanceof Array) {
        state.allAppsInfo = allAppsInfo
      }else {
        state.allAppsInfo = JSON.parse(allAppsInfo)
      }
      state.loadAppsInfo = false
    },
    // 设置加载应用信息状态
    setLoadAppsInfo(state, status) {
      state.loadAppsInfo = status
    },
    // 修改上锁状态
    setLockState(state, lockState) {
      Vue.set(state.lockAppsConfig, "isLock", lockState)
    },
    // 通过界面操作更新应用列表信息
    updateAppsInfoByView(state, {index, key, value}) {
      Vue.set(state.allAppsInfo[index], key, value)
    }
  },
  // 异步方法
  actions: {
    // 获取基本加锁配置信息
    getLockAppsConfig({commit}) {
      LockAppsUtil.getLockAppsConfig((lockAppsConfig) => {
        commit('setLockAppsConfig', lockAppsConfig)
      })
    },
    // 获取所有应用信息
    getAllAppsInfo({commit}) {
      commit('setLoadAppsInfo', true)
      LockAppsUtil.getAppInfoList((appsInfo) => {
        // 判断是否有返回值
        if (typeof appsInfo == "undefined") {
          appsInfo = []
        }
        commit('setAllAppsInfo', appsInfo)
      })
    },
    // 修改并同步上锁状态
    modLockState({ commit }, lockState) {
      LockAppsUtil.setLockState(lockState)
      commit('setLockState', lockState)
    }
  }
}
