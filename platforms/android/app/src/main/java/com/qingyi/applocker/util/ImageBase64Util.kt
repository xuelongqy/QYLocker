package com.qingyi.applocker.util

import android.text.TextUtils
import android.util.Base64
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream

/**
 * @Title: ImageBase64Util类
 * @Class: ImageBase64Util
 * @Description: 图片Base64编码工具类
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/4/27 9:53
 * @update_author
 * @update_time
 * @version V1.0
 * @param
 * @return
 * @throws
*/
object ImageBase64Util {
    /**
     * @Title: imageToBase64方法
     * @Class: ImageBase64Util
     * @Description: 图片文件转Base64
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/4/27 9:54
     * @update_author
     * @update_time
     * @version V1.0
     * @param
     * @return
     * @throws
    */
    fun imageToBase64(path: String): String {
        if (TextUtils.isEmpty(path)) {
            return ""
        }
        var `is`: InputStream? = null
        var data: ByteArray? = null
        var result = ""
        try {
            `is` = FileInputStream(path)
            //创建一个字符流大小的数组。
            data = ByteArray(`is`.available())
            //写入数组
            `is`.read(data)
            //用默认的编码格式进行编码
            result = "data:image/png;base64," + Base64.encodeToString(data, Base64.DEFAULT)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (null != `is`) {
                try {
                    `is`.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return result
    }
}