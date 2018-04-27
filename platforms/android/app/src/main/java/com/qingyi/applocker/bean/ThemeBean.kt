package com.qingyi.applocker.bean

import java.util.Date

/**
 * @Title: ThemeBean类
 * @Package: com.qingyi.applocker.bean
 * @Description: 主题实体类
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/4/26 13:43
 * @update_author
 * @update_time
 * @version V1.0
 * @exception
*/
data class ThemeBean (
        // 主题ID
        var id: Int = -1,
        // 主题名字
        var name:String = "",
        // 图片地址
        var images:ArrayList<String> = arrayListOf(),
        // 主题作者
        var author:String = "",
        // 主题描述
        var describe:String = "",
        // 主题版本
        var version:String = "",
        // 主题日期
        var date: Date = Date(),
        // 锁屏页面
        var lockPage:String = "",
        // 设置密码页面
        var setPwdPage:String = "",
        // 自带主题(不可卸载)
        var isBuiltIn:Boolean = false
)