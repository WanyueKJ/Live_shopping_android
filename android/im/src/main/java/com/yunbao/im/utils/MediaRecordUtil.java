package com.yunbao.im.utils;

import android.media.MediaRecorder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by  on 2018/7/19.
 */

public class MediaRecordUtil {

    private MediaRecorder mMediaRecorder;
    private long mStartTime;
    private ExecutorService mExecutorService;
    private String mOutPutPath;
    private Runnable mStartRecordRunable;
    private boolean mStartRecord;

    public MediaRecordUtil() {
        mMediaRecorder = new MediaRecorder();
        mExecutorService = Executors.newSingleThreadExecutor();
        mStartRecordRunable = new Runnable() {
            @Override
            public void run() {
                mStartRecord = false;
                try {
                    if (mStartTime > 0) {
                        mMediaRecorder.reset();
                    }
                    mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                    mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                    mMediaRecorder.setAudioSamplingRate(44100);
                    mMediaRecorder.setAudioEncodingBitRate(192000);
                    mMediaRecorder.setOutputFile(mOutPutPath);
                    mMediaRecorder.prepare();
                    mMediaRecorder.start();
                    mStartTime = System.currentTimeMillis();
                    mStartRecord = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }


    public void startRecord(String outPutPath) {
        mOutPutPath = outPutPath;
        mExecutorService.submit(mStartRecordRunable);
    }

    public long stopRecord() {
        if (mStartRecord) {
            mStartRecord = false;
            try {
                mMediaRecorder.setOnErrorListener(null);
                mMediaRecorder.setOnInfoListener(null);
                mMediaRecorder.setPreviewDisplay(null);
                mMediaRecorder.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return System.currentTimeMillis() - mStartTime;
        }
        return 0;
    }

    public void release() {
        if (mMediaRecorder != null) {
            try {
                mMediaRecorder.setOnErrorListener(null);
                mMediaRecorder.setOnInfoListener(null);
                mMediaRecorder.setPreviewDisplay(null);
                mMediaRecorder.stop();
                mMediaRecorder.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (mExecutorService != null) {
            mExecutorService.shutdownNow();
        }
    }
}
