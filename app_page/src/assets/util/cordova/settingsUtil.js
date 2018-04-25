import ExecUtil from "./cordova-exec-util"

/**
 * @Title: getSettingsConfig
 * @Description: 设置配置信息
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/3/2 11:21
 * @update_author
 * @update_time
 * @version V1.0
 * @param callBack 回调函数(参数:{lockAppsConfig}配置信息)
 * @return
 * @throws
 */
export let getSettingsConfig = function (callBack) {
  ExecUtil.exec(
    function (settingsConfig) {
      callBack(settingsConfig)
    },
    function (errorConfig) {
      callBack(errorConfig)
    },
    "Settings",
    "getSettingsConfig"
  )
}

// 设置应用锁模式
export let setLockModel = function (model) {
  ExecUtil.noCBExec(
    "Settings",
    "setLockModel",
    [model]
  )
}

// 设置重新锁定模式
export let setResetLockModel = function (model) {
  ExecUtil.noCBExec(
    "Settings",
    "setResetLockModel",
    [model]
  )
}

// 设置是否使用指纹
export let setUseFingerprint = function (isUse) {
  ExecUtil.noCBExec(
    "Settings",
    "setUseFingerprint",
    [isUse]
  )
}

// 设置是否启用高级模式
export let setAdvancedMode = function (isOpen) {
  ExecUtil.noCBExec(
    "Settings",
    "setAdvancedMode",
    [isOpen]
  )
}

// 设置是否锁定新应用
export let setLockNewApp = function (isOpen) {
  ExecUtil.noCBExec(
    "Settings",
    "setLockNewApp",
    [isOpen]
  )
}

// 设置防止卸载
export let setPreventUninstall = function (isOpen) {
  ExecUtil.noCBExec(
    "Settings",
    "setPreventUninstall",
    [isOpen]
  )
}

// 设置背景图片
export let setBgImageUrl = function (url) {
  ExecUtil.noCBExec(
    "Settings",
    "setBgImageUrl",
    [url]
  )
}

export default {
  getSettingsConfig,
  setLockModel,
  setResetLockModel,
  setUseFingerprint,
  setAdvancedMode,
  setLockNewApp,
  setPreventUninstall,
  setBgImageUrl
}
