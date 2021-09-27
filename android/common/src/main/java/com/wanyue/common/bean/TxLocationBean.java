package com.wanyue.common.bean;

import java.util.List;

/**
 * Created by  on 2018/7/18.
 * 腾讯定位结果的实体类
 */

public class TxLocationBean {
    private double lng;//经度
    private double lat;//纬度
    private String nation;//国家
    private String province;//省
    private String city;//市
    private String district;//区
    private String street;//街道
    private String address;//完整地址
    private List<TxLocationPoiBean> poiList;//周边

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<TxLocationPoiBean> getPoiList() {
        return poiList;
    }

    public void setPoiList(List<TxLocationPoiBean> poiList) {
        this.poiList = poiList;
    }
}
