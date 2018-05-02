package com.qingyi.applocker.activity

import android.os.Bundle
import android.util.Log
import com.qingyi.applocker.util.AppsUtil
import com.qingyi.applocker.util.LockerServiceUtil
import com.qingyi.applocker.util.LoggerUtil

/**
 * 主界面Activity
 * @ClassName: MainActivity
 * @Description: 主界面Activity
 * @author qingyi xuelongqy@foxmail.com
 * @date 2017/9/13 15:00
 */
class MainActivity : BaseHybridActivity(true, false) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // enable Cordova apps to be started in the background
        val extras = intent.extras
        if (extras != null && extras.getBoolean("cdvStartInBackground", false)) {
            moveTaskToBack(true)
        }

        // Set by <content src="cordova_index.html" /> in config.xml
//         loadUrl(launchUrl)
//        loadUrl("http://192.168.1.113:8080")
        loadUrl("http://192.168.1.4:8080")

        initMethod()
    }

    fun initMethod(){
         var lockerServiceUtil = LockerServiceUtil(this)
        // lockerServiceUtil.startUsageStatsLockerService()
         lockerServiceUtil.startAccessibilityLockerService()
         LoggerUtil.logAndroid(Log.INFO,"AppInfoList",AppsUtil(this).getAllAppsInfo().toString())
    }
}
