package com.wanyue.shop.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class GoodsBannerBean implements MultiItemEntity {
    public static final int IS_VIDEO=0;
    public static final int IS_PHOTO=1;

    public boolean isVideo;
    public String url;
    public String thumb;

    @Override
    public int getItemType() {
        if(isVideo){
            return IS_VIDEO;
        }else{
            return IS_PHOTO;
        }
    }
}
