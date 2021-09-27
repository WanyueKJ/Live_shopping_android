package com.wanyue.main.bean;

import com.wanyue.common.bean.LiveClassBean;
import com.wanyue.live.bean.LiveBean;

import java.util.List;

public class FeatureBean {
    private List<BannerBean>banner;
    private List<LiveClassBean>liveclass;
    private List<LiveBean>list;


    public List<BannerBean> getBanner() {
        return banner;
    }

    public void setBanner(List<BannerBean> banner) {
        this.banner = banner;
    }

    public List<LiveClassBean> getLiveclass() {
        return liveclass;
    }

    public void setLiveclass(List<LiveClassBean> liveclass) {
        this.liveclass = liveclass;
    }

    public List<LiveBean> getList() {
        return list;
    }

    public void setList(List<LiveBean> list) {
        this.list = list;
    }
}
