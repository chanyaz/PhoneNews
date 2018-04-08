package test.impl;

import com.ronin.net.base.BaseServiceImpl;

import io.reactivex.Observer;
import test.bean.Movie;
import test.service.MovieServiceDao;

/**
 * @author donghailong
 * @date 2018/4/4
 */

public class MovieServiceImpl extends BaseServiceImpl<MovieServiceDao> {

    public static MovieServiceImpl getInstance() {
        return BaseServiceImpl.get(MovieServiceImpl.class);
    }

    @Override
    protected Class<MovieServiceDao> getServiceClass() {
        return MovieServiceDao.class;
    }

    /**
     * 用于获取豆瓣电影Top250的数据
     *
     * @param observer 由调用者传过来的观察者对象
     * @param start    起始位置
     * @param count    获取长度
     */
    public void getTopMovie(int start, int count, Observer<Movie> observer) {
        ready(t.getTopMovie(start, count), observer);
    }




}
