package com.wanyue.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by  on 2017/9/21.
 */

public class CoinBean implements Parcelable{

    private String id;
    private String coin;
    private String money;
    private String give;
    private boolean checked;

    public CoinBean(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getGive() {
        return give;
    }

    public void setGive(String give) {
        this.give = give;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public CoinBean(Parcel in) {
        id = in.readString();
        coin = in.readString();
        money = in.readString();
        give = in.readString();
        checked = in.readByte() != 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(coin);
        dest.writeString(money);
        dest.writeString(give);
        dest.writeByte((byte) (checked ? 1 : 0));
    }

    public static final Creator<CoinBean> CREATOR = new Creator<CoinBean>() {
        @Override
        public CoinBean createFromParcel(Parcel in) {
            return new CoinBean(in);
        }

        @Override
        public CoinBean[] newArray(int size) {
            return new CoinBean[size];
        }
    };
}
