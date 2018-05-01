package com.qingyi.applocker.service

import android.accessibilityservice.AccessibilityService
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast
import com.qingyi.applocker.R
import com.qingyi.applocker.activity.AppLockActivity
import com.qingyi.applocker.activity.MainActivity
import com.qingyi.applocker.preferences.LockAppsPrefs
import com.qingyi.applocker.util.LoggerUtil
import com.xposed.qingyi.cmprotectedappsplus.constant.ThisApp
import android.app.NotificationManager
import android.app.NotificationChannel
import android.app.usage.UsageEvents
import android.content.Context
import android.graphics.Color
import com.qingyi.applocker.filter.AppsFilter
import com.qingyi.applocker.util.LockAppValidator


/**
 * 使用Accessibility方式监听应用启动的服务
 * @ClassName: AccessibilityLockerService
 * @Description: 使用Accessibility方式监听应用启动的服务
 * @author qingyi xuelongqy@foxmail.com
 * @date 2017/8/15 17:27
 */
class AccessibilityLockerService: AccessibilityService() {

    companion object {
        // 设置常驻通知栏ID
        private const val NOTICATION_ID = 0x1414
        // 通道ID
        const val CHANNEL_ID = ThisApp.PACKAGE_NAME + ".AccessibilityChannel"
        // 通道名称
        const val CHANNEL_NAME = "AccessibilityChannel"
    }

    // 缓存栈顶应用
    private var topPkg: String? = null
    // 加锁应用验证器
    private lateinit var lockAppValidator: LockAppValidator

    /**
     * 重写启动命令方法
     * 使服务在Kill后重新启动,生命力更强
     * @Title: onStartCommand
     * @Description: 重写启动命令方法
     * @author qingyi xuelongqy@foxmail.com
     * @date 2017/8/15 15:31
     * @param intent 上下文
     * @param flags 启动标识
     * @param startId 启动ID
     * @return 启动标识
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, Service.START_FLAG_REDELIVERY, startId)
    }

    /**
     * 系统成功连接上服务时调用方法
     * @Title: onServiceConnected
     * @Description: 系统成功连接上服务时调用方法
     * @author qingyi xuelongqy@foxmail.com
     * @date 2017/8/15 20:28
     */
    override fun onServiceConnected() {
        //启动前台服务
        startNotification()
        // 初始化验证器
        lockAppValidator = LockAppValidator(this)
        super.onServiceConnected()
    }

    /**
     * 服务中断时调用方法
     * @Title: onInterrupt
     * @Description: 服务中断时调用方法
     * @author qingyi xuelongqy@foxmail.com
     * @date 2017/8/15 20:27
     */
    override fun onInterrupt() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * 系统断开服务时调用方法
     * @Title: onUnbind
     * @Description: 系统断开服务时调用方法
     * @author qingyi xuelongqy@foxmail.com
     * @date 2017/8/15 20:29
     * @param intent 上下文
     */
    override fun onUnbind(intent: Intent?): Boolean {
        //关闭前台服务
        stopForeground(true)
        return super.onUnbind(intent)
    }

    /**
     * 获取应用事件并相应
     * 获取当前运行应用的包名
     * @Title: onAccessibilityEvent
     * @Description: 获取当前运行应用的包名
     * @author qingyi xuelongqy@foxmail.com
     * @date 2017/8/15 20:19
     * @param event 当前应用的事件
     */
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return
        // LoggerUtil.logAndroid(Log.INFO,"onAccessibilityEvent", "package=${event!!.packageName} activity=${event.className}")
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            // 判断是否过滤名单应用和页面
            if (AppsFilter.PackageFilterList.contains(event.packageName.toString()) || AppsFilter.ActivityFilterList.contains(event.className.toString())) {
                return
            }
            // 判断是否为锁屏页面
            if (event.packageName.toString() == this.application.packageName && event.className.toString() == AppLockActivity::class.java.name) {
                topPkg = event.packageName.toString()
                return
            }
            // 验证上一次是否为同一个应用
            if (topPkg == null || topPkg != event.packageName.toString()) {
                // 验证应用
                lockAppValidator.validatLockApp(event.packageName.toString(), event.className.toString())
            }
            topPkg = event.packageName.toString()
        }
    }

    /**
     * 开启常驻通知栏
     * 设置为前台服务,服务守护
     * @Title: startNotification
     * @Description: 开启常驻通知栏
     * @author qingyi xuelongqy@foxmail.com
     * @date 2017/8/15 16:02
     */
    private fun startNotification() {
        // 创建通道
        val notificationChannel: NotificationChannel
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(notificationChannel)
        }
        // 触发事件绑定(开启主程序)
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
        // 创建通知
        val notification: Notification.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = Notification.Builder(this, CHANNEL_ID)
        }
        else {
            notification = Notification.Builder(this)
        }
        notification
            //设置启动时间
            .setWhen(System.currentTimeMillis())
            //显示通知常驻时间
            .setShowWhen(true)
            //设置图标
            .setSmallIcon(R.mipmap.icon)
            //扩充通知头部内容,App名字后面
            .setSubText(getString(R.string.efficient_mode))
            //设置点击打开主程序
            .setContentIntent(pendingIntent)
        //设置为前台服务
        startForeground(NOTICATION_ID,notification.build())
    }
}