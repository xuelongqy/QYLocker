package com.qingyi.applocker.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.qingyi.applocker.util.LockerServiceUtil

/**
 * @Title: BootCompleteReceiver类
 * @Package: com.qingyi.applocker.receiver
 * @Description: 开机启动广播接收器
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/5/7 20:54
 * @update_author
 * @update_time
 * @exception
 * @version V1.0
*/
class BootCompleteReceiver: BroadcastReceiver() {

    /**
     * @Title: 重写onReceive方法
     * @Class: BootCompleteReceiver
     * @Description: 截获开机广播
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
            // 开机广播
            "android.intent.action.BOOT_COMPLETED" -> {
                val lockerServiceUtil = LockerServiceUtil(context.applicationContext)
                // 判断服务是否启动
                if (!lockerServiceUtil.isLockerServiceStart()) {
                    // 启动服务
                    lockerServiceUtil.startLockerService()
                }
            }
        }
    }
}