import ExecUtil from "./cordova-exec-util"

/**
 * @Title: chooseFile方法
 * @Description: 选择文件
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/4/23 13:45
 * @update_author
 * @update_time
 * @version V1.0
 * @param callBack 回调函数
 * @param title 标题名字
 * @param accept 文件类型
 * @return
 * @throws
 */
export let chooseFile = function (callBack, title, accept) {
  ExecUtil.exec(
    function (url) {
      callBack(url)
    },
    function (error) {
      callBack(error)
    },
    "FilePicker",
    "chooseFile",
    [title, accept]
  )
}

export default {
  chooseFile
}
