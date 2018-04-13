package test.model;

import com.ronin.net.helper.SchedulerHelper;
import com.ronin.net.observer.SimpleObserver;

import io.reactivex.Flowable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import test.model.movie.MovieServiceImpl;
import test.model.movie.bean.Movie;
import test.model.movie.bean.MovieDetail;

/**
 * @author donghailong
 * @date 2018/4/13
 */

public class NetCallTest {

    /**
     * 1. 顺序执行网络请求
     *
     * @desp: 第一个请求结束后，拿到结果数据，开始第二个网络请求，最后处理数据。
     * flatMap:
     * 1,2,3,4  --flatMap -->2,1,3,4
     * concatMap:
     * 1,2,3,4  --concatMap -->1,2,3,4
     * switchMap:
     * 1,2,3,4  --switchMap -->4
     */
    public static void testRxJavaSeqStream() {

        MovieServiceImpl.getInstance().getTopMovie(0, 10)
                .flatMap(new Function<Movie, ObservableSource<MovieDetail>>() {
                    @Override
                    public ObservableSource<MovieDetail> apply(Movie movie) throws Exception {
                        String title = movie.getTitle();
                        return MovieServiceImpl.getInstance().getMovieDetail(title);
                    }
                })
                .compose(SchedulerHelper.<MovieDetail>applySchedulers())
                .subscribe(new SimpleObserver<MovieDetail>() {
                    @Override
                    public void onNext(MovieDetail movieDetail) {

                    }
                });
    }


    public static void testRxJava() {
        MovieServiceImpl.getInstance().getTopMovie(0, 10)
                .flatMap(new Function<Movie, ObservableSource<MovieDetail>>() {
                    @Override
                    public ObservableSource<MovieDetail> apply(Movie movie) throws Exception {
                        String title = movie.getTitle();
                        return MovieServiceImpl.getInstance().getMovieDetail(title);
                    }
                })
                .compose(SchedulerHelper.<MovieDetail>applySchedulers())
                .subscribe(new SimpleObserver<MovieDetail>() {
                    @Override
                    public void onNext(MovieDetail movieDetail) {

                    }
                });
    }



}
