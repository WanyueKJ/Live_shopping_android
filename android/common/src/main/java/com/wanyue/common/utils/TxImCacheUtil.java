package com.wanyue.common.utils;

import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.http.CommonHttpConsts;
import com.wanyue.common.interfaces.CommonCallback;

import java.io.File;

/**
 * Created by  on 2018/10/17.
 * 腾讯IM 缓存工具
 */

public class TxImCacheUtil {

    public static void getImageFile(String fileName, String url, final CommonCallback<File> commonCallback) {
        if (commonCallback == null) {
            return;
        }
        File dir = new File(CommonAppConfig.CAMERA_IMAGE_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, fileName);
        if (file.exists()) {
            commonCallback.callback(file);
        } else {
            DownloadUtil downloadUtil = new DownloadUtil();
            downloadUtil.download(CommonHttpConsts.DOWNLOAD_TX_IM_IMG, dir, fileName, url, new DownloadUtil.Callback() {
                @Override
                public void onSuccess(File file) {
                    commonCallback.callback(file);
                }

                @Override
                public void onProgress(int progress) {

                }

                @Override
                public void onError(Throwable e) {
                    commonCallback.callback(null);
                }
            });
        }
    }



    public static void getVoiceFile(String fileName, String url, final CommonCallback<File> commonCallback) {
        if (commonCallback == null) {
            return;
        }
        File dir = new File(CommonAppConfig.MUSIC_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, fileName);
        if (file.exists()) {
            commonCallback.callback(file);
        } else {
            DownloadUtil downloadUtil = new DownloadUtil();
            downloadUtil.download(CommonHttpConsts.DOWNLOAD_TX_IM_VOICE, dir, fileName, url, new DownloadUtil.Callback() {
                @Override
                public void onSuccess(File file) {
                    commonCallback.callback(file);
                }

                @Override
                public void onProgress(int progress) {

                }

                @Override
                public void onError(Throwable e) {
                    commonCallback.callback(null);
                }
            });
        }
    }

}
