package com.wanyue.common.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;
import android.text.TextUtils;

import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.CommonApplication;
import com.wanyue.common.interfaces.CommonCallback;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by  on 2018/6/20.
 */

public class VideoLocalUtil {
    public static final int REQUEST_RECORD=1;
    private static final int DURCATION_LIMIT=60000;

    private ContentResolver mContentResolver;
    private Handler mHandler;
    private CommonCallback<List<VideoChooseBean>> mCallback;
    private boolean mStop;

    public VideoLocalUtil() {
        mContentResolver = CommonApplication.sInstance.getContentResolver();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (mCallback != null && msg != null) {
                    mCallback.callback((List<VideoChooseBean>) msg.obj);
                }
            }
        };
    }

    public void getLocalVideoList(CommonCallback<List<VideoChooseBean>> callback) {
        if (callback == null) {
            return;
        }
        mCallback = callback;
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mHandler != null) {
                    List<VideoChooseBean> videoList = getAllVideo();
                    Message msg = Message.obtain();
                    msg.obj = videoList;
                    mHandler.sendMessage(msg);
                }
            }
        }).start();
    }

    private List<VideoChooseBean> getAllVideo() {
        List<VideoChooseBean> videoList = new ArrayList<>();
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
                    if (duration <= 0||duration>DURCATION_LIMIT) {
                        continue;
                    }
                    String videoName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
                    if (TextUtils.isEmpty(videoName) || !videoName.endsWith(".mp4")) {
                        continue;
                    }
                    VideoChooseBean bean = new VideoChooseBean();
                    bean.setVideoPath(videoPath);
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


    public  void startOpenRecord(Activity context,File file){
        Intent intent=new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        Uri fileUri=null;
        try {
            //fileUri=Uri.fromFile(createMediaFile());如果这样写会报错
         fileUri= FileProvider.getUriForFile(context,context.getApplicationContext().getPackageName() + ".fileprovider",file);//这是正确的写法
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(fileUri==null) {
            return;
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,1);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,DURCATION_LIMIT);
        context.startActivityForResult(intent,REQUEST_RECORD);
    }

    public File createMediaFile() {
            if ((Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))) {
                // 选择自己的文件夹
                String path = CommonAppConfig.VIDEO_PATH;
                // Constants.video_url 是一个常量，代表存放视频的文件夹
                File mediaStorageDir = new File(path);
                if (!mediaStorageDir.exists()) {
                    if (!mediaStorageDir.mkdirs()) {
                        L.e("TAG", "文件夹创建失败");
                        return null;
                    }
                }
                // 文件根据当前的毫秒数给自己命名
                String timeStamp = String.valueOf(System.currentTimeMillis());
                timeStamp = timeStamp.substring(7);
                String imageFileName = "V" + timeStamp;
                String suffix = ".mp4";
                File mediaFile = new File(mediaStorageDir + File.separator + imageFileName + suffix);
                return mediaFile;
            }
        return null;
    }

}
