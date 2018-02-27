package com.qingyi.applocker.cordova.plugin

import org.apache.cordova.CordovaPlugin
import android.app.Activity
import android.util.Log
import com.qingyi.applocker.activity.BaseHybridActivity
import com.qingyi.applocker.util.DensityUtil
import com.qingyi.applocker.util.LoggerUtil
import org.apache.cordova.CallbackContext
import org.apache.cordova.CordovaInterface
import org.apache.cordova.CordovaWebView
import org.json.JSONArray


/**
 * Cordova通知栏和状态栏插件
 * @ClassName: StatusBarAndNavigationBarPlugin
 * @Description: Cordova通知栏和状态栏插件
 * @author qingyi xuelongqy@foxmail.com
 * @date 2017/8/21 0:19
 */
class StatusBarAndNavigationBarPlugin: CordovaPlugin() {
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
        //判断执行动作
        when (action) {
            //获取状态栏高度
            "getStatusBarHeight" -> {
                var height = getStatusBarHeight()
                callbackContext!!.success(height)
                callbackContext!!.error(0)
                return true
            }
            //获取导航栏高度
            "getNavigationBarHeight" -> {
                var height = getNavigationBarHeight()
                callbackContext!!.success(height)
                callbackContext!!.error(0)
                return true
            }
            //获取顶部高度
            "getTopHeight" -> {
                var height = getTopHeight()
                callbackContext!!.success(height)
                callbackContext!!.error(0)
                return true
            }
            //获取底部高度
            "getBottomHeight" -> {
                var height = getBottomHeight()
                callbackContext!!.success(height)
                callbackContext!!.error(0)
                return true
            }
        }
        return false
    }

    /**
     * 获取顶部高度(透明状态栏下)
     * @Title: getTopHeight
     * @Description: 获取顶部高度(透明状态栏下)
     * @author qingyi xuelongqy@foxmail.com
     * @date 2017/8/21 0:56
     * @return 顶部高度
     */
    fun getTopHeight(): Int {
        if (mActivity is BaseHybridActivity){
            return if((mActivity as BaseHybridActivity).getTransparent_StatusBar()) getStatusBarHeight() else 0
        }else{
            return 0
        }
    }

    /**
     * 获取底部高度(透明导航栏下)
     * @Title: getBottomHeight
     * @Description: 获取底部高度(透明导航栏下)
     * @author qingyi xuelongqy@foxmail.com
     * @date 2017/8/21 0:57
     * @return 底部高度
     */
    fun getBottomHeight(): Int {
        if (mActivity is BaseHybridActivity){
            return if((mActivity as BaseHybridActivity).getTransparent_NavigationBar()) getNavigationBarHeight() else 0
        }else{
            return 0
        }
    }

    /**
     * 获取状态栏高度
     * @Title: getStatusBarHeight
     * @Description: 获取状态栏高度
     * @author qingyi xuelongqy@foxmail.com
     * @date 2017/8/21 0:36
     * @return 状态栏高度
     */
    fun getStatusBarHeight():Int {
        var height = 0
        try {
            height = mActivity!!.getResources().getDimensionPixelSize(mActivity!!.getResources().getIdentifier("status_bar_height", "dimen", "android"));
            height = DensityUtil.dip2px(mActivity!!, height.toFloat())
        }catch (e: Exception){
            e.printStackTrace()
            LoggerUtil.logAndroid(LoggerUtil.IMPORTANT_LOG and Log.ERROR,"StatusBarAndNavigationBarPlugin.getStatusBarHeight",e.localizedMessage)
        }finally {
            return height
        }
    }

    /**
     * 获取状态栏高度
     * @Title: getNavigationBarHeight
     * @Description: 获取状态栏高度
     * @author qingyi xuelongqy@foxmail.com
     * @date 2017/8/21 0:36
     * @return 状态栏高度
     */
    fun getNavigationBarHeight():Int {
        var height = 0
        try {
            height = mActivity!!.getResources().getDimensionPixelSize(mActivity!!.getResources().getIdentifier("navigation_bar_height", "dimen", "android"));
            height = DensityUtil.dip2px(mActivity!!, height.toFloat())
        }catch (e: Exception){
            e.printStackTrace()
            LoggerUtil.logAndroid(LoggerUtil.IMPORTANT_LOG and Log.ERROR,"StatusBarAndNavigationBarPlugin.getNavigationBarHeight",e.localizedMessage)
        }finally {
            return height
        }
    }
}