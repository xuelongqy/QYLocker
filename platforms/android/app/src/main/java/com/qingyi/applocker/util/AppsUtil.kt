package com.qingyi.applocker.util

import android.content.Context
import com.qingyi.applocker.bean.AppInfoBean
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import com.qingyi.applocker.bean.AppInfoJsonBean
import com.xposed.qingyi.cmprotectedappsplus.constant.ThisApp
import java.util.LinkedHashMap
import java.util.ArrayList
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.IOException
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import com.qingyi.applocker.filter.AppsFilter
import com.qingyi.applocker.preferences.LockAppsPrefs


/**
 * Apps工具类(主要用于处理Apps的一些信息)
 * @ClassName: AppsUtil
 * @Description: Apps工具类
 * @author qingyi xuelongqy@foxmail.com
 * @date 2017/8/13 23:56
 */
class AppsUtil(val context: Context) {
    //记录上下文
    private val mContext = context
    // Json操作对象
    private var gson = GsonBuilder().create()

    /**
     * 获取所有应用信息
     * @Title: getAllAppsInfo
     * @Description: 获取所有应用的信息(名称、版本、logo等)
     * @author qingyi xuelongqy@foxmail.com
     * @date 2017/9/1 16:46
     * @return App信息集合
     */
    fun getAllAppsInfo(): ArrayList<AppInfoBean> {
        // 缓存应用信息集合
        val appInfoList = ArrayList<AppInfoBean>()
        // 获取设备中安装的所有应用信息
        val packageList = mContext.packageManager.getInstalledPackages(0)
        val packageMap = HashMap<String, PackageInfo>()
        packageList.forEach{
                packageMap[it.packageName] = it
        }
        // 获取具有图标启动项的应用
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        val resolveInfoList = mContext.packageManager.queryIntentActivities(intent, 0)
        //遍历应用信息
        resolveInfoList.forEach {
            if (packageMap.containsKey(it.activityInfo.packageName)) {
                val packageName = it.activityInfo.packageName
                // 验证过滤名单
                if (AppsFilter.AppsFilterMap.containsKey(packageName)){
                    return@forEach
                }
                // 添加应用信息
                val p = packageMap[packageName]!!
                val appInfoTmp = AppInfoBean(
                        appName =  p.applicationInfo.loadLabel(mContext.packageManager).toString(),
                        packageName = p.packageName,
                        versionName = p.versionName,
                        versionCode = p.versionCode,
                        appIcon = p.applicationInfo.loadIcon(mContext.packageManager),
                        isSystemAPP = p.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
                )
                appInfoList.add(appInfoTmp)
            }
        }
        LoggerUtil.logAndroid(Log.INFO, "getAllAppsInfo", "All app num is " + appInfoList.size)
        return appInfoList
    }

    //判断是否为系统应用
    fun isSystemAPP(packageInfo: PackageInfo): Boolean {
        if (packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM == 0) {
            // 非系统应用
            return false
        }
        //系统应用
        return true
    }

    /**
     * @Title: getActivitiesByPkg方法
     * @Class: AppsUtil
     * @Description: 通过包名获取所有Activities
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/4/24 8:56
     * @update_author
     * @update_time
     * @version V1.0
     * @param pkg[String] 包名
     * @return Activities列表
     * @throws
    */
    fun getActivitiesByPkg(pkg: String): ArrayList<String> {
        val pkgInfo = mContext.packageManager.getPackageInfo(pkg, PackageManager.GET_ACTIVITIES)
        val activityList = arrayListOf<String>()
        pkgInfo.activities.forEach {
            if (it.name != null) {
                activityList.add(it.name)
            }
        }
        return activityList
    }
}