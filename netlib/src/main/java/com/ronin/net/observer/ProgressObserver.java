package com.ronin.net.observer;

import android.content.Context;
import android.content.DialogInterface;

import com.ronin.net.base.BaseObserver;
import com.ronin.net.helper.ProgressDialogHelper;

import io.reactivex.disposables.Disposable;

/**
 * @author donghailong
 */
public class ProgressObserver<T> extends BaseObserver<T> implements DialogInterface.OnCancelListener {

    private ProgressDialogHelper progressDialogHelper;
    private Context context;

    public ProgressObserver(Context cx) {
        this.context = cx;
        progressDialogHelper = new ProgressDialogHelper(context, this);
    }

    @Override
    public void onSubscribe(Disposable d) {
        super.onSubscribe(d);
        progressDialogHelper.show();

    }

    @Override
    public void onNext(T t) {

    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        progressDialogHelper.dismiss();
    }

    @Override
    public void onComplete() {
        super.onComplete();
        progressDialogHelper.dismiss();

    }

    @Override
    public void onCancel(DialogInterface dialog) {
        dispose();
    }
}
