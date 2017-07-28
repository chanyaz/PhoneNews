package com.ronin.phonenews.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import cn.waps.AppConnect
import com.ronin.cc.util.APP_ID
import com.ronin.cc.util.getMetaValue
import com.ronin.phonenews.R

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            startActivity(Intent(this@MainActivity, NewsActivity::class.java))
        }
        initWapsAd()
    }

    /**
     * 初始化waps广告
     */
    private fun initWapsAd() {
        val app_pid = getMetaValue(applicationContext, "UMENG_CHANNEL")
        // 初始化统计器，并通过代码设置APP_ID, APP_PID
        if (!TextUtils.isEmpty(app_pid)) {
            AppConnect.getInstance(APP_ID, app_pid, this)
        } else {
            AppConnect.getInstance(APP_ID, this)
        }
        //初始化插屏ad
        AppConnect.getInstance(this).initPopAd(this)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }


}
