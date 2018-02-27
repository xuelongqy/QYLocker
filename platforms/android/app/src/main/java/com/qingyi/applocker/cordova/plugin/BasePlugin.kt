package com.qingyi.applocker.cordova.plugin

import android.app.Activity
import org.apache.cordova.CordovaInterface
import org.apache.cordova.CordovaPlugin
import org.apache.cordova.CordovaWebView

/**
 * @Title: BasePlugin
 * @Package: com.qingyi.applocker.cordova.plugin
 * @Description: 基础的插件类
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/2/26 11:15
 * @update_author
 * @update_time
 * @version V1.0
 * @exception
*/
open class BasePlugin: CordovaPlugin() {
    //缓存调用插件的Activity
    lateinit var mActivity: Activity

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
}