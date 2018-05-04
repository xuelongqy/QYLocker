package com.qingyi.applocker.activity

import android.content.Intent
import android.os.Bundle
import com.qingyi.applocker.preferences.LockAppsPrefs
import com.qingyi.applocker.util.LockerServiceUtil
import com.qingyi.applocker.util.ThemeUtil

/**
 * 主界面Activity
 * @ClassName: MainActivity
 * @Description: 主界面Activity
 * @author qingyi xuelongqy@foxmail.com
 * @date 2017/9/13 15:00
 */
class MainActivity : BaseHybridActivity(true, false) {
    // 伴生对象
    companion object {
        // 类的标识
        val TAG = MainActivity::class.java.simpleName
        val LOCK_PAGE_REQUEST_CODE = 0x1313
        val UNLOCK_MAIN = "unlock_main"
    }

    // 加锁应用配置
    private lateinit var lockAppsPrefs: LockAppsPrefs
    // 主题工具
    private lateinit var themeUtil: ThemeUtil
    // 是否解锁此软件
    private var isUnlockThis = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // enable Cordova apps to be started in the background
        val extras = intent.extras
        if (extras != null && extras.getBoolean("cdvStartInBackground", false)) {
            moveTaskToBack(true)
        }

        // Set by <content src="cordova_index.html" /> in config.xml
//         loadUrl(launchUrl)
        loadUrl("http://192.168.1.113:8080")
//        loadUrl("http://192.168.1.4:8080")

        initMethod()
    }

    fun initMethod(){
        // 初始变量
        lockAppsPrefs = LockAppsPrefs(this)
        themeUtil = ThemeUtil(this)

         var lockerServiceUtil = LockerServiceUtil(this)
        // lockerServiceUtil.startUsageStatsLockerService()
         lockerServiceUtil.startAccessibilityLockerService()
    }

    /**
     * @Title: 重写onStart方法
     * @Class: MainActivity
     * @Description: TODO
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/5/4 9:51
     * @update_author
     * @update_time
     * @version V1.0
     * @param
     * @return
     * @throws
    */
    override fun onStart() {
        // 主程序启动密码
        if (lockAppsPrefs.lockAppsConfig.theme != ""
                && lockAppsPrefs.lockAppsConfig.password != ""
                && themeUtil.getThemeByName(lockAppsPrefs.lockAppsConfig.theme) != null
                && !isUnlockThis) {
            // 当设置主题和密码后启动解锁页面
            val intent = Intent(this, AppLockActivity::class.java)
            intent.putExtra(AppLockActivity.PKG, this.packageName)
            intent.putExtra(AppLockActivity.ACT, this::class.java.name)
            this.startActivityForResult(intent, LOCK_PAGE_REQUEST_CODE)
        }
        super.onStart()
    }

    /**
     * @Title: 重写onActivityResult方法
     * @Class: MainActivity
     * @Description: 获取页面返回参数
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/5/4 10:12
     * @update_author
     * @update_time
     * @version V1.0
     * @param
     * @return
     * @throws
    */
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        when (requestCode) {
            // 获取解锁界面状态
            LOCK_PAGE_REQUEST_CODE -> {
                if (resultCode == RESULT_OK) {
                    // 获取解锁状态
                    isUnlockThis = intent!!.getBooleanExtra(UNLOCK_MAIN, false)
                }
            }
        }
    }
}
