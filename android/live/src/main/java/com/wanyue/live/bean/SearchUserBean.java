package com.wanyue.live.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;
import com.wanyue.common.bean.UserBean;

/**
 * Created by  on 2018/9/29.
 */

public class SearchUserBean extends UserBean {

    private int attention;

    @JSONField(name = "isattention")
    public int getAttention() {
        return attention;
    }

    @JSONField(name = "isattention")
    public void setAttention(int attention) {
        this.attention = attention;
    }

    public SearchUserBean() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.attention);
    }

    public SearchUserBean(Parcel in) {
        super(in);
        this.attention = in.readInt();
    }

    public static final Parcelable.Creator<SearchUserBean> CREATOR = new Parcelable.Creator<SearchUserBean>() {
        @Override
        public SearchUserBean[] newArray(int size) {
            return new SearchUserBean[size];
        }

        @Override
        public SearchUserBean createFromParcel(Parcel in) {
            return new SearchUserBean(in);
        }
    };
}
