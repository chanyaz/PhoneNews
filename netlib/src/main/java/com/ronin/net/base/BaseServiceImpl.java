package com.ronin.net.base;

import com.ronin.net.Net;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author donghailong
 */
public abstract class BaseServiceImpl<T> {
    private static Map<String, BaseServiceImpl> registryMap = new HashMap<>();
    protected T t;

    protected BaseServiceImpl() {
        t = Net.getService(this.<T>getServiceClass());
        try {
            String clazzName = this.getClass().getName();
            if (registryMap.containsKey(clazzName)) {
                throw new SingletonException("Cannot construct instance for class "
                        + clazzName + ", since an instance already exists!");
            } else {
                synchronized (registryMap) {
                    if (registryMap.containsKey(clazzName)) {
                        throw new SingletonException("Cannot construct instance for class "
                                + clazzName + ", since an instance already exists!");
                    } else {
                        registryMap.put(clazzName, this);
                    }
                }
            }
        } catch (SingletonException e) {
            e.printStackTrace();
        }

    }


    /**
     * 封装线程管理和订阅的过程
     */
    public static void ready(Observable<?> observable, Observer observer) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }


    /**
     * @param <T>
     * @return
     */
    protected abstract <T> Class<T> getServiceClass();


    @SuppressWarnings("unchecked")
    public static <S extends BaseServiceImpl> S get(final Class<S> clazz) {
        String clazzName = clazz.getName();
        if (!registryMap.containsKey(clazzName)) {
            synchronized (registryMap) {
                if (!registryMap.containsKey(clazzName)) {
                    S instance = null;
                    try {
                        instance = clazz.newInstance();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    return instance;
                }
            }
        }
        return (S) registryMap.get(clazzName);
    }


    static class SingletonException extends Exception {
        /**
         *
         */
        private static final long serialVersionUID = -8633183690442262445L;

        private SingletonException(String message) {
            super(message);
        }
    }


}
