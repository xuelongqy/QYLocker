package com.qingyi.applocker.xposed.hook

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import com.qingyi.applocker.activity.AppLockActivity
import com.qingyi.applocker.filter.AppsFilter
import com.qingyi.applocker.preferences.HistoryPrefs
import com.qingyi.applocker.preferences.LockAppsPrefs
import com.qingyi.applocker.preferences.SettingsPrefs
import com.qingyi.applocker.util.LockAppValidator
import com.qingyi.applocker.util.LoggerUtil
import com.xposed.qingyi.cmprotectedappsplus.constant.ThisApp
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers.findAndHookMethod

/**
 * @Title: LockApps类
 * @Package: com.qingyi.applocker.xposed.hook
 * @Description: 上锁应用hook类
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/5/4 14:42
 * @update_author
 * @update_time
 * @version V1.0
 * @exception
*/
object LockApps {
    val TAG = LockApps::class.java.simpleName

    /**
     * @Title: initValidation方法
     * @Class: LockApps
     * @Description: 验证应用上是否上锁
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/5/4 14:43
     * @update_author
     * @update_time
     * @version V1.0
     * @param
     * @return
     * @throws
    */
    fun initValidation(mContext: Context, pkgName: String) {
        try {
            // hook页面启动
            findAndHookMethod(Activity::class.java, "onStart", object : XC_MethodHook() {
                @Throws(Throwable::class)
                override fun beforeHookedMethod(param: MethodHookParam) {
                    // 获取页面
                    val activity = param.thisObject as Activity
                    // 判断是否过滤名单应用和页面
                    if (AppsFilter.PackageFilterList.contains(pkgName) || AppsFilter.ActivityFilterList.contains(activity::class.java.name)) return
                    // 上锁应用校验
                    // 判断此应用是否为历史
                    val historyConfig = HistoryPrefs.getHistoryConfig(mContext)
                    // 如果是一次解锁所有应用,只要有历史就解锁
                    val settingsConfig = SettingsPrefs.getSettingsConfig(mContext)
                    if (settingsConfig.resetLockModel == ThisApp.ONE_TO_ALL && !historyConfig.history.isEmpty()) return
                    if (historyConfig.history.contains(pkgName)) {
                        return
                    }else {
                        // 如果为每次解锁则清除历史
                        if (settingsConfig.resetLockModel == ThisApp.ALL_TO_ALL && !historyConfig.history.isEmpty()) {
                            historyConfig.history = arrayListOf()
                            HistoryPrefs.setHistoryConfig(mContext, historyConfig)
                        }
                    }
                    // 获取配置对象，判断是否为上锁应用
                    val lockAppsConfig = LockAppsPrefs.getLockAppsConfig(mContext)
                    // 判断此应用是否上锁
                    if (!lockAppsConfig.isLock || !lockAppsConfig.lockApps.keys.contains(pkgName)) return
                    // 判断此页面是否过滤
                    if (lockAppsConfig.lockApps[pkgName]!!.filterActivity.contains(activity::class.java.name)) return
                    // LoggerUtil.logAll(Log.INFO, "${LockApps::class.java.name}.validation", "pkgName = $pkgName activity = ${activity::class.java.name}")
                    // 跳转到解锁页面
                    val intent = Intent()
                    intent.component = ComponentName(ThisApp.PACKAGE_NAME, AppLockActivity::class.java.name)
                    intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION or Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                    intent.putExtra(AppLockActivity.PKG, pkgName)
                    intent.putExtra(AppLockActivity.ACT, activity::class.java.name)
                    activity.startActivity(intent)
                }
            })
            // hook页面关闭
            /*
            findAndHookMethod(Activity::class.java, "onWindowFocusChanged", Boolean::class.javaPrimitiveType, object : XC_MethodHook() {
                @Throws(Throwable::class)
                override fun beforeHookedMethod(param: MethodHookParam) {
                    // 获取页面
                    val activity = param.thisObject as Activity
                    if (param.args[0] as Boolean) {
                        // 如果为每次解锁则清除历史
                        if (SettingsPrefs.getSettingsConfig(mContext).resetLockModel == ThisApp.ALL_TO_ALL) {
                            // 获取配置对象，判断是否为上锁应用
                            val lockAppsConfig = LockAppsPrefs.getLockAppsConfig(mContext)
                            // 判断此应用是否上锁
                            if (!lockAppsConfig.isLock || !lockAppsConfig.lockApps.keys.contains(pkgName)) return
                            // 判断此页面是否过滤
                            if (lockAppsConfig.lockApps[pkgName]!!.filterActivity.contains(activity::class.java.name)) return
                            // 清除历史
                            val historyConfig = HistoryPrefs.getHistoryConfig(mContext)
                            historyConfig.history = arrayListOf()
                            HistoryPrefs.setHistoryConfig(mContext, historyConfig)
                        }
                    }
                }
            })
            */
        }catch (t: Throwable) {
            LoggerUtil.logAll(Log.WARN, "${LockApps::class.java.name}.validation", "Throwable=${t.localizedMessage}")
        }
    }
}