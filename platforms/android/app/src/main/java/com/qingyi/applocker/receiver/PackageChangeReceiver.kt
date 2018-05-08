package com.qingyi.applocker.receiver

import android.content.*
import android.util.Log
import com.google.gson.GsonBuilder
import com.qingyi.applocker.activity.AppAddedActivity
import com.qingyi.applocker.activity.AppLockActivity
import com.qingyi.applocker.preferences.LockAppsPrefs
import com.qingyi.applocker.preferences.SettingsPrefs
import com.qingyi.applocker.util.LoggerUtil
import com.xposed.qingyi.cmprotectedappsplus.constant.ThisApp


/**
 * @Title: PackageChangeReceiver类
 * @Package: com.qingyi.applocker.receiver
 * @Description: 软件应用包广播接收器(安装、更新、卸载)
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/5/8 1:09
 * @update_author
 * @update_time
 * @exception
 * @version V1.0
*/
class PackageChangeReceiver(val mContext: Context): BroadcastReceiver() {
    // 设置配置
    private val settingsPrefs = SettingsPrefs(mContext)
    private val lockAppsPrefs = LockAppsPrefs(mContext)

    // 初始化方法
    init {
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_PACKAGE_ADDED)
        filter.addAction(Intent.ACTION_PACKAGE_REPLACED)
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED)
        filter.addDataScheme("package")
        mContext.registerReceiver(this, filter)
    }

    override fun onReceive(context: Context, intent: Intent) {
        when(intent.action) {
            // 安装新应用
            Intent.ACTION_PACKAGE_ADDED -> {
                // 获取应用包名
                val packageName = intent.data.schemeSpecificPart
                // 判断是否开启锁定新应用
                if (!settingsPrefs.settingsConfig.lockNewApp || lockAppsPrefs.lockAppsConfig.lockApps.keys.contains(packageName)) return
                // 打开新应用弹窗
                val appAddedIntent = Intent(context, AppAddedActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                appAddedIntent.putExtra(AppAddedActivity.PACKAGE_NAME, packageName)
                context.startActivity(appAddedIntent)
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