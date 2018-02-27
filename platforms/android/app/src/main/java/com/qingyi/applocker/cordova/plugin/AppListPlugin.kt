package com.qingyi.applocker.cordova.plugin

import org.apache.cordova.CallbackContext
import org.json.JSONArray

/**
 * @Title: AppListPlugin类
 * @Package: com.qingyi.applocker.cordova.plugin
 * @Description: 应用列表插件(获取所有应用信息、上锁状态等)
 * @author XueLong xuelongqy@foxmail.com
 * @date 2018/2/26 11:11
 * @update_author
 * @update_time
 * @version V1.0
 * @exception
*/
class AppListPlugin: BasePlugin() {

    /**
     * @Title: 重写execute方法
     * @Class: AppListPlugin
     * @Description: App列表相关交互执行
     * @author XueLong xuelongqy@foxmail.com
     * @date 2018/2/26 11:17
     * @update_author
     * @update_time
     * @version V1.0
     * @param action 执行动作(对应方法)
     * @param args 方法参数
     * @param callbackContext 回调上下文
     * @return 执行动作是否有效
    */
    override fun execute(action: String?, args: JSONArray?, callbackContext: CallbackContext?): Boolean {
        when (action) {
            "getAppInfoList" -> {
                return true
            }
        }
        return false
    }
}