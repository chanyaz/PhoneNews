package com.ronin.phonenews.util;

import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.AsyncSubject;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.ReplaySubject;

/**
 * @author donghailong
 * @date 2018/1/10
 */

public class RxJavaTest {
    private static final String TAG = "RxJavaTest";

    public static void start() {
//        testSubject();
        testObservable();
    }

    private static void testObservable() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("1");
                emitter.onNext("2");
                emitter.onNext("3");
                emitter.onComplete();
                emitter.onNext("4");

            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    private Disposable mDisposable;

                    @Override
                    public void onSubscribe(Disposable disposable) {
                        mDisposable = disposable;

                    }

                    @Override
                    public void onNext(String s) {
                        if ("2".endsWith(s)) {
                            mDisposable.dispose();
                        }
                        Log.i(TAG, "onNext: s =" + s);
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    /**
     * Subject
     */
    public static void testSubject() {
        int count = 0;
        /**
         *PublishSubjec 是最直接的一个 Subject。当一个数据发射到 PublishSubject 中时，
         * PublishSubject 将立刻把这个数据发射到订阅到该 subject 上的所有 subscriber 中。
         */
        PublishSubject<Integer> publishSubject = PublishSubject.create();
        publishSubject.onNext(++count);
        publishSubject.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "publishSubject integer=" + integer);
            }
        });
        publishSubject.onNext(++count);
        publishSubject.onNext(++count);

        /**
         *ReplaySubject
         * 可以缓存所有发射给他的数据。当一个新的订阅者订阅的时候，缓存的所有数据都会发射给这个订阅者
         */
        ReplaySubject<Integer> replaySubject = ReplaySubject.create();
        replaySubject.onNext(++count);
        replaySubject.onNext(++count);
        replaySubject.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "replaySubject after integer=" + integer);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.i(TAG, "replaySubject throwable =" + throwable.getMessage());
            }
        });
        replaySubject.onError(new Throwable("msg"));
        replaySubject.onNext(++count);

        /**
         *BehaviorSubject
         * 只保留最后一个值。 等同于限制 ReplaySubject 的个数为 1 的情况。在创建的时候可以指定一个初始值，
         * 这样可以确保党订阅者订阅的时候可以立刻收到一个值。
         */
        BehaviorSubject<Integer> behaviorSubject = BehaviorSubject.create();
        behaviorSubject.onNext(++count);
        behaviorSubject.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "behaviorSubject integer=" + integer);
            }
        });
        behaviorSubject.subscribeOn(Schedulers.io());

        behaviorSubject.onNext(++count);

        /**
         *AsyncSubject
         * AsyncSubject 也缓存最后一个数据。
         * 区别是 AsyncSubject 只有当数据发送完成时（onCompleted 调用的时候）才发射这个缓存的最后一个数据
         *
         */
        AsyncSubject<Integer> asyncSubject = AsyncSubject.create();
        asyncSubject.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "asyncSubject integer=" + integer);
            }
        });
        asyncSubject.onNext(++count);
        asyncSubject.onNext(++count);
        asyncSubject.onComplete();
        asyncSubject.onNext(++count);

    }
}
