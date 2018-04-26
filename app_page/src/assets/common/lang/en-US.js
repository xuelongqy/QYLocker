/**
 * @Title: en-US.js 文件
 * @File: en-US.js
 * @Description: 英文字符串
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/1/26 10:21
 * @update_author
 * @update_time
 * @version V1.0
*/
module.exports = {
  comm: {
    "appName": "QYLocker",
    "author": "KnockYouth",
    "email": "xuelongqy@foxmail.com",
    "translators": "KnockYouth"
  },
  index: {
    "menu": "menu",
    "menu_list": [
      {
        "name": "App list",
        "flag": "AppList",
        "icon": "apps"
      },
      {
        "name": "Locker theme",
        "flag": "LockerTheme",
        "icon": "palette"
      },
      {
        "name": "Settings",
        "flag": "Settings",
        "icon": "settings"
      },
      {
        "name": "Help",
        "flag": "Help",
        "icon": "help"
      },
      {
        "name": "About",
        "flag": "About",
        "icon": "info"
      }
    ],
    "locked": "Locked",
    "unlocked": "Unlocked",
    "more": "more"
  },
  appList: {
    "allApps": "All",
    "sysApps": "System",
    "nonSysApps": "User",
    "search": "Search",
    "independentSetting": "Independent setting",
    "addPwd": "Add password",
    "filterPage": "Filter page",
    "setIndependentFail": "Independent setting failed,please verify that the password is added!"
  },
  theme: {
    "downloaded": "Downloaded",
    "chooseImage": "Choose image",
    "store": "Theme store",
    "name": "Name",
    "author": "Author",
    "date": "Date",
    "haveDownloaded": "Downloaded",
    "used": "Used",
    "importTheme": "Import local theme",
    "introduced": "Introduction to the Theme",
    "download": "Download",
    "use": "Use",
    "changePwd": "Change password",
    "chooseTheme": "Choose theme"
  },
  settings: {
    "unselected": "Unselected",
    "themeSet": "Theme Settings",
    "themeSetInfo": "Set the lock screen theme.",
    "bgImageSet": "Background image",
    "bgImageSetInfo": "It only shows after the theme is set.",
    "lockModelSet": "Lock Model",
    "lockModelSetInfo": {
      "stackPolling": "Stack polling",
      "listenApps": "Listen apps",
      "xposed": "Xposed"
    },
    "fingerprintSet": "Fingerprint",
    "fingerprintSetInfo": "Unlock with fingerprint",
    "resetLockSet": "Reset the lock",
    "resetLockSetInfo": {
      "oneToOne": "An app is unlocked once",
      "oneToAll": "All apps are unlocked once",
      "allToAll": "Unlock every time"
    },
    "advancedModeSet": "Advanced Mode",
    "advancedModeSetInfo": "The information for each lock app is displayed when unlocking",
    "lockNewAppSet": "Lock new app",
    "lockNewAppSetInfo": "Ask after installed the new app",
    "preventUninstallSet": "Prevent uninstall",
    "preventUninstallSetInfo": "Prevent uninstall of this app"
  }
}
