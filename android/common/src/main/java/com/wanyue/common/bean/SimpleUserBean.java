package com.wanyue.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;

public class SimpleUserBean implements Parcelable {
    private String id;

    @SerializedName("user_nickname")
    @JSONField(name = "user_nickname")
    private String userNickname;
    private String avatar;

    public SimpleUserBean() {
    }

    protected SimpleUserBean(Parcel in) {
        id = in.readString();
        userNickname = in.readString();
        avatar = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(userNickname);
        dest.writeString(avatar);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SimpleUserBean> CREATOR = new Creator<SimpleUserBean>() {
        @Override
        public SimpleUserBean createFromParcel(Parcel in) {
            return new SimpleUserBean(in);
        }

        @Override
        public SimpleUserBean[] newArray(int size) {
            return new SimpleUserBean[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getAvatar() {
        return avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
