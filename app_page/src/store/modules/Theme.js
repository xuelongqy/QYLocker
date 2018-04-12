/**
 * @Title: Theme.js文件
 * @File: Theme.js
 * @Description: 设置相关全局管理
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/4/12 15:06
 * @update_author
 * @update_time
 * @version V1.0
 * @exception
*/
export default {
  // 数据源
  state: {
    // 下载的主题
    downloadedThemes: [],
    // 商店的主题
    storeThemes: [],
    // 是否在主题信息
    loadThemesInfo: false
  },
  // 同步方法
  mutations: {
    // 设置加载主题信息状态
    setLoadThemeInfo(state, status) {
      state.loadThemesInfo = status
    }
  },
  // 异步方法
  actions: {
    // 获取下载的主题信息
    getDownloadedThemesInfo({commit}) {
      commit("setLoadThemeInfo", true)
    },
    // 获取在线主题
    getOnlineThemesInfo({commit}) {
      commit("setLoadThemeInfo", true)
    },
    // 获取更多的在线主题
    getOnlineThemesInfoMore({commit}) {
    }
  }
}
