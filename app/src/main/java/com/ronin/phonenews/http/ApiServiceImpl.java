package com.ronin.phonenews.http;

import com.ronin.cc.http.ServiceApi;
import com.ronin.net.base.BaseServiceImpl;
import com.ronin.net.helper.SchedulerHelper;
import com.ronin.net.manager.SingletonManager;
import com.ronin.phonenews.bean.NewsBean;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import test.model.movie.MovieServiceDao;
import test.model.movie.bean.Movie;
import test.model.movie.bean.MovieDetail;

/**
 * 网络库使用 实例
 *
 * @author donghailong
 * @date 2018/4/4
 */

public class ApiServiceImpl extends BaseServiceImpl<ServiceApi> implements ServiceApi {

    public static final String BASE_URL = "http://api.tianapi.com/";

    public static ApiServiceImpl getInstance() {
        return SingletonManager.get(ApiServiceImpl.class);
    }

    @Override
    protected Class<ServiceApi> serviceClass() {
        return ServiceApi.class;
    }

    @Override
    protected String getBaseUrl() {
        return BASE_URL;
    }

    @NotNull
    @Override
    public Observable<ResponseBody> downloadFile(@NotNull String fileUrl) {
        return null;
    }

    @NotNull
    @Override
    public Observable<NewsBean> getNewsList(@NotNull String path,
                                            @NotNull String key,
                                            int num,
                                            int page,
                                            @NotNull String word,
                                            int rand) {
        return SchedulerHelper.schedulerThread(service.getNewsList(path, key, num, page, word, rand));
    }
}
