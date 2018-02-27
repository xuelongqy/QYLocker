package com.qingyi.applocker.cordova.plugin

import android.app.Activity
import android.widget.Toast
import org.apache.cordova.CallbackContext
import org.apache.cordova.CordovaInterface
import org.apache.cordova.CordovaPlugin
import org.apache.cordova.CordovaWebView
import org.json.JSONArray

/**
 * Toast插件
 * js调用Android的Toast弹出提示窗口
 * @ClassName: ToastPlugin
 * @Description: Toast插件
 * @author qingyi xuelongqy@foxmail.com
 * @date 2017/8/22 15:33
 */
class ToastPlugin: CordovaPlugin() {
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
        when (action){
            "showLongToast" -> {
                showLongToast(args!!.getString(0))
                return true
            }
            "showShortToast" -> {
                showShortToast(args!!.getString(0))
                return true
            }
        }
        return false
    }

    /**
     * 弹出一个长时间的Toast
     * @Title: showLongToast
     * @Description: 弹出一个长时间的Toast
     * @author qingyi xuelongqy@foxmail.com
     * @date 2017/8/22 15:41
     * @param msg 需要显示的信息
     */
    fun showLongToast(msg:String){
        Toast.makeText(mActivity,msg,Toast.LENGTH_LONG).show()
    }

    /**
     * 弹出一个短时间的Toast
     * @Title: showLongToast
     * @Description: 弹出一个短时间的Toast
     * @author qingyi xuelongqy@foxmail.com
     * @date 2017/8/22 15:41
     * @param msg 需要显示的信息
     */
    fun showShortToast(msg:String){
        Toast.makeText(mActivity,msg,Toast.LENGTH_SHORT).show()
    }
}