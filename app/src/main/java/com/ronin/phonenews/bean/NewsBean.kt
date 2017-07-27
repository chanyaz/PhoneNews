package com.ronin.phonenews.bean

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by Administrator on 2017/7/27.
 */

class NewsBean : Parcelable {

    /**
     * code : 200
     * msg : success
     * newslist : [{"ctime":"2017-07-27 12:00","title":"美腿秀559（Celia 2017.07.27）","description":"华声美女","picUrl":"http://image.hnol.net/c/2017-07/27/11/20170727113555221-2285289.jpg","url":"http://bbs.voc.com.cn/mm/meinv-7928324-0-1.html"},{"ctime":"2017-07-25 09:00","title":"韩国美女尹爱智","description":"华声美女","picUrl":"http://image.hnol.net/c/2017-07/25/07/20170725074645191-5058976.jpg","url":"http://bbs.voc.com.cn/mm/meinv-7922975-0-1.html"},{"ctime":"2017-07-24 10:00","title":"[贴图]最美时光","description":"华声美女","picUrl":"http://image.hnol.net/c/2017-07/24/09/201707240919169871-4217076.jpg","url":"http://bbs.voc.com.cn/mm/meinv-7920972-0-1.html"},{"ctime":"2017-07-22 14:00","title":"你难以见到的极品美腿-赵芸","description":"华声美女","picUrl":"http://image.hnol.net/c/2017-07/22/03/20170722030453141-5748356.jpg","url":"http://bbs.voc.com.cn/mm/meinv-7918930-0-1.html"},{"ctime":"2017-07-22 11:00","title":"个个是美女","description":"华声美女","picUrl":"http://image.hnol.net/c/2017-07/22/10/201707221027164991-2240800.jpg","url":"http://bbs.voc.com.cn/mm/meinv-7919136-0-1.html"},{"ctime":"2017-07-21 09:00","title":"[贴图]清纯阳光美少女","description":"华声美女","picUrl":"http://image.hnol.net/c/2017-07/21/08/201707210823355641-4217076.jpg","url":"http://bbs.voc.com.cn/mm/meinv-7917041-0-1.html"},{"ctime":"2017-07-21 09:00","title":"[贴图]上海2017摄影器材展上的美女模特","description":"华声美女","picUrl":"","url":"http://bbs.voc.com.cn/mm/meinv-7917059-0-1.html"},{"ctime":"2017-07-20 08:00","title":"[贴图]公园里的清秀长发美女","description":"华声美女","picUrl":"","url":"http://bbs.voc.com.cn/mm/meinv-7910538-0-1.html"},{"ctime":"2017-07-19 20:00","title":"清新可爱的美女","description":"华声美女","picUrl":"http://image.hnol.net/c/2017-07/19/19/2017071919204131-2240800.jpg","url":"http://bbs.voc.com.cn/mm/meinv-7913910-0-1.html"},{"ctime":"2017-07-19 17:00","title":"美腿秀558（Nancy 2017.07.19）","description":"华声美女","picUrl":"http://image.hnol.net/c/2017-07/19/16/20170719161956721-2285289.jpg","url":"http://bbs.voc.com.cn/mm/meinv-7913492-0-1.html"}]
     */

    var code: Int = 0
    var msg: String? = null

    @SerializedName("newslist")
    var newslist: List<NewslistBean>? = null

    class NewslistBean {
        /**
         * ctime : 2017-07-27 12:00
         * title : 美腿秀559（Celia 2017.07.27）
         * description : 华声美女
         * picUrl : http://image.hnol.net/c/2017-07/27/11/20170727113555221-2285289.jpg
         * url : http://bbs.voc.com.cn/mm/meinv-7928324-0-1.html
         */

        var ctime: String? = null
        var title: String? = null
        var description: String? = null
        var picUrl: String? = null
        var url: String? = null
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(this.code)
        dest.writeString(this.msg)
        dest.writeList(this.newslist)
    }

    constructor() {}

    protected constructor(parcel: Parcel) {
        this.code = parcel.readInt()
        this.msg = parcel.readString()
        this.newslist = ArrayList<NewslistBean>()
        parcel.readList(this.newslist, NewslistBean::class.java.classLoader)
    }

    companion object {

        val CREATOR: Parcelable.Creator<NewsBean> = object : Parcelable.Creator<NewsBean> {
            override fun createFromParcel(source: Parcel): NewsBean {
                return NewsBean(source)
            }

            override fun newArray(size: Int): Array<NewsBean?> {
                return arrayOfNulls(size)
            }
        }
    }
}
