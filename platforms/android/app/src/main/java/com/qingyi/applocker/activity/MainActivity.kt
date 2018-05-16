package com.qingyi.applocker.activity

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import com.arialyy.aria.core.Aria
import com.qingyi.applocker.R
import com.qingyi.applocker.preferences.LockAppsPrefs
import com.qingyi.applocker.util.LockerServiceUtil
import com.qingyi.applocker.util.ThemeUtil
import com.arialyy.aria.core.download.DownloadTask
import com.arialyy.annotations.Download
import com.qingyi.applocker.util.LoggerUtil
import org.apache.cordova.CallbackContext
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*

/**
 * 主界面Activity
 * @ClassName: MainActivity
 * @Description: 主界面Activity
 * @author qingyi xuelongqy@foxmail.com
 * @date 2017/9/13 15:00
 */
class MainActivity : BaseHybridActivity(true, false, true) {
    // 伴生对象
    companion object {
        // 类的标识
        val TAG = MainActivity::class.java.simpleName
        val LOCK_PAGE_REQUEST_CODE = 0x1313
        val UNLOCK_MAIN = "unlock_main"
    }

    // 加锁应用配置
    private lateinit var lockAppsPrefs: LockAppsPrefs
    // 主题工具
    private lateinit var themeUtil: ThemeUtil
    // 应用锁服务工具
    private lateinit var lockerServiceUtil: LockerServiceUtil
    // 是否解锁此软件
    private var isUnlockThis = false
    // 缓存下载回调
    private var downloadCallbackContext: CallbackContext? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // enable Cordova apps to be started in the background
        val extras = intent.extras
        if (extras != null && extras.getBoolean("cdvStartInBackground", false)) {
            moveTaskToBack(true)
        }

        // 加载视图
        // Set by <content src="cordova_index.html" /> in config.xml
        loadUrl(launchUrl)
        // loadUrl("http://192.168.1.113:8080")
        //loadUrl("http://192.168.43.144:8080")

        // 注册Aria下载
        Aria.download(this).register()
        // 初始化操作
        initMethod()
        // 导入自带主题
        importAssetsTheme()
    }

    // 初始化方法
    private fun initMethod(){
        // 初始变量
        lockAppsPrefs = LockAppsPrefs(this)
        themeUtil = ThemeUtil(this)
        // 应用锁服务工具
        lockerServiceUtil = LockerServiceUtil(this.applicationContext)
         // lockerServiceUtil.startUsageStatsLockerService()
         // lockerServiceUtil.startAccessibilityLockerService()
    }

    /**
     * @Title: importAssetsTheme方法
     * @Class: MainActivity
     * @Description: 导入assets中的主题
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/5/16 2:25
     * @update_author
     * @update_time
     * @param
     * @return
     * @throws
     * @version V1.0
    */
    private fun importAssetsTheme() {
        object : Thread() {
            override fun run() {
                // 获取所有主题
                val assetsThemeList = assets.list("theme")
                for (assetsTheme in assetsThemeList) {
                    // 判断主题是否存在
                    if (themeUtil.getThemeByName(assetsTheme) == null) {
                        // 复制文件到缓存
                        try {
                            val cacheFile = File(cacheDir.absolutePath + File.separator + assetsTheme)
                            val bin = assets.open("theme/$assetsTheme")
                            val bout = FileOutputStream(cacheFile)
                            val buffer = ByteArray(1024)
                            var length = 0
                            while ({length = bin.read(buffer);length}() > 0) {
                                bout.write(buffer, 0, length)
                            }
                            // 导入主题
                            themeUtil.importTheme(cacheFile)
                            // 删除缓存文件
                            cacheFile.delete()
                        }catch (e: Exception) {
                            LoggerUtil.logAndroid(Log.INFO, "importAssetsTheme",e.localizedMessage)
                        }
                    }
                }
            }
        }.start()
    }

    /**
     * @Title: 重写onStart方法
     * @Class: MainActivity
     * @Description: TODO
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/5/4 9:51
     * @update_author
     * @update_time
     * @version V1.0
     * @param
     * @return
     * @throws
    */
    override fun onStart() {
        // 主程序启动密码
        if (!isUnlockThis &&
                lockAppsPrefs.lockAppsConfig.theme != ""
                && lockAppsPrefs.lockAppsConfig.password != ""
                && themeUtil.getThemeByName(lockAppsPrefs.lockAppsConfig.theme) != null) {
            // 当设置主题和密码后启动解锁页面
            val intent = Intent(this, AppLockActivity::class.java)
            intent.putExtra(AppLockActivity.PKG, this.packageName)
            intent.putExtra(AppLockActivity.ACT, this::class.java.name)
            this.startActivityForResult(intent, LOCK_PAGE_REQUEST_CODE)
        }else {
            isUnlockThis = true
        }
        // 判断服务权限
        if (isUnlockThis && !lockerServiceUtil.havePermission()) {
            // 申请权限
            val lockerServicePermissionsDialog = AlertDialog.Builder(this)
                    .setTitle(R.string.no_permissions)
                    .setMessage(R.string.no_locker_Service_permissions)
                    .setPositiveButton(R.string.settings, { dialog: DialogInterface, which: Int ->
                        // 申请权限
                        lockerServiceUtil.requestPermission()
                        // 启动服务
                        // lockerServiceUtil.startLockerService()
                        dialog.dismiss()
                    }).setCancelable(false).create()
            lockerServicePermissionsDialog.show()
        }
        // 判断服务是否启动
        else if (lockerServiceUtil.isLockerServiceStart()) {
            // 启动服务
            lockerServiceUtil.startLockerService()
        }
        super.onStart()
    }

    /**
     * @Title: 重写onActivityResult方法
     * @Class: MainActivity
     * @Description: 获取页面返回参数
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/5/4 10:12
     * @update_author
     * @update_time
     * @version V1.0
     * @param
     * @return
     * @throws
    */
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        when (requestCode) {
            // 获取解锁界面状态
            LOCK_PAGE_REQUEST_CODE -> {
                if (resultCode == RESULT_OK) {
                    // 获取解锁状态
                    isUnlockThis = intent!!.getBooleanExtra(UNLOCK_MAIN, false)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, intent)
    }

    /**
     * @Title: 重写onDestroy方法
     * @Class: MainActivity
     * @Description: 页面销毁时
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/5/16 1:03
     * @update_author
     * @update_time
     * @param
     * @return
     * @throws
     * @version V1.0
    */
    override fun onDestroy() {
        // 注销Aria下载
        Aria.download(this).unRegister()
        super.onDestroy()
    }

    /**
     * @Title: downloadTheme方法
     * @Class: MainActivity
     * @Description: 下载主题
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/5/16 1:12
     * @update_author
     * @update_time
     * @param downloadUrl[String] 下载地址
     * @param downloadCallbackContext[CallbackContext] 下载回调
     * @return
     * @throws
     * @version V1.0
    */
    fun downloadTheme(downloadUrl: String, downloadCallbackContext: CallbackContext) {
        runOnUiThread {
            this.downloadCallbackContext = downloadCallbackContext
        }
        // 判断是否有任务
        if (Aria.download(this).allNotCompletTask != null && !Aria.download(this).allNotCompletTask.isEmpty()) {
            if (downloadCallbackContext != null) {
                downloadCallbackContext.success(false.toString())
            }else {
                runOnUiThread {
                    Toast.makeText(this, R.string.downloadFailure, Toast.LENGTH_LONG).show()
                }
            }
            return
        }
        // 获取下载目录
        val downloadPath = Environment.getExternalStorageDirectory().absolutePath + File.separator + "Download" + File.separator + "QYLocker-Theme-" + Date().time + ".zip"
        // 下载文件
        runOnUiThread {
            try {
                Aria.download(this).load(downloadUrl).setFilePath(downloadPath).start()
            }catch (e: Exception) {
                LoggerUtil.logAndroid(Log.WARN, "$TAG.downloadTheme", e.localizedMessage)
                if (downloadCallbackContext != null) {
                    downloadCallbackContext.success(false.toString())
                }else {
                    runOnUiThread {
                        Toast.makeText(this, R.string.downloadFailure, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    /**
     * @Title: ariaTaskComplete方法
     * @Class: MainActivity
     * @Description: ariaTaskComplete下载完成方法
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/5/16 1:05
     * @update_author
     * @update_time
     * @param
     * @return
     * @throws
     * @version V1.0
    */
    @Download.onTaskComplete
    fun ariaTaskComplete(task: DownloadTask) {
        //在这里处理任务完成的状态
        LoggerUtil.logAndroid(Log.INFO, "ariaTaskComplete", task.downloadPath)
        if (downloadCallbackContext != null) {
            try {
                downloadCallbackContext!!.success(themeUtil.importTheme(task.downloadPath).toString())
                // 删除主题文件
                // File(task.downloadPath).delete()
            }catch (e: Exception) {
                LoggerUtil.logAndroid(Log.WARN, "$TAG.ariaTaskComplete", e.localizedMessage)
                runOnUiThread {
                    Toast.makeText(this, R.string.downloadFailure, Toast.LENGTH_LONG).show()
                }
            }
        }else {
            runOnUiThread {
                Toast.makeText(this, R.string.downloadSuccess, Toast.LENGTH_LONG).show()
            }
        }
    }
}
