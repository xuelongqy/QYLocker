package com.qingyi.applocker.preferences

import android.content.Context
import com.qingyi.applocker.bean.LockAppConfigBean
import com.crossbowffs.remotepreferences.RemotePreferences
import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import com.xposed.qingyi.cmprotectedappsplus.constant.ThisApp


/**
 * @Title: LockAppsPrefs类
 * @Package: com.qingyi.applocker.preferences
 * @Description: 加锁应用的配置
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/2/27 9:58
 * @update_author
 * @update_time
 * @version V1.0
 * @exception
*/
class LockAppsPrefs(val context: Context) {
    // 伴生对象
    companion object {
        private var cLockAppsJson: String? = null // 加锁应用配置的Json
    }

    // 获取加锁应用配置
    private val prefs: SharedPreferences = RemotePreferences(context,
            ThisApp.PREFERENCE_PROVIDER_AUTHORITY,
            ThisApp.PREFS_LOCK_APPS, true)
    // Json操作对象
    private var gson = GsonBuilder().setPrettyPrinting().create()

    // 加锁应用配置的Json
    var lockAppsJson: String?
        get() {
            if (cLockAppsJson == null) {
                update()
            }
            return cLockAppsJson
        }
        set(value) {
            val editor: SharedPreferences.Editor = prefs.edit()
            editor.putString(ThisApp.PREFS_LOCK_APPS_KEY, value)
            editor.apply()
            cLockAppsJson = value
        }
    // 加锁应用配置对象
    var lockAppsConfig: LockAppsConfig = LockAppsConfig()
    var isLock: Boolean // 是否开启应用锁
        get() {
            return lockAppsConfig.isLock
        }
        set(value) {
            lockAppsConfig.isLock = value
            lockAppsJson = gson.toJson(lockAppsConfig)
        }
    var theme: String // 主题
        get() {
            return lockAppsConfig.theme
        }
        set(value) {
            lockAppsConfig.theme = value
            lockAppsJson = gson.toJson(lockAppsConfig)
        }
    var password: String // 密码
        get() {
            return lockAppsConfig.password
        }
        set(value) {
            lockAppsConfig.password = value
            lockAppsJson = gson.toJson(lockAppsConfig)
        }
    var lockApps: ArrayList<LockAppConfigBean> // 加锁的应用
        get() {
            return lockAppsConfig.lockApps
        }
        set(value) {
            lockAppsConfig.lockApps = value
            lockAppsJson = gson.toJson(lockAppsConfig)
        }

    // 对象初始化
    init {
        // 获取配置
        lockAppsJson
    }

    // 更新配置
    fun update() {
        cLockAppsJson = prefs.getString(ThisApp.PREFS_LOCK_APPS_KEY, null)
        if (cLockAppsJson != null) {
            lockAppsConfig = gson.fromJson(cLockAppsJson, LockAppsConfig::class.java)
        }else {
            lockAppsJson = gson.toJson(lockAppsConfig)
        }
    }

    /**
     * @Title: LockAppsConfig类
     * @Package: com.qingyi.applocker.preferences
     * @Description: 加锁App配置
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/2/27 14:55
     * @update_author
     * @update_time
     * @version V1.0
     * @exception
    */
    class LockAppsConfig{
        var isLock: Boolean = false // 是否开启应用锁
        var theme: String = "" // 主题
        var password: String = "" // 密码
        var lockApps: ArrayList<LockAppConfigBean> = arrayListOf() // 加锁的应用
    }
}