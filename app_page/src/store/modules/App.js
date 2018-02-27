/**
 * @Title: app.js 文件
 * @File: app.js
 * @Description: App相关的全局管理
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/1/29 14:59
 * @update_author
 * @update_time
 * @version V1.0
*/
import SNBarUtil from '../../assets/util/cordova/statusBarAndNavigationBarUtil'

export default {
  // 数据源
  state: {
    // 设备准备完成
    deviceReady: false,
    // 状态栏高度
    topHeight: 0
  },
  // 同步方法
  mutations: {
    // 设置设备为准备状态
    setDeviceReady(state) {
      state.deviceReady = true
    },
    // 设置状态栏高度
    setTopHeight(state, height) {
      state.topHeight = height
    }
  },
  // 异步方法
  actions: {
    // 设备准备完毕(初始化操作)
    async deviceIsReady({ commit }) {
      //获取顶部高度
      await SNBarUtil.getTopHeight( height => {
        commit('setTopHeight', height)
      })
      // 设置为准备完成状态
      commit('setDeviceReady')
    }
  }
}
