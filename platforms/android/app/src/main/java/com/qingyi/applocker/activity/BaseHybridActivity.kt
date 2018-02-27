package com.qingyi.applocker.activity

import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import com.qingyi.applocker.util.LoggerUtil
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