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
    // 上锁状态
    lockState: false
  },
  // 同步方法
  mutations: {
    // 设置上锁状态
    setLockState(state, lockState) {
      state.lockState = lockState
    }
  },
  // 异步方法
  actions: {
    // 修改并同步上锁状态
    modLockState({ commit }, lockState) {
      commit('setLockState', lockState)
    }
  }
}
