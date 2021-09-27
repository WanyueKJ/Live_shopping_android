package com.wanyue.live.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by  on 2018/11/14.
 */

public class LiveGuardInfo implements Parcelable {

    private int myGuardType;//自己是守护类型
    private String myGuardEndTime;//守护到期时间
    private int guardNum;//守护人数

    public LiveGuardInfo() {
    }

    public int getMyGuardType() {
        return myGuardType;
    }

    public void setMyGuardType(int myGuardType) {
        this.myGuardType = myGuardType;
    }

    public String getMyGuardEndTime() {
        return myGuardEndTime;
    }

    public void setMyGuardEndTime(String myGuardEndTime) {
        this.myGuardEndTime = myGuardEndTime;
    }

    public int getGuardNum() {
        return guardNum;
    }

    public void setGuardNum(int guardNum) {
        this.guardNum = guardNum;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.myGuardType);
        dest.writeString(this.myGuardEndTime);
        dest.writeInt(this.guardNum);
    }


    protected LiveGuardInfo(Parcel in) {
        this.myGuardType = in.readInt();
        this.myGuardEndTime = in.readString();
        this.guardNum = in.readInt();
    }


    public static final Creator<LiveGuardInfo> CREATOR = new Creator<LiveGuardInfo>() {
        @Override
        public LiveGuardInfo[] newArray(int size) {
            return new LiveGuardInfo[size];
        }

        @Override
        public LiveGuardInfo createFromParcel(Parcel in) {
            return new LiveGuardInfo(in);
        }
    };


}
