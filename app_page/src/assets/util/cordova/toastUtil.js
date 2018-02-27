import ExecUtil from "./cordova-exec-util"

/**
 * 弹出一个长时间的Toast
 * @param msg 提示信息
 */
export let showLongToast = function (msg) {
  ExecUtil.noCBExec("Toast","showLongToast",[msg])
}

/**
 * 弹出一个短时间的Toast
 * @param msg 提示信息
 */
export let showShortToast = function (msg) {
  ExecUtil.noCBExec("Toast","showShortToast",[msg])
}

export default {
  showLongToast,
  showShortToast
}
