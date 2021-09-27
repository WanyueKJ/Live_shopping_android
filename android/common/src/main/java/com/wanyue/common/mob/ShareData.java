package com.wanyue.common.mob;

/**
 * Created by  on 2018/9/20.
 */

public class ShareData {
    private String mTitle;
    private String mDes;
    private String mImgUrl;
    private String mWebUrl;
    private String filePath;

    public ShareData() {
    }

    public ShareData(String title, String des, String imgUrl, String webUrl) {
        mTitle = title;
        mDes = des;
        mImgUrl = imgUrl;
        mWebUrl = webUrl;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDes() {
        return mDes;
    }

    public void setDes(String des) {
        mDes = des;
    }

    public String getImgUrl() {
        return mImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        mImgUrl = imgUrl;
    }

    public String getWebUrl() {
        return mWebUrl;
    }

    public void setWebUrl(String webUrl) {
        mWebUrl = webUrl;
    }

    public String getFilePath() {
        return filePath;
    }
}
