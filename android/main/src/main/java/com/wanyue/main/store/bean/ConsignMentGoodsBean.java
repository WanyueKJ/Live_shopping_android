package com.wanyue.main.store.bean;

import com.wanyue.common.utils.StringUtil;

public class ConsignMentGoodsBean {
    private String id;	//number	必须		商品ID
    private String image;	//string	必须		图片
    private String store_name;	//string	必须		名称
    private String  price;	//string	必须		价格
    private String  bring_price;	//string	必须		代销价格
    private String salenums;	//number	必须		销售数量
    private String priceTip;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getPrice() {
        return price;
    }

    public String getPriceTip() {
        if(priceTip==null){
          priceTip="售价："+ StringUtil.getPrice(price);
        }
        return priceTip;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBring_price() {
        return bring_price;
    }

    public void setBring_price(String bring_price) {
        this.bring_price = bring_price;
    }

    public String getSalenums() {
        return salenums;
    }

    public void setSalenums(String salenums) {
        this.salenums = salenums;
    }
}
