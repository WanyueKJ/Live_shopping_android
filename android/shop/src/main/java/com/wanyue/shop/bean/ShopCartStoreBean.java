package com.wanyue.shop.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;
import com.wanyue.common.utils.ListUtil;
import com.wanyue.common.utils.StringUtil;

import java.util.List;

public class ShopCartStoreBean  extends AbstractExpandableItem<ShopCartBean> implements MultiItemEntity {
    public static final int LEVEL_TYPE=0;
    public static final int TYPE_VALID=11;
    public static final int TYPE_INVALID=12;
    private List<ShopCartBean> list;
    @JSONField(name = "mer_id")
    @SerializedName("mer_id")
    private String id;
    private String name;
    private boolean isInvalid;
    private boolean isChecked=true;
    private PriceGroup priceGroup;
    private int totalCartNum;
    private String marks;
    private String cartIds;


    public List<ShopCartBean> getList() {
        return list;
    }

    public void setList(List<ShopCartBean> list) {
        this.list = list;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isInvalid() {
        return isInvalid;
    }

    public void setInvalid(boolean invalid) {
        isInvalid = invalid;
    }

    @Override
    public int getLevel() {
        return LEVEL_TYPE;
    }

    public void format(){
        setSubItems(list);
        if(ListUtil.haveData(list)){ //将商品跟店铺挂钩
            for(ShopCartBean shopCartBean:list){
               shopCartBean.setStore(this);
            }
        }
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isHaveNoCheckGoods(){
        if(!ListUtil.haveData(list)){
            return true;
        }
        boolean isHaveNoCheckGoods=false;
        for(ShopCartBean shopCartBean:list){
            if(!shopCartBean.isChecked()){
                isHaveNoCheckGoods=true;
                return isHaveNoCheckGoods;
            }
        }
        return isHaveNoCheckGoods;
    }



    public PriceGroup getPriceGroup() {
        return priceGroup;
    }

    public String getMarks() {
        return marks;
    }
    public void setMarks(String marks) {
        this.marks = marks;
    }
    public void setPriceGroup(PriceGroup priceGroup) {
        this.priceGroup = priceGroup;
    }

    @Override
    public int getItemType() {
        if(isInvalid){
            return TYPE_INVALID;
        }else{
            return TYPE_VALID;
        }
    }


    /*获取店铺下所有购物车商品id拼接*/
    public String getCartIds() {
        if(cartIds==null&&list!=null){
           cartIds= StringUtil.contact(list);
        }
        return cartIds;
    }



    public void setCartIds(String cartIds) {
        this.cartIds = cartIds;
    }

    public int getTotalCartNum() {
        if(totalCartNum==0){
            for(ShopCartBean shopCartBean:list){
                totalCartNum+=shopCartBean.getCartNum();
            }
        }
        return totalCartNum;
    }
}
