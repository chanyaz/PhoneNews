package com.ronin.net;

import com.ronin.net.helper.InterceptorHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author donghailong
 * @date 2018/4/4
 */

public class Net {
    public static String baseUrl = "https://api.douban.com/v2/movie/";
    public static final int TIMEOUT = 30000;

    private static Net net;
    private static volatile Retrofit retrofit;
    private static OkHttpClient httpClient;
    /**
     *
     */
    private static Map<String, String> headers = new HashMap<>();

    public static void init() {

    }

    //单例
    private static Net getInstance() {
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

    /**
     *
     */
    private static void initRetrofit() {
        if (null == httpClient) {
            httpClient = new OkHttpClient.Builder()
                    .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .addInterceptor(InterceptorHelper.headInterceptor(headers))
                    //添加日志拦截器
                    .addInterceptor(InterceptorHelper.logInterceptor())
                    .build();

        }

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                //请求的结果转为实体类
                .addConverterFactory(GsonConverterFactory.create())
                //适配RxJava2.0,RxJava1.x则为RxJavaCallAdapterFactory.create()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient)
                .build();
    }

    /**
     * @param service
     * @param <T>
     * @return
     */
    public static <T> T getService(Class<T> service) {
        if (retrofit == null) {
            initRetrofit();
        }
        return retrofit.create(service);
    }

    /**
     * @param header
     */
    public static void setHeaders(Map<String, String> header) {
        if (null == net) {
            getInstance();
        }
        headers = header;
    }

}
