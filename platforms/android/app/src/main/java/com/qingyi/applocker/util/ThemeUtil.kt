package com.qingyi.applocker.util

import android.content.Context
import android.util.Log
import android.webkit.MimeTypeMap
import com.google.gson.GsonBuilder
import com.qingyi.applocker.bean.ThemeBean
import com.xposed.qingyi.cmprotectedappsplus.constant.ThisApp
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.zip.ZipFile

/**
 * @Title: ThemeUtil类
 * @Package: com.qingyi.applocker.util
 * @Description: 主题工具类
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/4/26 12:19
 * @update_author
 * @update_time
 * @version V1.0
 * @exception
*/
class ThemeUtil(val context: Context) {
    // 伴生对象
    companion object {
        // 类的标识
        val TAG = ThemeUtil::class.java.simpleName
    }

    // Json操作对象
    private val gson = GsonBuilder().create()

    /**
     * @Title: importTheme方法
     * @Class: ThemeUtil
     * @Description: 导入主题
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/4/26 12:33
     * @update_author
     * @update_time
     * @version V1.0
     * @param filePath[String] 文件路径
     * @return
     * @throws
    */
    fun importTheme(filePath: String):Boolean {
        // 获取主题文件
        var theme = ZipFile(filePath)
        // 验证目录结构
        val themeJsonEntry = theme.getEntry(ThisApp.THEME_INFO_FILE)
        val webappEntry = theme.getEntry(ThisApp.THEME_WEBAPP)
        val image = theme.getEntry(ThisApp.THEME_IMAGE)
        if (themeJsonEntry != null && webappEntry != null && image != null) {
            LoggerUtil.logAndroid(Log.INFO, "importTheme", "is theme file")
            // 获取主题信息
            val themeInfoReader = BufferedReader(InputStreamReader(theme.getInputStream(themeJsonEntry)))
            var themeInfo = ""
            var line: String? = null
            while ({line = themeInfoReader.readLine();line}() != null) {
                themeInfo += line
            }
            var themeBean: ThemeBean
            try {
                themeBean = gson.fromJson(themeInfo, ThemeBean::class.java)
                LoggerUtil.logAndroid(Log.INFO, "importTheme", "ThemeName = ${themeBean.name}")
            }catch (e: Exception) {
                LoggerUtil.logAndroid(Log.INFO, "importTheme", "Exception = ${e.localizedMessage}")
                return false
            }
            // 解压主题到指定目录
            ZipUtil.upZipFileDir(theme, getThemePath() + File.separator + themeBean.name)
            return true
        }else {
            LoggerUtil.logAndroid(Log.INFO, "importTheme", "is not theme file")
            return false
        }
    }

    /**
     * @Title: getThemePath方法
     * @Class: ThemeUtil
     * @Description: 获取主题目录
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/4/26 15:11
     * @update_author
     * @update_time
     * @version V1.0
     * @return [String] 主题目录
     * @throws
    */
    fun getThemePath():String {
        return context.filesDir.absolutePath + File.separator + ThisApp.THEME_PATH
    }

    /**
     * @Title: getThemeByName方法
     * @Class: ThemeUtil
     * @Description: 通过名字获取主题
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/4/26 15:18
     * @update_author
     * @update_time
     * @version V1.0
     * @param themeName[String] 主题名字
     * @return [ThemeBean] 主题信息
     * @throws
    */
    fun getThemeByName(themeName: String):ThemeBean? {
        // 判断主题是否存在
        val themeDir = File(getThemePath() + File.separator + themeName)
        if (!themeDir.exists()) return null
        // 获取主题基本信息
        val themeInfoReader = BufferedReader(InputStreamReader(File(themeDir.absolutePath + File.separator + ThisApp.THEME_INFO_FILE).inputStream()))
        var themeInfo = ""
        var line: String? = null
        while ({line = themeInfoReader.readLine();line}() != null) {
            themeInfo += line
        }
        var themeBean: ThemeBean
        try {
            themeBean = gson.fromJson(themeInfo, ThemeBean::class.java)
        }catch (e: Exception) {
            return null
        }
        // 获取主题图片
        val imageDir = File(themeDir.absolutePath + File.separator + ThisApp.THEME_IMAGE)
        val imageFiels = imageDir.listFiles { file->
            MediaCheck.isImage(file)
        }
        for (image in imageFiels) {
            themeBean.imageUrl.add(image.absolutePath)
        }
        // 转换页面地址
        themeBean.lockPage = themeDir.absolutePath + File.separator + themeBean.lockPage
        themeBean.setPwdPage = themeDir.absolutePath + File.separator + themeBean.setPwdPage
        return themeBean
    }

    /**
     * @Title: getThemeList方法
     * @Class: ThemeUtil
     * @Description: 获取主题列表
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/4/26 16:07
     * @update_author
     * @update_time
     * @version V1.0
     * @return [ArrayList<ThemeBean>] 主题列表
     * @throws
    */
    fun getThemeList():ArrayList<ThemeBean> {
        // 缓存主题列表
        val themeList = arrayListOf<ThemeBean>()
        // 主题目录
        val themesDir = File(getThemePath())
        // 判断是否存在主题
        if (!themesDir.exists()) {
            return themeList
        }
        // 获取主题
        val themeDirs = themesDir.listFiles { file ->
            file.isDirectory
        }
        for (themeDir in themeDirs) {
            themeList.add(getThemeByName(themeDir.absolutePath.replace(getThemePath(), ""))!!)
        }
        return themeList
    }
}