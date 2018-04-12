package com.ronin.net.manager;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author donghailong
 * @date 2018/4/11
 */

public class DisposableManager {

    private volatile CompositeDisposable mDisposable = new CompositeDisposable();

    public static DisposableManager getInstance() {
        return Holder.INSTANCE;
    }

    private DisposableManager() {
    }

    /**
     * @param d
     */
    public void add(Disposable d) {
        if (null != mDisposable) {
            mDisposable.add(d);
        }
    }

    public boolean delete(Disposable d) {
        return mDisposable.delete(d);
    }

    /**
     *
     */
    public void clear() {
        if (null != mDisposable) {
            mDisposable.clear();
        }
    }


    private static class Holder {
        private static final DisposableManager INSTANCE = new DisposableManager();
    }
}
