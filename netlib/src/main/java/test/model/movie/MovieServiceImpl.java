package test.model.movie;

import com.ronin.net.base.BaseObserver;
import com.ronin.net.base.BaseServiceImpl;
import com.ronin.net.helper.SchedulerHelper;
import com.ronin.net.manager.SingletonManager;

import io.reactivex.Observable;
import test.model.movie.bean.Movie;
import test.model.movie.bean.MovieDetail;

import static com.ronin.net.helper.SchedulerHelper.schedulerThread;

/**
 * 网络库使用 实例
 *
 * @author donghailong
 * @date 2018/4/4
 */

public class MovieServiceImpl extends BaseServiceImpl<MovieServiceDao> implements MovieServiceDao {

    public static MovieServiceImpl getInstance() {
        return SingletonManager.get(MovieServiceImpl.class);
    }

    @Override
    protected Class<MovieServiceDao> serviceClass() {
        return MovieServiceDao.class;
    }

    @Override
    protected String getBaseUrl() {
        return "https://api.douban.com/v2/movie/";
    }

    @Override
    public Observable<Movie> getTopMovie(int start, int count) {
        return service.getTopMovie(start, count)
                .compose(SchedulerHelper.<Movie>applySchedulers());
    }

    @Override
    public Observable<MovieDetail> getMovieDetail(String title) {
        return SchedulerHelper.schedulerThread(service.getMovieDetail(title));
    }
}
