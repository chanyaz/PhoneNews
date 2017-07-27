package com.ronin.cc.util

import android.os.Environment
import java.io.File

/**
 * Created by Administrator on 2017/5/10.
 */

/**
 * 每页加载条数
 */
const val G_SP_NAME: String = "_phone_news_sp_"

const val APP_ID = "fe9c4f602ff5a3457f6cc0c2aacbb2e3";

const val G_PAGE_NUM = 20


const val G_KEY_PULL_REFRESH_OBJECT_ID = "PULL_REFRESH_OBJECT_ID"
const val G_KEY_SKY_PATH: String = "G_KEY_SKY_PATH"
const val G_KEY_SUN_PATH: String = "G_KEY_SUN_PATH"
const val G_KEY_BUILDING_PATH: String = "G_KEY_BUILDING_PATH"


const val PHONE_HINT: String = "请输入手机号码"
const val WECHAT_HINT: String = "输入微信号码"
const val QQ_HINT: String = "请输入QQ号码"
const val EMAIL_HINT: String = "请输入邮件地址"


const val UPLOAD_PULL_REFRESH_IMAGE_PATH = "pullrefresh"
val REFRESH_FILE_NAME = listOf("sun.png", "sky.png", "building.png")
var CACHE_PULL_IMAGE_PATH = Environment.getExternalStorageDirectory().path +
        File.separator + UPLOAD_PULL_REFRESH_IMAGE_PATH + File.separator

const val FILE_REQUEST_CODE = 0X0001