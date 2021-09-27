package com.wanyue.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;

/**
 * Created by  on 2019/7/9.
 */

public class GoodsBean implements Parcelable {

    @SerializedName("id")
    @JSONField(name = "id")
    private String mId;
    private String mUid;
    private String mVideoId;
    private String mLink;

    @JSONField(name = "store_name")
    @SerializedName("store_name")
    private String mName;
    @SerializedName("price")
    @JSONField(name = "price")
    private String mPriceNow;

    @SerializedName("unit_name")
    @JSONField(name = "unit_name")
    private String unitName;

    private String mDes;

    @SerializedName("image")
    @JSONField(name = "image")
    private String mThumb;
    private String mHits;
    private String mLocalPath;
    private boolean mAdded;
    private String addtime;
    private int issale;
    private int status;
    private int type;
    private String storeName;
    private String consignment; //代销收益
    private String sales;
    private String unitPrice;
    private SpecsValueBean attrInfo;
    @SerializedName("is_seckill")
    @JSONField(name = "is_seckill")
    private int isSeckill;

    private String salenums;

    public GoodsBean() {

    }

    @JSONField(name = "href")
    public String getLink() {
        return mLink;
    }

    @JSONField(name = "href")
    public void setLink(String link) {
        mLink = link;
    }


    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }



    public String getPriceNow() {
        return mPriceNow;
    }


    public String getUnitPrice() {
        if(unitPrice==null){
           unitPrice= "¥"+mPriceNow;
        }
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setPriceNow(String priceNow) {
        mPriceNow = priceNow;
    }

    @JSONField(name = "des")
    public String getDes() {
        return mDes;
    }

    @JSONField(name = "des")
    public void setDes(String des) {
        mDes = des;
    }


    public String getThumb() {
        return mThumb;
    }


    public void setThumb(String thumb) {
        mThumb = thumb;
    }


    public String getId() {
        return mId;
    }


    public void setId(String id) {
        mId = id;
    }

    @JSONField(name = "uid")
    public String getUid() {
        if(mUid==null) {
            return "";
        }
        return mUid;
    }

    @JSONField(name = "uid")
    public void setUid(String uid) {
        mUid = uid;
    }

    @JSONField(name = "videoid")
    public String getVideoId() {
        return mVideoId;
    }

    @JSONField(name = "videoid")
    public void setVideoId(String videoId) {
        mVideoId = videoId;
    }

    @JSONField(name = "hits")
    public String getHits() {
        return mHits;
    }

    @JSONField(name = "hits")
    public void setHits(String hits) {
        mHits = hits;
    }

    @JSONField(serialize = false)
    public String getLocalPath() {
        return mLocalPath;
    }

    @JSONField(serialize = false)
    public void setLocalPath(String localPath) {
        mLocalPath = localPath;
    }

    @JSONField(serialize = false)
    public boolean isAdded() {
        return mAdded;
    }

    @JSONField(serialize = false)
    public void setAdded(boolean added) {
        mAdded = added;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mUid);
        dest.writeString(mVideoId);
        dest.writeString(mLink);
        dest.writeString(mName);
        dest.writeString(mPriceNow);
        dest.writeString(mDes);
        dest.writeString(mThumb);
        dest.writeString(mHits);
        dest.writeString(mLocalPath);
        dest.writeString(addtime);
        dest.writeInt(issale);
        dest.writeInt(status);
        dest.writeInt(type);
        dest.writeString(storeName);
        dest.writeString(consignment);
        dest.writeString(unitName);

    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }



    @JSONField(name = "is_sale")

    public int getIssale() {
        return issale;
    }

    @JSONField(name = "is_sale")
    public void setIssale(int issale) {
        this.issale = issale;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public GoodsBean(Parcel in) {
        mId = in.readString();
        mUid = in.readString();
        mVideoId = in.readString();
        mLink = in.readString();
        mName = in.readString();
        mPriceNow = in.readString();
        mDes = in.readString();
        mThumb = in.readString();
        mHits = in.readString();
        mLocalPath = in.readString();
        addtime = in.readString();
        issale=in.readInt();
        status=in.readInt();
        type=in.readInt();
        storeName = in.readString();
        consignment= in.readString();
        unitName= in.readString();
    }



    @JSONField(name = "bring_price")
    public String getConsignment() {
        return consignment;
    }


    @JSONField(name = "bring_price")
    public void setConsignment(String consignment) {
        this.consignment = consignment;
    }


    public SpecsValueBean getAttrInfo() {
        return attrInfo;
    }

    public void setAttrInfo(SpecsValueBean attrInfo) {
        this.attrInfo = attrInfo;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public static final Creator<GoodsBean> CREATOR = new Creator<GoodsBean>() {
        @Override
        public GoodsBean createFromParcel(Parcel in) {
            return new GoodsBean(in);
        }
        @Override
        public GoodsBean[] newArray(int size) {
            return new GoodsBean[size];
        }
    };

    public int getIsSeckill() {
        return isSeckill;
    }

    public void setIsSeckill(int isSeckill) {
        this.isSeckill = isSeckill;
    }


    public String getCategory() {
        return isSeckill==1?"product_seckill":"product";
    }

    public String getSalenums() {
        return salenums;
    }

    public void setSalenums(String salenums) {
        this.salenums = salenums;
    }
}
