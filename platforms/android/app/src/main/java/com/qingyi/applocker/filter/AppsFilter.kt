package com.qingyi.applocker.filter

import com.qingyi.applocker.bean.LockAppConfigBean
import com.xposed.qingyi.cmprotectedappsplus.constant.ThisApp

/**
 * @Title: AppsFilter类
 * @Package: com.qingyi.applocker.filter
 * @Description: 应用过滤器(不需要显示的应用)
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/2/28 10:25
 * @update_author
 * @update_time
 * @version V1.0
 * @exception
*/
object AppsFilter {
    // 应用列表过滤名单
    val AppsFilterMap: Map<String, LockAppConfigBean> = mapOf(
            ThisApp.PACKAGE_NAME to LockAppConfigBean(ThisApp.PACKAGE_NAME)
    )
    // 解锁页面过滤名单
    val ActivityFilterList: ArrayList<String> = arrayListOf(
            // 输入法
            "android.inputmethodservice.SoftInputWindow"
    )
    // 解锁页面过滤应用
    val PackageFilterList: ArrayList<String> = arrayListOf(
            // 系统界面
            "com.android.systemui"
    )
}