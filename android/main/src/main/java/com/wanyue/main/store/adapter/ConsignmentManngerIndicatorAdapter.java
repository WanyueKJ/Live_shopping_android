package com.wanyue.main.store.adapter;

import android.content.Context;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.custom.ScaleTransitionPagerTitleView;
import com.wanyue.common.utils.DebugUtil;
import com.wanyue.common.utils.DpUtil;
import com.wanyue.common.utils.ListUtil;
import com.wanyue.main.R;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ConsignmentManngerIndicatorAdapter extends CommonNavigatorAdapter {
    private List<String> mTitle;
    private Context mContext;
    private ViewPager mViewPager;
    private int itemWidth;

    private int mSelectColor;
    private int mDefaultColor;
    private int mLineColor;
    private boolean mEableScale=true;
    private List<SimplePagerTitleView>mTitleViewList;


    public ConsignmentManngerIndicatorAdapter(@NotNull List<String>list, Context context, ViewPager viewPager) {
        mTitle = list;
        mContext = context;
        mViewPager = viewPager;
        itemWidth= CommonAppConfig.getWindowWidth()/mTitle.size();
        mSelectColor= ContextCompat.getColor(mContext, R.color.textColor);
        mDefaultColor=ContextCompat.getColor(mContext, R.color.textColor2);
        mLineColor=ContextCompat.getColor(mContext, R.color.global);
    }

    @Override
    public int getCount()  {
        return mTitle.size();
    }

    @Override
    public IPagerTitleView getTitleView(Context context, final int index) {
        if(mTitleViewList==null){
           mTitleViewList =new ArrayList<>(mTitle.size());
        }
        SimplePagerTitleView simplePagerTitleView =null;
        if(mEableScale){
            simplePagerTitleView=new ScaleTransitionPagerTitleView(context);
        }else{
            simplePagerTitleView=new SimplePagerTitleView(context);
        }

        simplePagerTitleView.setNormalColor(mDefaultColor);
        simplePagerTitleView.setSelectedColor(mSelectColor);
        simplePagerTitleView.setText(mTitle.get(index));
        simplePagerTitleView.setTextSize(15);
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

    public void notifyTitle(List<String> titleArray) {
        mTitle = titleArray;
        int size=mTitle.size();
        for(int i=0;i<size;i++){
            String title=titleArray.get(i);
            SimplePagerTitleView simplePagerTitleView= ListUtil.safeGetData(mTitleViewList,i);
            if(simplePagerTitleView!=null){
                simplePagerTitleView.setText(title);
            }
        }
    }

    public void notifyTitle(String title,int positin) {
        SimplePagerTitleView simplePagerTitleView= ListUtil.safeGetData(mTitleViewList,positin);
        if(simplePagerTitleView!=null){
           simplePagerTitleView.setText(title);
        }else{
            DebugUtil.sendException("notifyTitle失败");
        }
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


    public void setEableScale(boolean eableScale) {
        mEableScale = eableScale;
    }
}
