package com.qingyi.applocker.xposed.hook

import android.util.Log
import com.qingyi.applocker.util.LoggerUtil
import com.qingyi.applocker.xposed.XposedUtil
import com.xposed.qingyi.cmprotectedappsplus.constant.ThisApp
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers.findAndHookMethod
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * @Title: QyLocker类
 * @Package: com.qingyi.applocker.xposed.hook
 * @Description: 轻易锁的Hook类
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/5/7 16:23
 * @update_author
 * @update_time
 * @exception
 * @version V1.0
*/
object QyLocker {
    /**
     * @Title: initXposedActive方法
     * @Class: QyLocker
     * @Description: Hook此应用的Xposed模块激活状态为true
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/5/7 16:26
     * @update_author
     * @update_time
     * @param
     * @return
     * @throws
     * @version V1.0
    */
    fun initXposedActive(lPParam: XC_LoadPackage.LoadPackageParam) {
        try {
            findAndHookMethod(XposedUtil::class.java.name, lPParam.classLoader, "isXposedActive", object : XC_MethodHook() {
                @Throws(Throwable::class)
                override fun beforeHookedMethod(param: XC_MethodHook.MethodHookParam?) {
                    param!!.result = true
                }
            })
        }catch (t: Throwable) {
            LoggerUtil.logAll(Log.WARN, "${QyLocker::class.java.name}.initXposedActive", t.localizedMessage)
        }
    }
}