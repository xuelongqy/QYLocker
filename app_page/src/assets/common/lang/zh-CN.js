/**
 * @Title: zh-CN.js 文件
 * @File: zh-CN.js
 * @Description: 中文字符串
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/1/26 10:22
 * @update_author
 * @update_time
 * @version V1.0
*/
module.exports = {
  comm: {
    "appName": "轻易锁",
    "author": "KnockYouth",
    "email": "xuelongqy@foxmail.com",
    "translators": "KnockYouth"
  },
  index: {
    "menu": "菜单",
    "menu_list": [
      {
        "name": "应用列表",
        "flag": "AppList",
        "icon": "apps"
      },
      {
        "name": "锁屏主题",
        "flag": "LockerTheme",
        "icon": "palette"
      },
      {
        "name": "设置",
        "flag": "Settings",
        "icon": "settings"
      },
      {
        "name": "帮助",
        "flag": "Help",
        "icon": "help"
      },
      {
        "name": "关于",
        "flag": "About",
        "icon": "info"
      }
    ],
    "locked": "已上锁",
    "unlocked": "未上锁",
    "more": "更多"
  },
  appList: {
    "allApps": "所有应用",
    "sysApps": "系统应用",
    "nonSysApps": "用户应用",
    "search": "搜索",
    "independentSetting": "独立设置",
    "addPwd": "添加密码",
    "filterPage": "过滤页面",
    "setIndependentFail": "独立设置失败,请检验是否添加密码!"
  },
  theme: {
    "downloaded": "已下载",
    "store": "商店",
    "name": "名称",
    "author": "作者",
    "date": "日期",
    "haveDownloaded": "已下载",
    "used": "已使用",
    "importTheme": "导入本地主题",
    "introduced": "主题介绍",
    "download": "下载",
    "use": "使用",
    "changePwd": "修改密码",
    "chooseTheme": "选择主题",
    "importFail": "导入失败,请检验主题格式"
  },
  settings: {
    "unselected": "未选择",
    "chooseImage": "选择图片",
    "themeSet": "主题设置",
    "themeSetInfo": "设置锁屏主题",
    "bgImageSet": "背景图片",
    "bgImageSetInfo": "仅在主题中设置后生效",
    "lockModelSet": "应用锁模式",
    "lockModelSetInfo": {
      "stackPolling": "栈顶轮询",
      "listenApps": "应用监听",
      "xposed": "Xposed"
    },
    "fingerprintSet": "指纹设置",
    "fingerprintSetInfo": "使用指纹解锁",
    "resetLockSet": "重新锁定",
    "resetLockSetInfo": {
      "oneToOne": "一个应用一次解锁",
      "oneToAll": "所有应用一次解锁",
      "allToAll": "每次都需解锁"
    },
    "advancedModeSet": "高级模式",
    "advancedModeSetInfo": "解锁时显示每个锁定应用的信息",
    "lockNewAppSet": "锁定新应用",
    "lockNewAppSetInfo": "安装新应用后询问",
    "preventUninstallSet": "防止卸载",
    "preventUninstallSetInfo": "防止卸载此应用"
  }
}
