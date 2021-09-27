package com.yunbao.im.bean;

import android.text.TextUtils;

import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMElemType;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMSoundElem;
import com.tencent.imsdk.ext.message.TIMMessageExt;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.bean.ChatGiftBean;
import com.yunbao.common.bean.ChatReceiveGiftBean;

import java.io.File;


/**
 * Created by  on 2018/7/12.
 * IM 消息实体类
 */

public class ImMessageBean {

    public static final int TYPE_TEXT = 1;
    public static final int TYPE_IMAGE = 2;
    public static final int TYPE_VOICE = 3;
    public static final int TYPE_LOCATION = 4;
    public static final int TYPE_GIFT = 5;
    public static final int TYPE_ORDER = 6;
    public static final int TYPE_CALL = 7;

    private String uid;//发消息的人的id
    private TIMMessage timRawMessage;//腾讯IM消息对象
    private int type;
    private boolean fromSelf;
    private long time;
    private File imageFile;
    private boolean loading;
    private boolean sendFail;
    private ChatReceiveGiftBean mGiftBean;
    private ChatInfoBean chatInfoBean;

    public ImMessageBean(String uid, TIMMessage timRawMessage, int type, boolean fromSelf) {
        this.uid = uid;
        this.timRawMessage = timRawMessage;
        this.type = type;
        this.fromSelf = fromSelf;
        this.time = timRawMessage.timestamp() * 1000;
    }
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    public TIMMessage getTimRawMessage() {
        return timRawMessage;
    }

    public void setTimRawMessage(TIMMessage timRawMessage) {
        this.timRawMessage = timRawMessage;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isFromSelf() {
        return fromSelf;
    }

    public void setFromSelf(boolean fromSelf) {
        this.fromSelf = fromSelf;
    }

    public long getTime() {
        return time;
    }

    public File getImageFile() {
        return imageFile;
    }

    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public boolean isSendFail() {
        return sendFail;
    }

    public void setSendFail(boolean sendFail) {
        this.sendFail = sendFail;
    }

    public int getVoiceDuration() {
        int duration = 0;
        if (timRawMessage != null) {
            if (timRawMessage.getElementCount() > 0) {
                TIMElem elem0 = timRawMessage.getElement(0);
                if (elem0 != null && elem0.getType() == TIMElemType.Sound) {
                    TIMSoundElem e = (TIMSoundElem) elem0;
                    duration = (int) e.getDuration();
                }
            }
        }
        return duration;
    }

    public boolean isRead() {
        if (timRawMessage != null) {
            TIMMessageExt ext = new TIMMessageExt(timRawMessage);
            if (ext.getCustomInt() == 1) {
                return true;
            }
            if (timRawMessage.getElementCount() > 0) {
                TIMElem elem0 = timRawMessage.getElement(0);
                if (elem0 != null && elem0.getType() == TIMElemType.Sound) {
                    TIMSoundElem e = (TIMSoundElem) elem0;
                    String localPath = CommonAppConfig.MUSIC_PATH + e.getUuid();
                    if (!TextUtils.isEmpty(localPath)) {
                        File file = new File(localPath);
                        if (file.exists()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public ChatInfoBean getChatInfoBean() {
        return chatInfoBean;
    }

    public void setChatInfoBean(ChatInfoBean chatInfoBean) {
        this.chatInfoBean = chatInfoBean;
    }

    public ChatReceiveGiftBean getGiftBean() {
        return mGiftBean;
    }

    public void setGiftBean(ChatReceiveGiftBean giftBean) {
        mGiftBean = giftBean;
    }

}
