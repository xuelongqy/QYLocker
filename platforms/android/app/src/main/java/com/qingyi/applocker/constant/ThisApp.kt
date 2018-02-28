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
        val APP_NAME = "QYLocker"
        // 当前APP的包名
        val PACKAGE_NAME = "com.qingyi.applocker"
        // App前端(Html、CSS、JS、Json等)路径
        val FRONTEND_PATH = "/android_asset/www/"
        // 保存App信息的json文件路径
        val APP_INFO_JSON_FILE_PATH = "/android_asset/data/locker/app_info_map.json"

        // SharedPreferences相关
        val PREFERENCE_PROVIDER_AUTHORITY = "com.qingyi.applocker.preferences"
        val PREFS_LOCK_APPS = "lock_apps"
        val PREFS_LOCK_APPS_KEY = "LockApps"
        val PREFS_HISTORY = "history"
    }
}