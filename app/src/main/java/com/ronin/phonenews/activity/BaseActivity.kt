package com.ronin.phonenews.activity

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import cn.waps.AppConnect
import java.lang.ref.WeakReference

abstract class BaseActivity : AppCompatActivity() {

    protected open fun handleMessage(msg: Message) {

    }

    var mHandler = StaticInnerHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onDestroy() {
        super.onDestroy()
        AppConnect.getInstance(this).close()
    }


    /**
     * handler使用若引用方式，避免内存泄漏
     */
    class StaticInnerHandler(baseActivity: BaseActivity) : Handler() {

        private val mAy: BaseActivity?
        private var weakReference: WeakReference<BaseActivity>? = null

        init {
            if (weakReference == null) {
                weakReference = WeakReference(baseActivity)
            }
            mAy = weakReference!!.get()
        }


        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            mAy?.handleMessage(msg)
        }
    }


}
