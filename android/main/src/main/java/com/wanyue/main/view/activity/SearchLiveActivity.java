package com.wanyue.main.view.activity;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wanyue.common.activity.BaseActivity;
import com.wanyue.common.custom.refresh.RxRefreshView;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.live.activity.LiveAudienceActivity;
import com.wanyue.live.bean.LiveBean;
import com.wanyue.live.presenter.LiveRoomCheckLivePresenter;
import com.wanyue.main.R;
import com.wanyue.main.adapter.HomeLiveAdapter;
import com.wanyue.main.api.MainAPI;
import com.wanyue.shop.view.view.HotGoodsEmptyViewProxy;
import com.wanyue.shop.view.view.SearchViewProxy;
import java.util.List;
import io.reactivex.Observable;

public class SearchLiveActivity extends BaseActivity implements SearchViewProxy.SeacherListner, BaseQuickAdapter.OnItemClickListener, LiveRoomCheckLivePresenter.ActionListener {
    private RxRefreshView<LiveBean> mRefreshView;
    private ViewGroup mContainer;
    private TextView mTvSearch;

    private HomeLiveAdapter mHomeLiveAdapter;
    private String mKeyword;

    private SearchViewProxy mSearchViewProxy;
    private LiveRoomCheckLivePresenter mCheckLivePresenter;

    @Override
    public void init() {
        setTabTitle(R.string.search_live_room);
        mContainer = (ViewGroup) findViewById(R.id.container);
        mTvSearch = (TextView) findViewById(R.id.tv_search);
        mRefreshView=findViewById(R.id.refreshView);
        mRefreshView.setIconId(R.drawable.icon_empty_no_search);
        mHomeLiveAdapter=new HomeLiveAdapter(null);
        mHomeLiveAdapter.setOnItemClickListener(this);
        mRefreshView.setAdapter(mHomeLiveAdapter);
        mRefreshView.setReclyViewSetting(RxRefreshView.ReclyViewSetting.createGridSetting(this,2));
        mRefreshView.setDataListner(new RxRefreshView.DataListner<LiveBean>() {
            @Override
            public Observable<List<LiveBean>> loadData(int p) {
                return getData(p);
            }
            @Override
            public void compelete(List<LiveBean> data) {
            }
            @Override
            public void error(Throwable e) {
                e.printStackTrace();
            }
        });
        mRefreshView.setRefreshEnable(false);
        mSearchViewProxy=new SearchViewProxy();
        mSearchViewProxy.setHint("搜索直播间ID或房间名称");
        mSearchViewProxy.setEnableAutoSearch(false);
        mSearchViewProxy.setSeacherListner(this);
        getViewProxyMannger().addViewProxy(mContainer,mSearchViewProxy,mSearchViewProxy.getDefaultTag());
    }

    private Observable<List<LiveBean>> getData(int p) {
      return MainAPI.getLiveListBySearch(mKeyword,p);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_search_live;
    }

    @Override
    public void search(String keyward) {
        mKeyword=keyward;
        if(TextUtils.isEmpty(mKeyword)){
            ToastUtil.show("请输入关键词");
            return;
        }
        if(mRefreshView!=null){
           mRefreshView.initData();
        }
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        HomeLiveAdapter liveAdapter= (HomeLiveAdapter) adapter;
        LiveBean liveBean=liveAdapter.getItem(position);
        if(mCheckLivePresenter==null){
           mCheckLivePresenter=new LiveRoomCheckLivePresenter(this,this);
        }
        if(liveBean!=null){
            mCheckLivePresenter.checkLive(liveBean);
        }
    }
    @Override
    public void onLiveRoomChanged(LiveBean liveBean, String data) {
        LiveAudienceActivity.forward(this,liveBean,data);
    }
}
