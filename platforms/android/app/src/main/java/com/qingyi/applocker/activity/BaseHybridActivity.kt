package com.qingyi.applocker.activity

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import com.qingyi.applocker.R
import com.tbruyelle.rxpermissions2.RxPermissions
import org.apache.cordova.CordovaActivity

/**
 * 基础的混合模式Activity
 * 封装一些通用的方法和变量
 * @ClassName: BaseHybridActivity
 * @Description: 基础的混合模式Activity
 * @author qingyi xuelongqy@foxmail.com
 * @date 2017/8/19 22:06
 */
open class BaseHybridActivity(): CordovaActivity() {
    //状态栏透明
    private var transparent_StatusBar:Boolean = false
    //导航栏透明
    private var transparent_NavigationBar:Boolean = false
    // 前往权限设置弹窗
    lateinit var goSettingsDialog: AlertDialog

    constructor(transparent_StatusBar: Boolean, transparent_NavigationBar: Boolean) : this() {
        this.transparent_StatusBar = transparent_StatusBar
        this.transparent_NavigationBar = transparent_NavigationBar
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //初始化
        initMethod()
    }

    /**
     * 初始化方法
     * @Title: initMethod
     * @Description: 初始化方法
     * @author qingyi xuelongqy@foxmail.com
     * @date 2017/8/19 23:43
     */
    private fun initMethod() {
        //设置状态栏和导航栏
        setStatusBarAndNavigationBar()
        // 创建前往权限设置弹窗
        goSettingsDialog = AlertDialog.Builder(this)
                .setTitle(R.string.no_permissions)
                .setMessage(R.string.set_permissions)
                .setPositiveButton(R.string.settings, { dialog: DialogInterface, which: Int ->
                    val packageURI = Uri.parse("package:" + application.packageName)
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI)
                    startActivity(intent)
                    dialog.dismiss()
                }).setCancelable(false).create()
    }

    /**
     * @Title: 重写onStart方法
     * @Class: BaseHybridActivity
     * @Description: 当页面启动时
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/4/26 11:11
     * @update_author
     * @update_time
     * @version V1.0
     * @param
     * @return
     * @throws
    */
    override fun onStart() {
        // 申请权限
        requestPermissions()
        super.onStart()
    }

    /**
     * @Title: requestPermissions方法
     * @Class: BaseHybridActivity
     * @Description: 设置权限
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/4/26 11:10
     * @update_author
     * @update_time
     * @version V1.0
     * @param
     * @return
     * @throws
    */
    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return
        val rxPermissions = RxPermissions(this)
        rxPermissions
                .request(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe { granted ->
                    if (granted) {
                        // 所有权限申请成功
                    } else {
                        // 权限申请失败,跳转设置页面
                        goSettingsDialog.show()
                    }
                }
    }

    /**
     * 设置状态栏和导航栏
     * @Title: setStatusBarAndNavigationBar
     * @Description: 设置状态栏和导航栏
     * @author qingyi xuelongqy@foxmail.com
     * @date 2017/8/19 23:44
     */
    private fun setStatusBarAndNavigationBar(){
        val window = window
        if (transparent_StatusBar){
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.statusBarColor = Color.TRANSPARENT
        }
        if (transparent_NavigationBar){
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.navigationBarColor = Color.TRANSPARENT
        }
    }

    fun getTransparent_StatusBar(): Boolean{
        return transparent_StatusBar
    }

    fun getTransparent_NavigationBar(): Boolean {
        return transparent_NavigationBar
    }
}