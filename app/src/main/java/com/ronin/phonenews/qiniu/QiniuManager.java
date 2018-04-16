package com.ronin.phonenews.qiniu;

import android.text.TextUtils;
import android.util.Log;

import com.qiniu.android.common.FixedZone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.ronin.net.observer.SimpleObserver;

import org.json.JSONObject;

/**
 * Created by donghailong on 2018/4/9.
 */

public class QiniuManager {

    private static final String TAG = QiniuManager.class.getSimpleName();
    private UploadManager uploadManager;
    private Configuration config;
    private volatile boolean isCancelled = false;

    private QiniuManager() {
        init();
    }

    private void init() {
        config = new Configuration.Builder()
                // 分片上传时，每片的大小。 默认256K
                .chunkSize(512 * 1024)
                // 启用分片上传阀值。默认512K
                .putThreshhold(1024 * 1024)
                // 链接超时。默认10秒
                .connectTimeout(10)
                // 是否使用https上传域名
                .useHttps(true)
                // 服务器响应超时。默认60秒
                .responseTimeout(60)
                // recorder分片上传时，已上传片记录器。默认null
                .recorder(null)
                // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
                .recorder(null, null)
                // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
                .zone(FixedZone.zone2)
                .build();

        // 重用uploadManager。一般地，只需要创建一个uploadManager对象
        uploadManager = new UploadManager(config);

    }


    public static QiniuManager getInst() {
        return Holder.INSTANCE;
    }

    public UploadManager getUploadManager() {
        return uploadManager;
    }

    public void upload(final String filePath) {

        UploadImpl.getInstance().getToken().subscribe(new SimpleObserver<TokenBean>() {
            @Override
            public void onNext(final TokenBean bean) {
                Log.i(TAG, "onNext: token=" + bean.getToken());
                if (!TextUtils.isEmpty(bean.getToken())) {
                    uploadManager.put(filePath, null, bean.getToken(), new UpCompletionHandler() {
                        @Override
                        public void complete(String key, ResponseInfo info, JSONObject response) {
                            Log.i(TAG, key + ",\r\n " + info + ",\r\n " + response);
                            if (null != info && info.isOK()) {
                                Log.i(TAG, "complete: success");
                                String hash = response.optString("hash");

                            } else {
                                Log.i(TAG, "complete: fail");
                            }
                        }
                    }, new UploadOptions(null, null, false, new UpProgressHandler() {
                        @Override
                        public void progress(String key, double percent) {
                            Log.i(TAG, "upload percent:" + percent);
                        }
                    }, new UpCancellationSignal() {
                        @Override
                        public boolean isCancelled() {
                            return isCancelled;
                        }
                    }));
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                Log.i(TAG, "onError: " + e.getMessage());
            }
        });


    }

    private static class Holder {
        private static final QiniuManager INSTANCE = new QiniuManager();
    }
}
