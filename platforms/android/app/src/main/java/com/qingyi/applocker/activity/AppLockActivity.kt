package com.qingyi.applocker.activity

import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat
import android.util.Log
import com.qingyi.applocker.bean.ThemeBean
import com.qingyi.applocker.preferences.HistoryPrefs
import com.qingyi.applocker.preferences.LockAppsPrefs
import com.qingyi.applocker.preferences.SettingsPrefs
import com.qingyi.applocker.util.FingerprintUtil
import com.qingyi.applocker.util.LoggerUtil
import com.qingyi.applocker.util.ThemeUtil
import org.apache.cordova.CallbackContext
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
    }

    // 初始化操作
    private fun initMethod() {
        // 获取参数
        pkgName = intent.getStringExtra(PKG)
        activity = intent.getStringExtra(ACT)
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
                        fingerprintCallbackContext!!.success(JSONObject(hashMapOf<String,Any>("success" to false, "msg" to errString.toString())))
                    }
                    LoggerUtil.logAndroid(Log.INFO, "FingerPrintVerify", errString.toString())
                }
                // 验证失败
                override fun onAuthenticateFailed() {
                    if (fingerprintCallbackContext != null) {
                        fingerprintCallbackContext!!.success(JSONObject(hashMapOf<String,Any>("success" to false, "msg" to "")))
                    }
                    LoggerUtil.logAndroid(Log.INFO, "FingerPrintVerify", "failed")
                }
                override fun onAuthenticateHelp(helpMsgId: Int, helpString: CharSequence?) {
                }
                // 验证成功
                override fun onAuthenticateSucceeded(result: FingerprintManagerCompat.AuthenticationResult?) {
                    LoggerUtil.logAndroid(Log.INFO, "FingerPrintVerify", "success")
                    // 添加历史
                    historyPrefs.addHistory(pkgName!!, settingsPrefs.settingsConfig.resetLockModel)
                    // 关闭页面
                    this@AppLockActivity.finish()
                }

            })
        }
        // 初始化主题工具
        themeUtil = ThemeUtil(this)
        // 获取加锁主题
        if (lockAppsPrefs.lockAppsConfig.theme != "") {
            // 检验是否设置过密码
            lockTheme = themeUtil.getThemeByName(lockAppsPrefs.lockAppsConfig.theme)
        }
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
        val isRight = lockAppsPrefs.verifyPassword(pwd)
        // 判断密码是否正确
        if (isRight) {
            // 添加历史
            historyPrefs.addHistory(pkgName!!, settingsPrefs.settingsConfig.resetLockModel)
            // 关闭页面
            this.finish()
        }
        return isRight
    }

    /**
     * 监听返回键
     * @Title: onBackPressed
     * @Description: 监听返回键
     * @author qingyi xuelongqy@foxmail.com
     * @date 2017/9/13 15:35
     */
    override fun onBackPressed() {
        //返回桌面
        val homeIntent = Intent(Intent.ACTION_MAIN)
        homeIntent.addCategory(Intent.CATEGORY_HOME)
        this.startActivity(homeIntent)
    }
}