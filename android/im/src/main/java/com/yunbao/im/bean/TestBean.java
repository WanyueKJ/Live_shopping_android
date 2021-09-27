package com.yunbao.im.bean;

import com.google.gson.annotations.SerializedName;

public class TestBean  {
    @SerializedName("id")
    protected String mId;

    @SerializedName("user_nickname")
    protected String mUserNiceName;

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmUserNiceName() {
        return mUserNiceName;
    }

    public void setmUserNiceName(String mUserNiceName) {
        this.mUserNiceName = mUserNiceName;
    }
}
