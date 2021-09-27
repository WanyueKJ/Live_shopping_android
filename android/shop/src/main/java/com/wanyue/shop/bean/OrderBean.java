package com.wanyue.shop.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.common.utils.WordUtil;
import com.wanyue.shop.R;
import com.wanyue.shop.business.ShopEvent;
import com.wanyue.shop.business.ShopState;

import java.util.List;

public class OrderBean implements MultiItemEntity {

    @JSONField(name = "order_id")
    @SerializedName("order_id")
    private String orderId;
    @JSONField(name = "total_num")
    @SerializedName("total_num")
    private int totalNum;

    @JSONField(name = "total_price")
    @SerializedName("total_price")
    private String totalPrice;

    @JSONField(name = "_add_time")
    @SerializedName("_add_time")
    private String addTime;

    @JSONField(name = "refund_status")
    @SerializedName("refund_status")
    private int refundStatus;

    @JSONField(name = "mer_id")
    @SerializedName("mer_id")
    private String storeId;

    @JSONField(name = "shop_name")
    @SerializedName("shop_name")
    private String shopName;

    @JSONField(name = "_status")
    @SerializedName("_status")
    private OrderStatus orderStatus;

    @JSONField(name = "status_pic")
    @SerializedName("status_pic")
    private String statusPic;

    @JSONField(name = "pay_price")
    @SerializedName("pay_price")
    private String payPrice;

    @JSONField(name = "pay_postage")
    @SerializedName("pay_postage")
    private String payPostage;


    private int status;
    private String id;

    List<ShopCartBean>cartInfo;

    @SerializedName("real_name")
    @JSONField(name = "real_name")
    private String name;

    @SerializedName("user_address")
    @JSONField(name = "user_address")
    private String address;

    @SerializedName("user_phone")
    @JSONField(name = "user_phone")
    private String phone;

    @SerializedName("delivery_name")
    @JSONField(name = "delivery_name")
    private String deliveryName;//	string	非必须		快递名称/送货人姓名

    @SerializedName("delivery_type")
    @JSONField(name = "delivery_type")
    private String deliveryType;//	string	非必须		发货类型|send=送货,express=发货

    @SerializedName("delivery_id")
    @JSONField(name = "delivery_id")
    private String deliveryId;//	string	非必须		快递单号/手机号

    @SerializedName("bring_price")
    @JSONField(name = "bring_price")
    private String bringPrice;
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(int refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ShopCartBean> getCartInfo() {
        return cartInfo;
    }

    public void setCartInfo(List<ShopCartBean> cartInfo) {
        this.cartInfo = cartInfo;
    }

    @Override
    public int getItemType() {
        if(orderStatus!=null){
           return orderStatus.getType();
        }
        return -1;
    }

    private String bringPriceTip;
    public String getBringPriceTip(){
        if(bringPriceTip==null){
           bringPriceTip="代销收益"+StringUtil.getPrice(bringPrice);
        }
        return bringPriceTip;
    }



    public String getDeliveryName() {
        return deliveryName;
    }

    public void setDeliveryName(String deliveryName) {
        this.deliveryName = deliveryName;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public String getDeliveryTypeName() {
        if(StringUtil.equals(deliveryType, ShopState.ORDER_LOGISTICS_SEND)){
            return WordUtil.getString(R.string.delivery);
        }
        return WordUtil.getString(R.string.deliver_goods);
    }



    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(String deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getName() {
        return name;
    }

    public String getNameAndPhone() {
        return StringUtil.contact(name,"\t\t\t"+phone);
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }


    public String getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(String payPrice) {
        this.payPrice = payPrice;
    }

    public String getPayPostage() {
        return payPostage;
    }

    public void setPayPostage(String payPostage) {
        this.payPostage = payPostage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatusPic() {
        return statusPic;
    }

    public void setStatusPic(String statusPic) {
        this.statusPic = statusPic;
    }

    public String getBringPrice() {
        return bringPrice;
    }

    public void setBringPrice(String bringPrice) {
        this.bringPrice = bringPrice;
    }
}
