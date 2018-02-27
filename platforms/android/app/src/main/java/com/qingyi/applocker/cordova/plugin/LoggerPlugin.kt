package com.qingyi.applocker.cordova.plugin

import android.app.Activity
import android.util.Log
import com.qingyi.applocker.util.LoggerUtil
import org.apache.cordova.CallbackContext
import org.apache.cordova.CordovaInterface
import org.apache.cordova.CordovaPlugin
import org.apache.cordova.CordovaWebView
import org.json.JSONArray

/**
 * Created by Qingyi on 2017/8/22.
 */
class LoggerPlugin:CordovaPlugin() {
    //缓存调用插件的Activity
    private var mActivity: Activity? = null

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
            "i" -> {
                i(
                        args!!.getBoolean(0),
                        args!!.getString(1),
                        args!!.getString(2)
                )
                return true
            }
            "d" -> {
                d(
                        args!!.getBoolean(0),
                        args!!.getString(1),
                        args!!.getString(2)
                )
                return true
            }
            "w" -> {
                w(
                        args!!.getBoolean(0),
                        args!!.getString(1),
                        args!!.getString(2)
                )
                return true
            }
            "e" -> {
                e(
                        args!!.getBoolean(0),
                        args!!.getString(1),
                        args!!.getString(2)
                )
                return true
            }
        }
        return false
    }

    /**
     * 打印消息日志
     * @param important 是否重要
     * @param context 上下文,作为标识,方便查找日志输出的位置
     * @param msg 日志主要内容
     */
    fun i(important:Boolean, context: String, msg:String){
        LoggerUtil.logAndroid(
                if (important) LoggerUtil.IMPORTANT_LOG and Log.INFO else Log.INFO
                ,context, msg)
    }

    /**
     * 打印调试日志
     * @param important 是否重要
     * @param context 上下文,作为标识,方便查找日志输出的位置
     * @param msg 日志主要内容
     */
    fun d(important:Boolean, context: String, msg:String){
        LoggerUtil.logAndroid(
                if (important) LoggerUtil.IMPORTANT_LOG and Log.DEBUG else Log.DEBUG
                ,context, msg)
    }

    /**
     * 打印警告日志
     * @param important 是否重要
     * @param context 上下文,作为标识,方便查找日志输出的位置
     * @param msg 日志主要内容
     */
    fun w(important:Boolean, context: String, msg:String){
        LoggerUtil.logAndroid(
                if (important) LoggerUtil.IMPORTANT_LOG and Log.WARN else Log.WARN
                ,context, msg)
    }

    /**
     * 打印错误日志
     * @param important 是否重要
     * @param context 上下文,作为标识,方便查找日志输出的位置
     * @param msg 日志主要内容
     */
    fun e(important:Boolean, context: String, msg:String){
        LoggerUtil.logAndroid(
                if (important) LoggerUtil.IMPORTANT_LOG and Log.ERROR else Log.ERROR
                ,context, msg)
    }
}