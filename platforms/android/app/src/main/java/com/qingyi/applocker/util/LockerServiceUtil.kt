package com.qingyi.applocker.util

import android.app.ActivityManager
import android.app.AppOpsManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import android.util.Log
import com.qingyi.applocker.preferences.SettingsPrefs
import com.qingyi.applocker.receiver.LockerServiceBroadcastReceiver
import com.qingyi.applocker.service.AccessibilityLockerService
import com.qingyi.applocker.service.UsageStatsLockerService
import com.qingyi.applocker.xposed.XposedUtil
import com.xposed.qingyi.cmprotectedappsplus.constant.ThisApp

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
            val accessibilityEnabled:Int
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
            // 启动服务
            val intent = Intent(mContext, UsageStatsLockerService::class.java)
            mContext.startService(intent)
        }
    }
    fun stopUsageStatsLockerService() {
        // 停止服务
        val intent = Intent(mContext, UsageStatsLockerService::class.java)
        mContext.stopService(intent)
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
            //启动服务
            val intent = Intent(mContext, AccessibilityLockerService::class.java)
            mContext.startService(intent)
        }
    }
    fun stopAccessibilityLockerService() {
        // 停止服务
        val intent = Intent(mContext, AccessibilityLockerService::class.java)
        mContext.stopService(intent)
    }

    /**
     * @Title: isLockerServiceStart方法
     * @Class: LockerServiceUtil
     * @Description: 获取应用锁服务是否启动
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/5/7 21:01
     * @update_author
     * @update_time
     * @param
     * @return [Boolean] 应用锁服务是否启动
     * @throws
     * @version V1.0
    */
    fun isLockerServiceStart(): Boolean {
        // 判断Xposed模块是否启动
        if (XposedUtil.isXposedActive()) return true
        // 获取应用锁模式
        val lockModel = SettingsPrefs(mContext).settingsConfig.lockModel
        val service: ComponentName
        when(lockModel) {
            ThisApp.LISTEN_APPS -> {
                service = ComponentName(mContext, AccessibilityLockerService::class.java)
            }
            ThisApp.STACK_POLLING -> {
                service = ComponentName(mContext, UsageStatsLockerService::class.java)
            }
            else -> return false
        }
        // 判断服务是否运行
        val serviceIntent = (mContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).getRunningServiceControlPanel(service)
        return serviceIntent != null
    }

    /**
     * @Title: havePermission方法
     * @Class: LockerServiceUtil
     * @Description: 判断是否拥有权限
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/5/7 21:39
     * @update_author
     * @update_time
     * @param
     * @return [Boolean] 是否拥有权限
     * @throws
     * @version V1.0
    */
    fun havePermission(): Boolean {
        // 判断Xposed模块是否启动
        if (XposedUtil.isXposedActive()) return true
        // 获取应用锁模式
        val lockModel = SettingsPrefs(mContext).settingsConfig.lockModel
        // 判断模式
        when(lockModel) {
            ThisApp.LISTEN_APPS -> {
                return haveAccessibilityPermission(mContext)
            }
            ThisApp.STACK_POLLING -> {
                return havaUsageStatsManagerPermission(mContext)
            }
            else -> return false
        }
    }

    /**
     * @Title: requestPermission方法
     * @Class: LockerServiceUtil
     * @Description: 申请权限
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/5/7 21:38
     * @update_author
     * @update_time
     * @param
     * @return
     * @throws
     * @version V1.0
    */
    fun requestPermission() {
        // 判断是否拥有权限
        if (havePermission()) return
        // 获取应用锁模式
        val lockModel = SettingsPrefs(mContext).settingsConfig.lockModel
        // 判断模式
        when(lockModel) {
            ThisApp.LISTEN_APPS -> {
                setAccessibilityPermission(mContext)
            }
            ThisApp.STACK_POLLING -> {
                setUsageStatsManagerPermission(mContext)
            }
            else -> return
        }
    }

    /**
     * @Title: startLockerService方法
     * @Class: LockerServiceUtil
     * @Description: 启动应用锁服务
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/5/7 21:28
     * @update_author
     * @update_time
     * @param
     * @return
     * @throws
     * @version V1.0
    */
    fun startLockerService() {
        // 判断服务是否启动
        if (isLockerServiceStart()) return
        // 获取应用锁模式
        val lockModel = SettingsPrefs(mContext).settingsConfig.lockModel
        // 判断模式
        when(lockModel) {
            ThisApp.LISTEN_APPS -> {
                startAccessibilityLockerService()
            }
            ThisApp.STACK_POLLING -> {
                startUsageStatsLockerService()
            }
            else -> return
        }
    }

    /**
     * @Title: stopLockerService方法
     * @Class: LockerServiceUtil
     * @Description: 关闭应用锁服务
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/5/7 21:33
     * @update_author
     * @update_time
     * @param
     * @return
     * @throws
     * @version V1.0
    */
    fun stopLockerService() {
        // 判断服务是否启动
        if (!isLockerServiceStart()) return
        // 获取应用锁模式
        val lockModel = SettingsPrefs(mContext).settingsConfig.lockModel
        // 判断模式
        when(lockModel) {
            ThisApp.LISTEN_APPS -> {
                stopAccessibilityLockerService()
            }
            ThisApp.STACK_POLLING -> {
                stopUsageStatsLockerService()
            }
            else -> return
        }
        // 发送关闭服务广播
        val intent = Intent()
        intent.action = LockerServiceBroadcastReceiver.STOP_SERVICE
        mContext.sendBroadcast(intent)
    }
}