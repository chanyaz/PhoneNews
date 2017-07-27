package com.ronin.phonenews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ronin.cc.util.ContantsKt;
import com.ronin.cc.util.ExtKt;
import com.ronin.phonenews.R;

import cn.waps.AppConnect;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                startActivity(new Intent(MainActivity.this,NewsActivity.class));
            }
        });
        initWapsAd();
    }

    /**
     * 初始化waps广告
     */
    private void initWapsAd() {
        String app_pid = ExtKt.getMetaValue(getApplicationContext(), "UMENG_CHANNEL");
        // 初始化统计器，并通过代码设置APP_ID, APP_PID
        if (!TextUtils.isEmpty(app_pid)) {
            AppConnect.getInstance(ContantsKt.APP_ID, app_pid, this);
        } else {
            AppConnect.getInstance(ContantsKt.APP_ID, this);
        }
        //初始化插屏ad
        AppConnect.getInstance(this).initPopAd(this);

        // 初始化卸载广告
        AppConnect.getInstance(this).initUninstallAd(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
