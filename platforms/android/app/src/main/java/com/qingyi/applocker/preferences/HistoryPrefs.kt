package com.qingyi.applocker.preferences

import android.content.Context
import android.content.SharedPreferences
import com.crossbowffs.remotepreferences.RemotePreferences
import com.google.gson.GsonBuilder
import com.xposed.qingyi.cmprotectedappsplus.constant.ThisApp

/**
 * @Title: HistoryPrefs类
 * @Package: com.qingyi.applocker.preferences
 * @Description: 历史配置文件(记录历史打开应用)
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/5/1 21:51
 * @update_author
 * @update_time
 * @exception
 * @version V1.0
*/
class HistoryPrefs(val context: Context) {
    // 伴生对象
    companion object {
        // 类的标识
        val TAG = HistoryPrefs::class.java.simpleName
        // 设置配置的Json
        private var cHistoryJson: String? = null
        // 设置配置对象
        private var cHistoryConfig: HistoryConfig? = null
        // 获取配置对象
        fun getHistoryConfig(context: Context): HistoryConfig {
            val prefs: SharedPreferences = RemotePreferences(context,
                    ThisApp.PREFERENCE_PROVIDER_AUTHORITY,
                    ThisApp.PREFS_HISTORY)
            val preJson = prefs.getString(ThisApp.PREFS_HISTORY_KEY, null)
            if (preJson == null) {
                return HistoryConfig()
            }else {
                return GsonBuilder().create().fromJson(preJson, HistoryConfig::class.java)
            }
        }
        // 设置配置对象
        fun setHistoryConfig(context: Context, historyConfig: HistoryConfig) {
            val prefs: SharedPreferences = RemotePreferences(context,
                    ThisApp.PREFERENCE_PROVIDER_AUTHORITY,
                    ThisApp.PREFS_HISTORY)
            val editor: SharedPreferences.Editor = prefs.edit()
            editor.putString(ThisApp.PREFS_HISTORY_KEY, GsonBuilder().create().toJson(historyConfig))
            editor.apply()
        }
    }

    // 获取加锁应用配置
    private val prefs: SharedPreferences = RemotePreferences(context,
            ThisApp.PREFERENCE_PROVIDER_AUTHORITY,
            ThisApp.PREFS_HISTORY, true)
    // Json操作对象
    private val gson = GsonBuilder().create()

    // 设置配置的Json
    var historyJson: String?
        get() {
            if (cHistoryConfig == null) {
                update()
            }
            return cHistoryJson
        }
        set(value) {// 设置并同步配置
            val editor: SharedPreferences.Editor = prefs.edit()
            editor.putString(ThisApp.PREFS_HISTORY_KEY, value)
            editor.apply()
            cHistoryJson = value
        }

    // 设置配置对象
    var historyConfig: HistoryConfig
        get() {
            if (cHistoryConfig == null) {
                update()
            }
            return cHistoryConfig!!
        }
        set(value) {
            cHistoryConfig = value
        }

    // 对象初始化
    init {
        // 获取配置
        if (cHistoryJson == null) {
            historyJson
        }else {
            historyConfig = gson.fromJson(cHistoryJson, HistoryConfig::class.java)
        }
    }

    /**
     * @Title: update方法
     * @Class: LockAppsPrefs
     * @Description: 更新配置
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/3/2 10:20
     * @update_author
     * @update_time
     * @version V1.0
     * @param
     * @return
     * @throws
     */
    fun update() {
        cHistoryJson = prefs.getString(ThisApp.PREFS_HISTORY_KEY, null)
        if (cHistoryJson == null) {
            historyConfig = HistoryConfig()
            historyJson = gson.toJson(historyConfig)
        }else {
            historyConfig = gson.fromJson(cHistoryJson, HistoryConfig::class.java)
        }
    }

    /**
     * @Title: addHistory方法
     * @Class: HistoryPrefs
     * @Description: 添加应用历史
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/5/1 22:04
     * @update_author
     * @update_time
     * @param pkgName[String] 应用包名
     * @param reLockMode[String] 重新锁定模式
     * @return
     * @throws
     * @version V1.0
    */
    fun addHistory(pkgName: String, reLockMode: String) {
        // 如果是每次都需要解锁,则只记录一个历史
        if (reLockMode == ThisApp.ALL_TO_ALL){
            historyConfig.history = arrayListOf(pkgName)
        }else {
            if (!historyConfig.history.contains(pkgName)) historyConfig.history.add(pkgName)
        }
        historyJson = gson.toJson(historyConfig)
    }

    /**
     * @Title: cleanHistory方法
     * @Class: HistoryPrefs
     * @Description: 清除历史应用
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/5/1 22:35
     * @update_author
     * @update_time
     * @param
     * @return
     * @throws
     * @version V1.0
    */
    fun cleanHistory() {
        historyConfig.history = arrayListOf()
        historyJson = gson.toJson(historyConfig)
    }

    /**
     * @Title: HistoryConfig类
     * @Package: com.qingyi.applocker.preferences
     * @Description: 历史配置对象
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/5/1 21:56
     * @update_author
     * @update_time
     * @exception
     * @version V1.0
    */
    data class HistoryConfig(
            var history: ArrayList<String> = arrayListOf()
    )
}