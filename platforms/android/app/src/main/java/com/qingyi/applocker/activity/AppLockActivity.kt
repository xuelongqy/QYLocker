package com.qingyi.applocker.activity

import android.os.Bundle
import android.content.Intent
import android.graphics.Color
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat
import android.support.v4.widget.DrawerLayout
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.ArrayAdapter
import com.qingyi.applocker.R
import com.qingyi.applocker.bean.LockAppInfo
import com.qingyi.applocker.bean.ThemeBean
import com.qingyi.applocker.preferences.HistoryPrefs
import com.qingyi.applocker.preferences.LockAppsPrefs
import com.qingyi.applocker.preferences.SettingsPrefs
import com.qingyi.applocker.util.AppsUtil
import com.qingyi.applocker.util.FingerprintUtil
import com.qingyi.applocker.util.LoggerUtil
import com.qingyi.applocker.util.ThemeUtil
import com.qingyi.applocker.xposed.XposedUtil
import kotlinx.android.synthetic.main.activity_app_lock.*
import org.apache.cordova.CallbackContext
import org.apache.cordova.CordovaWebView
import org.apache.cordova.CordovaWebViewImpl
import org.apache.cordova.PluginResult
import org.apache.cordova.engine.SystemWebViewEngine
import org.json.JSONObject


/**
 * 应用锁Activity
 * @ClassName: AppLockActivity
 * @Description: 应用锁Activity
 * @author qingyi xuelongqy@foxmail.com
 * @date 2017/9/13 15:01
 */
class AppLockActivity: BaseHybridActivity(true, false) {
    // 伴生对象
    companion object {
        // 类的标识
        val TAG = AppLockActivity::class.java.simpleName
        // 包名
        val PKG = "pkg"
        // 页面
        val ACT = "act"
    }

    // 应用包名
    private var pkgName:String? = null
    // 页面名字
    private var activity:String? = null
    // 应用信息工具
    lateinit var appsUtil: AppsUtil
    // 加锁应用配置
    private lateinit var lockAppsPrefs: LockAppsPrefs
    // 历史配置
    private lateinit var historyPrefs: HistoryPrefs
    // 设置配置
    private lateinit var settingsPrefs: SettingsPrefs
    // 主题工具
    private lateinit var themeUtil: ThemeUtil
    // 加锁主题
    private var lockTheme: ThemeBean? = null
    // 指纹工具
    private lateinit var fingerprintUtil: FingerprintUtil
    // 指纹回调
    var fingerprintCallbackContext: CallbackContext? = null
    // 密码名字
    private lateinit var pwdName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_lock)
        init()

        // enable Cordova apps to be started in the background
        val extras = intent.extras
        if (extras != null && extras.getBoolean("cdvStartInBackground", false)) {
            moveTaskToBack(true)
        }
        // 初始化
        initMethod()
        // 判断主题是否存在
        if (lockTheme == null) {
            this.finish()
        }else {
            // 加载锁屏界面前端视图
            loadUrl(lockTheme!!.lockPage)
        }
        // 初始化界面
        initView()
    }

    /**
     * @Title: 重写makeWebView方法
     * @Class: AppLockActivity
     * @Description: 创建WebView控件
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/5/2 16:28
     * @update_author
     * @update_time
     * @version V1.0
     * @param
     * @return
     * @throws
    */
    override fun makeWebView(): CordovaWebView {
        val cordovaWebView = CordovaWebViewImpl(SystemWebViewEngine(al_web_view))
        return cordovaWebView
    }
    override fun createViews() {
        if (preferences.contains("BackgroundColor")) {
            val backgroundColor = preferences.getInteger("BackgroundColor", Color.WHITE)
            // Background of activity:
            appView.view.setBackgroundColor(backgroundColor)
        }
        appView.view.requestFocusFromTouch()
    }

    // 初始化操作
    private fun initMethod() {
        // 获取参数
        pkgName = intent.getStringExtra(PKG)
        activity = intent.getStringExtra(ACT)
        // 初始化应用信息工具
        appsUtil = AppsUtil(this.applicationContext)
        // 获取应用锁配置
        lockAppsPrefs = LockAppsPrefs(this)
        // 获取历史配置
        historyPrefs = HistoryPrefs(this)
        // 获取设置配置
        settingsPrefs = SettingsPrefs(this)
        // 初始化指纹工具
        fingerprintUtil = FingerprintUtil(this)
        if (isFingerprint()) {
            // 如果开启指纹则设置指纹监听事件
            fingerprintUtil.callFingerPrintVerify(object : FingerprintUtil.IFingerprintResultListener {
                override fun onInSecurity() {
                }
                override fun onNoEnroll() {
                }
                override fun onSupport() {
                }
                override fun onAuthenticateStart() {
                }
                // 指纹异常
                override fun onAuthenticateError(errMsgId: Int, errString: CharSequence?) {
                    if (fingerprintCallbackContext != null) {
                        val mPlugin = PluginResult(PluginResult.Status.OK, JSONObject(hashMapOf<String,Any>("success" to false, "msg" to errString.toString())))
                        mPlugin.keepCallback = true
                        fingerprintCallbackContext!!.sendPluginResult(mPlugin)
                    }
                    LoggerUtil.logAndroid(Log.INFO, "FingerPrintVerify", errString.toString())
                }
                // 验证失败
                override fun onAuthenticateFailed() {
                    if (fingerprintCallbackContext != null) {
                        val mPlugin = PluginResult(PluginResult.Status.OK, JSONObject(hashMapOf<String,Any>("success" to false, "msg" to "")))
                        mPlugin.keepCallback = true
                        fingerprintCallbackContext!!.sendPluginResult(mPlugin)
                    }
                    LoggerUtil.logAndroid(Log.INFO, "FingerPrintVerify", "failed")
                }
                override fun onAuthenticateHelp(helpMsgId: Int, helpString: CharSequence?) {
                }
                // 验证成功
                override fun onAuthenticateSucceeded(result: FingerprintManagerCompat.AuthenticationResult?) {
                    LoggerUtil.logAndroid(Log.INFO, "FingerPrintVerify", "success")
                    // 判断是否为主程序
                    if (pkgName!! == this@AppLockActivity.pkgName && activity!! == MainActivity::class.java.name) {
                        val intent = Intent()
                        intent.putExtra(MainActivity.UNLOCK_MAIN, true)
                        setResult(RESULT_OK, intent)
                    }else {
                        // 添加历史
                        // 判断Xposed模块是否激活
                        if (XposedUtil.isXposedActive()) {
                            historyPrefs.update()
                        }
                        historyPrefs.addHistory(pkgName!!, settingsPrefs.settingsConfig.resetLockModel)
                        // 是否过滤页面
                        if (al_filter_switch.isChecked) {
                            // 添加过滤页面
                            lockAppsPrefs.addFilterActivity(pkgName!!, activity!!)
                        }
                    }
                    // 关闭页面
                    this@AppLockActivity.finish()
                }

            })
        }
        // 初始化主题工具
        themeUtil = ThemeUtil(this)
        // 判断是否为主程序
        if (pkgName!! == this.pkgName && activity!! == MainActivity::class.java.name) {
            lockTheme = themeUtil.getThemeByName(lockAppsPrefs.lockAppsConfig.theme)
            pwdName = getString(R.string.master_pwd)
            al_drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }
        // 获取加锁主题
        else if (lockAppsPrefs.lockAppsConfig.lockApps[pkgName!!]!!.isIndependent) {
            // 检验是否独立设置
            lockTheme = themeUtil.getThemeByName(lockAppsPrefs.lockAppsConfig.lockApps[pkgName!!]!!.themes[0].theme)
            pwdName = lockAppsPrefs.lockAppsConfig.lockApps[pkgName!!]!!.themes[0].name
        }
        else if (lockAppsPrefs.lockAppsConfig.theme != "") {
            // 检验是否设置过密码
            lockTheme = themeUtil.getThemeByName(lockAppsPrefs.lockAppsConfig.theme)
            pwdName = getString(R.string.master_pwd)
            // 如果只有主密码并且没有高级模式则隐藏侧换栏目
            if (lockAppsPrefs.lockAppsConfig.lockApps[pkgName!!]!!.themes.isEmpty() && !settingsPrefs.settingsConfig.advancedMode) {
                al_drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
        }
    }

    /**
     * @Title: initView方法
     * @Class: AppLockActivity
     * @Description: 初始化视图
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/5/3 22:21
     * @update_author
     * @update_time
     * @param
     * @return
     * @throws
     * @version V1.0
    */
    private fun initView() {
        // 获取并设置解锁应用信息
        val lockAppInfo = appsUtil.getAppInfoByPkg(pkgName!!)
        al_app_name.text = lockAppInfo.applicationInfo.loadLabel(this.packageManager).toString()
        al_app_icon.setImageDrawable(lockAppInfo.applicationInfo.loadIcon(this.packageManager))
        // 设置包名和页面信息
        al_pkg_text.text = pkgName
        al_act_text.text = activity
        // 是否显示页面信息
        if (settingsPrefs.settingsConfig.advancedMode) {
            al_page_info.visibility = View.VISIBLE
        }else {
            al_page_info.visibility = View.GONE
        }
        // 设置密码列表
        val pwds = arrayListOf<String>()
        // 判断是否为主程序
        if (pkgName!! == this.pkgName && activity!! == MainActivity::class.java.name) {
            pwds.add(getString(R.string.master_pwd))
        }else {
            // 判断是否独立设置
            if (!lockAppsPrefs.lockAppsConfig.lockApps[pkgName!!]!!.isIndependent) {
                pwds.add(getString(R.string.master_pwd))
            }
            // 添加独立设置中的密码
            for (theme in lockAppsPrefs.lockAppsConfig.lockApps[pkgName!!]!!.themes) {
                pwds.add(theme.name)
            }
        }
        val pwdsAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, pwds)
        al_pwd_list.adapter = pwdsAdapter
        // 设置列表监听事件
        al_pwd_list.setOnItemClickListener { parent, view, position, id ->
            al_drawer_layout.closeDrawers()
            // 如果没有改变则不做处理
            if (this.pwdName == pwds[position]) {
                return@setOnItemClickListener
            }
            // 获取主题
            var lockTheme: ThemeBean? = null
            // 判断是否为主密码
            if (pwds[position] == getString(R.string.master_pwd)) {
                lockTheme = themeUtil.getThemeByName(lockAppsPrefs.lockAppsConfig.theme)
                this.lockTheme = lockTheme
                this.pwdName = pwds[position]
                loadUrl(this.lockTheme!!.lockPage)
                return@setOnItemClickListener
            }
            // 遍历密码列表
            for (theme in lockAppsPrefs.lockAppsConfig.lockApps[pkgName!!]!!.themes) {
                if (theme.name == pwds[position]) {
                    lockTheme = themeUtil.getThemeByName(theme.theme)
                    if (lockTheme != null) {
                        // 切换解锁页面
                        this.lockTheme = lockTheme
                        this.pwdName = pwds[position]
                        loadUrl(this.lockTheme!!.lockPage)
                    }
                    return@setOnItemClickListener
                }
            }
        }
    }

    /**
     * @Title: 重写onStop方法
     * @Class: AppLockActivity
     * @Description: 当解锁页面停止则销毁
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/5/4 10:02
     * @update_author
     * @update_time
     * @version V1.0
     * @param
     * @return
     * @throws
    */
    override fun onStop() {
        super.onStop()
        this.finish()
    }

    /**
     * @Title: isFingerprint方法
     * @Class: AppLockActivity
     * @Description: 是否使用指纹
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/5/2 0:13
     * @update_author
     * @update_time
     * @param
     * @return
     * @throws
     * @version V1.0
    */
    fun isFingerprint():Boolean {
        return settingsPrefs.settingsConfig.useFingerprint && fingerprintUtil.isHardwareDetected && fingerprintUtil.isHasEnrolledFingerprints
    }

    /**
     * @Title: verifyPassword方法
     * @Class: AppLockActivity
     * @Description: 验证密码
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/5/1 22:47
     * @update_author
     * @update_time
     * @param pwd[String] 密码
     * @return
     * @throws
     * @version V1.0
    */
    fun verifyPassword(pwd:String):Boolean {
        val isRight:Boolean
        // 判断使用默认密码还是独立设置的密码
        if (this.pwdName == getString(R.string.master_pwd)) {
            isRight = lockAppsPrefs.verifyPassword(pwd)
        }else {
            isRight = lockAppsPrefs.verifyPasswordByPwdName(pkgName!!, pwdName, pwd)
        }
        // 判断密码是否正确
        if (isRight) {
            // 判断是否为主程序
            if (pkgName!! == this.pkgName && activity!! == MainActivity::class.java.name) {
                val intent = Intent()
                intent.putExtra(MainActivity.UNLOCK_MAIN, true)
                setResult(RESULT_OK, intent)
            }else {
                // 添加历史
                historyPrefs.addHistory(pkgName!!, settingsPrefs.settingsConfig.resetLockModel)
                // 是否过滤页面
                if (al_filter_switch.isChecked) {
                    // 添加过滤页面
                    lockAppsPrefs.addFilterActivity(pkgName!!, activity!!)
                }
            }
            // 关闭页面
            this.finish()
        }
        return isRight
    }

    /**
     * @Title: getThemeData方法
     * @Class: AppLockActivity
     * @Description: 获取主题额外数据
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/5/2 13:42
     * @update_author
     * @update_time
     * @version V1.0
     * @return [String] 主题数据
     * @throws
    */
    fun getThemeData():String {
        return themeUtil.getThemeData(lockTheme!!.name)
    }

    /**
     * @Title: getLockAppInfo方法
     * @Class: AppLockActivity
     * @Description: 获取上锁应用信息
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/5/3 16:10
     * @update_author
     * @update_time
     * @version V1.0
     * @param
     * @return
     * @throws
    */
    fun getLockAppInfo():LockAppInfo {
        return appsUtil.getLockAppInfo(pkgName!!)
    }

    /**
     * 监听返回键
     * @Title: onBackPressed
     * @Description: 监听返回键
     * @author qingyi xuelongqy@foxmail.com
     * @date 2017/9/13 15:35
     */
    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        if (event!!.keyCode == KeyEvent.KEYCODE_BACK) {
            //返回桌面
            val homeIntent = Intent(Intent.ACTION_MAIN)
            homeIntent.addCategory(Intent.CATEGORY_HOME)
            this.startActivity(homeIntent)
        }
        return super.dispatchKeyEvent(event)
    }
}