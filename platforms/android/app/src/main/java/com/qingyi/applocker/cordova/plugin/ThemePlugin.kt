package com.qingyi.applocker.cordova.plugin

import android.app.Activity
import com.google.gson.GsonBuilder
import com.qingyi.applocker.util.ThemeUtil
import org.json.JSONArray
import android.content.Intent
import android.util.Log
import com.qingyi.applocker.activity.AppLockActivity
import com.qingyi.applocker.activity.SetPwdActivity
import com.qingyi.applocker.preferences.SettingsPrefs
import com.qingyi.applocker.util.ImageBase64Util
import com.qingyi.applocker.util.LoggerUtil
import org.apache.cordova.*


class ThemePlugin: CordovaPlugin() {
    // 缓存调用插件的Activity
    private var mActivity: Activity? = null
    // 主题工具
    private lateinit var themeUtil: ThemeUtil
    // 设置配置
    private lateinit var settingsPrefs:SettingsPrefs
    // Json操作对象
    private val gson = GsonBuilder().setDateFormat("yyyy-MM-dd").create()
    // 设置密码请求码
    private val SET_PWD_REQUEST = 0X1313
    // 设置密码回调函数
    private var setPwdCallbackContext:CallbackContext? = null

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
        // 初始化变量
        themeUtil = ThemeUtil(mActivity!!)
        settingsPrefs = SettingsPrefs(mActivity!!)
    }

    /**
     * 重写执行方法
     * JS调用Android方法的管理
     * @Title: 的方法
     * @Description: 重写执行方法
     * @author qingyi xuelongqy@foxmail.com
     * @date 2017/8/21 0:23
     * @param action 执行动作(对应方法)
     * @param args 方法参数
     * @param callbackContext 回调上下文
     * @return 执行动作是否有效
     */
    override fun execute(action: String?, args: JSONArray?, callbackContext: CallbackContext?): Boolean {
        when (action) {
            // 导入主题
            "importTheme" -> {
                cordova.threadPool.execute {
                    callbackContext!!.success(themeUtil.importTheme(args!!.getString(0)).toString())
                    callbackContext.error(false.toString())
                }
                return true
            }
            // 获取主题列表
            "getThemeList" -> {
                cordova.threadPool.execute {
                    callbackContext!!.success(gson.toJson(themeUtil.getThemeList()))
                    callbackContext.error("[]")
                }
                return true
            }
            // 删除主题
            "deleteTheme" -> {
                cordova.threadPool.execute {
                    themeUtil.deleteTheme(args!!.getString(0))
                    callbackContext!!.success()
                    callbackContext.error(false.toString())
                }
                return true
            }
            // 设置主题密码
            "setThemePwd" -> {
                // 跳转到设置密码页面
                this.setPwdCallbackContext = callbackContext
                val intent = Intent(mActivity, SetPwdActivity::class.java)
                intent.putExtra(SetPwdActivity.SET_PWD_TYPE, SetPwdActivity.SET_PWD)
                intent.putExtra(SetPwdActivity.THEME_NAME, args!!.getString(0))
                this.cordova.startActivityForResult(this, intent, SET_PWD_REQUEST)
                return true
            }
            // 取消设置密码
            "cancelSetPwd" -> {
                if (mActivity is SetPwdActivity) {
                    (mActivity as SetPwdActivity).cancelSetPwd()
                }
                return true
            }
            // 使用主题并设置密码
            "setPassword" -> {
                cordova.threadPool.execute {
                    if (mActivity is SetPwdActivity) {
                        (mActivity as SetPwdActivity).setThemeAndPwd(args!!.getString(0))
                    }
                }
                return true
            }
            // 解锁屏幕
            "unlock" -> {
                cordova.threadPool.execute {
                    // 设置密码时,验证上一次密码
                    if (mActivity is SetPwdActivity) {
                        val isUnlock = (mActivity as SetPwdActivity).verifyPassword(args!!.getString(0))
                        // 返回密码错误
                        if (!isUnlock) {
                            callbackContext!!.success(isUnlock.toString())
                        }
                    }
                    // 解锁应用时,验证密码
                    else if (mActivity is AppLockActivity) {
                        val isUnlock = (mActivity as AppLockActivity).verifyPassword(args!!.getString(0))
                        // 返回密码错误
                        if (!isUnlock) {
                            callbackContext!!.success(isUnlock.toString())
                        }
                    }
                }
                return true
            }
            // 获取指纹状态
            "isFingerprint" -> {
                cordova.threadPool.execute {
                    // 设置密码时验证,返回不支持指纹
                    if (mActivity is SetPwdActivity) {
                        callbackContext!!.success(false.toString())
                        callbackContext.error(false.toString())
                    }
                    // 解锁界面,获取是否使用者指纹
                    else if (mActivity is AppLockActivity) {
                        callbackContext!!.success((mActivity as AppLockActivity).isFingerprint().toString())
                        callbackContext.error(false.toString())
                    }
                }
                return true
            }
            // 设置指纹监听
            "setFingerprintListener" -> {
                cordova.threadPool.execute {
                    // 判断是否为解锁页面
                    if (mActivity is AppLockActivity) {
                        // 设置监听回调
                        val mPlugin = PluginResult(PluginResult.Status.NO_RESULT)
                        mPlugin.keepCallback = true
                        callbackContext!!.sendPluginResult(mPlugin)
                        (mActivity as AppLockActivity).fingerprintCallbackContext = callbackContext
                    }
                }
                return true
            }
            // 获取背景图片
            "getBgImg" -> {
                cordova.threadPool.execute {
                    callbackContext!!.success(ImageBase64Util.imageToBase64(settingsPrefs.settingsConfig.bgImageUrl))
                    callbackContext.error("")
                }
                return true
            }
            // 设置主题数据(主题所需自定义的额外数据)
            "setThemeData" -> {
                cordova.threadPool.execute {
                    // 仅仅在设置主题页面使用
                    if (mActivity is SetPwdActivity) {
                        (mActivity as SetPwdActivity).setThemeData(args!!.getString(0))
                    }
                }
                return true
            }
            // 获取主题数据(主题所需自定义的额外数据)
            "getThemeData" -> {
                cordova.threadPool.execute {
                    // 设置主题界面
                    if (mActivity is SetPwdActivity) {
                        callbackContext!!.success((mActivity as SetPwdActivity).getThemeData())
                        callbackContext.error("")
                    }
                    // 解锁界面
                    else if(mActivity is AppLockActivity){
                        callbackContext!!.success((mActivity as AppLockActivity).getThemeData())
                        callbackContext.error("")
                    }
                }
                return true
            }
        }
        return false
    }

    /**
     * @Title: 重写onActivityResult方法
     * @Class: ThemePlugin
     * @Description: 获取页面返回的数据
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/4/27 13:37
     * @update_author
     * @update_time
     * @version V1.0
     * @param
     * @return
     * @throws
    */
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        when (resultCode) {
            Activity.RESULT_OK -> {
                // 设置密码
                if (SET_PWD_REQUEST === requestCode) {
                    if (this.setPwdCallbackContext != null) {
                        this.setPwdCallbackContext!!.success(intent!!.getBooleanExtra(SetPwdActivity.SET_PWD_STATE, false).toString())
                    }
                }
            }
        }
    }
}