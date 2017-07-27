package com.ronin.cc.adapter

import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ronin.phonenews.R
import com.ronin.phonenews.bean.NewsBean

/**
 * Created by Administrator on 2017/3/10.
 */
class NewsListAdapter
    : BaseQuickAdapter<NewsBean.NewslistBean, BaseViewHolder>(R.layout.item_news_recyclerview) {

    override fun convert(helper: BaseViewHolder, item: NewsBean.NewslistBean?) {
//        helper.addOnClickListener(R.id.imgView)
//                .addOnClickListener(R.id.tweetName)
//                .addOnClickListener(R.id.tweetText)


        helper.getView<TextView>(R.id.tv_title)?.text = item!!.title
        helper.getView<TextView>(R.id.tv_description)?.text = item.description
        val imageView = helper.getView<ImageView>(R.id.iv_image)
        val imageUrl = item.picUrl
        Glide.with(mContext).load(imageUrl)
                .error(R.drawable.ic_launcher_background)
                .placeholder(R.drawable.ic_launcher_background)
                .into(imageView)

    }

}
