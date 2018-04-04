package com.ronin.net.base;

import android.util.Log;

import com.ronin.net.listener.OnNextListener;

import io.reactivex.disposables.Disposable;

/**
 * Created by donghailong on 2018/4/4.
 */

public abstract class SimpleObserver<T> extends BaseObserver<T> {
    private static final String TAG = "SimpleObserver";


    public SimpleObserver() {
    }

    @Override
    public void onSubscribe(Disposable d) {
        super.onSubscribe(d);
        Log.d(TAG, TAG + "-->onSubscribe:" + d);

    }

    @Override
    public void onNext(T t) {
        Log.d(TAG, TAG + "-->onNext:" + t);

    }

    @Override
    public void onError(Throwable e) {
        Log.d(TAG, TAG + "-->onError:" + e);
    }

    @Override
    public void onComplete() {
        Log.d(TAG, TAG + "-->onComplete");
    }
}
