package com.qingyi.applocker.xposed.hook

import android.app.Application
import android.content.Context
import android.util.Log
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
                val mContext = param.args[0] as Context
                when (lpparam.packageName) {
                    SystemUI.PACKAGE_NAME -> {
                        SystemUI.initScreenOff(lpparam, mContext)
                    }
                    ThisApp.PACKAGE_NAME -> {
                        QyLocker.initXposedActive(lpparam)
                    }
                }
                LockApps.initValidation(mContext, lpparam.packageName)
            }
        })
    }
}