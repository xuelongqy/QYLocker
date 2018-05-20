package com.qingyi.applocker.xposed.hook

import android.content.ComponentName
import android.content.Context
import android.content.IIntentReceiver
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.qingyi.applocker.activity.AppAddedActivity
import com.qingyi.applocker.preferences.LockAppsPrefs
import com.qingyi.applocker.preferences.SettingsPrefs
import com.qingyi.applocker.util.LoggerUtil
import com.xposed.qingyi.cmprotectedappsplus.constant.ThisApp
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * @Title: PackageChange类
 * @Package: com.qingyi.applocker.xposed.hook
 * @Description: Hook应用包改变(安装、卸载、更新)
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/5/8 15:19
 * @update_author
 * @update_time
 * @exception
 * @version V1.0
*/
object PackageChange {
    // 包名
    val PACKAGE_NAME = "android"
    // 包管理器服务类名
    val CLASS_NAME = "com.android.server.pm.PackageManagerService"

    fun initPackageChange(lPParam: XC_LoadPackage.LoadPackageParam, mContext: Context) {
        // 判断是否锁定新应用
        if (!SettingsPrefs.getSettingsConfig(mContext).lockNewApp) return
        try {
            XposedHelpers.findAndHookMethod(CLASS_NAME, lPParam.classLoader, "sendPackageBroadcast",
                    String::class.java,
                    String::class.java,
                    Bundle::class.java,
                    Int::class.javaPrimitiveType,
                    String::class.java,
                    IIntentReceiver::class.java,
                    IntArray::class.java,
                    object : XC_MethodHook() {
                @Throws(Throwable::class)
                override fun beforeHookedMethod(param: MethodHookParam) {
                    val action = param.args[0] as String
                    val pkg = param.args[1] as String
                    // 判断是否已经加锁
                    if (LockAppsPrefs.getLockAppsConfig(mContext).lockApps.containsKey(pkg)) return
                    when (action) {
                        // 新应用安装
                        Intent.ACTION_PACKAGE_ADDED -> {
                            LoggerUtil.logAll(Log.WARN, "${PackageChange::class.java.name}.initPackageChange", "context=${lPParam.packageName},pkg=$pkg")
                            // 启动页面
                            val intent = Intent()
                            intent.component = ComponentName(ThisApp.PACKAGE_NAME, AppAddedActivity::class.java.name)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                            intent.putExtra(AppAddedActivity.PACKAGE_NAME, pkg)
                            mContext.startActivity(intent)
                        }
                    }
                }
            })
        }catch (t: Throwable) {
            LoggerUtil.logAll(Log.WARN, "${PackageChange::class.java.name}.initPackageChange", t.localizedMessage)
        }
    }
}