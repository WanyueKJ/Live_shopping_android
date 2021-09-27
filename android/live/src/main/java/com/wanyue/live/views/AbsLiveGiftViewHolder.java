package com.wanyue.live.views;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.wanyue.common.Constants;
import com.wanyue.common.bean.LiveGiftBean;
import com.wanyue.common.interfaces.OnItemClickListener;
import com.wanyue.common.utils.DpUtil;
import com.wanyue.common.views.AbsViewHolder;
import com.wanyue.live.R;
import com.wanyue.live.adapter.LiveGiftCountAdapter;
import com.wanyue.live.adapter.LiveGiftPagerAdapter;

import java.util.List;

/**
 * Created by  on 2019/8/28.
 */

public abstract class AbsLiveGiftViewHolder extends AbsViewHolder implements View.OnClickListener, LiveGiftPagerAdapter.ActionListener, OnItemClickListener<String> {

    protected boolean mFirstLoadData = true;
    protected ViewPager mViewPager;
    protected RadioGroup mRadioGroup;
    protected View mLoading;
    protected View mArrow;
    protected View mBtnSend;
    protected View mBtnSendGroup;
    protected TextView mBtnChooseCount;
    protected PopupWindow mGiftCountPopupWindow;//选择分组数量的popupWindow
    protected Drawable mDrawable1;
    protected Drawable mDrawable2;
    protected LiveGiftPagerAdapter mLiveGiftPagerAdapter;
    protected static final String DEFAULT_COUNT = "1";
    protected String mCount = DEFAULT_COUNT;
    protected String mLiveUid;
    protected String mStream;
    protected ActionListener mActionListener;

    public AbsLiveGiftViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    public AbsLiveGiftViewHolder(Context context, ViewGroup parentView, Object... args) {
        super(context, parentView, args);
    }


    @Override
    public void init() {
        mLoading = findViewById(R.id.loading);
        mArrow = findViewById(R.id.arrow);
        mBtnSend = findViewById(R.id.btn_send);
        mBtnSendGroup = findViewById(R.id.btn_send_group);
        mBtnChooseCount = (TextView) findViewById(R.id.btn_choose);
        mDrawable1 = ContextCompat.getDrawable(mContext, R.drawable.bg_live_gift_send);
        mDrawable2 = ContextCompat.getDrawable(mContext, R.drawable.bg_live_gift_send_2);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (mRadioGroup != null) {
                    RadioButton radioButton = (RadioButton) mRadioGroup.getChildAt(position);
                    if (radioButton != null) {
                        radioButton.setChecked(true);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
        mBtnSend.setOnClickListener(this);
        if (mBtnChooseCount != null) {
            mBtnChooseCount.setOnClickListener(this);
        }
    }

    public abstract void loadData();

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_send) {
            if (mActionListener != null) {
                mActionListener.onSendClick();
            }
        } else if (i == R.id.btn_choose) {
            showGiftCount();
        } else if (i == R.id.coin) {
            if (mActionListener != null) {
                mActionListener.onCoinClick();
            }
        }
    }

    @Override
    public void onItemChecked(LiveGiftBean bean) {
        if (mActionListener != null) {
            mActionListener.onGiftChanged(bean);
        }
        mBtnSend.setEnabled(true);
        if (getGiftType() != Constants.GIFT_TYPE_DAO) {
            if (!DEFAULT_COUNT.equals(mCount)) {
                mCount = DEFAULT_COUNT;
                mBtnChooseCount.setText(DEFAULT_COUNT);
                if (mActionListener != null) {
                    mActionListener.onCountChanged(mCount);
                }
            }
            if (bean.getType() == LiveGiftBean.TYPE_DELUXE) {
                if (mBtnChooseCount != null && mBtnChooseCount.getVisibility() == View.VISIBLE) {
                    mBtnChooseCount.setVisibility(View.INVISIBLE);
                    mArrow.setVisibility(View.INVISIBLE);
                    mBtnSend.setBackgroundDrawable(mDrawable2);
                }
            } else {
                if (mBtnChooseCount != null && mBtnChooseCount.getVisibility() != View.VISIBLE) {
                    mBtnChooseCount.setVisibility(View.VISIBLE);
                    mArrow.setVisibility(View.VISIBLE);
                    mBtnSend.setBackgroundDrawable(mDrawable1);
                }
            }
        }else{
            if (mActionListener != null) {
                mActionListener.onCountChanged(mCount);
            }
        }
    }


    @Override
    public void onItemClick(String bean, int position) {
        if (getGiftType() != Constants.GIFT_TYPE_DAO) {
            mCount = bean;
            mBtnChooseCount.setText(bean);
            hideGiftCount();
            if (mActionListener != null) {
                mActionListener.onCountChanged(bean);
            }
        }
    }

    /**
     * 显示分组数量
     */
    private void showGiftCount() {
        View v = LayoutInflater.from(mContext).inflate(R.layout.view_gift_count, null);
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, true));
        LiveGiftCountAdapter adapter = new LiveGiftCountAdapter(mContext);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
        mGiftCountPopupWindow = new PopupWindow(v, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mGiftCountPopupWindow.setBackgroundDrawable(new ColorDrawable());
        mGiftCountPopupWindow.setOutsideTouchable(true);
        mGiftCountPopupWindow.showAtLocation(mBtnChooseCount, Gravity.BOTTOM | Gravity.RIGHT, DpUtil.dp2px(70), DpUtil.dp2px(40));
    }

    public abstract int getGiftType();

    /**
     * 隐藏分组数量
     */
    private void hideGiftCount() {
        if (mGiftCountPopupWindow != null) {
            mGiftCountPopupWindow.dismiss();
        }
        mGiftCountPopupWindow = null;
    }

    protected <T extends LiveGiftBean> void showGiftList(List<T> list) {
        mLiveGiftPagerAdapter = new LiveGiftPagerAdapter(mContext, list, getGiftType());
        mLiveGiftPagerAdapter.setActionListener(this);
        mViewPager.setAdapter(mLiveGiftPagerAdapter);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        for (int i = 0, size = mLiveGiftPagerAdapter.getCount(); i < size; i++) {
            RadioButton radioButton = (RadioButton) inflater.inflate(R.layout.view_gift_indicator, mRadioGroup, false);
            radioButton.setId(i + 10000);
            if (i == 0) {
                radioButton.setChecked(true);
            }
            mRadioGroup.addView(radioButton);
        }
    }

    public void setVisibleSendGroup(boolean visible) {
        if (mBtnSendGroup != null) {
            if (visible) {
                if (mBtnSendGroup.getVisibility() != View.VISIBLE) {
                    mBtnSendGroup.setVisibility(View.VISIBLE);
                }
            } else {
                if (mBtnSendGroup.getVisibility() == View.VISIBLE) {
                    mBtnSendGroup.setVisibility(View.INVISIBLE);
                }
            }
        }
    }


    protected boolean isFirstLoadData() {
        if (mFirstLoadData) {
            mFirstLoadData = false;
            return true;
        }
        return false;
    }

    public void setActionListener(ActionListener actionListener) {
        mActionListener = actionListener;
    }


    public interface ActionListener {
        void onCountChanged(String count);

        void onGiftChanged(LiveGiftBean bean);

        void onSendClick();

        void onCoinClick();
    }

}
