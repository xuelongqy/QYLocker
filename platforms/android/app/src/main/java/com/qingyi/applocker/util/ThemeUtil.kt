package com.qingyi.applocker.util

import android.content.Context
import android.util.Log
import com.google.gson.GsonBuilder
import com.qingyi.applocker.bean.ThemeBean
import com.xposed.qingyi.cmprotectedappsplus.constant.ThisApp
import java.util.zip.ZipFile
import java.io.*


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
        // 内置主题名字
        val BUILTIN_THEME_LIST = arrayListOf("Pattern","PIN","Password")
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
        var theme = ZipFile(File(filePath))
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
            val themeBean: ThemeBean
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
     * @Title: deleteTheme方法
     * @Class: ThemeUtil
     * @Description: 删除主题
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/4/27 11:10
     * @update_author
     * @update_time
     * @version V1.0
     * @param themeName[String] 主题名称
     * @return
     * @throws
    */
    fun deleteTheme(themeName:String) {
        // 获取当前主题目录
        val themeDir = File(getThemePath() + File.separator + themeName)
        if (themeDir.exists() && themeDir.isDirectory) {
            // 删除主题目录
            try {
                FileUtil.deleteDir(themeDir)
            }catch (e: Exception) {
                LoggerUtil.logAndroid(Log.WARN, "deleteTheme", "Delete theme failure. Exception=${e.localizedMessage}")
            }
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
     * @param themesDir[File] 主题目录
     * @return [ThemeBean] 主题信息
     * @throws
    */
    fun getThemeByName(themeName: String):ThemeBean? {
        // 主题目录
        val themeDir = File(getThemePath() + File.separator + themeName)
        // 判断主题是否存在
        if (!themeDir.exists()) return null
        // 获取主题基本信息
        val themeInfoReader = BufferedReader(InputStreamReader(File(themeDir.absolutePath + File.separator + ThisApp.THEME_INFO_FILE).inputStream()))
        var themeInfo = ""
        var line: String? = null
        while ({line = themeInfoReader.readLine();line}() != null) {
            themeInfo += line
        }
        val themeBean: ThemeBean
        try {
            themeBean = gson.fromJson(themeInfo, ThemeBean::class.java)
        }catch (e: Exception) {
            return null
        }
        // 获取主题图片
        val imageDir = File(themeDir.absolutePath + File.separator + ThisApp.THEME_IMAGE)
        val imageFiles = imageDir.listFiles { file->
            MediaCheck.isImage(file)
        }
        for (image in imageFiles) {
            themeBean.images.add(ImageBase64Util.imageToBase64(image.absolutePath))
        }
        // 转换页面地址
        themeBean.lockPage = if (themeBean.lockPage.contains("http://") || themeBean.lockPage.contains("https://"))
            themeBean.lockPage
        else
            "file:" + themeDir.absolutePath + File.separator + ThisApp.THEME_WEBAPP + File.separator + themeBean.lockPage
        themeBean.setPwdPage = if (themeBean.setPwdPage.contains("http://") || themeBean.setPwdPage.contains("https://"))
            themeBean.setPwdPage
        else
            "file:" + themeDir.absolutePath + File.separator + ThisApp.THEME_WEBAPP + File.separator + themeBean.setPwdPage
        // 判断是否为默认主题
        if (BUILTIN_THEME_LIST.contains(themeName)) themeBean.isBuiltIn = false
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
            var theme: ThemeBean? = null
            try {
                theme = getThemeByName(themeDir.absolutePath.replace(getThemePath(), ""))
                if (theme != null) {
                    themeList.add(theme)
                }
            }catch (e: Exception) {
                LoggerUtil.logAndroid(Log.WARN, "getThemeList", "Get theme from{${themeDir.absolutePath}} failure. Exception=${e.localizedMessage}")
            }
        }
        return themeList
    }

    /**
     * @Title: setThemeData方法
     * @Class: ThemeUtil
     * @Description: 设置主题额外数据
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/5/2 11:24
     * @update_author
     * @update_time
     * @version V1.0
     * @param themeName[String] 主题名称
     * @return
     * @throws
    */
    fun setThemeData(themeName: String, data: String) {
        // 主题目录
        val themeDir = File(getThemePath() + File.separator + themeName)
        // 判断主题是否存在
        if (!themeDir.exists()) return
        // 将数据写入文件
        val fileOutputStream: FileOutputStream
        var bufferedWriter: BufferedWriter? = null
        try {
            /**
             * "data"为文件名,MODE_PRIVATE表示如果存在同名文件则覆盖，
             * 还有一个MODE_APPEND表示如果存在同名文件则会往里面追加内容
             */
            fileOutputStream = File(themeDir.absolutePath + File.separator + ThisApp.THEME_DATA_FILE).outputStream()
            bufferedWriter = BufferedWriter(
                    OutputStreamWriter(fileOutputStream))
            bufferedWriter.write(data)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * @Title: getThemeData方法
     * @Class: ThemeUtil
     * @Description: 主题获取额外数据
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/5/2 11:25
     * @update_author
     * @update_time
     * @version V1.0
     * @param themeName[String] 主题名称
     * @return
     * @throws
    */
    fun getThemeData(themeName: String):String {
        // 数据文件
        val dataFile = File(getThemePath() + File.separator + themeName + File.separator + ThisApp.THEME_DATA_FILE)
        // 主题数据
        var themeData = ""
        // 判断文件是否存在
        if (!dataFile.exists()) return themeData
        // 获取主题数据
        try {
            val themeDataReader = BufferedReader(InputStreamReader(dataFile.inputStream()))
            var line: String? = null
            while ({line = themeDataReader.readLine();line}() != null) {
                themeData += line
            }
        }catch (e: Exception) {
            LoggerUtil.logAndroid(Log.WARN, "getThemeData", "Exception = ${e.localizedMessage}")
        }
        return themeData
    }
}