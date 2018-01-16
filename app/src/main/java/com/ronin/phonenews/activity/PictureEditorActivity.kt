package com.ronin.phonenews.activity

import android.graphics.*
import android.os.Bundle
import android.support.v7.widget.AppCompatCheckBox
import android.widget.ImageView
import android.widget.SeekBar
import com.ronin.phonenews.R
import com.ronin.phonenews.util.XThread
import java.util.concurrent.Callable

/**
 * Created by Administrator on 2017/8/8.
 */
class PictureEditorActivity : BaseActivity(), SeekBar.OnSeekBarChangeListener {


    var checkbox: AppCompatCheckBox? = null
    var hueSeekBar: SeekBar? = null
    var satSeekBar: SeekBar? = null
    var lumSeekBar: SeekBar? = null
    var imageView: ImageView? = null

    var hueProgress = 1.0f
    var lumProgress = 1.0f
    var satProgress = 1.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture_editor)

        checkbox = findViewById(R.id.checkbox) as AppCompatCheckBox
        imageView = findViewById(R.id.imageView) as ImageView
        hueSeekBar = findViewById(R.id.hueSeekBar) as SeekBar
        satSeekBar = findViewById(R.id.satSeekBar) as SeekBar
        lumSeekBar = findViewById(R.id.lumSeekBar) as SeekBar



        checkbox?.setOnCheckedChangeListener {
            _, isChecked ->
            if (isChecked) {

                val bm = XThread.submit(Callable<Bitmap> {
                    return@Callable reliefImage()
                })
                imageView!!.setImageBitmap(bm)

            } else {
                imageView!!.setImageResource(R.drawable.animation_img1)
            }
        }
        hueSeekBar?.setOnSeekBarChangeListener(this)
        satSeekBar?.setOnSeekBarChangeListener(this)
        lumSeekBar?.setOnSeekBarChangeListener(this)

    }

    /**
     * 对相片进行浮雕处理
     */
    private fun reliefImage(): Bitmap {
        val bm = BitmapFactory.decodeResource(resources, R.drawable.animation_img1)
        val width = bm.width
        val height = bm.height
        val size = width * height
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        var newPixels = IntArray(size)
        var oldPixels = IntArray(size)

        bm.getPixels(oldPixels, 0, width, 0, 0, width, height)

        for (i in 1..size - 1) {
            //前一个像素
            val preColor = oldPixels[i - 1]
            var preA = Color.alpha(preColor)
            var preR = Color.red(preColor)
            var preG = Color.green(preColor)
            var preB = Color.blue(preColor)

            //后一个像素
            val nextColor = oldPixels[i]
            val newA = Color.alpha(nextColor)
            val newR = Color.red(nextColor)
            val newG = Color.green(nextColor)
            val newB = Color.blue(nextColor)

            preA = (preA - newA + 127)
            preR = (preR - newR + 127)
            preG = (preG - newG + 127)
            preB = (preB - newB + 127)

            if (preA > 255) {
                preA = 255
            }
            if (preR > 255) {
                preR = 255
            }
            if (preG > 255) {
                preG = 255
            }
            if (preB > 255) {
                preB = 255
            }

            newPixels[i] = Color.argb(preA, preR, preG, preB)

        }

        bmp.setPixels(newPixels, 0, width, 0, 0, width, height)

        return bmp
    }


    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

        when (seekBar!!.id) {
            R.id.hueSeekBar -> {
                hueProgress = progress * 1.0f / 127
            }
            R.id.satSeekBar -> {
                satProgress = progress * 1.0f / 127
            }
            R.id.lumSeekBar -> {
                lumProgress = progress * 1.0f / 127
            }
        }

        imageView!!.setImageBitmap(
                handlerBitmap(hue = hueProgress, sat = satProgress, lum = lumProgress))
    }

    /**
     * 图片的色调、饱和度和亮度进行处理
     */
    private fun handlerBitmap(hue: Float, sat: Float, lum: Float): Bitmap {
        val bm = BitmapFactory.decodeResource(resources, R.drawable.animation_img1)
        val bmp = Bitmap.createBitmap(bm.width, bm.height, Bitmap.Config.ARGB_8888)
        var canvas = Canvas(bmp)
        var paint = Paint()
        val hueMatrix = ColorMatrix()
        val satMatrix = ColorMatrix()
        val lumMatrix = ColorMatrix()
        var imgMatrix = ColorMatrix()

        hueMatrix.setRotate(0, hue)
        hueMatrix.setRotate(1, hue)
        hueMatrix.setRotate(2, hue)

        satMatrix.setSaturation(sat)

        lumMatrix.setScale(lum, lum, lum, 1f)

        imgMatrix.reset()
        imgMatrix.postConcat(hueMatrix)
        imgMatrix.postConcat(satMatrix)
        imgMatrix.postConcat(lumMatrix)


        paint.colorFilter = ColorMatrixColorFilter(imgMatrix)
        canvas.drawBitmap(bm, 0f, 0f, paint)

        return bmp

    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }

}