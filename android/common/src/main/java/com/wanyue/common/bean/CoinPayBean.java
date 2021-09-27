package com.wanyue.common.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;

/**
 * Created by  on 2019/4/11.
 */

public class CoinPayBean {
    @SerializedName("id")
    @JSONField(name = "id")
    private String mId;
    @SerializedName("name")
    @JSONField(name = "name")
    private String mName;
    @SerializedName("thumb")
    @JSONField(name = "thumb")
    private String mThumb;
    @SerializedName("href")
    @JSONField(name = "href")
    private String mHref;

    private int mIcon;

    private boolean mChecked;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
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

    @JSONField(serialize = false)
    public boolean isChecked() {
        return mChecked;
    }

    @JSONField(serialize = false)
    public void setChecked(boolean checked) {
        mChecked = checked;
    }


    @JSONField(name = "href")
    public String getHref() {
        return mHref;
    }

    @JSONField(name = "href")
    public void setHref(String href) {
        mHref = href;
    }

    public int getIcon() {
        return mIcon;
    }

    public void setIcon(int icon) {
        mIcon = icon;
    }
}
