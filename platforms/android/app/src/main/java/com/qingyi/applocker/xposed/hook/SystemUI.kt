package com.qingyi.applocker.xposed.hook

import android.content.Context
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.util.Log
import com.qingyi.applocker.preferences.HistoryPrefs
import com.qingyi.applocker.util.LoggerUtil
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers.findAndHookMethod
import de.robv.android.xposed.XposedHelpers.findClass
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * @Title: SystemUI类
 * @Package: com.qingyi.applocker.xposed.hook
 * @Description: 系统界面的Hook
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/5/7 15:36
 * @update_author
 * @update_time
 * @exception
 * @version V1.0
*/
object SystemUI {
    // 系统界面包名
    const val PACKAGE_NAME = "com.android.systemui"


    /**
     * @Title: initScreenOff方法
     * @Class: SystemUI
     * @Description: Hook屏幕锁屏
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/5/7 16:11
     * @update_author
     * @update_time
     * @param
     * @return
     * @throws
     * @version V1.0
    */
    fun initScreenOff(lPParam: XC_LoadPackage.LoadPackageParam, mContext: Context) {
        // 锁屏类
        val hookedClass = "$PACKAGE_NAME.keyguard.KeyguardViewMediator"
        // Hook方法
        val hook = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: XC_MethodHook.MethodHookParam?) {
                // 清除历史
                val historyConfig = HistoryPrefs.getHistoryConfig(mContext)
                if (!historyConfig.history.isEmpty()) {
                    historyConfig.history = arrayListOf()
                    HistoryPrefs.setHistoryConfig(mContext, historyConfig)
                }
            }
        }
        // Hook
        try {
            findAndHookMethod(hookedClass, lPParam.classLoader, "onScreenTurnedOff", *if (SDK_INT >= Build.VERSION_CODES.M) arrayOf<Any>(hook) else arrayOf<Any>(Int::class.javaPrimitiveType!!, hook))
        } catch (t: Throwable) {
            LoggerUtil.logAll(Log.WARN, "${SystemUI::class.java.name}.initScreenOff", t.localizedMessage)
        }

    }
}