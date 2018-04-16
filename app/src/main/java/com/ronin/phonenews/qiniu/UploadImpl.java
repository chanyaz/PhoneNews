package com.ronin.phonenews.qiniu;

import com.ronin.net.base.BaseServiceImpl;
import com.ronin.net.helper.SchedulerHelper;

import io.reactivex.Observable;


/**
 * Created by donghailong on 2018/4/9.
 */

public class UploadImpl extends BaseServiceImpl<UploadService> implements UploadService {

    public static UploadImpl getInstance() {
        return get(UploadImpl.class);
    }

    @Override
    protected Class<UploadService> serviceClass() {
        return UploadService.class;
    }

    @Override
    protected String getBaseUrl() {
        return "http://118.24.84.171:9001/";
    }

    @Override
    public Observable<TokenBean> getToken() {
        return SchedulerHelper.schedulerThread(service.getToken());
    }
}
