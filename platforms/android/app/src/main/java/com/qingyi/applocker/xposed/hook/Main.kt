package com.qingyi.applocker.xposed.hook

import android.app.Application
import android.content.Context
import android.util.Log
import com.qingyi.applocker.preferences.HistoryPrefs
import com.qingyi.applocker.preferences.LockAppsPrefs
import com.qingyi.applocker.preferences.SettingsPrefs
import com.qingyi.applocker.util.LoggerUtil
import com.xposed.qingyi.cmprotectedappsplus.constant.ThisApp
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.callbacks.XC_LoadPackage
import de.robv.android.xposed.XposedHelpers.findAndHookMethod

/**
 * @Title: Main类
 * @Package:com.qingyi.applocker.xposed.hook
 * @Description: Xposed入口,监听方法并hook
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/5/4 13:34
 * @update_author
 * @update_time
 * @version V1.0
 * @exception
*/
class Main: IXposedHookZygoteInit, IXposedHookLoadPackage {
    // 半生对象
    companion object {
        // 上下文
        lateinit var mContext: Context
        // 历史配置
        var historyConfigr: HistoryPrefs.HistoryConfig? = null
            get() {
                if (field == null) {
                    field = HistoryPrefs.getHistoryConfig(mContext)
                }
                return field
            }
        // 应用锁配置
        var lockAppsConfig: LockAppsPrefs.LockAppsConfig? = null
            get() {
                if (field == null) {
                    field = LockAppsPrefs.getLockAppsConfig(mContext)
                }
                return field
            }
        // 设置配置
        var settingsConfig: SettingsPrefs.SettingsConfig? = null
            get() {
                if (field == null) {
                    LoggerUtil.logAll(Log.INFO, "${Main::class.java.name}.settingsConfig", "get()")
                    field = SettingsPrefs.getSettingsConfig(mContext)
                }
                return field
            }
    }

    // Xposed模块初始化完成
    @Throws(Throwable::class)
    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam?) {
        LoggerUtil.logAll(Log.INFO, "${Main::class.java.name}.initZygote", "Xposed loaded!")
    }

    /**
     * @Title: handleLoadPackage方法
     * @Class: Main
     * @Description: 监听方法并hook
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/5/4 13:36
     * @update_author
     * @update_time
     * @version V1.0
     * @param lpparam[XC_LoadPackage.LoadPackageParam] hook所需参数
     * @return
     * @throws
    */
    @Throws(Throwable::class)
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        // 注册Hook
        findAndHookMethod(Application::class.java, "attach", Context::class.java, object : XC_MethodHook() {
            // 方法执行前hook
            @Throws(Throwable::class)
            override fun afterHookedMethod(param: MethodHookParam) {}
            // 方法执行后hook
            @Throws(Throwable::class)
            override fun beforeHookedMethod(param: MethodHookParam) {
                // 获取上下文
                mContext = param.args[0] as Context
                when (lpparam.packageName) {
                    SystemUI.PACKAGE_NAME -> {
                        // 屏幕关闭
                        SystemUI.initScreenOff(lpparam, mContext)
                    }
                    ThisApp.PACKAGE_NAME -> {
                        // 设置Xposed模块激活
                        QyLocker.initXposedActive(lpparam)
                    }
                    PackageChange.PACKAGE_NAME -> {
                        // 应用包改变(安装、卸载、更新)
                        PackageChange.initPackageChange(lpparam, mContext)
                    }
                }
                LockApps.initValidation(mContext, lpparam.packageName)
            }
        })
    }
}