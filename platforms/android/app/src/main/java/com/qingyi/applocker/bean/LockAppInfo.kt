package com.qingyi.applocker.bean

/**
 * @Title: LockAppInfo实体类
 * @Package: com.qingyi.applocker.bean
 * @Description: 用于获取上锁应用信息
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/5/3 15:47
 * @update_author
 * @update_time
 * @version V1.0
 * @exception
*/
data class LockAppInfo (
        // 应用名字
        var name:String = "",
        // 应用图标
        var icon:String = ""
)