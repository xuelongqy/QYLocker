package com.qingyi.applocker.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

/**
 * @Title: LockerServiceBroadcastReceiver
 * @Package: com.qingyi.applocker.receiver
 * @Description: 应用锁服务广播接收器
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/5/7 23:12
 * @update_author
 * @update_time
 * @exception
 * @version V1.0
*/
class LockerServiceBroadcastReceiver(val mContext: Context, val listener: LockerServiceBroadcastListener): BroadcastReceiver() {
    // 伴生对象
    companion object {
        val STOP_SERVICE = "com.qingyi.applocker.action.STOP_SERVICE"
    }

    // 初始化方法
    init {
        val filter = IntentFilter()
        filter.addAction(STOP_SERVICE)
        mContext.registerReceiver(this, filter)
    }

    /**
     * @Title: 重写onReceive方法
     * @Class: LockerServiceBroadcastReceiver
     * @Description: 截获应用锁服务广播
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/5/7 20:56
     * @update_author
     * @update_time
     * @param context[Context] 上下文
     * @param intent[Intent] 传递意图信息
     * @return
     * @throws
     * @version V1.0
     */
    override fun onReceive(context: Context, intent: Intent) {
        when(intent.action) {
            // 关闭服务广播
            STOP_SERVICE -> {
                listener.onStopService()
            }
        }
    }

    /**
     * @Title: LockerServiceBroadcastListener接口
     * @Package: com.qingyi.applocker.receiver
     * @Description: 应用锁服务广播监听器
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/5/7 23:14
     * @update_author
     * @update_time
     * @exception
     * @version V1.0
    */
    interface LockerServiceBroadcastListener {
        fun onStopService()
    }
}