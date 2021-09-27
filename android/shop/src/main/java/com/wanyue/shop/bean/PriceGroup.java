package com.wanyue.shop.bean;

import androidx.annotation.Nullable;
import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;
import com.wanyue.common.utils.ObjectUtil;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.common.utils.WordUtil;
import com.wanyue.shop.R;

public class PriceGroup {
    private String costPrice;
    private String storeFreePostage;
    private double storePostage;
    private double totalPrice;
    private String vipPrice;
    private String storePostageFormat;

    @JSONField(name = "mer_id")
    @SerializedName( "mer_id")
    private String storeId;


    public String getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(String costPrice) {
        this.costPrice = costPrice;
    }

    public String getStoreFreePostage() {
        return storeFreePostage;
    }

    public void setStoreFreePostage(String storeFreePostage) {
        this.storeFreePostage = storeFreePostage;
    }

    public double getStorePostage() {
        return storePostage;
    }

    public String getFormatStorePostage(){
        if(storePostageFormat!=null){
            return storePostageFormat;
        }
        if(storePostage>0){
           storePostageFormat= StringUtil.getFormatPrice(storePostage);
        }else{
           storePostageFormat= WordUtil.getString(R.string.free_postage);
        }
        return storePostageFormat;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStorePostageFormat() {
        return storePostageFormat;
    }

    public void setStorePostageFormat(String storePostageFormat) {
        this.storePostageFormat = storePostageFormat;
    }

    public void setStorePostage(double storePostage) {
        this.storePostage = storePostage;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getVipPrice() {
        return vipPrice;
    }

    public void setVipPrice(String vipPrice) {
        this.vipPrice = vipPrice;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(ObjectUtil.isAClass(this,obj)){
            PriceGroup group= (PriceGroup) obj;
            return StringUtil.equals(storeId,group.storeId);
        }
        return super.equals(obj);
    }
}
