package com.qingyi.applocker.util

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import android.util.Log
import com.qingyi.applocker.service.AccessibilityLockerService
import com.qingyi.applocker.service.UsageStatsLockerService

/**
 * 应用锁服务助手
 * 管理不同的服务方式,验证权限
 * @ClassName: LockerServiceUtil
 * @Description: 应用锁服务助手
 * @author qingyi xuelongqy@foxmail.com
 * @date 2017/8/15 0:21
 */
class LockerServiceUtil(context: Context) {

    //记录上下文
    private var mContext = context

    companion object {
        /**
         * 判断是否拥有UsageStatsManager(查看应用使用情况)权限
         * @Title: havaUsageStatsManagerPermission
         * @Description: 判断是否拥有UsageStatsManager(查看应用使用情况)权限
         * @author qingyi xuelongqy@foxmail.com
         * @date 2017/8/15 0:28
         * @param context 上下文
         * @return 是否拥有权限
         */
        fun havaUsageStatsManagerPermission(context: Context): Boolean {
            try {
                val packageManager = context.packageManager
                val applicationInfo = packageManager.getApplicationInfo(context.packageName, 0)
                val appOpsManager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
                val mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName)
                return mode == AppOpsManager.MODE_ALLOWED
            } catch (e: PackageManager.NameNotFoundException) {
                return true
            }
        }

        /**
         * 跳转到设置UsageStatsManager(查看应用使用情况)权限页面
         * @Title: setUsageStatsManagerPermission
         * @Description: 设置UsageStatsManager(查看应用使用情况)权限
         * @author qingyi xuelongqy@foxmail.com
         * @date 2017/8/15 0:40
         * @param context 上下文
         */
        fun setUsageStatsManagerPermission(context: Context) {
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        /**
         * 判断是否拥有辅助功能权限
         * @Title: haveAccessibilityPermission
         * @Description: 判断是否拥有辅助功能权限
         * @author qingyi xuelongqy@foxmail.com
         * @date 2017/8/15 17:21
         * @param context 上下文
         * @return 是否拥有权限
         */
        fun haveAccessibilityPermission(context: Context): Boolean {
            var accessibilityEnabled:Int
            try {
                accessibilityEnabled = Settings.Secure.getInt(context.contentResolver,
                        android.provider.Settings.Secure.ACCESSIBILITY_ENABLED)
            } catch (e: Settings.SettingNotFoundException) {
                LoggerUtil.logAndroid(Log.WARN and LoggerUtil.IMPORTANT_LOG,"haveAccessibilityPermission",e.localizedMessage)
                e.printStackTrace()
                return false
            }

            if (accessibilityEnabled == 1) {
                val services = Settings.Secure.getString(context.contentResolver,
                        Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
                if (services != null) {
                    return services.toLowerCase().contains(context.packageName.toLowerCase())
                }
            }
            return false
        }

        /**
         * 跳转到设置辅助功能页面
         * @Title: setAccessibilityPermission
         * @Description: 设置辅助功能权限
         * @author qingyi xuelongqy@foxmail.com
         * @date 2017/8/15 17:24
         * @param context 上下文
         */
        fun setAccessibilityPermission(context: Context) {
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    /**
     * 启动UsageStatsManager方式监听应用启动的服务
     * @Title: startUsageStatsLockerService
     * @Description: 启动UsageStatsManager
     * @author qingyi xuelongqy@foxmail.com
     * @date 2017/8/15 0:22
     */
    fun startUsageStatsLockerService() {
        //判断是否拥有权限
        if(!havaUsageStatsManagerPermission(mContext)){
            setUsageStatsManagerPermission(mContext);
            return
        }
        //启动服务
        var intent = Intent(mContext, UsageStatsLockerService::class.java)
        mContext.startService(intent)
    }

    /**
     * 启动AccessibilityService方式监听应用启动的服务
     * @Title: startAccessibilityLockerService
     * @Description: 启动AccessibilityService方式监听应用启动的服务
     * @author qingyi xuelongqy@foxmail.com
     * @date 2017/8/15 20:10
     */
    fun startAccessibilityLockerService() {
        //判断是否拥有权限
        if(!haveAccessibilityPermission(mContext)){
            setAccessibilityPermission(mContext)
            return
        }
        //启动服务
        var intent = Intent(mContext, AccessibilityLockerService::class.java)
        mContext.startService(intent)
    }

}