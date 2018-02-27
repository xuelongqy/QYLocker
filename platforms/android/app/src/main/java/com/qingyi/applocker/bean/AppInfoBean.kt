package com.qingyi.applocker.bean

import android.graphics.drawable.Drawable

/**
 * App信息实体类
 * appName: 应用名
 * packageName: 包名
 * versionName: 版本名
 * versionCode: 版本号
 * appIcon: 应用图标
 * isSystemAPP: 是否为系统应用
 * @ClassName: AppInfoBean
 * @Description: 这里用一句话描述这个类的作用
 * @author qingyi xuelongqy@foxmail.com
 * @date 2017/8/14 0:03
 */
data class AppInfoBean(val appName: String,
                       val packageName: String,
                       val versionName: String,
                       val versionCode: Int,
                       val appIcon: Drawable,
                       val isSystemAPP: Boolean)