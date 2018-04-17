package com.ronin.phonenews.application

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.ronin.cc.util.G_SP_NAME
import com.ronin.phonenews.util.ThreadPoolUtils
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.ronin.cc.util.NeverHandler
import com.ronin.cc.util.toast
import com.ronin.phonenews.R
import com.ronin.phonenews.activity.MainActivity
import com.ronin.phonenews.activity.NewsActivity


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
        initCrachConfig()
        gSP = getSharedPreferences(G_SP_NAME, Context.MODE_PRIVATE)
        ThreadPoolUtils.init()

    }

    private fun initCrachConfig() {
        CaocConfig.Builder.create()
//                .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //default: CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM
//                .enabled(false) //default: true
//                .showErrorDetails(false) //default: true
//                .showRestartButton(false) //default: true
//                .logErrorOnRestart(false) //default: true
//                .trackActivities(true) //default: false
//                .minTimeBetweenCrashesMs(2000) //default: 3000
//                .errorDrawable(R.drawable.) //default: bug image
//                .restartActivity(NewsActivity::class.java) //default: null (your app's launch activity)
//                .errorActivity(YourCustomErrorActivity::class.java) //default: null (default error activity)
//                .eventListener(YourCustomEventListener()) //default: null
                .apply()
    }


}