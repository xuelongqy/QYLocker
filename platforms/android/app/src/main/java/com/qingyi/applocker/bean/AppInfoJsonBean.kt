package com.qingyi.applocker.bean

/**
 * 用于json存储App信息实体类
 * appName: 应用名
 * packageName: 包名
 * versionName: 版本名
 * versionCode: 版本号
 * appIcon: 应用图标地址
 * isSystemAPP: 是否为系统应用
 * isLock 是否上锁
 * isIndependent 是否独立设置
 * theme 主题(独立设置时有效)
 * password 密码(独立设置时有效)
 * @ClassName: AppInfoJsonBean
 * @Description: 用于json存储App信息实体类
 * @author qingyi xuelongqy@foxmail.com
 * @date 2017/9/1 17:21
 */
data class AppInfoJsonBean(var appName: String = "",
                           var packageName: String = "",
                           var versionName: String = "",
                           var versionCode: Int = 0,
                           var appIcon: String = "",
                           var isSystemAPP: Boolean = false,
                           var isLock: Boolean = false,
                           var isIndependent: Boolean = false,
                           var theme: String = "",
                           var password: String = "")