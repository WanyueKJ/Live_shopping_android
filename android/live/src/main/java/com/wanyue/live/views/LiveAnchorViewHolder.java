package com.wanyue.live.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wanyue.common.custom.viewanimator.AnimationBuilder;
import com.wanyue.common.custom.viewanimator.ViewAnimator;
import com.wanyue.common.http.HttpCallback;
import com.wanyue.live.R;
import com.wanyue.live.activity.LiveAnchorActivity;
import com.wanyue.live.http.LiveHttpUtil;

/**
 * Created by  on 2018/10/9.
 * 主播直播间逻辑
 */

public class LiveAnchorViewHolder extends AbsLiveViewHolder {

    private ImageView mBtnFunction;
    private Drawable mDrawable0;
    private Drawable mDrawable1;
    private View mBtnShop;
    private ImageView mImgShop;

    private ViewAnimator mViewAnimator;


    public LiveAnchorViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_live_anchor;
    }

    @Override
    public void init() {
        super.init();
        mDrawable0 = ContextCompat.getDrawable(mContext, R.mipmap.icon_live_func_0);
        mDrawable1 = ContextCompat.getDrawable(mContext, R.mipmap.icon_live_func_1);
        mBtnFunction =findViewById(R.id.btn_function);
        mBtnFunction.setImageDrawable(mDrawable0);
        mBtnShop=findViewById(R.id.btn_shop);
        mBtnFunction =  findViewById(R.id.btn_function);
        mImgShop =findViewById(R.id.img_shop);

        mBtnFunction.setOnClickListener(this);
        if(mBtnShop!=null){
           mBtnShop.setOnClickListener(this);
        }
        if(mImgShop!=null){
            startShopAnim();
        }

    }
    private void startShopAnim() {
        mViewAnimator= ViewAnimator.animate(mImgShop).scale(1F,0.7F,1F).repeatCount(1000000).duration(1000).start();
    }

    @Override
    public void onClick(View v) {
        if (!canClick()) {
            return;
        }
        super.onClick(v);
        int i = v.getId();
       if (i == R.id.btn_function) {
            showFunctionDialog();
        } else if (i == R.id.btn_shop) {
            ((LiveAnchorActivity) mContext).openGoodsWindow();
        }

    }

    public void setShopBtnVisible(boolean show) {
        if (mBtnShop != null) {
            if (show) {
                if (mBtnShop.getVisibility() != View.VISIBLE) {
                    mBtnShop.setVisibility(View.VISIBLE);
                }
            } else {
                if (mBtnShop.getVisibility() == View.VISIBLE) {
                    mBtnShop.setVisibility(View.GONE);
                }
            }
        }
    }

    /**
     * 显示功能弹窗
     */
    private void showFunctionDialog() {
        if (mBtnFunction != null) {
            mBtnFunction.setImageDrawable(mDrawable1);
        }
        ((LiveAnchorActivity) mContext).showFunctionDialog();
    }

    /**
     * 设置功能按钮变暗
     */
    public void setBtnFunctionDark() {
        if (mBtnFunction != null) {
            mBtnFunction.setImageDrawable(mDrawable0);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mViewAnimator!=null){
           mViewAnimator.cancel();
            mViewAnimator=null;
        }
    }
}
