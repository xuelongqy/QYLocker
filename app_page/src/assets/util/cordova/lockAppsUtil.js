import ExecUtil from "./cordova-exec-util"

/**
 * @Title: getLockAppsConfig方法
 * @Description: 基本加锁配置信息
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/3/2 11:21
 * @update_author
 * @update_time
 * @version V1.0
 * @param callBack 回调函数(参数:{lockAppsConfig}配置信息)
 * @return
 * @throws
*/
export let getLockAppsConfig = function (callBack) {
  ExecUtil.exec(
    function (lockAppsConfig) {
      callBack(lockAppsConfig)
    },
    function (errorConfig) {
      callBack(errorConfig)
    },
    "LockApps",
    "getLockAppsConfig"
  )
}

/**
 * @Title: getAppInfoList方法
 * @Description: 获取应用信息列表
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/3/2 10:35
 * @update_author
 * @update_time
 * @version V1.0
 * @param callBack 回调函数(参数:{appsInfo}应用信息)
 * @return
 * @throws
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

/**
 * @Title: addLockApp方法
 * @Description: 添加加锁应用
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/3/2 10:40
 * @update_author
 * @update_time
 * @version V1.0
 * @param pkg 包名
 * @param callBack 回调函数(参数:{success}是否成功)
 * @return
 * @throws
*/
export let addLockApp = function (pkg) {
  ExecUtil.noCBExec(
    "LockApps",
    "addLockApp",
    [pkg]
  )
}

/**
 * @Title: removeLockApp
 * @Description: 删除加锁应用
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/3/2 10:40
 * @update_author
 * @update_time
 * @version V1.0
 * @param pkg 包名
 * @param callBack 回调函数(参数:{success}是否成功)
 * @return
 * @throws
 */
export let removeLockApp = function (pkg) {
  ExecUtil.noCBExec(
    "LockApps",
    "removeLockApp",
    [pkg]
  )
}

export default {
  getLockAppsConfig,
  getAppInfoList,
  addLockApp,
  removeLockApp
}
