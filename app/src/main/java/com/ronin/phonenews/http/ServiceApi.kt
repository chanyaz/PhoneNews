package com.ronin.cc.http

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Url


/**
 * Created by Administrator on 2017/5/18.
 */
interface ServiceApi {
    @GET
    fun downloadFile(@Url fileUrl: String): Observable<ResponseBody>


}

