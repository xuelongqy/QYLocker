package com.xposed.qingyi.cmprotectedappsplus.constant

/**
 * 当前App相关的常量
 * @ClassName: ThisApp
 * @Description: 当前App相关的常量
 * @author qingyi xuelongqy@foxmail.com
 * @date 2017/8/19 17:25
 */
class ThisApp {
    companion object {
        // 当前APP名称
        const val APP_NAME = "QYLocker"
        // 当前APP的包名
        const val PACKAGE_NAME = "com.qingyi.applocker"
        // App前端(Html、CSS、JS、Json等)路径
        const val FRONTEND_PATH = "/android_asset/www/"

        // SharedPreferences相关
        const val PREFERENCE_PROVIDER_AUTHORITY = "com.qingyi.applocker.preferences"
        const val PREFS_LOCK_APPS = "lock_apps"
        const val PREFS_LOCK_APPS_KEY = "LockApps"
        const val PREFS_SETTINGS = "settings"
        const val PREFS_SETTINGS_KEY = "Settings"
        const val PREFS_HISTORY = "history"
        const val PREFS_HISTORY_KEY = "History"

        // 主题相关
        const val THEME_PATH = "theme"
        const val THEME_INFO_FILE = "theme.json"
        const val THEME_WEBAPP = "webapp"
        const val THEME_IMAGE = "image"

        // 重新锁定模式
        const val ONE_TO_ONE = "oneToOne"
        const val ONE_TO_ALL = "oneToAll"
        const val ALL_TO_ALL = "allToAll"
    }
}