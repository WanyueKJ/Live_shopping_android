package com.wanyue.live.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by  on 2018/11/6.
 * 守护特权
 */

public class GuardRightBean {

    private String title;
    private String des;
    private String icon0;
    private String icon1;
//    private int iconIndex;
    private boolean mChecked;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    @JSONField(name = "thumb_c")
    public String getIcon1() {
        return icon1;
    }

    @JSONField(name = "thumb_c")
    public void setIcon1(String icon1) {
        this.icon1 = icon1;
    }

    @JSONField(name = "thumb_g")
    public String getIcon0() {
        return icon0;
    }

    @JSONField(name = "thumb_g")
    public void setIcon0(String icon0) {
        this.icon0 = icon0;
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean checked) {
        mChecked = checked;
    }

//    public int getIconIndex() {
//        return iconIndex;
//    }
//
//    public void setIconIndex(int iconIndex) {
//        this.iconIndex = iconIndex;
//    }
}
