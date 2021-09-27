package com.wanyue.live.bean;

/**
 * Created by  on 2018/10/8.
 */

public class LiveTimeChargeBean {
    private int mCoin;
    private boolean mChecked;

    public LiveTimeChargeBean() {
    }

    public LiveTimeChargeBean(int coin) {
        mCoin = coin;
    }

    public LiveTimeChargeBean(int coin, boolean checked) {
        mCoin = coin;
        mChecked = checked;
    }

    public int getCoin() {
        return mCoin;
    }

    public void setCoin(int coin) {
        mCoin = coin;
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean checked) {
        mChecked = checked;
    }
}
