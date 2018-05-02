import ExecUtil from "./ExecUtil"

/**
 * @Title: cancelSetPwd方法
 * @Description: 取消密码设置
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/4/28 14:28
 * @update_author
 * @update_time
 * @version V1.0
 * @param
 * @return
 * @throws
*/
export let cancelSetPwd = function () {
  ExecUtil.noCBExec(
    "Theme",
    "cancelSetPwd"
  )
}

/**
 * @Title: setPassword方法
 * @Description: 设置密码
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/4/28 15:22
 * @update_author
 * @update_time
 * @version V1.0
 * @param pwd 密码
 * @return
 * @throws
*/
export let setPassword = function (pwd) {
  ExecUtil.noCBExec(
    "Theme",
    "setPassword",
    [pwd]
  )
}

/**
 * @Title: unlock方法
 * @Description: 解锁
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/4/28 16:22
 * @update_author
 * @update_time
 * @version V1.0
 * @param callBack 回调函数,返回是否解锁成功,目前只返回false,解锁成功会由程序处理
 * @param pwd 密码
 * @return
 * @throws
*/
export let unlock = function (callBack, pwd) {
  ExecUtil.exec(
    function (isUnlock) {
      callBack(eval(isUnlock))
    },
    function (error) {
      callBack(eval(error))
    },
    "Theme",
    "unlock",
    [pwd]
  )
}

/**
 * @Title: getBgImg方法
 * @Description: 获取背景图片
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/4/28 16:22
 * @update_author
 * @update_time
 * @version V1.0
 * @param callBack 回调函数,返回背景图片,异常返回为""
 * @return
 * @throws
 */
export let getBgImg = function (callBack) {
  ExecUtil.exec(
    function (img) {
      callBack(img)
    },
    function (error) {
      callBack(error)
    },
    "Theme",
    "getBgImg"
  )
}

/**
 * @Title: isFingerprint方法
 * @Description: 获取是否支持指纹
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/4/28 16:22
 * @update_author
 * @update_time
 * @version V1.0
 * @param callBack 回调函数,返回是否支持指纹
 * @return
 * @throws
 */
export let isFingerprint = function (callBack) {
  ExecUtil.exec(
    function (is) {
      callBack(eval(is))
    },
    function (error) {
      callBack(eval(error))
    },
    "Theme",
    "isFingerprint"
  )
}

/**
 * @Title: setFingerprintListener方法
 * @Description: 设置指纹监听事件
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/4/28 16:22
 * @update_author
 * @update_time
 * @version V1.0
 * @param callBack 回调函数,判断指纹是否成功以及提示信息格式为{ "success":false,"msg":"" }
 * @return
 * @throws
 */
export let setFingerprintListener = function (callBack) {
  ExecUtil.exec(
    function (data) {
      callBack(data)
    },
    function (error) {
      callBack(error)
    },
    "Theme",
    "setFingerprintListener"
  )
}

/**
 * @Title: getThemeData方法
 * @Description: 获取主题额外数据
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/5/2 13:55
 * @update_author
 * @update_time
 * @version V1.0
 * @param callBack 回调函数,返回主题额外数据
 * @return
 * @throws
*/
export let getThemeData = function (callBack) {
  ExecUtil.exec(
    function (data) {
      callBack(data)
    },
    function (error) {
      callBack(error)
    },
    "Theme",
    "getThemeData"
  )
}

/**
 * @Title: setThemeData方法
 * @Description: 设置主题额外数据
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/5/2 13:57
 * @update_author
 * @update_time
 * @version V1.0
 * @param data 主题数据
 * @return
 * @throws
*/
export let setThemeData = function (data) {
  ExecUtil.noCBExec(
    "Theme",
    "setThemeData",
    [data]
  )
}

export default {
  cancelSetPwd,
  setPassword,
  unlock,
  getBgImg,
  isFingerprint,
  setFingerprintListener,
  getThemeData,
  setThemeData
}
