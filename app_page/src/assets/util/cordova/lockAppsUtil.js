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
 * @Title: setLockState方法
 * @Description: 设置上锁状态
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/4/8 15:42
 * @update_author
 * @update_time
 * @version V1.0
 * @param state 上锁状态
 * @return
 * @throws
*/
export let setLockState = function (state) {
  ExecUtil.noCBExec(
    "LockApps",
    "setLockState",
    [state]
  )
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

/**
 * @Title: openApp方法
 * @Description: 启动App
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/4/11 10:32
 * @update_author
 * @update_time
 * @version V1.0
 * @param pkg 启动应用的包名
 * @return
 * @throws
*/
export let openApp = function (pkg) {
  ExecUtil.noCBExec(
    "LockApps",
    "openApp",
    [pkg]
  )
}

/**
 * @Title: setIndependentSettingIState方法
 * @Description: 设置独立设置状态
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/4/23 13:45
 * @update_author
 * @update_time
 * @version V1.0
 * @param callBack 回调函数
 * @param pkg 包名
 * @param state 状态
 * @return
 * @throws
*/
export let setIndependentSettingIState = function (callBack, pkg, state) {
  ExecUtil.exec(
    function (isSuccess) {
      callBack(eval(isSuccess))
    },
    function (error) {
      callBack(eval(error))
    },
    "LockApps",
    "setIndependentSettingIState",
    [pkg, state]
  )
}

/**
 * @Title: getActivities方法
 * @Description: 获取页面列表
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/4/24 9:19
 * @update_author
 * @update_time
 * @version V1.0
 * @param callBack 回调函数
 * @param pkg 包名
 * @return
 * @throws
*/
export let getActivities = function (callBack, pkg) {
  ExecUtil.exec(
    function (activities) {
      callBack(JSON.parse(activities))
    },
    function (error) {
      callBack(JSON.parse(error))
    },
    "LockApps",
    "getActivities",
    [pkg]
  )
}

/**
 * @Title: addFilterActivity方法
 * @Description: 添加过滤页面
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/4/24 13:34
 * @update_author
 * @update_time
 * @version V1.0
 * @param pkg 包名
 * @param activity 页面
 * @return
 * @throws
*/
export let addFilterActivity = function (pkg, activity) {
  ExecUtil.noCBExec(
    "LockApps",
    "addFilterActivity",
    [pkg,activity]
  )
}

/**
 * @Title: removeFilterActivity方法
 * @Description: 删除过滤页面
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/4/24 13:34
 * @update_author
 * @update_time
 * @version V1.0
 * @param pkg 包名
 * @param activity 页面
 * @return
 * @throws
 */
export let removeFilterActivity = function (pkg, activity) {
  ExecUtil.noCBExec(
    "LockApps",
    "removeFilterActivity",
    [pkg,activity]
  )
}

export default {
  getLockAppsConfig,
  getAppInfoList,
  setLockState,
  addLockApp,
  removeLockApp,
  openApp,
  setIndependentSettingIState,
  getActivities,
  addFilterActivity,
  removeFilterActivity
}
