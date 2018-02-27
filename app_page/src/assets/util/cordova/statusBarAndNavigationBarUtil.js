import ExecUtil from "./cordova-exec-util"

/**
 * 获取高度
 * @param action 执行动作
 * @param callBack 回调函数(参数:{int}高度)
 */
let getHeight = function (action, callBack) {
  if (!ExecUtil.cordovaAvailable()){
    callBack(0)
  }
  ExecUtil.exec(
    function (h) {
      callBack(h)
    },
    function (h) {
      callBack(h)
    },
    "StatusBarAndNavigationBar",
    action
  )
}

/**
 * 获取状态栏高度
 * @param callBack 回调函数(参数:{int}高度)
 */
export let getStatusBarHeight = function (callBack) {
  getHeight("getTopHeight",callBack)
}

/**
 * 获取导航栏高度
 * @param callBack 回调函数(参数:{int}高度)
 */
export let getNavigationBarHeight = function (callBack) {
  getHeight("getNavigationBarHeight",callBack)
}

/**
 * 获取顶部高度
 * @param callBack 回调函数(参数:{int}高度)
 */
export let getTopHeight = function (callBack) {
  getHeight("getTopHeight",callBack)
}

/**
 * 获取底部高度
 * @param callBack 回调函数(参数:{int}高度)
 */
export let getBottomHeight = function (callBack) {
  getHeight("getBottomHeight",callBack)
}

export default {
  getStatusBarHeight,
  getNavigationBarHeight,
  getTopHeight,
  getBottomHeight
}
