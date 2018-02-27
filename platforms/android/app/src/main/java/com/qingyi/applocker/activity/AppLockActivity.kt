package com.qingyi.applocker.activity

import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import android.content.Intent
import android.content.pm.PackageManager


/**
 * 应用锁Activity
 * @ClassName: AppLockActivity
 * @Description: 应用锁Activity
 * @author qingyi xuelongqy@foxmail.com
 * @date 2017/9/13 15:01
 */
class AppLockActivity: BaseHybridActivity(true, false) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // enable Cordova apps to be started in the background
        val extras = intent.extras
        if (extras != null && extras.getBoolean("cdvStartInBackground", false)) {
            moveTaskToBack(true)
        }

        //加载锁屏界面前端视图
        //loadUrl(launchUrl)
        loadUrl("http://192.168.42.139:8082")

        initMethod()
    }

    fun initMethod() {

    }

    /**
     * 监听返回键
     * @Title: onBackPressed
     * @Description: 监听返回键
     * @author qingyi xuelongqy@foxmail.com
     * @date 2017/9/13 15:35
     */
    override fun onBackPressed() {
        //返回桌面
        val homeIntent = Intent(Intent.ACTION_MAIN)
        homeIntent.addCategory(Intent.CATEGORY_HOME)
        this.startActivity(homeIntent)
    }
}