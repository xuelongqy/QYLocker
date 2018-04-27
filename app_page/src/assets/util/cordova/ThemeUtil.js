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

// 获取主题列表
export let getThemeList = function (callBack) {
  ExecUtil.exec(
    function (themeList) {
      callBack(JSON.parse(themeList))
    },
    function (error) {
      callBack(JSON.parse(error))
    },
    "Theme",
    "getThemeList"
  )
}

// 删除主题
export let deleteTheme = function (callBack, themeName) {
  ExecUtil.exec(
    function () {
      callBack()
    },
    function () {
      callBack()
    },
    "Theme",
    "deleteTheme",
    [themeName]
  )
}

// 设置主题密码
export let setThemePwd = function (callBack, themeName) {
  ExecUtil.exec(
    function (isSuccess) {
      callBack(eval(isSuccess))
    },
    function (error) {
      callBack(eval(error))
    },
    "Theme",
    "setThemePwd",
    [themeName]
  )
}

export default {
  importTheme,
  getThemeList,
  deleteTheme,
  setThemePwd
}
