package com.qingyi.applocker.xposed.hook

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.qingyi.applocker.activity.AppLockActivity
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
     * @Title: validation方法
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
    fun validation(mContext: Context, pkgName: String) {
        // 判断是否为上锁应用
        if (!LockAppsPrefs(mContext).lockAppsConfig.lockApps.keys.contains(pkgName)) return
        try {
            // hook页面启动
            findAndHookMethod(Activity::class.java, "onStart", object : XC_MethodHook() {
                @Throws(Throwable::class)
                override fun beforeHookedMethod(param: MethodHookParam) {
                    val activity = param.thisObject as Activity
                    LoggerUtil.logAll(Log.INFO, "${LockApps::class.java.name}.validation", "pkgName = $pkgName activity = ${activity::class.java.name} context = ${mContext::class.java.name}")
                    // 上锁应用校验
                    LockAppValidator(mContext).validatLockApp(pkgName, activity::class.java.name)
                }
            })
            // hook页面关闭
            findAndHookMethod(Activity::class.java, "onWindowFocusChanged", Boolean::class.javaPrimitiveType, object : XC_MethodHook() {
                @Throws(Throwable::class)
                override fun beforeHookedMethod(param: MethodHookParam) {
                    if (param.args[0] as Boolean) {
                        // 如果为每次解锁则清除历史
                        if (SettingsPrefs(mContext).settingsConfig.resetLockModel == ThisApp.ALL_TO_ALL) {
                            HistoryPrefs(mContext).cleanHistory()
                        }
                    }
                }
            })
        }catch (t: Throwable) {
            LoggerUtil.logAll(Log.WARN, "${LockApps::class.java.name}.validation", "Throwable=${t.localizedMessage}")
        }
    }
}