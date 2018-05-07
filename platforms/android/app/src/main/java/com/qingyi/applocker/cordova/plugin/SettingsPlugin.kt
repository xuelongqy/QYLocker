package com.qingyi.applocker.cordova.plugin

import android.app.Activity
import android.app.AlertDialog
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import com.google.gson.GsonBuilder
import com.qingyi.applocker.R
import com.qingyi.applocker.preferences.HistoryPrefs
import com.qingyi.applocker.preferences.SettingsPrefs
import com.qingyi.applocker.receiver.UninstallProtectionReceiver
import com.qingyi.applocker.util.LockerServiceUtil
import com.qingyi.applocker.xposed.XposedUtil
import com.xposed.qingyi.cmprotectedappsplus.constant.ThisApp
import org.apache.cordova.CallbackContext
import org.apache.cordova.CordovaInterface
import org.apache.cordova.CordovaPlugin
import org.apache.cordova.CordovaWebView
import org.json.JSONArray

/**
 * @Title: SettingsPlugin类
 * @Package: com.qingyi.applocker.cordova.plugin
 * @Description: 设置配置插件
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/4/25 10:23
 * @update_author
 * @update_time
 * @version V1.0
 * @exception
*/
class SettingsPlugin : CordovaPlugin() {
    // 伴生对象
    companion object {
        // 类的标识
        val TAG = SettingsPlugin::class.java.simpleName
    }

    // 缓存调用插件的Activity
    private lateinit var mActivity: Activity
    // 加锁应用配置
    private lateinit var settingsPrefs: SettingsPrefs
    // 历史配置
    private lateinit var historyPrefs: HistoryPrefs
    // 应用锁服务工具
    private lateinit var lockerServiceUtil: LockerServiceUtil
    // 系统设备管理器工具
    private lateinit var devicePolicyManager: DevicePolicyManager
    // 设备管理名字
    private lateinit var deviceAdmin: ComponentName
    // Json操作对象
    private val gson = GsonBuilder().create()

    /**
     * 重写初始化插件方法
     * 获取调用插件的Activity
     * @Title: initialize
     * @Description: 重写初始化插件方法
     * @author qingyi xuelongqy@foxmail.com
     * @date 2017/8/21 0:20
     * @param cordova cordova的接口
     * @param webView cordova的Web视图
     */
    override fun initialize(cordova: CordovaInterface?, webView: CordovaWebView?) {
        super.initialize(cordova, webView)
        // 获取调用的Activity
        mActivity = cordova!!.activity

        // 初始化对象
        settingsPrefs = SettingsPrefs(mActivity.applicationContext)
        historyPrefs = HistoryPrefs(mActivity.applicationContext)
        lockerServiceUtil = LockerServiceUtil(mActivity.applicationContext)
        devicePolicyManager = mActivity.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        deviceAdmin = ComponentName(mActivity, UninstallProtectionReceiver::class.java)
    }

    /**
     * @Title: 重写execute方法
     * @Class: SettingsPlugin
     * @Description: App列表相关交互执行
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/2/26 11:17
     * @update_author
     * @update_time
     * @version V1.0
     * @param action 执行动作(对应方法)
     * @param args 方法参数
     * @param callbackContext 回调上下文
     * @return 执行动作是否有效
     */
    override fun execute(action: String?, args: JSONArray?, callbackContext: CallbackContext?): Boolean {
        when (action) {
            // 获取设置的配置信息
            "getSettingsConfig" -> {
                cordova.threadPool.execute {
                    val settingsConfig = SettingsPrefs.SettingsConfig()
                    settingsConfig.clon(settingsPrefs.settingsConfig)
                    // 判断是否启用Xposed模块
                    if (XposedUtil.isXposedActive()) {
                        settingsConfig.lockModel = ThisApp.XPOSED
                    }
                    // 判断是否使用设备管理器
                    settingsConfig.preventUninstall = devicePolicyManager.isAdminActive(deviceAdmin)
                    if (settingsConfig.preventUninstall != settingsPrefs.settingsConfig.preventUninstall) {
                        settingsPrefs.setPreventUninstall(settingsConfig.preventUninstall)
                    }
                    callbackContext!!.success(gson.toJson(settingsConfig))
                    callbackContext.error("{}")
                }
                return true
            }
            // 设置应用锁模式
            "setLockModel" -> {
                cordova.threadPool.execute {
                    // 获取应用锁模式
                    val lockModel = args!!.getString(0)
                    // 不为Xpose模式
                    if (lockModel != ThisApp.XPOSED) {
                        settingsPrefs.setLockModel(args.getString(0))
                        // 关闭之前服务
                        lockerServiceUtil.stopLockerService()
                        // 判断服务权限
                        if (!lockerServiceUtil.havePermission()) {
                            mActivity.runOnUiThread {
                                // 申请权限
                                val lockerServicePermissionsDialog = AlertDialog.Builder(mActivity)
                                        .setTitle(R.string.no_permissions)
                                        .setMessage(R.string.no_locker_Service_permissions)
                                        .setPositiveButton(R.string.settings, { dialog: DialogInterface, which: Int ->
                                            // 申请权限
                                            lockerServiceUtil.requestPermission()
                                            // 启动服务
                                            lockerServiceUtil.startLockerService()
                                            dialog.dismiss()
                                        }).setCancelable(false).create()
                                lockerServicePermissionsDialog.show()
                            }
                        }
                    }
                }
                return true
            }
            // 设置重新锁定模式
            "setResetLockModel" -> {
                cordova.threadPool.execute {
                    settingsPrefs.setResetLockModel(args!!.getString(0))
                    // 清除历史
                    historyPrefs.cleanHistory()
                }
                return true
            }
            // 设置是否使用指纹
            "setUseFingerprint" -> {
                cordova.threadPool.execute {
                    settingsPrefs.setUseFingerprint(args!!.getBoolean(0))
                }
                return true
            }
            // 设置是否启用高级模式
            "setAdvancedMode" -> {
                cordova.threadPool.execute {
                    settingsPrefs.setAdvancedMode(args!!.getBoolean(0))
                }
                return true
            }
            // 设置是否锁定新应用
            "setLockNewApp" -> {
                cordova.threadPool.execute {
                    settingsPrefs.setLockNewApp(args!!.getBoolean(0))
                }
                return true
            }
            // 设置防止卸载
            "setPreventUninstall" -> {
                cordova.threadPool.execute {
                    // 获取是否防止卸载状态
                    val isPreventUninstall = args!!.getBoolean(0)
                    // 设置设备管理器状态
                    if (isPreventUninstall) {
                        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
                        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, deviceAdmin)
                        mActivity.startActivity(intent)
                    }else {
                        devicePolicyManager.removeActiveAdmin(deviceAdmin)
                    }
                    settingsPrefs.setPreventUninstall(isPreventUninstall)
                }
                return true
            }
            // 设置背景图片
            "setBgImageUrl" -> {
                cordova.threadPool.execute {
                    settingsPrefs.setBgImageUrl(args!!.getString(0))
                }
                return true
            }
        }
        return false
    }
}