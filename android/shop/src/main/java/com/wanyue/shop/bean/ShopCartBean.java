package com.wanyue.shop.bean;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.annotation.JSONField;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;
import com.wanyue.common.bean.GoodsBean;
import com.wanyue.common.utils.StringUtil;

public class ShopCartBean  implements MultiItemEntity {
    public static final int TYPE_VALID=21;
    public static final int TYPE_INVALID=22;
    private boolean isInvalid;

    private GoodsBean productInfo;
    private String id;

    @SerializedName("cart_num")
    @JSONField(name = "cart_num")
    private int cartNum;
    @SerializedName("product_id")
    @JSONField(name = "product_id")
    private String productId;

    @SerializedName("truePrice")
    @JSONField(name = "truePrice")
    private double productPrice;

    private boolean isChecked=true;

    private ShopCartStoreBean store;

    private String unique;

    @SerializedName("is_reply")
    @JSONField(name = "is_reply")
    private String isReply;

    @Override
    public int getItemType() {
        if(isInvalid){
            return TYPE_INVALID;
        }else{
            return TYPE_VALID;
        }
}

    public boolean isInvalid() {
        return isInvalid;
    }

    public void setInvalid(boolean invalid) {
        isInvalid = invalid;
    }

    public GoodsBean getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(GoodsBean productInfo) {
        this.productInfo = productInfo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCartNum() {
        return cartNum;
    }
    public void setCartNum(int cartNum) {
        this.cartNum = cartNum;
    }
    public String getProductId() {
        return productId;
    }
    public void setProductId(String productId) {
        this.productId = productId;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public void setStore(ShopCartStoreBean store) {
        this.store = store;
    }
    public ShopCartStoreBean getStore() {
        return store;
    }


    public String getIsReply() {
        return isReply;
    }

    public boolean isReply(){
      if(StringUtil.isInt(isReply)){
        return Integer.parseInt(isReply)==1;
      }else if(StringUtil.isBoolean(isReply)){
          return Boolean.parseBoolean(isReply);
      }
      return false;
    }



    public void setIsReply(String isReply) {
        this.isReply = isReply;
    }

    public String getUnique() {
        return unique;
    }

    public void setUnique(String unique) {
        this.unique = unique;
    }

    @NonNull
    @Override
    public String toString() {
        return id;
    }
}
