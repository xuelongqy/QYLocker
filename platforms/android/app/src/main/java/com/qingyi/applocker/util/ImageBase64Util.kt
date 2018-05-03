package com.qingyi.applocker.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.Base64
import java.io.ByteArrayOutputStream
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

    /**
     * @Title: drawableToBase64方法
     * @Class: ImageBase64Util
     * @Description: Drawable转Base64
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/5/3 15:53
     * @update_author
     * @update_time
     * @version V1.0
     * @param icon[Drawable] 图标
     * @return [String] Base64图片编码
     * @throws
    */
    fun drawableToBase64(icon: Drawable): String {
        val bitmap = Bitmap
                .createBitmap(
                        icon.intrinsicWidth,
                        icon.intrinsicHeight,
                        Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        icon.setBounds(0, 0, icon.intrinsicWidth,
                icon.intrinsicHeight)
        icon.draw(canvas)
        val size = bitmap.width * bitmap.height * 4

        // 创建一个字节数组输出流,流的大小为size
        val baos = ByteArrayOutputStream(size)
        // 设置位图的压缩格式，质量为100%，并放入字节数组输出流中
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, baos)
        // 将字节数组输出流转化为字节数组byte[]
        val imagedata = baos.toByteArray()

        return "data:image/png;base64," + Base64.encodeToString(imagedata, Base64.DEFAULT)
    }
}