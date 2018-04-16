package com.ronin.phonenews.activity

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Message
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import cn.waps.AppConnect
import com.ronin.cc.util.APP_ID
import com.ronin.cc.util.getMetaValue
import com.ronin.phonenews.R
import com.ronin.phonenews.qiniu.QiniuManager
import com.ronin.phonenews.util.UriHelper
import com.tbruyelle.rxpermissions.RxPermissions
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.functions.Consumer
import rx.functions.Action1
import java.util.ArrayList

class MainActivity : BaseActivity() {

    private val TAG = "MainActivity"

    private val REQUEST_CODE_CHOOSE = 0X001
    internal var mSelected: List<Uri> = ArrayList()

    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)

    }

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == Activity.RESULT_OK) {
            mSelected = Matisse.obtainResult(data)
            Log.i(TAG, "mSelected: $mSelected")
            if (mSelected.size > 0) {
                val filePath = UriHelper.uri2Path(this, mSelected[0])
                Log.i(TAG, "onActivityResult: filePath=$filePath")
                QiniuManager.getInst().upload(filePath)
            }

        }
    }

    private fun clickPhoto() {
        val rxPermissions = RxPermissions(this)
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe({ aBoolean ->
                    if (aBoolean!!) {
                        Matisse.from(this@MainActivity)
                                .choose(MimeType.allOf())
                                .countable(true)
                                .maxSelectable(9)
                                //                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                                .gridExpectedSize(resources.getDimensionPixelSize(R.dimen.grid_expected_size))
                                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                                .thumbnailScale(0.85f)
                                .imageEngine(GlideEngine())
                                .forResult(REQUEST_CODE_CHOOSE)
                    } else {
                        Toast.makeText(this@MainActivity, "permission denied",
                                Toast.LENGTH_LONG).show()
                    }
                })

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

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    fun startPictureEditorActivity(view: View) {
        val intent = Intent(this, PictureEditorActivity::class.java)
//        startActivity(intent)
        startActivity(intent, ActivityOptionsCompat
                .makeSceneTransitionAnimation(this).toBundle())
    }

}
