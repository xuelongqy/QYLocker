package com.qingyi.applocker.service

import android.app.*
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import com.qingyi.applocker.R
import com.qingyi.applocker.activity.MainActivity
import android.os.Build
import com.qingyi.applocker.util.LoggerUtil
import com.xposed.qingyi.cmprotectedappsplus.constant.ThisApp


/**
 * 使用UsageStatsManager(应用最近使用情况)方式监听应用启动的服务
 * 标识: QYLocker-UsageStatsService
 * @ClassName: UsageStatsLockerService
 * @Description: 使用UsageStatsManager方式监听应用启动的服务
 * @author qingyi xuelongqy@foxmail.com
 * @date 2017/8/15 0:06
 */
class UsageStatsLockerService: IntentService("QYLocker-UsageStatsService") {

    companion object {
        // 设置轮询间隔时间
        private const val POLLING_INTERVAL:Long = 100
        // 设置常驻通知栏ID
        private const val NOTICATION_ID = 0x1313
        // 通道ID
        const val CHANNEL_ID = ThisApp.PACKAGE_NAME + ".UsageStatsChannel"
        // 通道名称
        const val CHANNEL_NAME = "UsageStatsChannel"
    }

    //缓存activityManager
    lateinit var activityManager:ActivityManager

    /**
     * 重写创建监听方法
     * @Title: onCreate
     * @Description: 创建监听方法
     * @author qingyi xuelongqy@foxmail.com
     * @date 2017/8/15 0:07
     */
    override fun onCreate() {
        super.onCreate()
        LoggerUtil.logAndroid(Log.INFO,"UsageStatsLockerService.onCreate", "UsageStatsLockerService start!");
        activityManager = ((getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?)!!)
    }

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
        //启动前台服务
        startNotification()
        return super.onStartCommand(intent, Service.START_FLAG_REDELIVERY, startId)
    }

    /**
     * 重写监听销毁事件
     * 关闭前台服务
     * @Title: onDestroy
     * @Description: 重写监听销毁事件
     * @author qingyi xuelongqy@foxmail.com
     * @date 2017/8/15 16:35
     */
    override fun onDestroy() {
        //关闭前台服务
        stopForeground(true)
        super.onDestroy()
    }

    /**
     * 异步执行方法,轮询判断栈顶Activity
     * @Title: onHandleIntent
     * @Description: 异步执行线程
     * @author qingyi xuelongqy@foxmail.com
     * @date 2017/8/15 0:09
     * @param intent 交互对象
     */
    override fun onHandleIntent(intent: Intent?) {
        //轮询获取
        while (true){
            LoggerUtil.logAndroid(Log.INFO,"UsageStatsLockerService.onHandleIntent", "package=${getLauncherTopPackageName()}")

            //启动时间间隔
            try {
                Thread.sleep(POLLING_INTERVAL)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 获取栈顶应用的包名
     * @Title: getLauncherTopPackageName
     * @Description: 获取栈顶应用的包名
     * @author qingyi xuelongqy@foxmail.com
     * @date 2017/8/15 1:08
     * @return 栈顶应用的包名
     */
    fun getLauncherTopPackageName(): String {
        //获取UsageStatsManager对象
        val sUsageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        //设置获取应用使用情况的开始时间和截至时间
        val endTime = System.currentTimeMillis()
        val beginTime = endTime - 5000
        //缓存获取的栈顶应用的包名
        var result = ""
        val event = UsageEvents.Event()
        val usageEvents = sUsageStatsManager.queryEvents(beginTime, endTime)
        //遍历最近时间应用的使用情况
        while (usageEvents.hasNextEvent()) {
            usageEvents.getNextEvent(event)
            //判断栈顶应用
            if (event.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                result = event.packageName
            }
        }
        //判断是否正确获取
        if (!android.text.TextUtils.isEmpty(result)) {
            return result
        }
        return ""
    }

    /**
     * 开启常驻通知栏
     * 设置为前台服务,服务守护
     * @Title: startNotification
     * @Description: 开启常驻通知栏
     * @author qingyi xuelongqy@foxmail.com
     * @date 2017/8/15 16:02
     */
    fun startNotification() {
        // 创建通道
        val notificationChannel: NotificationChannel
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(AccessibilityLockerService.CHANNEL_ID,
                    AccessibilityLockerService.CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(notificationChannel)
        }
        //触发事件绑定(开启主程序)
        val notificationIntent = Intent(this,MainActivity::class.java)
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
            .setSubText(getString(R.string.traditional_mode))
            //设置点击打开主程序
            .setContentIntent(pendingIntent)
        //设置为前台服务
        startForeground(NOTICATION_ID,notification.build())
    }
}