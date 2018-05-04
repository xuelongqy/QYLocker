package com.qingyi.applocker.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.qingyi.applocker.bean.ThemeBean
import com.qingyi.applocker.preferences.LockAppsPrefs
import com.qingyi.applocker.util.LoggerUtil
import com.qingyi.applocker.util.ThemeUtil
import android.widget.Toast
import android.widget.EditText
import com.qingyi.applocker.R


class SetPwdActivity: BaseHybridActivity(true, false) {
    // 伴生对象
    companion object {
        // 类的标识
        val TAG = SetPwdActivity::class.java.simpleName
        // 是否为添加密码
        val IS_APP_ADD_PWD = "is_app_add_pwd"
        // 主题名字
        val THEME_NAME = "theme_name"
        // 应用包名
        val PKG_NAME = "pkg_name"
        // 设置状态
        val SET_PWD_STATE = "set_pwd_state"
    }

    // 主题名字
    private var themeName:String? = null
    private var isAppAddPwd = false
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
    // 是否为验证密码
    private var isVerifyPassword = true

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
        if (isAppAddPwd) {
            // 判断是否为添加密码
            setNowPwd()
        }
        else if (lockTheme == null) {
            // 判断之前是否设置过密码
            setNowPwd()
        }else {
            vaildLastPwd()
        }
    }

    // 初始化函数
    private fun initMethod() {
        // 获取参数
        themeName = intent.getStringExtra(THEME_NAME)
        isAppAddPwd = intent.getBooleanExtra(IS_APP_ADD_PWD, false)
        pkgName = intent.getStringExtra(PKG_NAME)
        if (themeName == null) {
            this.finish()
        }
        // 设置返回参数
        resultIntent.putExtra(SET_PWD_STATE, false)
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
    private fun vaildLastPwd() {
        isVerifyPassword = true
        loadUrl(lockTheme!!.lockPage)
    }
    // 设置密码
    private fun setNowPwd() {
        isVerifyPassword = false
        loadUrl(themeBean!!.setPwdPage)
    }
    // 取消设置
    fun cancelSetPwd() {
        resultIntent.putExtra(SET_PWD_STATE, false)
        this.finish()
    }

    /**
     * @Title: setThemeAndPwd方法
     * @Class: SetPwdActivity
     * @Description: 设置主题和密码
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/4/28 14:56
     * @update_author
     * @update_time
     * @version V1.0
     * @param pwd[String] 密码
     * @return
     * @throws
    */
    fun setThemeAndPwd(pwd:String) {
        try {
            // 判断是否为为应用添加密码
            if (isAppAddPwd) {
                // 提示输入增加密码的名字
                val editText = EditText(this@SetPwdActivity)
                val inputDialog = AlertDialog.Builder(this@SetPwdActivity)
                        .setTitle(getString(R.string.input_pwd_name)).setView(editText)
                        .setPositiveButton(getString(R.string.ok),null)
                        .setNegativeButton(getString(R.string.cancel),null).create()
                // 设置窗口监听事件
                inputDialog.setOnShowListener({
                    val positionButton = inputDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    val negativeButton = inputDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                    positionButton.setOnClickListener({
                        // 判断名字是否为空
                        if (editText.text.toString() == "") {
                            Toast.makeText(this@SetPwdActivity,
                                    getString(R.string.not_empty),
                                    Toast.LENGTH_SHORT).show()
                        }
                        else {
                            // 判断名字是否存在
                            var nameExists = false
                            if (editText.text.toString() == getString(R.string.master_pwd)) nameExists = true
                            for (theme in lockAppsPrefs.lockAppsConfig.lockApps[pkgName!!]!!.themes) {
                                if (theme.name == editText.text.toString()) nameExists = true
                            }
                            if (nameExists) {
                                Toast.makeText(this@SetPwdActivity,
                                        getString(R.string.name_exists),
                                        Toast.LENGTH_SHORT).show()
                            }else {
                                // 添加密码
                                lockAppsPrefs.addAppPwd(pkgName!!, editText.text.toString(), themeName!!, pwd)
                                resultIntent.putExtra(SET_PWD_STATE, true)
                                inputDialog.dismiss()
                                this@SetPwdActivity.finish()
                            }
                        }
                    })
                    negativeButton.setOnClickListener({
                        // 取消设置
                        resultIntent.putExtra(SET_PWD_STATE, false)
                        inputDialog.dismiss()
                        this@SetPwdActivity.finish()
                    })
                })
                inputDialog.show()
            }else {
                lockAppsPrefs.setThemeAndPwd(themeName!!, pwd)
            }
        }catch (e: Exception) {
            // 设置失败
            LoggerUtil.logAndroid(Log.WARN, "$TAG-setThemeAndPwd", "Exception=${e.localizedMessage}")
            resultIntent.putExtra(SET_PWD_STATE, false)
            this.finish()
        }
    }

    /**
     * @Title: verifyPassword方法
     * @Class: SetPwdActivity
     * @Description: 验证密码
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/4/28 15:57
     * @update_author
     * @update_time
     * @version V1.0
     * @param pwd[String] 密码
     * @return [Boolean] 密码是否正确
     * @return
     * @throws
    */
    fun verifyPassword(pwd:String):Boolean {
        val isRight = lockAppsPrefs.verifyPassword(pwd)
        // 判断密码是否正确
        if (isRight) {
            setNowPwd()
        }
        return isRight
    }

    /**
     * @Title: getThemeData方法
     * @Class: SetPwdActivity
     * @Description: 获取主题额外数据
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/5/2 13:43
     * @update_author
     * @update_time
     * @version V1.0
     * @return [String] 主题数据
     * @throws
    */
    fun getThemeData():String {
        // 判断是否为验证密码
        if (isVerifyPassword) {
            return themeUtil.getThemeData(lockTheme!!.name)
        }else {
            return themeUtil.getThemeData(themeBean!!.name)
        }
    }

    /**
     * @Title: setThemeData方法
     * @Class: SetPwdActivity
     * @Description: 设置主题额外数据
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/5/2 13:47
     * @update_author
     * @update_time
     * @version V1.0
     * @param data[String] 数据
     * @return
     * @throws
    */
    fun setThemeData(data: String) {
        if (!isVerifyPassword) {
            themeUtil.setThemeData(themeBean!!.name, data)
        }
    }
}