package com.qingyi.applocker.cordova.plugin

import android.app.Activity
import com.qingyi.applocker.util.ThemeUtil
import org.apache.cordova.CallbackContext
import org.apache.cordova.CordovaInterface
import org.apache.cordova.CordovaPlugin
import org.apache.cordova.CordovaWebView
import org.json.JSONArray

class ThemePlugin: CordovaPlugin() {
    // 缓存调用插件的Activity
    private var mActivity: Activity? = null
    // 主题工具
    private lateinit var themeUtil: ThemeUtil

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
            "getThemeList" -> {

            }
        }
        return false
    }
}