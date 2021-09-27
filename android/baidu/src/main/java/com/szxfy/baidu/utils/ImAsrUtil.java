package com.wanyue.baidu.utils;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;

/**
 * Created by  on 2018/7/28.
 * 聊天时候语音识别
 */

public class ImAsrUtil {

    private EventManager mManager;
    private EventListener mEventListener;
    private String mJsonString;
    private AsrCallback mCallback;

    public ImAsrUtil(Context context) {
        mManager = EventManagerFactory.create(context, "asr");
        mEventListener = new EventListener() {
            @Override
            public void onEvent(String name, String params, byte[] bytes, int offset, int length) {
                switch (name) {
                    case SpeechConstant.CALLBACK_EVENT_ASR_READY://引擎准备就绪，可以开始说话
                        //L.e(TAG, "引擎准备就绪，可以开始说话------>"+params);
                        break;
                    case SpeechConstant.CALLBACK_EVENT_ASR_BEGIN://检测到说话开始
                        //L.e(TAG, "检测到说话开始------>" + params);
//                        if(mCallback!=null){
//                            mCallback.onSpeakStart();
//                        }
                        break;
                    case SpeechConstant.CALLBACK_EVENT_ASR_END://检测到说话结束
                        //L.e(TAG, "检测到说话结束------>" + params);
//                        if (mCallback != null) {
//                            mCallback.onSpeakEnd();
//                        }
                        break;
                    case SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL://识别结果，类型json
                        JSONObject obj = JSON.parseObject(params);
                        String result = obj.getJSONArray("results_recognition").getString(0);
                        //L.e(TAG, "识别结果------>" + result);
                        if (mCallback != null) {
                            mCallback.onResult(result);
                        }
                        break;
                    case SpeechConstant.CALLBACK_EVENT_ASR_FINISH://识别结束（可能含有错误信息）
                        //L.e(TAG, "识别结束------>" + params);
                        break;
                    case SpeechConstant.CALLBACK_EVENT_ASR_LONG_SPEECH://长语音额外的回调，表示长语音识别结束
                        //L.e(TAG, "长语音识别结束------>" + params);
                        break;

                }
            }
        };
        mManager.registerListener(mEventListener);
        JSONObject params = new JSONObject();
        //DECODER = 0 ，表示禁用离线功能，只使用在线功能；DECODER = 2 ，表示启用离线功能，但是SDK强制在线优先。
        params.put(SpeechConstant.DECODER, 0);
        //开启长语音
        params.put(SpeechConstant.VAD_ENDPOINT_TIMEOUT, 0);
        //关闭自动断句
        params.put(SpeechConstant.VAD, SpeechConstant.VAD_TOUCH);
        //在选择PID为长句（输入法模式）的时候，是否禁用标点符号
        params.put(SpeechConstant.DISABLE_PUNCTUATION, true);
        //是否需要语音音量数据回调，开启后有CALLBACK_EVENT_ASR_VOLUME事件回调
        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);
        //是否需要语音音频数据回调，开启后有CALLBACK_EVENT_ASR_AUDIO事件
        params.put(SpeechConstant.ACCEPT_AUDIO_DATA, false);
        mJsonString = params.toJSONString();
    }

    /**
     * 开始录音
     */
    public void start() {
        if (mManager != null) {
            mManager.send(SpeechConstant.ASR_START, mJsonString, null, 0, 0);
        }
    }

    /**
     * 停止录音
     */
    public void stop() {
        if (mManager != null) {
            mManager.send(SpeechConstant.ASR_STOP, null, null, 0, 0);
            mManager.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0);
        }
    }


    public void release() {
        if (mManager != null) {
            mManager.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0);
            mManager.unregisterListener(mEventListener);
        }
        mManager=null;
        mCallback=null;
    }

    public interface AsrCallback {
        //void onSpeakStart();

        void onResult(String result);

        //void onSpeakEnd();
    }

    public void setAsrCallback(AsrCallback callback){
        mCallback=callback;
    }
}
