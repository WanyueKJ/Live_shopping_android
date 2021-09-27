package com.wanyue.shop.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.wanyue.common.bean.GoodsBean;

public class GoodsTypeBean extends GoodsBean implements MultiItemEntity {
    public static final  int TYPE_DOULE=0;
    public static final  int TYPE_SINGLE=1;
    private int itemType=TYPE_DOULE;
    @Override
    public int getItemType() {
        return itemType;
    }



    public void setItemType(int itemType) {
        this.itemType = itemType;
    }
}
