package com.wanyue.main.view.proxy;

import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wanyue.common.custom.refresh.RxRefreshView;
import com.wanyue.common.proxy.RxViewProxy;
import com.wanyue.live.bean.LiveBean;
import com.wanyue.live.presenter.LiveRoomCheckLivePresenter;
import com.wanyue.main.R;
import com.wanyue.main.adapter.HomeLiveAdapter;
import java.util.List;
import io.reactivex.Observable;

public abstract class HomeLiveViewProxy extends RxViewProxy implements BaseQuickAdapter.OnItemClickListener {
    private RxRefreshView<LiveBean> mRefreshView;
    private HomeLiveAdapter mHomeLiveAdapter;
    private boolean isFirstVisible=true;
    private RecyclerView.RecycledViewPool mRecycledViewPool;
    private LiveRoomCheckLivePresenter mCheckLivePresenter;
    @Override
    public int getLayoutId() {
        return R.layout.view_home_live;
    }

    @Override
    protected void initView(ViewGroup contentView) {
        super.initView(contentView);
        mRefreshView = findViewById(R.id.refreshView);
        mRefreshView.setIconId(R.mipmap.icon_empty_no_live);
        mHomeLiveAdapter=new HomeLiveAdapter(null);
        mRefreshView.setAdapter(mHomeLiveAdapter);
        mRefreshView.setReclyViewSetting(RxRefreshView.ReclyViewSetting.createGridSetting(getActivity(), 2,0));
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
        if(mRecycledViewPool!=null){
           mRefreshView.setRecycledViewPool(mRecycledViewPool);
        }
        mHomeLiveAdapter.setOnItemClickListener(this);
    }

    public void setRecycledViewPool(RecyclerView.RecycledViewPool recycledViewPool) {
        mRecycledViewPool = recycledViewPool;
    }

    public abstract Observable<List<LiveBean>> getData(int p);
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
           if(mRefreshView!=null){
              mRefreshView.initData();
           }
        }
    }


    public void initData(){
        if(mRefreshView!=null){
           mRefreshView.initData();
        }
    }

    public void setCheckLivePresenter(LiveRoomCheckLivePresenter checkLivePresenter) {
        mCheckLivePresenter = checkLivePresenter;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        HomeLiveAdapter liveAdapter= (HomeLiveAdapter) adapter;
        LiveBean liveBean=liveAdapter.getItem(position);
        if(liveBean!=null&&mCheckLivePresenter!=null){
            mCheckLivePresenter.checkLive(liveBean);
        }

    }
}
