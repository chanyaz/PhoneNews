package com.ronin.net.helper;

import com.ronin.net.base.BaseObserver;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 *
 * @author donghailong
 * @date 2018/4/12
 */

public class SchedulerHelper {
    /**
     * 切换调度线程
     *
     * @param observable
     * @param observer
     * @return
     */
    public static <X> Observable<X> schedulerThread(Observable<X> observable, BaseObserver<X> observer) {
        schedulerThread(observable).subscribe(observer);
        return observable;
    }

    /**
     * 切换调度线程
     *
     * @param observable
     * @return
     */
    public static <X> Observable<X> schedulerThread(Observable<X> observable) {
        return observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 切换调度线程
     *
     * @param <X>
     * @return 返回 ObservableTransformer，配合Observable的{compose}操作符使用
     */
    public static <X> ObservableTransformer<X, X> applySchedulers() {
        return new ObservableTransformer<X, X>() {
            @Override
            public ObservableSource<X> apply(Observable<X> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
