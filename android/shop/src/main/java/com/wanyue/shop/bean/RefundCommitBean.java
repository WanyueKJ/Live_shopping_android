package com.wanyue.shop.bean;

import android.text.TextUtils;

public class RefundCommitBean {
    private String text;
    private String refund_reason_wap_img;
    private String refund_reason_wap_explain;
    private String uni;

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getRefund_reason_wap_img() {
        return refund_reason_wap_img;
    }
    public void setRefund_reason_wap_img(String refund_reason_wap_img) {
        this.refund_reason_wap_img = refund_reason_wap_img;
    }
    public String getRefund_reason_wap_explain() {
        return refund_reason_wap_explain;
    }
    public void setRefund_reason_wap_explain(String refund_reason_wap_explain) {
        this.refund_reason_wap_explain = refund_reason_wap_explain;
    }
    public String getUni() {
        return uni;
    }
    public void setUni(String uni) {
        this.uni = uni;
    }

    public String check(){
        if(TextUtils.isEmpty(text)){
            return "请选择退款原因";
        }else if(text.length()>100){
            return "字数过多";
        }
        else if(TextUtils.isEmpty(uni)){
            return null;
        }
        return null;
    }


}
