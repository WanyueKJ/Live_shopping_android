package com.wanyue.main.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;
import com.wanyue.common.utils.StringUtil;

public class CommissionBean implements MultiItemEntity {
    public static final int TYPE=2;

    @SerializedName("add_time")
    @JSONField(name = "add_time")
    private String time;
    private String title;
    private String number;
    private int pm;
    private String result;

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public int getPm() {
        return pm;
    }
    public void setPm(int pm) {
        this.pm = pm;
    }

    @Override
    public int getItemType() {
        return TYPE;
    }

    public  String getResult() {
        if(result==null){
          if(pm==0){
              result="-"+number;
          }else{
              result="+"+number;
          }
        }
        return result;
    }

    public String getResult(boolean isMustUnit) {
        if(isMustUnit){
          if(result==null){
             result= StringUtil.getPrice(number);
          }
          return result;
        }else{
          return getResult();
        }
    }
}
