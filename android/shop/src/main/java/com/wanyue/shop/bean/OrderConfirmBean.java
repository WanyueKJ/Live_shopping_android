package com.wanyue.shop.bean;

import android.util.ArrayMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;
import com.wanyue.common.Constants;
import com.wanyue.common.bean.UserBean;
import com.wanyue.common.utils.ListUtil;
import com.wanyue.common.utils.StringUtil;

import java.util.List;

public class OrderConfirmBean {
    @SerializedName("total_price")
    @JSONField(name = "total_price")
    private double totalPrice;
    private List<ShopCartStoreBean>cartInfo;
    private String orderKey;
    private UserBean userInfo;

    @SerializedName("pay_price")
    @JSONField(name = "pay_price")
    private double payPrice;

    private String payType;
    private int  isUseCode;

    private AddressInfoBean addressInfo;
    private String liveUid;

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public int getIsUseCode() {
        return isUseCode;
    }

    public void setIsUseCode(int isUseCode) {
        this.isUseCode = isUseCode;
    }

    public List<ShopCartStoreBean> getCartInfo() {
        return cartInfo;
    }



    public void setCartInfo(List<ShopCartStoreBean> cartInfo) {
        this.cartInfo = cartInfo;
    }

    public String getOrderKey() {
        return orderKey;
    }

    public void setOrderKey(String orderKey) {
        this.orderKey = orderKey;
    }

    public UserBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserBean userInfo) {
        this.userInfo = userInfo;
    }

    public boolean haveGoods() {
        return ListUtil.haveData(cartInfo);
    }

    public double getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(double payPrice) {
        this.payPrice = payPrice;
    }

    public boolean checkBalance() {
       if(userInfo!=null){
         return   userInfo.getBalance()>=totalPrice;
       }
       return false;
    }

    public boolean checkIsWxPay() {
        return StringUtil.equals(payType, Constants.PAY_TYPE_WX);

    }

    public AddressInfoBean getAddressInfo() {
        return addressInfo;
    }
    public void setAddressInfo(AddressInfoBean addressInfo) {
        this.addressInfo = addressInfo;
    }

    public int getAddrId(){
      return addressInfo==null?0:addressInfo.getId();
    }
    public String getCouponJson(){
        return "{}";
    }


    public String getLiveUid() {
        return liveUid;
    }

    public void setLiveUid(String liveUid) {
        this.liveUid = liveUid;
    }

    public String getMarkJson() {
        if(!ListUtil.haveData(cartInfo)){
            return "{}";
        }
        ArrayMap<String,String> map=new ArrayMap<>();
        for(ShopCartStoreBean storeBean:cartInfo){
            String mark=storeBean.getMarks();
            map.put(storeBean.getId(),mark);
        }
        return JSON.toJSONString(map);
    }
}
