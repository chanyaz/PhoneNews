package com.ronin.cc.http

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Created by Administrator on 2017/5/18.
 */
class Rhttp private constructor() {

    val BASE_URL = "http://baidu.com/"
    var retrofit: Retrofit
    var serviceApi: ServiceApi
    val reBuilder: Retrofit.Builder

    companion object {
        private var inst = Rhttp()
        fun getInstance(): Rhttp {
            return inst
        }
    }

    init {
        val clientBuilder = OkHttpClient.Builder()
        clientBuilder.connectTimeout(10, TimeUnit.SECONDS)
        reBuilder = Retrofit.Builder()
                .client(clientBuilder.build())
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
        retrofit = reBuilder.build()
        serviceApi = retrofit.create(ServiceApi::class.java)
    }



}

