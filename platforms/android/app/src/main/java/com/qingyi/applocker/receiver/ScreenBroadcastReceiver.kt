package com.qingyi.applocker.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.R.attr.action
import com.qingyi.applocker.preferences.HistoryPrefs
import android.os.PowerManager
import android.content.IntentFilter
import com.qingyi.applocker.receiver.ScreenBroadcastReceiver.ScreenStateListener








/**
 * @Title: ScreenBroadcastReceiver类
 * @Package: com.qingyi.applocker.receiver
 * @Description: 屏幕广播接收器
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/5/2 15:00
 * @update_author
 * @update_time
 * @version V1.0
 * @exception
*/
class ScreenBroadcastReceiver(private val mContext: Context, private val mScreenStateListener: ScreenStateListener = ScreenStateListenerImpl(mContext)): BroadcastReceiver() {

    /**
     * @Title: 重写onReceive方法
     * @Class: ScreenBroadcastReceiver
     * @Description: 广播接收函数
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/5/2 15:05
     * @update_author
     * @update_time
     * @version V1.0
     * @param context[Context] 上下文
     * @param intent[Intent] 广播信息
     * @return
     * @throws
    */
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action!!
        when(action) {
            Intent.ACTION_SCREEN_ON -> {    // 开屏
                mScreenStateListener.onScreenOn()
            }
            Intent.ACTION_SCREEN_OFF -> {   // 锁屏
                mScreenStateListener.onScreenOff()
            }
            Intent.ACTION_USER_PRESENT -> { // 解锁
                mScreenStateListener.onUserPresent()
            }
        }
    }

    /**
     * @Title: getScreenState方法
     * @Class: ScreenBroadcastReceiver
     * @Description: 获取当前屏幕状态
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/5/2 15:31
     * @update_author
     * @update_time
     * @version V1.0
     * @param
     * @return
     * @throws
    */
    private fun getScreenState() {
        val manager = mContext
                .getSystemService(Context.POWER_SERVICE) as PowerManager
        if (manager.isInteractive) {
            mScreenStateListener.onScreenOn()
        } else {
            mScreenStateListener.onScreenOff()
        }
    }

    /**
     * @Title: start方法
     * @Class: ScreenBroadcastReceiver
     * @Description: 启动广播接收器
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/5/2 15:34
     * @update_author
     * @update_time
     * @version V1.0
     * @param
     * @return
     * @throws
    */
    fun start() {
        registerListener()
        getScreenState()
    }

    /**
     * @Title: stop方法
     * @Class: ScreenBroadcastReceiver
     * @Description: 关闭广播接收器
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/5/2 15:34
     * @update_author
     * @update_time
     * @version V1.0
     * @param
     * @return
     * @throws
    */
    fun stop() {
        unregisterListener()
    }

    /**
     * @Title: registerListener方法
     * @Class: ScreenBroadcastReceiver
     * @Description: 注册接收器
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/5/2 15:32
     * @update_author
     * @update_time
     * @version V1.0
     * @param
     * @return
     * @throws
    */
    private fun registerListener() {
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_SCREEN_ON)
        filter.addAction(Intent.ACTION_SCREEN_OFF)
        filter.addAction(Intent.ACTION_USER_PRESENT)
        mContext.registerReceiver(this, filter)
    }

    /**
     * @Title: unregisterListener方法
     * @Class: ScreenBroadcastReceiver
     * @Description: 注销接收器
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/5/2 15:32
     * @update_author
     * @update_time
     * @version V1.0
     * @param
     * @return
     * @throws
    */
    private fun unregisterListener() {
        mContext.unregisterReceiver(this)
    }

    /**
     * @Title: ScreenStateListener接口
     * @Package: com.qingyi.applocker.receiver
     * @Description: 屏幕状态监听器接口
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/5/2 15:10
     * @update_author
     * @update_time
     * @version V1.0
     * @exception
    */
    interface ScreenStateListener {
        // 当屏幕打开时
        fun onScreenOn()
        // 当屏幕关闭时
        fun onScreenOff()
        // 当屏幕解锁时
        fun onUserPresent()
    }

    /**
     * @Title: ScreenStateListenerImpl
     * @Package: com.qingyi.applocker.receiver
     * @Description: 屏幕状态监听器实现(默认)
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/5/2 15:26
     * @update_author
     * @update_time
     * @version V1.0
     * @exception
    */
    class ScreenStateListenerImpl(val mContext: Context):ScreenStateListener {
        // 历史配置
        private var historyPrefs = HistoryPrefs(mContext)

        // 当屏幕打开时
        override fun onScreenOn() {
        }
        // 当屏幕关闭时
        override fun onScreenOff() {
            // 清除历史解锁应用
            historyPrefs.cleanHistory()
        }
        // 当屏幕解锁时
        override fun onUserPresent() {
        }
    }
}