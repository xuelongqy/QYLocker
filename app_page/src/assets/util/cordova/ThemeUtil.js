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
 * @param filePath 文件路径
 * @return
 * @throws
 */
export let importTheme = function (callBack, filePath) {
  ExecUtil.exec(
    function (isSuccess) {
      callBack(eval(isSuccess))
    },
    function (error) {
      callBack(eval(error))
    },
    "Theme",
    "importTheme",
    [filePath]
  )
}

export default {
  importTheme
}
