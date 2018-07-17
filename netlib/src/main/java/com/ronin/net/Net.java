package com.ronin.net;

import android.text.TextUtils;
import android.util.Patterns;

import com.ronin.net.helper.InterceptorHelper;
import com.ronin.net.config.ConfigInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.ronin.net.config.ConfigInfo.TIMEOUT;

/**
 * @author donghailong
 * @date 2018/4/4
 */

public class Net {

    private String baseUrl = ConfigInfo.API_URL;
    private Map<String, String> headers = new HashMap<>();

    private volatile Retrofit retrofit;

    private Net() {
    }

    private static class Holder {
        private static final Net INSTANCE = new Net();
    }

    public static Net getInstance() {
        return Holder.INSTANCE;
    }

    /**
     *
     */
    private void initRetrofit() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
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


    public void setBaseUrl(String baseUrl) {
        if (!TextUtils.isEmpty(baseUrl)
                && Patterns.WEB_URL.matcher(baseUrl).find()) {
            this.baseUrl = baseUrl;
        }
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
