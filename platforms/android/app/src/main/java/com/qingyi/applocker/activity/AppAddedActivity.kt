package com.qingyi.applocker.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.qingyi.applocker.R
import com.qingyi.applocker.preferences.LockAppsPrefs
import com.qingyi.applocker.util.AppsUtil
import kotlinx.android.synthetic.main.activity_app_added.*

/**
 * @Title: AppAddedActivity类
 * @Package: com.qingyi.applocker.activity
 * @Description: 新应用添加上锁提示
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/5/8 11:19
 * @update_author
 * @update_time
 * @exception
 * @version V1.0
*/
class AppAddedActivity: AppCompatActivity() {
    // 伴生对象
    companion object {
        // App包名标识
        const val PACKAGE_NAME = "package_name"
    }

    // App包名
    private var pkgName:String? = null
    // 加锁应用配置
    private lateinit var lockAppsPrefs: LockAppsPrefs
    // 应用信息工具
    lateinit var appsUtil: AppsUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_added)

        // 获取参数
        pkgName = intent.getStringExtra(PACKAGE_NAME)
        if (pkgName == null) this.finish()
        // 初始化变量
        lockAppsPrefs = LockAppsPrefs(this)
        appsUtil = AppsUtil(this.applicationContext)
        // 获取应用信息
        val pkgInfo = appsUtil.getAppInfoByPkg(pkgName!!)
        // 设置应用信息
        aa_app_name.text = pkgInfo.applicationInfo.loadLabel(this.packageManager).toString()
        aa_app_icon.setImageDrawable(pkgInfo.applicationInfo.loadIcon(this.packageManager))
        // 按钮监听
        aa_later_btn.setOnClickListener {
            this.finish()
        }
        aa_lock_app_btn.setOnClickListener {
            lockAppsPrefs.addLockApp(pkgName!!)
            this.finish()
        }
    }
}