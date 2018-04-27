package com.qingyi.applocker.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.qingyi.applocker.bean.ThemeBean
import com.qingyi.applocker.preferences.LockAppsPrefs
import com.qingyi.applocker.util.ThemeUtil

class SetPwdActivity: BaseHybridActivity(true, false) {
    // 伴生对象
    companion object {
        // 类的标识
        val TAG = SetPwdActivity::class.java.simpleName
        // 设置类型
        val SET_PWD_TYPE = "set_pwd_type"
        val SET_PWD = "set_pwd"
        val ADD_PWD = "add_pwd"
        val DEL_PWD = "del_pwd"
        // 主题名字
        val THEME_NAME = "theme_name"
        // 应用包名
        val PKG_NAME = "pkg_name"
        // 设置状态
        val SET_PWD_STATE = "set_pwd_state"
    }

    // 设置类型(设置密码/修改密码)
    private var setPwdType:String? = null
    // 主题名字
    private var themeName:String? = null
    // 应用包名
    private var pkgName:String? = null
    // 页面返回参数
    private val resultIntent = Intent()
    // 加锁应用配置
    lateinit var lockAppsPrefs: LockAppsPrefs
    // 主题工具
    private lateinit var themeUtil:ThemeUtil
    // 主题信息
    private var themeBean:ThemeBean? = null
    // 以前设置加锁的主题
    private var lockTheme:ThemeBean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // enable Cordova apps to be started in the background
        val extras = intent.extras
        if (extras != null && extras.getBoolean("cdvStartInBackground", false)) {
            moveTaskToBack(true)
        }

        // 初始化
        initMethod()

        //加载锁屏界面前端视图
        //loadUrl(launchUrl)
        if (lockTheme == null) {
            // 判断之前是否设置过密码
            setNowPwd()
        }else {
            vaildLastPwd()
        }
    }

    // 初始化函数
    fun initMethod() {
        // 获取参数
        setPwdType = intent.getStringExtra(SET_PWD_TYPE)
        themeName = intent.getStringExtra(THEME_NAME)
        pkgName = intent.getStringExtra(PKG_NAME)
        if (setPwdType == null || themeName == null) {
            this.finish()
        }
        // 设置返回参数
        resultIntent.putExtra(SET_PWD_STATE, true)
        setResult(Activity.RESULT_OK, resultIntent)
        // 初始化主题工具
        themeUtil = ThemeUtil(this)
        // 初始化应用锁配置
        lockAppsPrefs = LockAppsPrefs(this)
        // 获取主题
        themeBean = themeUtil.getThemeByName(themeName!!)
        if (themeBean == null) {
            resultIntent.putExtra(SET_PWD_STATE, false)
        }
        // 获取加锁主题
        if (lockAppsPrefs.lockAppsConfig.theme != "") {
            // 检验是否设置过密码
            lockTheme = themeUtil.getThemeByName(lockAppsPrefs.lockAppsConfig.theme)
        }
    }

    // 验证上一次密码
    fun vaildLastPwd() {
        loadUrl(lockTheme!!.lockPage)
    }
    // 设置密码
    fun setNowPwd() {
        loadUrl(themeBean!!.setPwdPage)
    }
    // 重新验证密码
    fun vauldNowPws() {
        loadUrl(themeBean!!.lockPage)
    }
}