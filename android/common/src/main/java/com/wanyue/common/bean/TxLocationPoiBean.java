package com.wanyue.common.bean;

/**
 * Created by  on 2018/7/18.
 * 腾讯定位sdk 的poi 实体类
 */

public class TxLocationPoiBean {

    private String id;
    private String title;
    private String address;
    private String category;
    private Location location;
    private double _distance;
    private String _dir_desc;
    private boolean checked;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public double get_distance() {
        return _distance;
    }

    public void set_distance(double _distance) {
        this._distance = _distance;
    }

    public String get_dir_desc() {
        return _dir_desc;
    }

    public void set_dir_desc(String _dir_desc) {
        this._dir_desc = _dir_desc;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public static class Location {
        private double lat;
        private double lng;

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }
    }
}
