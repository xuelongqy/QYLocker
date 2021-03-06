package com.qingyi.applocker.cordova.plugin

import android.app.Activity
import com.qingyi.applocker.bean.AppInfoJsonBean
import com.qingyi.applocker.preferences.LockAppsPrefs
import com.qingyi.applocker.util.AppsUtil
import org.apache.cordova.CallbackContext
import org.apache.cordova.CordovaInterface
import org.apache.cordova.CordovaPlugin
import org.apache.cordova.CordovaWebView
import org.json.JSONArray
import android.graphics.drawable.Drawable
import android.util.Base64
import java.io.ByteArrayOutputStream
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import com.google.gson.GsonBuilder
import com.qingyi.applocker.util.LoggerUtil
import android.content.Intent
import com.qingyi.applocker.util.ImageBase64Util


/**
 * @Title: LockAppsPlugin类
 * @Package: com.qingyi.applocker.cordova.plugin
 * @Description: 应用列表插件(获取所有应用信息、上锁状态等)
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/2/26 11:11
 * @update_author
 * @update_time
 * @version V1.0
 * @exception
*/
class LockAppsPlugin : CordovaPlugin() {
    // 伴生对象
    companion object {
        // 类的标识
        val TAG = LockAppsPlugin::class.java.simpleName
    }

    // 缓存调用插件的Activity
    lateinit var mActivity: Activity
    // 应用信息工具
    lateinit var appsUtil: AppsUtil
    // 加锁应用配置
    lateinit var lockAppsPrefs: LockAppsPrefs
    // Json操作对象
    private val gson = GsonBuilder().create()

    /**
     * 重写初始化插件方法
     * 获取调用插件的Activity
     * @Title: initialize
     * @Description: 重写初始化插件方法
     * @author qingyi xuelongqy@foxmail.com
     * @date 2017/8/21 0:20
     * @param cordova cordova的接口
     * @param webView cordova的Web视图
     */
    override fun initialize(cordova: CordovaInterface?, webView: CordovaWebView?) {
        super.initialize(cordova, webView)
        // 获取调用的Activity
        mActivity = cordova!!.activity

        // 初始化对象
        appsUtil = AppsUtil(mActivity.applicationContext)
        lockAppsPrefs = LockAppsPrefs(mActivity.applicationContext)
    }

    /**
     * @Title: 重写execute方法
     * @Class: LockAppsPlugin
     * @Description: App列表相关交互执行
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/2/26 11:17
     * @update_author
     * @update_time
     * @version V1.0
     * @param action 执行动作(对应方法)
     * @param args 方法参数
     * @param callbackContext 回调上下文
     * @return 执行动作是否有效
    */
    override fun execute(action: String?, args: JSONArray?, callbackContext: CallbackContext?): Boolean {
        when (action) {
            // 获取基本的配置信息
            "getLockAppsConfig" -> {
                cordova.threadPool.execute {
                    callbackContext!!.success(GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
                            .toJson(lockAppsPrefs.lockAppsConfig))
                    callbackContext.error("{}")
                }
                return true
            }
            // 获取应用列表
            "getAppsInfo" -> {
                cordova.threadPool.execute {
                    // Cordova多线程处理
                    val allAppsInfo = getAllAppsLockInfo()
                    mActivity.runOnUiThread {
                        callbackContext!!.success(allAppsInfo)
                        callbackContext.error("[]")
                    }
                }
                return true
            }
            // 设置上锁状态
            "setLockState" -> {
                cordova.threadPool.execute {
                    lockAppsPrefs.setLockState(args!!.getBoolean(0))
                }
                return true
            }
            // 添加加锁应用
            "addLockApp" -> {
                cordova.threadPool.execute {
                    lockAppsPrefs.addLockApp(args!!.getString(0))
                }
                return true
            }
            // 删除加锁应用
            "removeLockApp" -> {
                cordova.threadPool.execute {
                    lockAppsPrefs.removeLockApp(args!!.getString(0))
                }
                return true
            }
            // 打开应用
            "openApp" -> {
                val intent = mActivity.packageManager.getLaunchIntentForPackage(args!!.getString(0))
                mActivity.startActivity(intent)
            }
            // 设置独立设置状态
            "setIndependentSettingIState" -> {
                cordova.threadPool.execute {
                    // Cordova多线程处理
                    val isSuccess = lockAppsPrefs.setIndependentSettingIState(args!!.getString(0), args.getBoolean(1))
                    mActivity.runOnUiThread {
                        callbackContext!!.success(isSuccess.toString())
                        callbackContext.error(false.toString())
                    }
                }
                return true
            }
            // 获取Activity列表
            "getActivities" -> {
                cordova.threadPool.execute {
                    // Cordova多线程处理
                    val activityList = appsUtil.getActivitiesByPkg(args!!.getString(0))
                    mActivity.runOnUiThread {
                        callbackContext!!.success(gson.toJson(activityList))
                        callbackContext.error("[]")
                    }
                }
                return true
            }
            // 添加Activity
            "addFilterActivity" -> {
                cordova.threadPool.execute {
                    lockAppsPrefs.addFilterActivity(args!!.getString(0), args.getString(1))
                }
                return true
            }
            // 删除Activity
            "removeFilterActivity" -> {
                cordova.threadPool.execute {
                    lockAppsPrefs.removeFilterActivity(args!!.getString(0), args.getString(1))
                }
                return true
            }
            // 删除密码
            "removeAppPwd" -> {
                cordova.threadPool.execute {
                    lockAppsPrefs.removeAppPwd(args!!.getString(0), args.getString(1))
                }
                return true
            }
        }
        return false
    }

    /**
     * @Title: getAllLockAppsInfo方法
     * @Class: LockAppsPlugin
     * @Description: 获取所有应用加锁情况
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/2/27 16:22
     * @update_author
     * @update_time
     * @version V1.0
     * @return [String] 所有应用加锁情况的Json数据
     * @throws
    */
    fun getAllAppsLockInfo(): String {
        // 获取所有应用信息
        val appInfoList = appsUtil.getAllAppsInfo()
        // 缓存
        val allAppsLockInfo = HashMap<String, AppInfoJsonBean>()
        // 转换所有应用信息
        for (appInfo in appInfoList) {
            val appInfoJsonBean = AppInfoJsonBean()
            appInfoJsonBean.appName = appInfo.appName
            appInfoJsonBean.packageName = appInfo.packageName
            appInfoJsonBean.versionName = appInfo.versionName
            appInfoJsonBean.versionCode = appInfo.versionCode
            appInfoJsonBean.isSystemAPP = appInfo.isSystemAPP
            appInfoJsonBean.appIcon = ImageBase64Util.drawableToBase64(appInfo.appIcon)
            allAppsLockInfo[appInfoJsonBean.packageName] = appInfoJsonBean
        }
        // 读取加锁应用
        for ((pkg, lockApp) in lockAppsPrefs.lockAppsConfig.lockApps) {
            if (allAppsLockInfo.containsKey(pkg)) {
                val appInfoJsonBean = allAppsLockInfo[pkg]!!
                appInfoJsonBean.isLock = true
                appInfoJsonBean.isIndependent = lockApp.isIndependent
                appInfoJsonBean.themes = lockApp.themes
                appInfoJsonBean.filterActivity = lockApp.filterActivity
//                appInfoJsonBean.theme = lockApp.theme
//                appInfoJsonBean.password = lockApp.password
                allAppsLockInfo[lockApp.packageName] = appInfoJsonBean
            }
        }
        return gson.toJson(allAppsLockInfo.values)
    }
}