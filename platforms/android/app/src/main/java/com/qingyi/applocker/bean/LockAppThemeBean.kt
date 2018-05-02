package com.qingyi.applocker.bean

/**
 * @Title: LockAppThemeBean实体类
 * @Package: com.qingyi.applocker.bean
 * @Description: 加锁应用主题信息
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/4/11 11:15
 * @update_author
 * @update_time
 * @version V1.0
 * theme: 主题
 * password: 密码
*/
data class LockAppThemeBean (
        var name: String = "",
        var theme: String = "",
        var password: String = ""
)