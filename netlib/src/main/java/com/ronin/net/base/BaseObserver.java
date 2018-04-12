package com.ronin.net.base;

import android.util.Log;

import com.ronin.net.manager.DisposableManager;

import java.io.IOException;

import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

/**
 * @author donghailong
 * @date 2018/4/4
 */

public abstract class BaseObserver<T> implements Observer<T> {

    private static final String TAG = "BaseObserver";

    protected Disposable disposable;
    protected String errMessage;
    private DisposableManager mDisposableManager = DisposableManager.getInstance();

    @Override
    public void onSubscribe(Disposable d) {
        Log.d(TAG, TAG + "-->onSubscribe:" + d);
        disposable = d;
        if (null != mDisposableManager) {
            mDisposableManager.add(disposable);
        }
    }

    @Override
    public void onComplete() {
        Log.d(TAG, TAG + "-->onComplete");
        dispose();
    }

    @Override
    public void onError(Throwable e) {
        Log.d(TAG, TAG + "-->onError:" + e.getMessage());

        if (e instanceof HttpException) {
            errMessage = "网络请求错误!";
        } else if (e instanceof IOException) {
            errMessage = "网络出错!";
        }

        dispose();
    }

    /**
     *
     */
    public final void dispose() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        if (null != mDisposableManager) {
            mDisposableManager.delete(disposable);
        }
    }


}
