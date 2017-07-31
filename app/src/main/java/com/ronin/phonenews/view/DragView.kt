package com.ronin.phonenews.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * Created by Administrator on 2017/7/31.
 */
class DragView : View {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    var lastx: Int? = null
    var lasty: Int? = null

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        val rawx = event?.rawX?.toInt()
        val rawy = event?.rawY?.toInt()

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                lastx = rawx
                lasty = rawy
            }
            MotionEvent.ACTION_MOVE -> {

                val dx = rawx?.minus(lastx!!)
                val dy = rawy?.minus(lasty!!)

//                layout(left.plus(dx!!), top.plus(dy!!), right.plus(dx), bottom.plus(dy))

                offsetLeftAndRight(dx!!)
                offsetTopAndBottom(dy!!)

                lastx = rawx
                lasty = rawy

            }
            MotionEvent.ACTION_UP -> {

            }
        }

        return true
    }

}

