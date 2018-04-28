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

export default {
  cancelSetPwd,
  setPassword,
  unlock
}
