package com.ronin.net.base;

import com.ronin.net.Net;

import java.util.Map;

import io.reactivex.annotations.NonNull;

/**
 * @author donghailong
 */
public abstract class BaseServiceImpl<T> {
    protected T service;

    protected BaseServiceImpl() {
        Net.getInstance().setBaseUrl(getBaseUrl());
        Net.getInstance().setHeaders(getHeaders());
        service = Net.getInstance().getService(this.serviceClass());
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
    protected String getBaseUrl() {
        return null;
    }

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
