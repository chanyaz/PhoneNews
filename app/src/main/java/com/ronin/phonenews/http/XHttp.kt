package com.ronin.cc.http

import com.ronin.phonenews.http.HttpConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Created by Administrator on 2017/5/18.
 */
object XHttp {

    var retrofit: Retrofit
    var serviceApi: ServiceApi
    val reBuilder: Retrofit.Builder

    init {
        val clientBuilder = OkHttpClient.Builder()
        clientBuilder.connectTimeout(HttpConfig.TIMEOUT, TimeUnit.SECONDS)
        reBuilder = Retrofit.Builder()
                .baseUrl(HttpConfig.BASE_URL)
                .client(clientBuilder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
        retrofit = reBuilder.build()
        serviceApi = retrofit.create(ServiceApi::class.java)
    }


}

