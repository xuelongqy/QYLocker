package com.qingyi.applocker.bean

/**
 * @Title: LockAppConfigBean实体类
 * @Package: com.qingyi.applocker.bean
 * @Description: 加锁应用配置实体类
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/2/27 17:23
 * @update_author
 * @update_time
 * @version V1.0
 * @exception
*/
class LockAppConfigBean (var packageName: String = "",
                         var isIndependent: Boolean = false,
                         var themes: ArrayList<LockAppThemeBean> = arrayListOf()
//                         var theme: String = "",
//                         var password: String = ""
)