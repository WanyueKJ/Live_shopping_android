package com.wanyue.main.bean;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import com.wanyue.common.utils.WordUtil;
import com.wanyue.main.R;

public class SpreadManBean {

    /*
    ├─ uid	string	非必须		用户ID
├─ nickname	string	非必须		昵称
├─ avatar	string	非必须		头像
├─ time	string	非必须		添加时间
├─ childCount	number	非必须		推广人数
├─ orderCount	number	非必须		订单数量
├─ numberCount
    */


    private String uid;
    private String nickname;
    private String time;
    private int childCount;
    private int orderCount;
    private String numberCount;
    private String avatar;

    private Spannable result;

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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getChildCount() {
        return childCount;
    }

    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    public String getNumberCount() {
        return numberCount;
    }

    public void setNumberCount(String numberCount) {
        this.numberCount = numberCount;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public Spannable getResult() {
        if(result==null){
            String childCountString=Integer.toString(childCount);
            String orderCountString=Integer.toString(orderCount);
            String content= WordUtil.getString(R.string.spread_result_tip1,
                    childCountString,
                    orderCountString,
                    numberCount
            );
            SpannableStringBuilder style = new SpannableStringBuilder(content);
            result=style;
            style.setSpan(new ForegroundColorSpan(Color.parseColor("#FF5121")), 0, childCountString.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        return result;
    }
}
