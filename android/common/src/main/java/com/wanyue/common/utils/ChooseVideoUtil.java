package com.wanyue.common.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.wanyue.common.CommonApplication;
import com.wanyue.common.bean.ChooseVideoBean;
import com.wanyue.common.interfaces.CommonCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by  on 2018/6/20.
 */

public class ChooseVideoUtil {

    private ContentResolver mContentResolver;
    private Handler mHandler;
    private CommonCallback<List<ChooseVideoBean>> mCallback;
    private boolean mStop;

    public ChooseVideoUtil() {

        mContentResolver = CommonApplication.sInstance.getContentResolver();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (mCallback != null && msg != null) {
                    mCallback.callback((List<ChooseVideoBean>) msg.obj);
                }
            }
        };
    }

    public void getLocalVideoList(CommonCallback<List<ChooseVideoBean>> callback) {
        if (callback == null) {
            return;
        }
        mCallback = callback;
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mHandler != null) {
                    List<ChooseVideoBean> videoList = getAllVideo();
                    Message msg = Message.obtain();
                    msg.obj = videoList;
                    mHandler.sendMessage(msg);
                }
            }
        }).start();
    }

    private List<ChooseVideoBean> getAllVideo() {
        List<ChooseVideoBean> videoList = new ArrayList<>();
        String[] mediaColumns = new String[]{
                MediaStore.Video.VideoColumns._ID,
                MediaStore.Video.VideoColumns.DATA,
                MediaStore.Video.VideoColumns.DISPLAY_NAME,
                MediaStore.Video.VideoColumns.DURATION
        };
        Cursor cursor = null;
        try {
            cursor = mContentResolver.query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    mediaColumns, null, null, null);
            if (cursor != null) {
                while (!mStop && cursor.moveToNext()) {
                    String videoPath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                    File file = new File(videoPath);
                    boolean canRead = file.canRead();
                    long length = file.length();
                    if (!canRead || length == 0) {
                        continue;
                    }
                    long duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                    if (duration <= 0) {
                        continue;
                    }
                    String videoName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
                    if (TextUtils.isEmpty(videoName) || !videoName.endsWith(".mp4")) {
                        continue;
                    }
                    ChooseVideoBean bean = new ChooseVideoBean(ChooseVideoBean.FILE);
                    bean.setVideoFile(file);
                    bean.setDuration(duration);
                    bean.setVideoName(videoName);
                    bean.setDurationString(StringUtil.getDurationText(duration));
                    videoList.add(bean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return videoList;
    }

    public void release() {
        mStop = true;
        mCallback = null;
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        mHandler = null;
    }


    /**
     * 把视频保存到ContentProvider,在选择上传的时候能找到
     */
    public static void saveVideoInfo(Context context, String videoPath, long duration) {
        try {
            File videoFile = new File(videoPath);
            String fileName = videoFile.getName();
            long currentTimeMillis = System.currentTimeMillis();
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.TITLE, fileName);
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
            values.put(MediaStore.MediaColumns.DATE_MODIFIED, currentTimeMillis);
            values.put(MediaStore.MediaColumns.DATE_ADDED, currentTimeMillis);
            values.put(MediaStore.MediaColumns.DATA, videoPath);
            values.put(MediaStore.MediaColumns.SIZE, videoFile.length());
            values.put(MediaStore.Video.VideoColumns.DATE_TAKEN, currentTimeMillis);
            values.put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4");
            values.put(MediaStore.Video.VideoColumns.DURATION, duration);
            context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
