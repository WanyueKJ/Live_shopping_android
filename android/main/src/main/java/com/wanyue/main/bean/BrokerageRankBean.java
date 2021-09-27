package com.wanyue.main.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;

public class BrokerageRankBean {
    private String uid;
    private String nickname;
    private String avatar;
    @SerializedName("brokerage_price")
    @JSONField(name = "brokerage_price")
    private String brokeragePrice;


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBrokeragePrice() {
        return brokeragePrice;
    }

    public void setBrokeragePrice(String brokeragePrice) {
        this.brokeragePrice = brokeragePrice;
    }
}
