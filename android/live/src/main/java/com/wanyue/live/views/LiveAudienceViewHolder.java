package com.wanyue.live.views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import com.wanyue.common.utils.ViewUtil;
import com.wanyue.live.R;
import com.wanyue.live.activity.LiveActivity;
import com.wanyue.live.activity.LiveAudienceActivity;

/**
 * Created by  on 2018/10/9.
 * 观众直播间逻辑
 */

public class LiveAudienceViewHolder extends AbsLiveViewHolder {

    private String mLiveUid;
    private String mStream;
    private View mBtnGoods;
    private View mGoodsIcon;
    private ScaleAnimation mAnimation;

    public LiveAudienceViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_live_audience;
    }

    @Override
    public void init() {
        super.init();
       // findViewById(R.id.btn_close).setOnClickListener(this);
        findViewById(R.id.btn_share).setOnClickListener(this);
        findViewById(R.id.btn_light).setOnClickListener(this);
        findViewById(R.id.btn_more).setOnClickListener(this);
        findViewById(R.id.btn_gift).setOnClickListener(this);
        mBtnGoods = findViewById(R.id.btn_goods);
        mBtnGoods.setOnClickListener(this);
        mGoodsIcon = findViewById(R.id.goods_icon);
        ViewUtil.setVisibility(mGoodsIcon, View.GONE);
    }

    public void setLiveInfo(String liveUid, String stream) {
        mLiveUid = liveUid;
        mStream = stream;
    }

    @Override
    public void onClick(View v) {
        if (!canClick()) {
            return;
        }
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.btn_light) {
            light();
        } else if (i == R.id.btn_share) {
            openShareWindow();

        } else if (i == R.id.btn_gift) {
            openGiftWindow();
        } else if (i == R.id.btn_goods) {
            ((LiveAudienceActivity) mContext).openGoodsWindow();
        }else if (i == R.id.btn_more) {
            ((LiveAudienceActivity) mContext).openMoreWindow(v);
        }


    }

    private void light() {
        ((LiveAudienceActivity) mContext).light();
    }

    /**
     * 退出直播间
     */
    private void close() {
        ((LiveAudienceActivity) mContext).onBackPressed();
    }


    /**
     * 打开礼物窗口
     */
    private void openGiftWindow() {
        ((LiveAudienceActivity) mContext).openGiftWindow();
    }

    /**
     * 打开分享窗口
     */
    private void openShareWindow() {
        ((LiveActivity) mContext).openShareWindow();
    }

    /**
     * 动画停止
     */
    public void clearAnim() {
        if (mGoodsIcon != null) {
            mGoodsIcon.clearAnimation();
        }
    }

    public void setShopOpen(boolean isOpen) {

    }
}
