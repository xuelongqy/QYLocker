package com.qingyi.applocker.util

import android.util.Log
import com.xposed.qingyi.cmprotectedappsplus.constant.ThisApp
import de.robv.android.xposed.XposedBridge

/**
 * 统一日志管理工具
 * 统一管理Xposed日志和Android日志
 * @ClassName: LoggerUtil
 * @Description: 统一日志管理工具
 * @author qingyi xuelongqy@foxmail.com
 * @date 2017/8/19 17:26
 */
class LoggerUtil {
    companion object {
        //Xpsoed日志开启状态
        val XPOSED_LOG_OPEN = true
        //Android日志开启状态
        val ANDROID_LOG_OPEN = true
        //重要日志
        val IMPORTANT_LOG = 7

        //重要日志标识组
        private val IMPORTANT_LOGS = arrayOf(
                IMPORTANT_LOG,
                IMPORTANT_LOG and Log.ERROR,
                IMPORTANT_LOG and Log.WARN,
                IMPORTANT_LOG and Log.DEBUG,
                IMPORTANT_LOG and Log.INFO
        )

        /**
         * 计算日志级别
         * @param level 参数中的日志级别
         * @return 计算后最终的日志级别
         */
        private fun getCorrectLevel(level:Int): Int {
            return when (level) {
                Log.ERROR and IMPORTANT_LOG -> Log.ERROR
                Log.WARN and IMPORTANT_LOG -> Log.WARN
                Log.DEBUG and IMPORTANT_LOG -> Log.DEBUG
                Log.INFO and IMPORTANT_LOG -> Log.INFO
                else -> level
            }
        }

        /**
         * 同时打印安卓和Xposed日志
         * @param level 日志级别(除了重要日志,在Xposed中无效)
         * @param context 上下文,作为标识,方便查找日志输出的位置
         * @param msg 日志主要内容
         */
        fun logAll(level:Int, context: String, msg:String) {
            //打印Xposed日志
            logXposed(level,context,msg)
            //打印Android日志
            logAndroid(level,context,msg)
        }

        /**
         * 打印Xposed日志
         * @param level 日志级别(除了重要日志,在Xposed中无效)
         * @param context 上下文,作为标识,方便查找日志输出的位置
         * @param msg 日志主要内容
         */
        fun logXposed(level:Int, context: String, msg:String) {
            if (XPOSED_LOG_OPEN || level in IMPORTANT_LOGS){
                XposedBridge.log("${ThisApp.APP_NAME}: Tag=${context} Msg->${msg}")
            }
        }

        /**
         * 打印安卓日志
         * @param level 日志级别
         * @param context 上下文,作为标识,方便查找日志输出的位置
         * @param msg 日志主要内容
         */
        fun logAndroid(level:Int, context: String, msg:String) {
            if (ANDROID_LOG_OPEN || level in IMPORTANT_LOGS){
                var logText = "${ThisApp.APP_NAME}: Msg->${msg}"
                when (getCorrectLevel(level)){
                    Log.ERROR -> Log.e(context,logText)
                    Log.DEBUG -> Log.d(context,logText)
                    Log.WARN -> Log.w(context,logText)
                    else -> Log.i(context,logText)
                }
            }
        }
    }
}