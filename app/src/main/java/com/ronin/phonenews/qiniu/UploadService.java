package com.ronin.phonenews.qiniu;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by donghailong on 2018/4/9.
 */

public interface UploadService {

    @GET("dev/token")
    Observable<TokenBean> getToken();
}
