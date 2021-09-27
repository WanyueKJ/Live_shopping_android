package com.wanyue.live.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by  on 2019/4/27.
 */

public class LiveAdminRoomBean implements Parcelable{
    private String mLiveUid;
    private String mUserNiceName;
    private String mAvatar;
    private String mAvatarThumb;
    private int mSex;
    private int mLevel;


    public LiveAdminRoomBean() {
    }


    @JSONField(name = "liveuid")
    public String getLiveUid() {
        return mLiveUid;
    }

    @JSONField(name = "liveuid")
    public void setLiveUid(String liveUid) {
        mLiveUid = liveUid;
    }

    @JSONField(name = "nickname")
    public String getUserNiceName() {
        return mUserNiceName;
    }

    @JSONField(name = "nickname")
    public void setUserNiceName(String userNiceName) {
        mUserNiceName = userNiceName;
    }

    @JSONField(name = "avatar")
    public String getAvatar() {
        return mAvatar;
    }

    @JSONField(name = "avatar")
    public void setAvatar(String avatar) {
        mAvatar = avatar;
    }

    @JSONField(name = "avatar_thumb")
    public String getAvatarThumb() {
        return mAvatarThumb;
    }

    @JSONField(name = "avatar_thumb")
    public void setAvatarThumb(String avatarThumb) {
        mAvatarThumb = avatarThumb;
    }

    @JSONField(name = "sex")
    public int getSex() {
        return mSex;
    }

    @JSONField(name = "sex")
    public void setSex(int sex) {
        mSex = sex;
    }

    @JSONField(name = "level")
    public int getLevel() {
        return mLevel;
    }

    @JSONField(name = "level")
    public void setLevel(int level) {
        mLevel = level;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mLiveUid);
        dest.writeString(mUserNiceName);
        dest.writeString(mAvatar);
        dest.writeString(mAvatarThumb);
        dest.writeInt(mSex);
        dest.writeInt(mLevel);
    }

    public LiveAdminRoomBean(Parcel in) {
        mLiveUid = in.readString();
        mUserNiceName = in.readString();
        mAvatar = in.readString();
        mAvatarThumb = in.readString();
        mSex = in.readInt();
        mLevel = in.readInt();
    }

    public static final Creator<LiveAdminRoomBean> CREATOR = new Creator<LiveAdminRoomBean>() {
        @Override
        public LiveAdminRoomBean createFromParcel(Parcel in) {
            return new LiveAdminRoomBean(in);
        }

        @Override
        public LiveAdminRoomBean[] newArray(int size) {
            return new LiveAdminRoomBean[size];
        }
    };
}
