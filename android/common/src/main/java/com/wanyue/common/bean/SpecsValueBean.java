package com.wanyue.common.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;

public class SpecsValueBean {
    private String price;
    private int stock;
    private String suk;
    private String image;
    @SerializedName( "product_id")
    @JSONField(name = "product_id")
    private String goodsId;
    private int addNum;

    @SerializedName( "unique")
    @JSONField(name = "unique")
    private String id;
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getSuk() {
        return suk;
    }

    public void setSuk(String suk) {
        this.suk = suk;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public int getAddNum() {
        return addNum;
    }
    public void setAddNum(int addNum) {
        this.addNum = addNum;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
}
