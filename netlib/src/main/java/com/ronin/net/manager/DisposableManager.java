package com.ronin.net.manager;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author donghailong
 * @date 2018/4/11
 */

public class DisposableManager {

    private static volatile CompositeDisposable sDisposable = new CompositeDisposable();

    public static DisposableManager getInstance() {
        return Holder.INSTANCE;
    }

    private DisposableManager() {
    }

    /**
     * @param d
     */
    public void add(Disposable d) {
        if (null != sDisposable) {
            sDisposable.add(d);
        }
    }

    public boolean delete(Disposable d) {
        return sDisposable.delete(d);
    }

    /**
     *
     */
    public void clear() {
        if (null != sDisposable) {
            sDisposable.clear();
        }
    }


    private static class Holder {
        private static final DisposableManager INSTANCE = new DisposableManager();
    }
}
