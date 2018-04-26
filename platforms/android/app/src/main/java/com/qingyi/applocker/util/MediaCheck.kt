package com.qingyi.applocker.util

import android.webkit.MimeTypeMap
import java.io.File
import java.util.*

/**
 * @Title: MediaCheck类
 * @Package: 媒体文件判断类
 * @Description: TODO
 * @author XueLong xuelongqy@foxmail.com
 * @date 2017/12/20 14:11
 * @update_author
 * @update_time
 * @version V1.0
*/
object MediaCheck {
    // 判断是否为图片
    fun isImage(file: File): Boolean {
        if (file == null) return false
        return getMimeType(file).contains("image", true)
    }

    // 判断是否为视频
    fun isVideo(file: File): Boolean {
        return getMimeType(file).contains("video", true)
    }

    // 获取文件后缀
    private fun getSuffix(file: File?): String? {
        if (file == null || !file.exists() || file.isDirectory) {
            return null
        }
        val fileName = file.name
        if (fileName == "" || fileName.endsWith(".")) {
            return null
        }
        val index = fileName.lastIndexOf(".")
        return if (index != -1) {
            fileName.substring(index + 1).toLowerCase(Locale.US)
        } else {
            null
        }
    }

    // 获取文件MimeType
    fun getMimeType(file: File): String {
        val suffix = getSuffix(file) ?: return "file/*"
        val type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(suffix)
        return if (type != null || !type!!.isEmpty()) {
            type
        } else "file/*"
    }
}