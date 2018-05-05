package com.qingyi.applocker.util

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import com.qingyi.applocker.activity.AppLockActivity
import com.qingyi.applocker.preferences.HistoryPrefs
import com.qingyi.applocker.preferences.LockAppsPrefs
import com.qingyi.applocker.preferences.SettingsPrefs
import com.qingyi.applocker.xposed.hook.LockApps
import com.xposed.qingyi.cmprotectedappsplus.constant.ThisApp

/**
 * @Title: LockAppValidator类
 * @Package: com.qingyi.applocker.util
 * @Description: 上锁App验证器
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/4/9 22:48
 * @update_author
 * @update_time
 * @exception
 * @version V1.0
*/
class LockAppValidator(private val mContext: Context) {
    // 伴生对象
    companion object {
        // 类的标识
        val TAG = LockAppValidator::class.java.simpleName
    }

    // 加锁应用配置
    private var lockAppsPrefs = LockAppsPrefs(mContext)
    // 设置配置
    private var settingsPrefs = SettingsPrefs(mContext)
    // 历史配置
    private var historyPrefs = HistoryPrefs(mContext)

    /**
     * @Title: validatLockApp方法
     * @Class: LockAppValidator
     * @Description: 验证加锁应用
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/4/9 23:02
     * @update_author
     * @update_time
     * @param pkg[String] 包名
     * @param activity[String] Activity名
     * @return
     * @throws
     * @version V1.0
    */
    fun validatLockApp(pkg: String, activity: String) {
        // 判断是否开启应用锁
        if (!lockAppsPrefs.lockAppsConfig.isLock) {
            return
        }
        // 判断是否为加锁应用
        if (lockAppsPrefs.lockAppsConfig.lockApps.containsKey(pkg) && !lockAppsPrefs.lockAppsConfig.lockApps[pkg]!!.filterActivity.contains(activity)){
            // 如果是一次解锁所有应用,只要有历史就解锁
            if (settingsPrefs.settingsConfig.resetLockModel == ThisApp.ONE_TO_ALL && !historyPrefs.historyConfig.history.isEmpty()) {
                return
            }
            // 判断是否有打开历史
            if (historyPrefs.historyConfig.history.contains(pkg)) {
                return
            }
            // 打开解锁页面
            val intent = Intent(mContext, AppLockActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION or Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            intent.putExtra(AppLockActivity.PKG, pkg)
            intent.putExtra(AppLockActivity.ACT, activity)
            mContext.startActivity(intent)
        }else {
            // 判断是否为每次解锁,是则清空历史
            if (settingsPrefs.settingsConfig.resetLockModel == ThisApp.ALL_TO_ALL && !historyPrefs.historyConfig.history.isEmpty()) {
                historyPrefs.cleanHistory()
            }
        }
    }
}