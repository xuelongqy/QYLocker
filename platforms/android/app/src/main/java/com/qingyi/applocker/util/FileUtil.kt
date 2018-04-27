package com.qingyi.applocker.util

import java.io.File

/**
 * @Title: FileUtil类
 * @Package: com.qingyi.applocker.util
 * @Description: 文件工具类
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/4/27 11:31
 * @update_author
 * @update_time
 * @version V1.0
 * @exception
*/
object FileUtil {
    /**
     * @Title: deleteDir方法
     * @Class: FileUtil
     * @Description: 删除文件夹
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/4/27 11:31
     * @update_author
     * @update_time
     * @version V1.0
     * @param
     * @return
     * @throws
    */
    fun deleteDir(root:File) {
        if (!root.exists() || !root.isDirectory) return
        val files = root.listFiles()
        if (files != null) {
            for (f in files) {
                if (f.isDirectory) { // 判断是否为文件夹
                    deleteDir(f)
                } else {
                    if (f.exists()) { // 判断是否存在
                        f.delete()
                    }
                }
            }
        }
        root.delete()
    }
}