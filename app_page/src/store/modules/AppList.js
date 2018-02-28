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
import LockAppsUtil from '../../assets/util/cordova/lockAppsUtil'

export default {
  // 数据源
  state: {
    // 所有应用信息
    allAppsInfo: [],
    // 是否在加载应用信息
    loadAppsInfo: false
  },
  // 同步方法
  mutations: {
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
    }
  },
  // 异步方法
  actions: {
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
    }
  }
}
