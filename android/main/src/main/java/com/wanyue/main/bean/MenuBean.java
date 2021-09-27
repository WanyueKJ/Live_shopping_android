package com.wanyue.main.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;

public class MenuBean {
    private int id=-1;
    private String pic;
    private String name;

    public MenuBean() {
    }

    public MenuBean(String name, int id, int localIcon) {
        this.name = name;
        this.id = id;
        this.localIcon = localIcon;
    }

    @SerializedName("wap_url")
    @JSONField(name="wap_url")
    private String url;

    private int localIcon;

    public String getName() {
        return name;
    }


    public int getLocalIcon() {
        return localIcon;
    }

    public void setLocalIcon(int localIcon) {
        this.localIcon = localIcon;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
