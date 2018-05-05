package com.qingyi.applocker.cordova.plugin

import android.app.Activity
import org.apache.cordova.CordovaInterface
import org.apache.cordova.CordovaPlugin
import org.apache.cordova.CordovaWebView
import org.apache.cordova.CallbackContext
import org.apache.cordova.PluginResult
import android.content.Intent
import org.json.JSONArray
import android.util.Log
import com.qingyi.applocker.util.FilePathUtil
import com.qingyi.applocker.util.LoggerUtil


class FilePickerPlugin: CordovaPlugin() {
    // 缓存调用插件的Activity
    private var mActivity: Activity? = null
    // 缓存文件选择回调
    private var chooseFileCallbackContext: CallbackContext? = null
    // 文件选择请求码
    private val CHOOSE_FILE_REQUEST = 0X111

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
        //获取调用的Activity
        mActivity = cordova!!.activity
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
            // 选择文件
            "chooseFile" -> {
                cordova.threadPool.execute {
                    this.chooseFile(args!!.getString(0), args.getString(1), callbackContext!!)
                }
                return true
            }
        }
        return false
    }

    /**
     * 文件选择
     *
     * @param title     Title of the dialog
     * @param accept   文件类型
     * @param callbackContext 回调
     */
    private fun chooseFile(title: String, accept: String, callbackContext: CallbackContext) {
        if (this.chooseFileCallbackContext != null) {
            this.chooseFileCallbackContext = null
        }

        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = accept
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        try {
            this.cordova.startActivityForResult(this, Intent.createChooser(intent, title), CHOOSE_FILE_REQUEST)
            val mPlugin = PluginResult(PluginResult.Status.NO_RESULT)
            mPlugin.keepCallback = true
            this.chooseFileCallbackContext = callbackContext
            callbackContext.sendPluginResult(mPlugin)
        } catch (ex: android.content.ActivityNotFoundException) {
            callbackContext.error("")
        }
    }

    /**
     * @Title: 重写onActivityResult方法
     * @Class: FilePickerFlugin
     * @Description: 用于获取返回的文件选择地址
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/4/26 9:00
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
                val uri = intent!!.data
                if (CHOOSE_FILE_REQUEST == requestCode) {
                    if (this.chooseFileCallbackContext != null) {
                        LoggerUtil.logAndroid(Log.INFO, "FilePickerPlugin", "File = ${uri.path}")
                        this.chooseFileCallbackContext!!.success(FilePathUtil.getFilePathByUri(mActivity!!, uri))
                        //this.chooseFileCallbackContext!!.success(uri.path)
                    }
                }
            }
        }
    }
}