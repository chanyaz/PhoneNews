package com.ronin.net.base;

import com.ronin.net.Net;

import java.util.Map;

import io.reactivex.annotations.NonNull;

/**
 * @author donghailong
 */
public abstract class BaseServiceImpl<T> {
    protected T service;
    private Net net;

    protected BaseServiceImpl() {
        if (null == net) {
            net = new Net.Builder()
                    .setBaseUrl(getBaseUrl())
                    .setHeaders(getHeaders())
                    .build();
        }
        service = net.getService(this.serviceClass());
    }

    /**
     * 接口服务类
     *
     * @return
     */
    protected abstract Class<T> serviceClass();

    /**
     * @return
     */
    @NonNull
    protected abstract String getBaseUrl();

    protected Map<String, String> getHeaders() {
        return null;
    }

    /**
     * @return
     */
    public T getService() {
        return service;
    }


}
