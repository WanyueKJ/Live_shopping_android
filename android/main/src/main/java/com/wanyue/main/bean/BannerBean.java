package com.wanyue.main.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;

public class BannerBean implements IBanner{
    private String pic;

    @SerializedName("wap_url")
    @JSONField(name="wap_url")
    private String url;


    @Override
    public String getImageUrl() {
        return pic;
    }

    @Override
    public String getData() {
        return url;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
