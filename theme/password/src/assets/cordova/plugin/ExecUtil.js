/**
 * cordova插件交互工具
 * @File: cordova-exec-util.js
 * @Description: cordova插件交互工具
 * @author qingyi xuelongqy@foxmail.com
 * @date 2017/8/21 10:16
 * @version V1.0
 */

/**
 * 判断cordova是否可用
 * @return {boolean} 是否可用
 */
export let cordovaAvailable = function () {
  return !(typeof cordova == "undefined")
}

/**
 * cordova默认的执行方法
 * @param success 成功回调函数
 * @param fail 失败回调函数
 * @param service 服务名称(插件名称)
 * @param action 执行动作(对应调用的原生方法)
 * @param args 传入参数
 */
export let exec = function (success, fail, service, action, args = null) {
  if (cordovaAvailable()){
    cordova.exec(success, fail, service, action, args)
  }else {
    if (success != null) success()
    if (fail != null) success()
  }
}

/**
 * 没有回调的执行方法
 * @param service 服务名称(插件名称)
 * @param action 执行动作(对应调用的原生方法)
 * @param args 传入参数
 */
export let noCBExec = function (service, action, args = null) {
  if (cordovaAvailable()){
    cordova.exec(null, null, service, action, args)
  }
}

/**
 * 只有成功回调的执行方法
 * @param success 成功回调函数
 * @param service 服务名称(插件名称)
 * @param action 执行动作(对应调用的原生方法)
 * @param args 传入参数
 */
export let SCBExec = function (success, service, action, args = null) {
  if (cordovaAvailable()){
    cordova.exec(success, null, service, action, args)
  }
}

/**
 * 只有失败的回调方法
 * @param success 成功回调函数
 * @param fail 失败回调函数
 * @param service 服务名称(插件名称)
 * @param action 执行动作(对应调用的原生方法)
 * @param args 传入参数
 */
export let FCBExec = function (fail, service, action, args = null) {
  if (cordovaAvailable()){
    cordova.exec(null, fail, service, action, args)
  }
}

export default {
  cordovaAvailable,
  exec,
  noCBExec,
  SCBExec,
  FCBExec
}
