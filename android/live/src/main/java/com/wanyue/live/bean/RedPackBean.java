package com.wanyue.live.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by  on 2018/11/21.
 */

public class RedPackBean implements Parcelable {

    private int id;
    private String uid;
    private String liveUid;
    private int type;
    private int sendType;
    private int coin;
    private int count;
    private String title;
    private String userNiceName;
    private String avatar;
    private int isRob;
    private int robTime;

    public RedPackBean() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @JSONField(name = "liveuid")
    public String getLiveUid() {
        return liveUid;
    }

    @JSONField(name = "liveuid")
    public void setLiveUid(String liveUid) {
        this.liveUid = liveUid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @JSONField(name = "type_grant")
    public int getSendType() {
        return sendType;
    }

    @JSONField(name = "type_grant")
    public void setSendType(int sendType) {
        this.sendType = sendType;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    @JSONField(name = "nums")
    public int getCount() {
        return count;
    }

    @JSONField(name = "nums")
    public void setCount(int count) {
        this.count = count;
    }

    @JSONField(name = "des")
    public String getTitle() {
        return title;
    }

    @JSONField(name = "des")
    public void setTitle(String title) {
        this.title = title;
    }

    @JSONField(name = "user_nicename")
    public String getUserNiceName() {
        return userNiceName;
    }

    @JSONField(name = "user_nicename")
    public void setUserNiceName(String userNiceName) {
        this.userNiceName = userNiceName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @JSONField(name = "isrob")
    public int getIsRob() {
        return isRob;
    }

    @JSONField(name = "isrob")
    public void setIsRob(int isRob) {
        this.isRob = isRob;
    }

    @JSONField(name = "second")
    public int getRobTime() {
        return robTime;
    }

    @JSONField(name = "second")
    public void setRobTime(int robTime) {
        this.robTime = robTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public RedPackBean(Parcel in) {
        this.id = in.readInt();
        this.uid = in.readString();
        this.liveUid = in.readString();
        this.type = in.readInt();
        this.sendType = in.readInt();
        this.coin = in.readInt();
        this.count = in.readInt();
        this.title = in.readString();
        this.userNiceName = in.readString();
        this.avatar = in.readString();
        this.isRob = in.readInt();
        this.robTime = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.uid);
        dest.writeString(this.liveUid);
        dest.writeInt(this.type);
        dest.writeInt(this.sendType);
        dest.writeInt(this.coin);
        dest.writeInt(this.count);
        dest.writeString(this.title);
        dest.writeString(this.userNiceName);
        dest.writeString(this.avatar);
        dest.writeInt(this.isRob);
        dest.writeInt(this.robTime);
    }

    public static final Creator<RedPackBean> CREATOR = new Creator<RedPackBean>() {
        @Override
        public RedPackBean[] newArray(int size) {
            return new RedPackBean[size];
        }

        @Override
        public RedPackBean createFromParcel(Parcel in) {
            return new RedPackBean(in);
        }
    };


}
