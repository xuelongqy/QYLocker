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
        // 加锁应用配置对象
        private var cLockAppsConfig: LockAppsConfig? = null
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
    var lockAppsConfig: LockAppsConfig
        get() {
            if (cLockAppsConfig == null) {
                update()
            }
            return cLockAppsConfig!!
        }
        set(value) {
            cLockAppsConfig = value
        }

    // 对象初始化
    init {
        // 获取配置
        if (cLockAppsJson == null) {
            lockAppsJson
        }else {
            lockAppsConfig = gson.fromJson(cLockAppsJson, LockAppsConfig::class.java)
        }
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
            lockAppsConfig = LockAppsConfig()
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
     * @Title: setLockState方法
     * @Class: LockAppsPrefs
     * @Description: 设置上锁状态
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/4/8 15:38
     * @update_author
     * @update_time
     * @version V1.0
     * @param state[Boolean] 上锁状态
     * @return
     * @throws
    */
    fun setLockState(state: Boolean) {
        LoggerUtil.logAndroid(Log.INFO, TAG, TAG + ".setLockState: state = $state")
        lockAppsConfig.isLock = state
        lockAppsJson = gson.toJson(lockAppsConfig)
    }

    /**
     * @Title: setIndependentSettingIState方法
     * @Class: LockAppsPrefs
     * @Description: 设置独立设置状态
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/4/23 13:30
     * @update_author
     * @update_time
     * @version V1.0
     * @param pkg[String] 包名
     * @param state[Boolean] 状态
     * @return [Boolean] 设置是否成功
     * @throws
    */
    fun setIndependentSettingIState(pkg: String, state: Boolean): Boolean {
        // 判断是否添加过密码
        if (state && lockAppsConfig.lockApps[pkg]!!.themes.isEmpty()) {
            return false
        }
        lockAppsConfig.lockApps[pkg]!!.isIndependent = state
        lockAppsJson = gson.toJson(lockAppsConfig)
        return true
    }

    /**
     * @Title: addFilterActivity方法
     * @Class: LockAppsPrefs
     * @Description: 添加过滤Activity
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/4/24 13:18
     * @update_author
     * @update_time
     * @version V1.0
     * @param pkg[String] 包名
     * @param activity[String] 页面
     * @return
     * @throws
    */
    fun addFilterActivity(pkg: String,activity: String) {
        lockAppsConfig.lockApps[pkg]!!.filterActivity.add(activity)
        lockAppsJson = gson.toJson(lockAppsConfig)
    }

    /**
     * @Title: removeFilterActivity方法
     * @Class: LockAppsPrefs
     * @Description: 删除过滤Activity
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/4/24 13:19
     * @update_author
     * @update_time
     * @version V1.0
     * @param pkg[String] 包名
     * @param activity[String] 页面
     * @return
     * @throws
    */
    fun removeFilterActivity(pkg: String,activity: String) {
        lockAppsConfig.lockApps[pkg]!!.filterActivity.remove(activity)
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