import ExecUtil from "./cordova-exec-util"

/**
 * 获取应用列表
 */
export let getAppInfoList = function (callBack) {
  ExecUtil.exec(
    function (appsInfo) {
      callBack(appsInfo)
    },
    function (appsInfo) {
      callBack(appsInfo)
    },
    "LockApps",
    "getAppsInfo")
}

export default {
  getAppInfoList
}
