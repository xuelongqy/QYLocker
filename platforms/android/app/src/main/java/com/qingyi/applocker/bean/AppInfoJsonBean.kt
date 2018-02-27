package com.qingyi.applocker.bean

/**
 * 用于json存储App信息实体类
 * appName: 应用名
 * packageName: 包名
 * versionName: 版本名
 * appIcon: 应用图标地址
 * isSystemAPP: 是否为系统应用
 * isLock 是否上锁
 * isIndependent 是否独立设置
 * password 密码(独立设置时有效)
 * @ClassName: AppInfoJsonBean
 * @Description: 用于json存储App信息实体类
 * @author qingyi xuelongqy@foxmail.com
 * @date 2017/9/1 17:21
 */
data class AppInfoJsonBean(val appName: String,
                           val packageName: String,
                           val versionName: String,
                           val appIcon: String,
                           val isSystemAPP: Boolean,
                           val isLock: Boolean = false,
                           val isIndependent: Boolean = false,
                           val password: String = "")