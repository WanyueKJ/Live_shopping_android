package com.wanyue.common.proxy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import com.wanyue.common.utils.L;
import java.util.List;

public class ViewProxyPageAdapter extends PagerAdapter {
    private BaseProxyMannger mBaseProxyMannger;
    private List<? extends BaseViewProxy>mViewList;
    private BaseViewProxy mCurrentBaseViewProxy;

    private int mInstantiatePostion=0;
    private boolean mIsFirstInstantiate=true;
    public boolean isPrint=false;

    public ViewProxyPageAdapter(@Nullable BaseProxyMannger baseProxyMannger,@Nullable List<? extends BaseViewProxy> viewList) {
        mBaseProxyMannger = baseProxyMannger;
        mViewList = viewList;
        if(mViewList!=null){
            for(BaseViewProxy baseViewProxy:mViewList){
                baseViewProxy.setAddViewPager(true);
            }
        }
    }

    public void attachViewPager(@Nullable ViewPager viewPager,int position){
        L.e("attachViewPager执行了");
        mInstantiatePostion=position;
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }
            @Override
            public void onPageSelected(int i) {
                if(mCurrentBaseViewProxy!=null){
                   mCurrentBaseViewProxy.setUserVisibleHint(false);
                }
                   mCurrentBaseViewProxy=mViewList.get(i);
                   mCurrentBaseViewProxy.setUserVisibleHint(true);
            }
            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
        viewPager.setAdapter(this);
        viewPager.setCurrentItem(position);
    }


    public boolean isNotChildViewProxy(){
        return mBaseProxyMannger!=null&&mBaseProxyMannger instanceof ViewProxyMannger;
    }

    public void attachViewPager(@Nullable ViewPager viewPager){
        attachViewPager(viewPager,0);
    }

    @Override
    public int getCount() {
        return mViewList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        BaseViewProxy viewProxy = mViewList.get(position);
        if(isPrint){
           L.e("instantiateItem=="+position);
        }
        mBaseProxyMannger.addViewProxy(container,viewProxy,viewProxy.getDefaultTag());
        if(mIsFirstInstantiate&&position==mInstantiatePostion){
           if(isNotChildViewProxy()){
              viewProxy.setUserVisibleHint(true);
           }else{
              ViewProxyChildMannger childMannger= (ViewProxyChildMannger) mBaseProxyMannger;
              if(childMannger==null){
                  return viewProxy.getContentView();
              }
               BaseViewProxy baseViewProxy= childMannger.getParentViewProxy();
               if(baseViewProxy==null){
                  return viewProxy.getContentView();
               }
               if(!baseViewProxy.isAddViewPager()||baseViewProxy.isUserVisble()){
                  viewProxy.setUserVisibleHint(true);
               }else{
                   if(childMannger!=null){
                      childMannger.setUserVisibleViewProxy(viewProxy);
                   }
               }
           }
        }
        return viewProxy.getContentView();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        BaseViewProxy viewProxy = mViewList.get(position);
        mBaseProxyMannger.removeViewProxy(viewProxy);
    }
}
