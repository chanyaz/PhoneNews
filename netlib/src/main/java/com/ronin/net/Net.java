package com.ronin.net;

import android.text.TextUtils;

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
    private static final int TIMEOUT = 10000;
    private String baseUrl;
    private Map<String, String> headers = new HashMap<>();

    private volatile Retrofit retrofit;
    private OkHttpClient httpClient;

    private Net() {
    }

    /**
     *
     */
    private void initRetrofit() {
        httpClient = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(InterceptorHelper.headInterceptor(headers))
                //添加日志拦截器
                .addInterceptor(InterceptorHelper.logInterceptor())
                .build();


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
    public <T> T getService(Class<T> service) {
        initRetrofit();
        return retrofit.create(service);
    }

    public static class Builder {
        private String baseUrl;
        private Map<String, String> headers;

        public Builder() {
        }

        public Builder(Net net) {
            this.baseUrl = net.baseUrl;
            this.headers = net.headers;
        }

        public Builder setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        /**
         * @param header
         */
        public Builder setHeaders(Map<String, String> header) {
            this.headers = header;
            return this;
        }

        public Net build() {
            final Net net = new Net();
            net.baseUrl = this.baseUrl;
            net.headers = this.headers;
            return net;
        }
    }

}
