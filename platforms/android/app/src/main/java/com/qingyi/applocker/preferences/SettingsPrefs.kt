package com.qingyi.applocker.preferences

import android.content.Context
import android.content.SharedPreferences
import com.crossbowffs.remotepreferences.RemotePreferences
import com.google.gson.GsonBuilder
import com.xposed.qingyi.cmprotectedappsplus.constant.ThisApp

/**
 * @Title: SettingsPrefs类
 * @Package: com.qingyi.applocker.preferences
 * @Description: 设置配置类
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/4/25 10:22
 * @update_author
 * @update_time
 * @version V1.0
 * @exception
*/
class SettingsPrefs(val context: Context) {
    // 伴生对象
    companion object {
        // 类的标识
        val TAG = SettingsPrefs::class.java.simpleName
        // 设置配置的Json
        private var cSettingsJson: String? = null
        // 设置配置对象
        private var cSettingsConfig: SettingsConfig? = null
    }

    // 获取加锁应用配置
    private val prefs: SharedPreferences = RemotePreferences(context,
            ThisApp.PREFERENCE_PROVIDER_AUTHORITY,
            ThisApp.PREFS_SETTINGS, true)
    // Json操作对象
    private val gson = GsonBuilder().create()

    // 设置配置的Json
    var settingsJson: String?
        get() {
            if (cSettingsConfig == null) {
                update()
            }
            return cSettingsJson
        }
        set(value) {// 设置并同步配置
            val editor: SharedPreferences.Editor = prefs.edit()
            editor.putString(ThisApp.PREFS_SETTINGS_KEY, value)
            editor.apply()
            cSettingsJson = value
        }

    // 设置配置对象
    var settingsConfig: SettingsConfig
        get() {
            if (cSettingsConfig == null) {
                update()
            }
            return cSettingsConfig!!
        }
        set(value) {
            cSettingsConfig = value
        }

    // 对象初始化
    init {
        // 获取配置
        if (cSettingsJson == null) {
            settingsJson
        }else {
            settingsConfig = gson.fromJson(cSettingsJson, SettingsConfig::class.java)
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
        cSettingsJson = prefs.getString(ThisApp.PREFS_SETTINGS_KEY, null)
        if (cSettingsJson == null) {
            settingsConfig = SettingsConfig()
            settingsJson = gson.toJson(settingsConfig)
        }else {
            settingsConfig = gson.fromJson(cSettingsJson, SettingsConfig::class.java)
        }
    }

    /**
     * @Title: setLockModel方法
     * @Class: SettingsPrefs
     * @Description: 设置加锁模式
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/4/25 13:45
     * @update_author
     * @update_time
     * @version V1.0
     * @param model[String] 应用锁模式
     * @return
     * @throws
    */
    fun setLockModel(model: String) {
        settingsConfig.lockModel = model
        settingsJson = gson.toJson(settingsConfig)
    }

    /**
     * @Title: setResetLockModel方法
     * @Class: SettingsPrefs
     * @Description: 设置重新加锁模式
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/4/25 13:53
     * @update_author
     * @update_time
     * @version V1.0
     * @param model[String] 重新加锁模式
     * @return
     * @throws
    */
    fun setResetLockModel(model: String) {
        settingsConfig.resetLockModel = model
        settingsJson = gson.toJson(settingsConfig)
    }

    // 设置是否使用指纹
    fun setUseFingerprint(isUse: Boolean) {
        settingsConfig.useFingerprint = isUse
        settingsJson = gson.toJson(settingsConfig)
    }

    // 设置是否启用高级模式
    fun setAdvancedMode(isOpen: Boolean) {
        settingsConfig.advancedMode = isOpen
        settingsJson = gson.toJson(settingsConfig)
    }

    // 设置是否锁定新应用
    fun setLockNewApp(isOpen: Boolean) {
        settingsConfig.lockNewApp = isOpen
        settingsJson = gson.toJson(settingsConfig)
    }

    // 设置防止卸载
    fun setPreventUninstall(isOpen: Boolean) {
        settingsConfig.preventUninstall = isOpen
        settingsJson = gson.toJson(settingsConfig)
    }

    // 设置背景图片
    fun setBgImageUrl(url: String) {
        settingsConfig.bgImageUrl = url
        settingsJson = gson.toJson(settingsConfig)
    }

    /**
     * @Title: SettingsConfigs类
     * @Package: com.qingyi.applocker.preferences
     * @Description: 设置配置类
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/4/25 9:56
     * @update_author
     * @update_time
     * @version V1.0
     * @exception
    */
    data class SettingsConfig(
            // 背景图片
            var bgImageUrl: String = "",
            // 应用锁模式
            var lockModel: String = "listenApps",
            // 重新锁定
            var resetLockModel: String = ThisApp.ONE_TO_ONE,
            // 使用指纹
            var useFingerprint: Boolean = false,
            // 高级模式
            var advancedMode: Boolean = false,
            // 锁定新应用
            var lockNewApp: Boolean = false,
            // 防止卸载
            var preventUninstall: Boolean = false
    )
}