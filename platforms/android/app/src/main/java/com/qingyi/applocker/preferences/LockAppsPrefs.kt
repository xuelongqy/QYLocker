package com.qingyi.applocker.preferences

import android.content.Context
import com.qingyi.applocker.bean.LockAppConfigBean
import com.crossbowffs.remotepreferences.RemotePreferences
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import com.qingyi.applocker.util.LoggerUtil
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
        // 类的标识
        val TAG = LockAppsPrefs::class.java.simpleName
        // 加锁应用配置的Json
        private var cLockAppsJson: String? = null
    }

    // 获取加锁应用配置
    private val prefs: SharedPreferences = RemotePreferences(context,
            ThisApp.PREFERENCE_PROVIDER_AUTHORITY,
            ThisApp.PREFS_LOCK_APPS, true)
    // Json操作对象
    private val gson = GsonBuilder().create()

    // 加锁应用配置的Json
    var lockAppsJson: String?
        get() {
            if (cLockAppsJson == null) {
                update()
            }
            return cLockAppsJson
        }
        set(value) {// 设置并同步配置
            val editor: SharedPreferences.Editor = prefs.edit()
            editor.putString(ThisApp.PREFS_LOCK_APPS_KEY, value)
            editor.apply()
            cLockAppsJson = value
        }
    // 加锁应用配置对象
    var lockAppsConfig: LockAppsConfig = LockAppsConfig()

    // 对象初始化
    init {
        // 获取配置
        lockAppsJson
    }

    /**
     * @Title: update方法
     * @Class: LockAppsPrefs
     * @Description: 更新配置
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/3/2 10:20
     * @update_author
     * @update_time
     * @version V1.0
     * @param
     * @return
     * @throws
    */
    fun update() {
        cLockAppsJson = prefs.getString(ThisApp.PREFS_LOCK_APPS_KEY, null)
        if (cLockAppsJson == null) {
            lockAppsJson = gson.toJson(lockAppsConfig)
        }else {
            lockAppsConfig = gson.fromJson(cLockAppsJson, LockAppsConfig::class.java)
        }
    }

    /**
     * @Title: addLockApp方法
     * @Class: LockAppsPrefs
     * @Description: 添加应用锁
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/3/2 10:21
     * @update_author
     * @update_time
     * @version V1.0
     * @param pkg[String] 包名
     * @return
     * @throws
    */
    fun addLockApp(pkg: String) {
        LoggerUtil.logAndroid(Log.INFO, TAG, TAG + ".addLockApp: PackageName = $pkg")
        lockAppsConfig.lockApps[pkg] = LockAppConfigBean(pkg)
        lockAppsJson = gson.toJson(lockAppsConfig)
    }

    /**
     * @Title: removeLockApp方法
     * @Class: LockAppsPrefs
     * @Description: 删除应用锁
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/3/2 10:21
     * @update_author
     * @update_time
     * @version V1.0
     * @param pkg[String] 包名
     * @return
     * @throws
    */
    fun removeLockApp(pkg: String) {
        LoggerUtil.logAndroid(Log.INFO, TAG, TAG + ".removeLockApp: PackageName = $pkg")
        lockAppsConfig.lockApps.remove(pkg)
        lockAppsJson = gson.toJson(lockAppsConfig)
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
        @Expose
        var isLock: Boolean = false // 是否开启应用锁
        @Expose
        var theme: String = "" // 主题
        var password: String = "" // 密码
        var lockApps: HashMap<String, LockAppConfigBean> = hashMapOf()  // 加锁的应用
    }
}