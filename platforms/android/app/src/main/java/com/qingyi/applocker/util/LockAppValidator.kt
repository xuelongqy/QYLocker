package com.qingyi.applocker.util

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.qingyi.applocker.activity.AppLockActivity
import com.qingyi.applocker.preferences.LockAppsPrefs

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
        // 遍历加锁应用列表
        if (lockAppsPrefs.lockAppsConfig.lockApps.containsKey(pkg)){
            val intent = Intent(mContext, AppLockActivity::class.java)
            mContext.startActivity(intent)
            Toast.makeText(mContext, "Package: $pkg\nActivity: $activity", Toast.LENGTH_LONG).show()
        }
    }
}