package com.ronin.cc.http

import com.ronin.phonenews.bean.NewsBean
import com.ronin.phonenews.http.HttpConfig
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url


/**
 * Created by Administrator on 2017/5/18.
 */
interface ServiceApi {
    @GET
    fun downloadFile(@Url fileUrl: String): Observable<ResponseBody>

    @GET("{newsId}/")
    fun getNewsList(@Path("newsId") path: String,
                    @Query("key") key: String = HttpConfig.KEY,
                    @Query("num") num: Int = HttpConfig.PAGE_NUM,
                    @Query("page") page: Int = 1,
                    @Query("word") word: String = "",
                    @Query("rand") rand: Int = 0): Observable<NewsBean>



}

