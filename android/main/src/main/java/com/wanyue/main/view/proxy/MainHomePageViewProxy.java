package com.wanyue.main.view.proxy;

import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.appbar.AppBarLayout;
import com.wanyue.common.bean.LiveClassBean;
import com.wanyue.common.proxy.BaseViewProxy;
import com.wanyue.common.proxy.RxViewProxy;
import com.wanyue.common.proxy.ViewProxyPageAdapter;
import com.wanyue.common.server.observer.DefaultObserver;
import com.wanyue.common.utils.ListUtil;
import com.wanyue.live.activity.LiveAudienceActivity;
import com.wanyue.live.bean.LiveBean;
import com.wanyue.live.presenter.LiveRoomCheckLivePresenter;
import com.wanyue.main.R;
import com.wanyue.main.adapter.MainNavigatorAadpter;
import com.wanyue.main.api.MainAPI;
import com.wanyue.main.bean.BannerBean;
import com.wanyue.main.bean.FeatureBean;
import com.wanyue.main.view.activity.SearchLiveActivity;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class MainHomePageViewProxy extends RxViewProxy implements LiveRoomCheckLivePresenter.ActionListener {
    private FrameLayout mVpTopContainer;
    private AppBarLayout mAppBarLayout;
    private FrameLayout mVpBannerContainer;
    private MagicIndicator mIndicator;
    private ViewPager mViewPager;
    private BannerViewProxy<BannerBean>mBannerViewProxy;
    private LiveRoomCheckLivePresenter mCheckLivePresenter;
    @Override
    public int getLayoutId() {
        return R.layout.view_main_home_page;
    }

    @Override
    protected void initView(ViewGroup contentView) {
        super.initView(contentView);
        setDefaultStatusBarPadding();
        mVpTopContainer = (FrameLayout) findViewById(R.id.vp_top_container);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        mVpBannerContainer = (FrameLayout) findViewById(R.id.vp_banner_container);
        mIndicator = (MagicIndicator) findViewById(R.id.indicator);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        MainDefaultHeadViewProxy viewProxy=new MainDefaultHeadViewProxy() {
            @Override
            public void foward() {
                startActivity(SearchLiveActivity.class);
            }
        };
        viewProxy.putArgs("title",getString(R.string.search_live_room));
        getViewProxyChildMannger().addViewProxy(mVpTopContainer,viewProxy,viewProxy.getDefaultTag());
    }

    private boolean isFirstVisble=true;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser&&isFirstVisble){
            isFirstVisble=false;
            initData();
        }
    }

    private void initData() {
        MainAPI.getFeatured(1).
                compose(this.<FeatureBean>bindUntilOnDestoryEvent()).subscribe(new DefaultObserver<FeatureBean>() {
            @Override
            public void onNext(FeatureBean featureBean) {
              List<BannerBean>list=featureBean.getBanner();
              List<LiveClassBean>liveClassBean=featureBean.getLiveclass();
              List<LiveBean>beanList=featureBean.getList();
              initBannerView(list);
              initIndicator(liveClassBean);
            }
        });
    }

    private List<LiveClassBean>mIndicatorList;
    private void initIndicator(List<LiveClassBean>liveClassBeanList) {
        if(mIndicatorList!=null||mViewPager.getChildCount()>1){
            return;
        }
        if(mIndicatorList==null){
           mIndicatorList=new ArrayList<>();
           LiveClassBean liveClassBean=new LiveClassBean();
           liveClassBean.setId(LiveClassBean.FOLLOW);
           liveClassBean.setName(getString(R.string.follow));
           mIndicatorList.add(liveClassBean);

           liveClassBean=new LiveClassBean();
           liveClassBean.setId(LiveClassBean.FEATURED);
           liveClassBean.setName(getString(R.string.featured));
           mIndicatorList.add(liveClassBean);
        }
        if(liveClassBeanList!=null){
           mIndicatorList.addAll(liveClassBeanList);
        }
       List<HomeLiveViewProxy> viewProxyList=initLiveViewList();
       ViewProxyPageAdapter pageAdapter = new ViewProxyPageAdapter(getViewProxyChildMannger(),viewProxyList);
       mViewPager.setOffscreenPageLimit(viewProxyList.size());
       CommonNavigator commonNavigator = new CommonNavigator(getActivity());
       MainNavigatorAadpter mainNavigatorAadpter=new MainNavigatorAadpter(mIndicatorList,getActivity(),mViewPager);
       mainNavigatorAadpter.setEableScale(false);
       commonNavigator.setAdapter(mainNavigatorAadpter);
       mIndicator.setNavigator(commonNavigator);
       ViewPagerHelper.bind(mIndicator, mViewPager);
       pageAdapter.attachViewPager(mViewPager,1);
    }

    private List<HomeLiveViewProxy> initLiveViewList() {
      RecyclerView.RecycledViewPool recycledViewPool=new RecyclerView.RecycledViewPool();
      List<HomeLiveViewProxy>list=new ArrayList<>();
      for(final LiveClassBean liveClassBean:mIndicatorList){
           int id=liveClassBean.getId();
          HomeLiveViewProxy homeLiveViewProxy=null;
           if(id==LiveClassBean.FEATURED){
           homeLiveViewProxy=new HomeLiveViewProxy() {
                 @Override
                 public Observable<List<LiveBean>> getData(int p) {
                     return MainAPI.getFeatured(p).map(new Function<FeatureBean, List<LiveBean>>() {
                         @Override
                         public List<LiveBean> apply(FeatureBean featureBean) throws Exception {
                             return featureBean.getList();
                         }
                     });
                 }
             };
           }else if(id==LiveClassBean.FOLLOW){
               homeLiveViewProxy=new HomeLiveViewProxy() {
                   @Override
                   public Observable<List<LiveBean>> getData(int p) {
                       return MainAPI.getLiveListByFollow(p);
                   }
               };
           }else{
               homeLiveViewProxy=new HomeLiveViewProxy(){
                   @Override
                   public Observable<List<LiveBean>> getData(int p) {
                    return MainAPI.getLiveListByClass(liveClassBean.getId(),p);
                   }
               };
           }

          homeLiveViewProxy.setRecycledViewPool(recycledViewPool);
          list.add(homeLiveViewProxy);
      }
        if(mCheckLivePresenter==null){
           mCheckLivePresenter=new LiveRoomCheckLivePresenter(getActivity(),this);
        }
        for(HomeLiveViewProxy viewProxy:list){
            viewProxy.setCheckLivePresenter(mCheckLivePresenter);
        }

        return list;
    }

    private void initBannerView(List<BannerBean> beanList) {
       if(!ListUtil.haveData(beanList)){
           return;
       }
        if(mBannerViewProxy==null){
           mBannerViewProxy=new BannerViewProxy<>();
           mBannerViewProxy.setData(beanList);
           getViewProxyChildMannger().addViewProxy(mVpBannerContainer, mBannerViewProxy, mBannerViewProxy.getDefaultTag());
        }else{
           mBannerViewProxy.update(beanList);
        }
    }

    @Override
    public void onLiveRoomChanged(LiveBean liveBean, String data) {
        LiveAudienceActivity.forward(getActivity(),liveBean,data);
    }
}
