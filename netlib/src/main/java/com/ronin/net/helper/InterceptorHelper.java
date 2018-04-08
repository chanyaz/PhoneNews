package com.ronin.net.helper;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class InterceptorHelper {
    private static final String TAG = "InterceptorHelper";
    public final static Charset UTF8 = Charset.forName("UTF-8");

    /**
     * 日志拦截器
     *
     * @return
     */
    public static HttpLoggingInterceptor logInterceptor() {
        return new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
//                Log.w(TAG, "log: " + message);
            }
        }).setLevel(HttpLoggingInterceptor.Level.BODY);
        //设置打印数据的级别
    }

    public static Interceptor headInterceptor(final Map<String, String> headers) {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder builder = chain.request()
                        .newBuilder();
                if (headers != null && headers.size() > 0) {
                    Set<String> keys = headers.keySet();
                    for (String headerKey : keys) {
                        builder.addHeader(headerKey, headers.get(headerKey)).build();
                    }
                }
                return chain.proceed(builder.build());
            }
        };
    }
}
