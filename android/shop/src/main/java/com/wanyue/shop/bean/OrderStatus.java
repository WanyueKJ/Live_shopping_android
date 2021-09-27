package com.wanyue.shop.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;
public class OrderStatus {
   /* "_type":1,
            "_title":"未发货",
            "_msg":"商家未发货,请耐心等待",
            "_class":"state-nfh",
            "_payType":"余额支付"*/
    @JSONField(name = "_type")
    @SerializedName("_type")
    private int type;
    @JSONField(name = "_title")
    @SerializedName("_title")
    private String title;
    @JSONField(name = "_msg")
    @SerializedName("_msg")
    private String msg;

    @JSONField(name = "_payType")
    @SerializedName("_payType")
    private String payType;





    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getPayType() {
        return payType;
    }
    public void setPayType(String payType) {
        this.payType = payType;
    }





}
