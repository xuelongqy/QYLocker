package com.qingyi.applocker.cordova.plugin

import android.app.Activity
import com.google.gson.GsonBuilder
import com.qingyi.applocker.util.ThemeUtil
import org.apache.cordova.CallbackContext
import org.apache.cordova.CordovaInterface
import org.apache.cordova.CordovaPlugin
import org.apache.cordova.CordovaWebView
import org.json.JSONArray
import android.content.Intent
import com.qingyi.applocker.activity.SetPwdActivity


class ThemePlugin: CordovaPlugin() {
    // 缓存调用插件的Activity
    private var mActivity: Activity? = null
    // 主题工具
    private lateinit var themeUtil: ThemeUtil
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