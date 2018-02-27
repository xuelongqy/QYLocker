import ExecUtil from "./cordova-exec-util"

/**
 * 打印消息日志
 * @param important 是否重要
 * @param context 上下文,作为标识,方便查找日志输出的位置
 * @param msg 日志主要内容
 */
export let i = function (important, context, msg) {
  ExecUtil.noCBExec("Logger","i",[important,context,msg])
}

/**
 * 打印调试日志
 * @param important 是否重要
 * @param context 上下文,作为标识,方便查找日志输出的位置
 * @param msg 日志主要内容
 */
export let d = function (important, context, msg) {
  ExecUtil.noCBExec("Logger","d",[important,context,msg])
}

/**
 * 打印警告日志
 * @param important 是否重要
 * @param context 上下文,作为标识,方便查找日志输出的位置
 * @param msg 日志主要内容
 */
export let w = function (important, context, msg) {
  ExecUtil.noCBExec("Logger","w",[important,context,msg])
}

/**
 * 打印错误日志
 * @param important 是否重要
 * @param context 上下文,作为标识,方便查找日志输出的位置
 * @param msg 日志主要内容
 */
export let e = function (important, context, msg) {
  ExecUtil.noCBExec("Logger","e",[important,context,msg])
}

export default {
  i,d,w,e
}
