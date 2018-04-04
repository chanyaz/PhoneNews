package com.ronin.net.api;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author donghailong
 * @date 2018/4/4
 */

public class Net {
    public static String baseUrl = "https://api.douban.com/v2/movie/";

    private static Net net;
    private static volatile Retrofit retrofit;

    //单例
    public static Net getInstance() {
        if (net == null) {
            synchronized (Net.class) {
                if (net == null) {
                    net = new Net();
                }
            }
        }
        return net;
    }

    private Net() {
        initRetrofit();
    }

    private static void initRetrofit() {

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                //请求的结果转为实体类
                .addConverterFactory(GsonConverterFactory.create())
                //适配RxJava2.0,RxJava1.x则为RxJavaCallAdapterFactory.create()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static <T> T getService(Class<T> tClass) {
        if (retrofit == null) {
            initRetrofit();
        }
        return retrofit.create(tClass);
    }


}
