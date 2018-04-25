package com.qingyi.applocker.cordova.plugin

import android.app.Activity
import com.google.gson.GsonBuilder
import com.qingyi.applocker.preferences.SettingsPrefs
import org.apache.cordova.CallbackContext
import org.apache.cordova.CordovaInterface
import org.apache.cordova.CordovaPlugin
import org.apache.cordova.CordovaWebView
import org.json.JSONArray

/**
 * @Title: SettingsPlugin类
 * @Package: com.qingyi.applocker.cordova.plugin
 * @Description: 设置配置插件
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/4/25 10:23
 * @update_author
 * @update_time
 * @version V1.0
 * @exception
*/
class SettingsPlugin : CordovaPlugin() {
    // 伴生对象
    companion object {
        // 类的标识
        val TAG = SettingsPlugin::class.java.simpleName
    }

    // 缓存调用插件的Activity
    lateinit var mActivity: Activity
    // 加锁应用配置
    lateinit var settingsPrefs: SettingsPrefs
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
        settingsPrefs = SettingsPrefs(mActivity.applicationContext)
    }

    /**
     * @Title: 重写execute方法
     * @Class: SettingsPlugin
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
            // 获取设置的配置信息
            "getSettingsConfig" -> {
                cordova.threadPool.execute {
                    callbackContext!!.success(gson.toJson(settingsPrefs.settingsConfig))
                    callbackContext.error("{}")
                }
                return true
            }
            // 设置应用锁模式
            "setLockModel" -> {
                cordova.threadPool.execute {
                    settingsPrefs.setLockModel(args!!.getString(0))
                }
                return true
            }
            // 设置重新锁定模式
            "setResetLockModel" -> {
                cordova.threadPool.execute {
                    settingsPrefs.setResetLockModel(args!!.getString(0))
                }
                return true
            }
            // 设置是否使用指纹
            "setUseFingerprint" -> {
                cordova.threadPool.execute {
                    settingsPrefs.setUseFingerprint(args!!.getBoolean(0))
                }
                return true
            }
            // 设置是否启用高级模式
            "setAdvancedMode" -> {
                cordova.threadPool.execute {
                    settingsPrefs.setAdvancedMode(args!!.getBoolean(0))
                }
                return true
            }
            // 设置是否锁定新应用
            "setLockNewApp" -> {
                cordova.threadPool.execute {
                    settingsPrefs.setLockNewApp(args!!.getBoolean(0))
                }
                return true
            }
            // 设置防止卸载
            "setPreventUninstall" -> {
                cordova.threadPool.execute {
                    settingsPrefs.setPreventUninstall(args!!.getBoolean(0))
                }
                return true
            }
            // 设置防止卸载
            "setBgImageUrl" -> {
                cordova.threadPool.execute {
                    settingsPrefs.setBgImageUrl(args!!.getString(0))
                }
                return true
            }
        }
        return false
    }
}