package com.wanyue.main.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;

public class SpreadOrderBean  implements MultiItemEntity {
    public static final int TYPE=2;

    @JSONField(name = "order_id")
    @SerializedName( "order_id")
    private String orderId;

    private String time;
    private double number;
    private String avatar;
    private String nickname;
    private String type;


    public static int getTYPE() {
        return TYPE;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getNumber() {
        return number;
    }

    public void setNumber(double number) {
        this.number = number;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int getItemType() {
        return TYPE;
    }

}

