package com.ronin.net.base;

import android.util.Log;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 *
 * @author donghailong
 * @date 2018/4/4
 */

public abstract class BaseObserver<T> implements Observer<T> {

    protected Disposable disposable;

    @Override
    public void onSubscribe(Disposable d) {
        //添加业务处理
        disposable = d;
    }
}
