package com.ronin.cc.util

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.net.ConnectivityManager
import android.os.Build
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import java.util.regex.Pattern

/**
 * Created by ronindong on 2017/4/1.
 */


inline fun <T> supportApi(sdkInt: Int, code: () -> T) {
    if (Build.VERSION.SDK_INT >= sdkInt) {
        code()
    }
}

fun String.iStartAlphabet(): Boolean {
    val s = get(0)
    return (s in 'a'..'z') || (s in 'A'..'Z')
}


fun isDigits(input: CharSequence): Boolean {
    val length = input.length
    return (0..length - 1).any { Character.isDigit(input[it]) }
}

fun isEmail(input: String): Boolean {
    val regex = "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$"
    return regexMatch(input, regex)
}

/**
 * 中国移动：134,135,136,137,138,139,147,150,151,152,157,158,158,178,182,183,184,187,188
 * 中国联通：130,131,132,145,155,156,175,176,185,186
 * 中国电信：133,134,149,153,173,177,180,181,189
 * 虚拟运营商：170,171
 */
fun isChineseMobile(input: String) = regexMatch(input, "^(\\+?\\d{2}-?)?1[34578]\\d{9}$")


fun regexMatch(input: String, regex: String) = Pattern.compile(regex).matcher(input).matches()


fun Activity.hideSoftKeyboard() {
    (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}


fun Fragment.isNetwork() = activity!!.isNetwork()

fun Context.isNetwork(): Boolean {
    if (isWifi() || isMobile()) {
        return true
    }
    return false
}

fun Context.isWifi(): Boolean {
    val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val info = connMgr.activeNetworkInfo
    info.apply { }
    if (info != null && info.type == ConnectivityManager.TYPE_WIFI) {
        return true
    }
    return false
}


fun Context.isMobile(): Boolean {
    val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val info = connMgr.activeNetworkInfo
    if (info != null && info.type == ConnectivityManager.TYPE_MOBILE) {
        return true
    }
    return false
}

fun Context.toast(text: CharSequence) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun Context.toast(@StringRes resId: Int) {
    Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
}

fun Context.getScreenHeight(): Int {
    return Resources.getSystem().displayMetrics.heightPixels
}

fun Context.getScreenWidth(): Int {
    return Resources.getSystem().displayMetrics.widthPixels
}

inline fun dp2px(dip: Int): Float {
    return Resources.getSystem().displayMetrics.density * dip + 0.5f
}

inline fun px2dp(px: Int): Int {
    return ((px - 0.5f) / Resources.getSystem().displayMetrics.density) as Int
}

fun getMetaValue(cx: Context, metaName: String): String {

    val packageManager = cx.packageManager
    try {
        val info = packageManager.getApplicationInfo(cx.packageName, PackageManager.GET_META_DATA)
        val metaValue = info.metaData.getString(metaName.toString()).toString()
        return metaValue
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        return metaName.toString()
    }

}
