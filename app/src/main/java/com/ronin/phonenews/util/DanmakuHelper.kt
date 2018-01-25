package com.ronin.phonenews.util

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.annotation.MainThread
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.style.BackgroundColorSpan
import android.text.style.ImageSpan
import com.ronin.cc.util.dp2px
import com.ronin.phonenews.R.id.danmaku_view
import com.ronin.pullrefreshlibrary.util.Utils
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.ObservableSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.functions.Predicate
import io.reactivex.schedulers.Schedulers
import master.flame.danmaku.controller.DrawHandler
import master.flame.danmaku.danmaku.model.BaseDanmaku
import master.flame.danmaku.danmaku.model.DanmakuTimer
import master.flame.danmaku.danmaku.model.IDanmakus
import master.flame.danmaku.danmaku.model.IDisplayer
import master.flame.danmaku.danmaku.model.android.BaseCacheStuffer
import master.flame.danmaku.danmaku.model.android.DanmakuContext
import master.flame.danmaku.danmaku.model.android.Danmakus
import master.flame.danmaku.danmaku.model.android.SpannedCacheStuffer
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser
import master.flame.danmaku.danmaku.util.IOUtils
import master.flame.danmaku.ui.widget.DanmakuView
import java.io.IOException
import java.io.InputStream
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap

/**
 * Created by donghailong on 2018/1/25.
 */
class DanmakuHelper(danmakuView: DanmakuView) {

    private val overlappingEnablePair: HashMap<Int, Boolean> = HashMap()
    private var showDanmaku: Boolean = false
    private var mDanmakuView: DanmakuView = danmakuView
    private var danmakuContext: DanmakuContext = DanmakuContext.create()
    private val parser = object : BaseDanmakuParser() {
        override fun parse(): IDanmakus {
            return Danmakus()
        }
    }
    private val mCacheStufferAdapter = object : BaseCacheStuffer.Proxy() {

        private var mDrawable: Drawable? = null

        override fun prepareDrawing(danmaku: BaseDanmaku, fromWorkerThread: Boolean) {
            // 根据你的条件检查是否需要需要更新弹幕
            if (danmaku.text is Spanned) {
                // FIXME 这里只是简单启个线程来加载远程url图片，请使用你自己的异步线程池，最好加上你的缓存池
                ThreadPoolUtils.getInstance().execute(Runnable {
                    val url = "http://www.bilibili.com/favicon.ico"
                    var inputStream: InputStream? = null
                    var drawable = mDrawable
                    if (drawable == null) {
                        try {
                            val urlConnection = URL(url).openConnection()
                            inputStream = urlConnection.getInputStream()
                            drawable = BitmapDrawable.createFromStream(inputStream, "bitmap")
                            mDrawable = drawable
                        } catch (e: MalformedURLException) {
                            e.printStackTrace()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        } finally {
                            IOUtils.closeQuietly(inputStream)
                        }
                    }
                    if (drawable != null) {
                        drawable.setBounds(0, 0, 100, 100)
                        val spannable = createSpannable(drawable)
                        danmaku.text = spannable
                        mDanmakuView?.invalidateDanmaku(danmaku, false)
                        return@Runnable
                    }
                })
            }
        }

        override fun releaseResource(baseDanmaku: BaseDanmaku) {

        }
    }

    private val sColors = intArrayOf(Color.CYAN,
            Color.parseColor("#CC00FF"),
            Color.parseColor("#0066FF"),
            Color.parseColor("#FF9900"),
            Color.parseColor("#00FFFF"),
            Color.parseColor("#33FF33"),
            Color.parseColor("#FF9933"),
            Color.parseColor("#FF66FF"),
            Color.MAGENTA)

    private fun addDanmaku(content: String, withBorder: Boolean) {
        val danmaku = danmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL)
        danmaku.text = content
        danmaku.padding = 5
        danmaku.textSize = dp2px(20)
        val i = Random().nextInt(sColors.size)
        danmaku.textColor = sColors[i]
        danmaku.time = mDanmakuView.getCurrentTime()
        if (withBorder) {
            danmaku.borderColor = Color.GREEN
        }
        mDanmakuView.addDanmaku(danmaku)
    }

    init {

        mDanmakuView.enableDanmakuDrawingCache(true)
        mDanmakuView.setCallback(object : DrawHandler.Callback {
            override fun prepared() {
                showDanmaku = true
                mDanmakuView.start()
                generateSomeDanmaku()
            }

            override fun updateTimer(timer: DanmakuTimer) {

            }

            override fun danmakuShown(danmaku: BaseDanmaku) {

            }

            override fun drawingFinished() {

            }
        })

        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL, true)
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true)

        danmakuContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 3.0f)
                .setCacheStuffer(SpannedCacheStuffer(), mCacheStufferAdapter)
        danmakuContext.preventOverlapping(overlappingEnablePair)
        mDanmakuView.prepare(parser, danmakuContext)
    }


    private fun createSpannable(drawable: Drawable?): SpannableStringBuilder {
        val text = "bitmap"
        val spannableStringBuilder = SpannableStringBuilder(text)
        val span = ImageSpan(drawable)//ImageSpan.ALIGN_BOTTOM);
        spannableStringBuilder.setSpan(span, 0, text.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        spannableStringBuilder.append("图文混排")
        spannableStringBuilder.setSpan(BackgroundColorSpan(Color.parseColor("#8A2233B1")), 0, spannableStringBuilder.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        return spannableStringBuilder
    }

    /**
     * 绘制背景(自定义弹幕样式)
     */
    private class BackgroundCacheStuffer : SpannedCacheStuffer() {
        // 通过扩展SimpleTextCacheStuffer或SpannedCacheStuffer个性化你的弹幕样式
        internal val paint = Paint()

        override fun measure(danmaku: BaseDanmaku, paint: TextPaint, fromWorkerThread: Boolean) {
            danmaku.padding = 10  // 在背景绘制模式下增加padding
            super.measure(danmaku, paint, fromWorkerThread)
        }

        public override fun drawBackground(danmaku: BaseDanmaku?, canvas: Canvas?, left: Float, top: Float) {
            paint.color = -0x7edacf65
            canvas!!.drawRect(left + 2, top + 2, left + danmaku!!.paintWidth - 2, top + danmaku.paintHeight - 2, paint)
        }

        override fun drawStroke(danmaku: BaseDanmaku, lineText: String?, canvas: Canvas, left: Float, top: Float, paint: Paint) {
            // 禁用描边绘制
        }
    }

    internal var mDisposable: Disposable? = null

    /**
     * 随机生成一些弹幕内容以供测试
     */
    private fun generateSomeDanmaku() {
        ThreadPoolUtils.getInstance().execute({
            val time = Random().nextInt(500).toLong()
            mDisposable = Observable.interval(time, TimeUnit.MILLISECONDS).flatMap { num ->
                Observable.just<String>("num: " + num)
                        .delay(1, TimeUnit.SECONDS)
            }.repeat().subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe { s -> addDanmaku(s, false) }
        })
    }

    fun pause() {
        if (mDanmakuView.isPrepared) {
            mDanmakuView.pause()
        }
    }

    fun resume() {
        if (mDanmakuView.isPrepared && mDanmakuView.isPaused) {
            mDanmakuView.resume()
        }
    }

    fun destroy() {
        mDanmakuView.release()
        mDisposable!!.dispose()
    }


}