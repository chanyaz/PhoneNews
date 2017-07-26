package com.ronin.cc.adapter

import android.text.method.LinkMovementMethod
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ronin.cc.entity.Company
import com.ronin.phonenews.R

/**
 * Created by Administrator on 2017/3/10.
 */
class CompanyListAdapter
    : BaseQuickAdapter<Company, BaseViewHolder>(R.layout.layout_animation) {

    override fun convert(helper: BaseViewHolder, item: Company?) {
//        helper.addOnClickListener(R.id.imgView)
//                .addOnClickListener(R.id.tweetName)
//                .addOnClickListener(R.id.tweetText)

        when (helper.layoutPosition % 3) {
            0 -> helper.setImageResource(R.id.imgView, R.drawable.animation_img1)
            1 -> helper.setImageResource(R.id.imgView, R.drawable.animation_img2)
            2 -> helper.setImageResource(R.id.imgView, R.drawable.animation_img3)
        }

        helper.getView<TextView>(R.id.tweetName)?.text = "Hoteis in Rio de Janeiro"
        val msg = "http://www.baidu.com"
        helper.getView<TextView>(R.id.tweetText)?.text = msg
        helper.getView<TextView>(R.id.tweetText)?.movementMethod = LinkMovementMethod()

    }

}
