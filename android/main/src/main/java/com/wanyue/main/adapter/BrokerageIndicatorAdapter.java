package com.wanyue.main.adapter;

import android.content.Context;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.bean.LiveClassBean;
import com.wanyue.common.custom.ScaleTransitionPagerTitleView;
import com.wanyue.common.utils.DpUtil;
import com.wanyue.main.R;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;
import org.jetbrains.annotations.NotNull;

public class BrokerageIndicatorAdapter extends CommonNavigatorAdapter {

    private String[] mTitle;
    private Context mContext;
    private ViewPager mViewPager;
    private int itemWidth;

    private int mSelectColor;
    private int mDefaultColor;
    private int mLineColor;

    public BrokerageIndicatorAdapter(@NotNull String[]titleArray, Context context, ViewPager viewPager) {
        mTitle = titleArray;
        mContext = context;
        mViewPager = viewPager;
        itemWidth= CommonAppConfig.getWindowWidth()/mTitle.length;
        mSelectColor= ContextCompat.getColor(mContext, R.color.global);
        mDefaultColor=ContextCompat.getColor(mContext, R.color.gray1);
        mLineColor=ContextCompat.getColor(mContext, R.color.global);
    }

    @Override
    public int getCount()  {
        return mTitle.length;
    }
    @Override
    public IPagerTitleView getTitleView(Context context, final int index) {
        SimplePagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView(context);
        simplePagerTitleView.setNormalColor(mDefaultColor);
        simplePagerTitleView.setSelectedColor(mSelectColor);
        simplePagerTitleView.setText(mTitle[index]);
        simplePagerTitleView.setTextSize(15);
        simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewPager != null) {
                    mViewPager.setCurrentItem(index);
                }
            }
        });
        return simplePagerTitleView;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
        linePagerIndicator.setMode(LinePagerIndicator.MODE_EXACTLY);
        linePagerIndicator.setXOffset(DpUtil.dp2px(10));
        linePagerIndicator.setLineWidth(DpUtil.dp2px(15));
        linePagerIndicator.setRoundRadius(DpUtil.dp2px(2));
        linePagerIndicator.setColors(mLineColor);
        return linePagerIndicator;
    }
}
