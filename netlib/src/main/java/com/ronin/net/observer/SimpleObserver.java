package com.ronin.net.observer;

import com.ronin.net.base.BaseObserver;

import io.reactivex.disposables.Disposable;

/**
 * @author donghailong
 * @date 2018/4/4
 */

public abstract class SimpleObserver<T> extends BaseObserver<T> {
    private static final String TAG = "SimpleObserver";

    public SimpleObserver() {
    }

    @Override
    public void onSubscribe(Disposable d) {
        super.onSubscribe(d);
    }

    /**
     * @param t
     */
    @Override
    public abstract void onNext(T t);

    @Override
    public void onError(Throwable e) {
        super.onError(e);
    }

    @Override
    public void onComplete() {
        super.onComplete();
    }
}
