package com.wanyue.common.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;

/**
 * Created by  on 2018/9/28.
 * 我的 页面的item
 */

public class UserItemBean {

    @SerializedName( "id")
    @JSONField(name = "id")
    private int mId;

    @SerializedName("name")
    @JSONField(name = "name")
    private String mName;

    @SerializedName( "thumb")
    @JSONField(name = "thumb")
    private String mThumb;

    @SerializedName( "href")
    @JSONField(name = "href")
    private String mHref;

    private String mText;


    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getThumb() {
        return mThumb;
    }

    public void setThumb(String thumb) {
        mThumb = thumb;
    }

    public String getHref() {
        return mHref;
    }

    public void setHref(String href) {
        mHref = href;
    }

    @JSONField(serialize = false)
    public String getText() {
        return mText;
    }

    @JSONField(serialize = false)
    public void setText(String text) {
        mText = text;
    }
}
