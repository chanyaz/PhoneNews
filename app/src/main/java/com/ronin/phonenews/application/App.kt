package com.ronin.phonenews.application

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.ronin.cc.util.G_SP_NAME
import com.ronin.phonenews.util.ThreadPoolUtils

/**
 * Created by Administrator on 2017/7/27.
 */
class App : Application() {
    lateinit var gSP: SharedPreferences

    companion object {
        private var _inst: App? = null
        fun of() = _inst!!
    }

    override fun onCreate() {
        super.onCreate()
        _inst = this

        gSP = getSharedPreferences(G_SP_NAME, Context.MODE_PRIVATE)
        ThreadPoolUtils.init()

//        NeverHandler.init(object : NeverHandler.CrashHandler {
//            override fun uncaughtException(t: Thread, e: Throwable) {
//                toast(e.toString())
//            }
//        })

    }


}