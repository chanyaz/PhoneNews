package com.ronin.cc.util

import android.os.Handler
import android.os.Looper


/**
 * Created by Administrator on 2017/5/15.
 */
class NeverHandler private constructor() {
    private var mCrashHandler: CrashHandler? = null

    companion object {
        private var mInstance: NeverHandler? = null
        private fun getInstance(): NeverHandler? {
            if (mInstance == null) {
                synchronized(NeverHandler::class.java) {
                    if (mInstance == null) {
                        mInstance = NeverHandler()
                    }
                }
            }
            return mInstance
        }

        fun init(crashHandler: CrashHandler) {
            getInstance()!!.setCrashHandler(crashHandler)
        }
    }


    private fun setCrashHandler(crashHandler: CrashHandler) {

        mCrashHandler = crashHandler
        Handler(Looper.getMainLooper()).post {
            while (true) {
                try {
                    Looper.loop()
                } catch (e: Throwable) {
                    if (mCrashHandler != null) {//捕获异常处理
                        mCrashHandler!!.uncaughtException(Looper.getMainLooper().thread, e)
                    }
                }
            }
        }

        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            if (mCrashHandler != null) {//捕获异常处理
                mCrashHandler!!.uncaughtException(t, e)
            }
        }

    }

    interface CrashHandler {
        fun uncaughtException(t: Thread, e: Throwable)
    }
}