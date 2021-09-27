package com.yunbao.im.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;
import com.yunbao.common.bean.UserBean;

/**
 * Created by  on 2017/8/14.
 * IM 聊天用户 实体类
 */

public class ImUserBean extends UserBean {

    private String lastMessage;
    private int unReadCount;
    private String lastTime;
    private int fromType;
    private int msgType;
    private boolean anchorItem;
    private boolean hasConversation;
    private long lastTimeStamp;


    public int getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public boolean isAnchorItem() {
        return anchorItem;
    }

    public void setAnchorItem(boolean anchorItem) {
        this.anchorItem = anchorItem;
    }

    public long getLastTimeStamp() {
        return lastTimeStamp;
    }

    public void setLastTimeStamp(long lastTimeStamp) {
        this.lastTimeStamp = lastTimeStamp;
    }

    public boolean isHasConversation() {
        return hasConversation;
    }

    public void setHasConversation(boolean hasConversation) {
        this.hasConversation = hasConversation;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.lastMessage);
        dest.writeInt(this.unReadCount);
        dest.writeString(this.lastTime);
        dest.writeInt(this.fromType);
        dest.writeLong(this.lastTimeStamp);
    }

    public ImUserBean() {

    }

    protected ImUserBean(Parcel in) {
        super(in);
        this.lastMessage = in.readString();
        this.unReadCount = in.readInt();
        this.lastTime = in.readString();
        this.fromType = in.readInt();
        this.lastTimeStamp = in.readLong();
    }

    public static final Creator<ImUserBean> CREATOR = new Creator<ImUserBean>() {
        @Override
        public ImUserBean[] newArray(int size) {
            return new ImUserBean[size];
        }

        @Override
        public ImUserBean createFromParcel(Parcel in) {
            return new ImUserBean(in);
        }
    };

}
