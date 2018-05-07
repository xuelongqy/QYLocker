package com.qingyi.applocker.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.qingyi.applocker.util.AppsUtil


/**
 * @Title: AppPackageBroadcastReceiver类
 * @Package: com.qingyi.applocker.receiver
 * @Description: 软件应用包广播接收器(安装、更新、卸载)
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/5/8 1:09
 * @update_author
 * @update_time
 * @exception
 * @version V1.0
*/
class AppPackageBroadcastReceiver(val mContext: Context): BroadcastReceiver() {
    init {
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_PACKAGE_ADDED)
        filter.addAction(Intent.ACTION_PACKAGE_REPLACED)
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED)
        mContext.registerReceiver(this, filter)
    }

    override fun onReceive(context: Context, intent: Intent) {
        when(intent.action) {
            // 安装新应用
            Intent.ACTION_PACKAGE_ADDED -> {
                val packageName = intent.data.schemeSpecificPart
                // 获取并设置解锁应用信息
                val lockAppInfo = AppsUtil(context).getAppInfoByPkg(packageName)
            }
            // 更新应用
            Intent.ACTION_PACKAGE_REPLACED -> {
                return
            }
            // 卸载应用
            Intent.ACTION_PACKAGE_REMOVED -> {
                return
            }
        }
    }
}