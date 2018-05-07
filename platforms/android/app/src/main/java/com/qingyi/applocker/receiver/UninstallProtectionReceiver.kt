package com.qingyi.applocker.receiver

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.qingyi.applocker.util.LoggerUtil

/**
 * @Title: UninstallProtectionReceiver类
 * @Package: com.qingyi.applocker.receiver
 * @Description: 保护此软件防止卸载
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/5/8 0:22
 * @update_author
 * @update_time
 * @exception
 * @version V1.0
*/
class UninstallProtectionReceiver: DeviceAdminReceiver() {
    override fun onEnabled(context: Context?, intent: Intent?) {
        super.onEnabled(context, intent)
        LoggerUtil.logAndroid(Log.INFO, "${UninstallProtectionReceiver::class.java.name}.UninstallProtectionReceiver", "Device admin is now active!")
    }
}