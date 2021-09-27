package com.wanyue.shop.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.utils.DpUtil;
import com.wanyue.common.utils.ListUtil;
import com.wanyue.shop.R;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

public class BuyerOrderIndicatorAdapter extends CommonNavigatorAdapter {
    private String[] mTitle;
    private Context mContext;
    private ViewPager mViewPager;
    private int itemWidth;
    private int mSelectColor;
    private int mLineColor;
    private List<SimplePagerTitleView>mTitleViewList;

    public BuyerOrderIndicatorAdapter(@NotNull String[]list, Context context, ViewPager viewPager) {
        mTitle = list;
        mContext = context;
        mViewPager = viewPager;
        itemWidth= CommonAppConfig.getWindowWidth()/mTitle.length;
        mSelectColor= ContextCompat.getColor(mContext, R.color.textColor);
        mLineColor=ContextCompat.getColor(mContext, R.color.global);
    }
    @Override
    public int getCount()  {
        return mTitle.length;
    }
    @Override
    public IPagerTitleView getTitleView(Context context, final int index) {
        if(mTitleViewList==null){
           mTitleViewList =new ArrayList<>(mTitle.length);
        }
        SimplePagerTitleView simplePagerTitleView = new BoldPagerTitleView(context){
        };
        simplePagerTitleView.setNormalColor(mSelectColor);
        simplePagerTitleView.setGravity(Gravity.CENTER);
        simplePagerTitleView.setSelectedColor(mSelectColor);
        simplePagerTitleView.setText(mTitle[index]);
        simplePagerTitleView.setTextSize(13);
        simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewPager != null) {
                    mViewPager.setCurrentItem(index);
                }
            }
        });
        mTitleViewList.add(simplePagerTitleView);
        return simplePagerTitleView;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
        linePagerIndicator.setMode(LinePagerIndicator.MODE_MATCH_EDGE);
        linePagerIndicator.setXOffset(DpUtil.dp2px(20));
        linePagerIndicator.setRoundRadius(DpUtil.dp2px(2));
        linePagerIndicator.setColors(mLineColor);
        return linePagerIndicator;
    }

    public void notifyTitle(String[] titleArray) {
        mTitle = titleArray;
        int size=mTitle.length;
        for(int i=0;i<size;i++){
            String title=titleArray[i];
            SimplePagerTitleView simplePagerTitleView= ListUtil.safeGetData(mTitleViewList,i);
            if(simplePagerTitleView!=null){
               simplePagerTitleView.setText(title);
            }
        }
    }

    public static class BoldPagerTitleView extends SimplePagerTitleView{
        public BoldPagerTitleView(Context context) {
            super(context);
            int padding = UIUtil.dip2px(context, 5);
            setPadding(0, padding, 0, padding);
            setSingleLine(false);
            //setEllipsize(TextUtils.TruncateAt.END);
        }

        @Override
        public void onSelected(int index, int totalCount) {
            super.onSelected(index,totalCount);
            setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            setGravity(Gravity.CENTER);
        }

        @Override
        public void onDeselected(int index, int totalCount) {
            super.onDeselected(index,totalCount);
            setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }
    }
}
