package com.wanyue.shop.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;
import com.wanyue.shop.business.ShopState;

import java.util.List;

public class StoreGoodsBean  {
    private String description;
    @SerializedName("store_name")
    @JSONField(name = "store_name")
    private String name;

    @SerializedName("video_link")
    @JSONField(name = "video_link")
    private String videoLink;

    @SerializedName("ot_price")
    @JSONField(name = "ot_price")
    private String originalPrice;

    private String price;

    private int sales;

    private int stock;

    @SerializedName("unit_name")
    @JSONField(name = "unit_name")
    private String unitName;


    @SerializedName("slider_image")
    @JSONField(name = "slider_image")
    private List<String> slideImage;

    @SerializedName("give_integral")
    @JSONField(name = "give_integral")
    private String integral;

    @SerializedName("is_seckill")
    @JSONField(name = "is_seckill")
    private int isSeckill;

    private String  id;

    private boolean userCollect;
    private boolean userLike;

   private String image;

    private String pid;


    @SerializedName("mer_id")
    @JSONField(name = "mer_id")
    private int storeId; //店铺id

    @SerializedName("shop_name")
    @JSONField(name = "shop_name")
    private String storeName; //店铺名称

    @SerializedName("shop_avatar")
    @JSONField(name = "shop_avatar")
    private String storeAvatar; //店铺头像


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public List<String> getSlideImage() {
        return slideImage;
    }

    public void setSlideImage(List<String> slideImage) {
        this.slideImage = slideImage;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public boolean isUserLike() {
        return userLike;
    }
    public void setUserLike(boolean userLike) {
        this.userLike = userLike;
    }
    public boolean isUserCollect() {
        return userCollect;
    }
    public void setUserCollect(boolean userCollect) {
        this.userCollect = userCollect;
    }
    public int getIsSeckill() {
        return isSeckill;
    }
    public void setIsSeckill(int isSeckill) {
        this.isSeckill = isSeckill;
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

    public String getId() {
        return id;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreAvatar() {
        return storeAvatar;
    }

    public void setStoreAvatar(String storeAvatar) {
        this.storeAvatar = storeAvatar;
    }

    public String getCategory() {
        return isSeckill==1? ShopState.PRODUCT_TYPE_SECKILL :ShopState.PRODUCT_TYPE_NORMAL;
    }


    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
