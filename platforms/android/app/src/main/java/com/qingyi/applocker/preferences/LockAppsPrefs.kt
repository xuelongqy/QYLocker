package com.qingyi.applocker.preferences

import android.content.Context
import com.qingyi.applocker.bean.LockAppConfigBean
import com.crossbowffs.remotepreferences.RemotePreferences
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import com.qingyi.applocker.bean.LockAppThemeBean
import com.qingyi.applocker.util.LoggerUtil
import com.xposed.qingyi.cmprotectedappsplus.constant.ThisApp
import java.security.MessageDigest
import kotlin.experimental.and


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
        // 获取配置对象
        fun getLockAppsConfig(context: Context): LockAppsConfig {
            val prefs: SharedPreferences = RemotePreferences(context,
                    ThisApp.PREFERENCE_PROVIDER_AUTHORITY,
                    ThisApp.PREFS_LOCK_APPS)
            val preJson = prefs.getString(ThisApp.PREFS_LOCK_APPS_KEY, null)
            if (preJson == null) {
                return LockAppsConfig()
            }else {
                return GsonBuilder().create().fromJson(preJson, LockAppsConfig::class.java)
            }
        }
        // 设置配置对象
        fun setLockAppsConfig(context: Context, lockAppsConfig: LockAppsConfig) {
            val prefs: SharedPreferences = RemotePreferences(context,
                    ThisApp.PREFERENCE_PROVIDER_AUTHORITY,
                    ThisApp.PREFS_LOCK_APPS)
            val editor: SharedPreferences.Editor = prefs.edit()
            editor.putString(ThisApp.PREFS_LOCK_APPS_KEY, GsonBuilder().create().toJson(lockAppsConfig))
            editor.apply()
        }
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
     * @Title: setThemeAndPwd方法
     * @Class: LockAppsPrefs
     * @Description: 设置主题和方法
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/4/28 15:00
     * @update_author
     * @update_time
     * @version V1.0
     * @param theme[String] 主题
     * @param pwd[String] 密码
     * @return
     * @throws
    */
    fun setThemeAndPwd(theme:String, pwd:String) {
        lockAppsConfig.theme = theme
        lockAppsConfig.password = encryptedPwd(pwd)
        lockAppsJson = gson.toJson(lockAppsConfig)
    }

    /**
     * @Title: encryptedPwd方法
     * @Class: LockAppsPrefs
     * @Description: 加密密码
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/4/28 15:05
     * @update_author
     * @update_time
     * @version V1.0
     * @param pwd[String] 密码
     * @return [String] 密文
     * @throws
     */
    private fun encryptedPwd(password:String):String {
        val instance = MessageDigest.getInstance("MD5")//获取md5加密对象
        val digest = instance.digest(password.toByteArray())//对字符串加密，返回字节数组
        val sb = StringBuffer()
        for (b in digest) {
            var i :Int = b.toInt() and 0xff//获取低八位有效值
            var hexString = Integer.toHexString(i)//将整数转化为16进制
            if (hexString.length < 2) {
                hexString = "0$hexString"//如果是一位的话，补0
            }
            sb.append(hexString)
        }
        return sb.toString()
    }

    /**
     * @Title: verifyPassword方法
     * @Class: LockAppsPrefs
     * @Description: 验证密码
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/4/28 15:56
     * @update_author
     * @update_time
     * @version V1.0
     * @param pwd[String] 密码
     * @return [Boolean] 密码是否正确
     * @throws
    */
    fun verifyPassword(pwd:String):Boolean {
        return lockAppsConfig.password == encryptedPwd(pwd)
    }

    /**
     * @Title: verifyPasswordByPwdName方法
     * @Class: LockAppsPrefs
     * @Description: 根据独立设置的密码名字验证密码
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/5/3 23:22
     * @update_author
     * @update_time
     * @param pkg[String] 包名
     * @param pwdName[String] 密码名字
     * @param pwd[String] 密码
     * @return [Boolean] 密码是否正确
     * @return
     * @throws
     * @version V1.0
    */
    fun verifyPasswordByPwdName(pkg:String, pwdName:String, pwd:String):Boolean {
        for (theme in lockAppsConfig.lockApps[pkg]!!.themes) {
            if (theme.name == pwdName) {
                return theme.password == encryptedPwd(pwd)
            }
        }
        return false
    }

    /**
     * @Title: addPwd方法
     * @Class: LockAppsPrefs
     * @Description: 添加密码
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/5/3 10:27
     * @update_author
     * @update_time
     * @version V1.0
     * @param pkg[String] 包名
     * @param name[String] 密码名字
     * @param pkg[String] 主题
     * @param pkg[String] 密码
     * @return
     * @throws
    */
    fun addAppPwd(pkg:String, name:String, theme:String, password: String) {
        val lockAppThemeBean = LockAppThemeBean(name, theme, encryptedPwd(password))
        lockAppsConfig.lockApps[pkg]!!.themes.add(lockAppThemeBean)
        lockAppsJson = gson.toJson(lockAppsConfig)
    }

    /**
     * @Title: removePwd方法
     * @Class: LockAppsPrefs
     * @Description: 删除密码
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/5/3 10:28
     * @update_author
     * @update_time
     * @version V1.0
     * @param pkg[String] 包名
     * @param name[String] 密码名字
     * @return
     * @throws
    */
    fun removeAppPwd(pkg:String, name:String){
        for (lockAppThemeBean in  lockAppsConfig.lockApps[pkg]!!.themes) {
            if (lockAppThemeBean.name == name) {
                lockAppsConfig.lockApps[pkg]!!.themes.remove(lockAppThemeBean)
                // 如果没有密码则取消独立模式
                if (lockAppsConfig.lockApps[pkg]!!.themes.isEmpty()) {
                    lockAppsConfig.lockApps[pkg]!!.isIndependent = false
                }
                lockAppsJson = gson.toJson(lockAppsConfig)
                break
            }
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
        @Expose
        var isLock: Boolean = false // 是否开启应用锁
        @Expose
        var theme: String = "" // 主题
        var password: String = "" // 密码
        var lockApps: HashMap<String, LockAppConfigBean> = hashMapOf()  // 加锁的应用
    }
}