package com.yunbao.im.bean;

/**
 * Created by  on 2019/4/1.
 */

public class ImMsgLocationBean {
    private String mAddress;
    private int mZoom;
    private double mLat;
    private double mLng;

    public ImMsgLocationBean(String address, int zoom, double lat, double lng) {
        mAddress = address;
        mZoom = zoom;
        mLat = lat;
        mLng = lng;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public int getZoom() {
        return mZoom;
    }

    public void setZoom(int zoom) {
        mZoom = zoom;
    }

    public double getLat() {
        return mLat;
    }

    public void setLat(double lat) {
        mLat = lat;
    }

    public double getLng() {
        return mLng;
    }

    public void setLng(double lng) {
        mLng = lng;
    }
}
