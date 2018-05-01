package com.qingyi.applocker.util

import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat
import android.support.v4.os.CancellationSignal

class FingerprintUtil(ctx: Context) {

    private var mFingerprintManager: FingerprintManagerCompat? = null
    private var mKeyManager: KeyguardManager? = null
    private var mCancellationSignal: CancellationSignal? = null
    private val mActivity: Activity

    /**
     * 是否录入指纹，有些设备上即使录入了指纹，但是没有开启锁屏密码的话此方法还是返回false
     *
     * @return
     */
    val isHasEnrolledFingerprints: Boolean
        get() {
            try {
                return mFingerprintManager!!.hasEnrolledFingerprints()
            } catch (e: Exception) {
                return false
            }

        }

    /**
     * 是否有指纹识别硬件支持
     *
     * @return
     */
    val isHardwareDetected: Boolean
        get() {
            try {
                return mFingerprintManager!!.isHardwareDetected
            } catch (e: Exception) {
                return false
            }

        }

    /**
     * 判断是否开启锁屏密码
     *
     * @return
     */
    private val isKeyguardSecure: Boolean
        get() {
            try {
                return mKeyManager!!.isKeyguardSecure
            } catch (e: Exception) {
                return false
            }

        }

    init {
        mActivity = ctx as Activity
        mFingerprintManager = FingerprintManagerCompat.from(mActivity)
        mKeyManager = mActivity.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

    }

    fun callFingerPrintVerify(listener: IFingerprintResultListener?) {
        if (!isHardwareDetected) {
            return
        }
        if (!isHasEnrolledFingerprints) {
            listener?.onNoEnroll()
            return
        }
        if (!isKeyguardSecure) {
            listener?.onInSecurity()
            return
        }
        listener?.onSupport()

        listener?.onAuthenticateStart()
        if (mCancellationSignal == null) {
            mCancellationSignal = CancellationSignal()
        }
        try {
            mFingerprintManager!!.authenticate(null, 0, mCancellationSignal, object : FingerprintManagerCompat.AuthenticationCallback() {
                //多次尝试都失败会走onAuthenticationError，会停止响应一段时间，提示尝试次数过多，请稍后再试。
                override fun onAuthenticationError(errMsgId: Int, errString: CharSequence?) {
                    listener?.onAuthenticateError(errMsgId, errString)
                }

                //指纹验证失败走此方法，例如小米前4次验证失败走onAuthenticationFailed,第5次走onAuthenticationError
                override fun onAuthenticationFailed() {
                    listener?.onAuthenticateFailed()
                }

                override fun onAuthenticationHelp(helpMsgId: Int, helpString: CharSequence?) {
                    listener?.onAuthenticateHelp(helpMsgId, helpString)

                }

                //当验证的指纹成功时会回调此函数，然后不再监听指纹sensor
                override fun onAuthenticationSucceeded(result: FingerprintManagerCompat.AuthenticationResult?) {
                    listener?.onAuthenticateSucceeded(result)
                }

            }, null)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 指纹识别回调接口
     */
    interface IFingerprintResultListener {
        fun onInSecurity()

        fun onNoEnroll()

        fun onSupport()

        fun onAuthenticateStart()

        fun onAuthenticateError(errMsgId: Int, errString: CharSequence?)

        fun onAuthenticateFailed()

        fun onAuthenticateHelp(helpMsgId: Int, helpString: CharSequence?)

        fun onAuthenticateSucceeded(result: FingerprintManagerCompat.AuthenticationResult?)

    }

    fun cancelAuthenticate() {
        if (mCancellationSignal != null) {
            mCancellationSignal!!.cancel()
            mCancellationSignal = null
        }
    }


    fun onDestroy() {
        cancelAuthenticate()
        mKeyManager = null
        mFingerprintManager = null

    }
}