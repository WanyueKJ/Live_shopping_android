package com.wanyue.common.utils;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;

import java.io.File;

/**
 * Created by  on 2017/9/4.
 */

public class DownloadUtil {

    public void download(String tag, String fileDir, String fileName, String url, final Callback callback) {
        File file = new File(fileDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        OkGo.<File>get(url).tag(tag).execute(new FileCallback(fileDir, fileName) {
            @Override
            public void onSuccess(Response<File> response) {
                //下载成功结束后的回调
                if (callback != null) {
                    callback.onSuccess(response.body());
                }
            }

            @Override
            public void downloadProgress(Progress progress) {
                if (callback != null) {
                    int val = (int) (progress.currentSize * 100 / progress.totalSize);
                    L.e("下载进度--->" + val);
                    callback.onProgress(val);
                }
            }

            @Override
            public void onError(Response<File> response) {
                super.onError(response);
                Throwable e = response.getException();
                L.e("下载失败--->" + e);
                if (callback != null) {
                    callback.onError(e);
                }
            }
        });
    }

    public void download(String tag, File fileDir, String fileName, String url, final Callback callback) {
        OkGo.<File>get(url).tag(tag).execute(new FileCallback(fileDir.getAbsolutePath(), fileName) {
            @Override
            public void onSuccess(Response<File> response) {
                //下载成功结束后的回调
                if (callback != null) {
                    callback.onSuccess(response.body());
                }
            }

            @Override
            public void downloadProgress(Progress progress) {
                if (callback != null) {
                    int val = (int) (progress.currentSize * 100 / progress.totalSize);
                    L.e("下载进度--->" + val);
                    callback.onProgress(val);
                }
            }

            @Override
            public void onError(Response<File> response) {
                super.onError(response);
                Throwable e = response.getException();
                L.e("下载失败--->" + e);
                if (callback != null) {
                    callback.onError(e);
                }
            }
        });
    }


    public interface Callback {
        void onSuccess(File file);

        void onProgress(int progress);

        void onError(Throwable e);
    }
}
