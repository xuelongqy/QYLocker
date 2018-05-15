import ThemeUtil from '../../assets/util/cordova/ThemeUtil'
import Vue from 'vue'

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
    // 自带主题标识
    BUILD_IN_THEMES: ["Pattern","PIN","Password"],
    // 下载的主题
    downloadedThemes: [],
    // 商店的主题
    storeThemes: [],
    // 是否在主题信息
    loadThemesInfo: false,
    // 滑到底部没有更多主题
    noMoreTheme: false,
    // 商店主题当前页
    currentPage: 1
  },
  // 同步方法
  mutations: {
    // 设置加载主题信息状态
    setLoadThemeInfo(state, status) {
      state.loadThemesInfo = status
    },
    // 设置是否还有更多主题
    setNoMoreTheme(state, status) {
      state.noMoreTheme = status
    },
    // 设置下载主题
    setDownLoadedThemes(state, themeList) {
      state.downloadedThemes = themeList
    },
    // 设置在线主题
    setStoreThemes(state, themeList) {
      state.storeThemes = themeList
    },
    // 设置当前页数
    setCurrentPage(state, page) {
      state.currentPage = page
    }
  },
  // 异步方法
  actions: {
    // 获取下载的主题信息
    getDownloadedThemesInfo({commit}) {
      commit("setLoadThemeInfo", true)
      ThemeUtil.getThemeList((themeList) => {
        commit("setDownLoadedThemes", themeList)
        commit("setLoadThemeInfo", false)
      })
    },
    // 获取在线主题
    getOnlineThemesInfo({state, commit}, searchKey) {
      commit("setLoadThemeInfo", true)
      commit("setCurrentPage", 1)
      // 获取主题列表
      Vue.prototype.$http.post('/theme/themeList',
        {
          page: state.currentPage,
          searchKey: searchKey
      }).then((response) => {
        const themeList = response.data.result
        // 计算地址
        for (const index in themeList) {
          // 文件
          themeList[index].file = Vue.prototype.$servicePath + themeList[index].file
          // 图片
          for (const imageIndex in themeList[index].images) {
            themeList[index].images[imageIndex] = Vue.prototype.$servicePath + themeList[index].images[imageIndex]
          }
        }
        commit('setStoreThemes', themeList)
        // console.log(themeList)
        commit("setLoadThemeInfo", false)
        // console.log(response.data.result)
        commit("setNoMoreTheme", false)
      }).catch((error) => {
        // 网络异常
        alert("Network anomaly -> " + error.toString())
        commit("setLoadThemeInfo", false)
      })
    },
    // 获取更多的在线主题
    getOnlineThemesInfoMore({state, commit}, searchKey) {
      commit("setCurrentPage", state.currentPage + 1)
      commit("setNoMoreTheme", false)
      // 获取主题列表
      Vue.prototype.$http.post('/theme/themeList',
        {
          page: state.currentPage,
          searchKey: searchKey
        }).then((response) => {
        const themeList = response.data.result
        // 判断列表是否为空
        if (themeList.length == 0) {
          commit("setCurrentPage", state.currentPage - 1)
          commit("setNoMoreTheme", true)
          return
        }else {
          commit("setNoMoreTheme", false)
        }
        // 计算地址
        for (const index in themeList) {
          // 文件
          themeList[index].file = Vue.prototype.$servicePath + themeList[index].file
          // 图片
          for (const imageIndex in themeList[index].images) {
            themeList[index].images[imageIndex] = Vue.prototype.$servicePath + themeList[index].images[imageIndex]
          }
        }
        commit('setStoreThemes', state.storeThemes.concat(themeList))
        // console.log(themeList)
        commit("setLoadThemeInfo", false)
        // console.log(response.data.result)
      }).catch((error) => {
        // 网络异常
        alert("Network anomaly -> " + error.toString())
        commit("setLoadThemeInfo", false)
      })
    }
  }
}
