package com.wanyue.main.store.view.proxy;

import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.Observer;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.wanyue.common.adapter.base.BaseMutiRecyclerAdapter;
import com.wanyue.common.custom.refresh.RxRefreshView;
import com.wanyue.common.proxy.RxViewProxy;
import com.wanyue.common.server.observer.DefaultObserver;
import com.wanyue.common.server.observer.DialogObserver;
import com.wanyue.main.R;
import com.wanyue.main.api.MainAPI;
import com.wanyue.main.business.MainEvent;
import com.wanyue.main.store.adapter.ConsignmentManngerAdapter;
import com.wanyue.main.store.bean.ConsignMentGoodsBean;
import com.wanyue.shop.view.activty.GoodsDetailActivity;

import java.util.List;

import io.reactivex.Observable;

public abstract class GoodsManngerViewProxy extends RxViewProxy implements BaseQuickAdapter.OnItemClickListener, BaseMutiRecyclerAdapter.OnItemChildClickListener2<ConsignMentGoodsBean> {
    private RxRefreshView<ConsignMentGoodsBean> mRefreshView;
    private ConsignmentManngerAdapter mConsignmentManngerAdapter;
    private boolean mIsSale;
    private boolean mIsFirstVisible=true;

    @Override
    public int getLayoutId() {
       return R.layout.view_goods_mannger;
    }
    @Override
    protected void initView(ViewGroup contentView) {
        super.initView(contentView);
        mRefreshView = findViewById(R.id.refreshView);
        mRefreshView.setIconId(R.drawable.icon_empty_no_goods);
        mConsignmentManngerAdapter=new ConsignmentManngerAdapter(null);
        mConsignmentManngerAdapter.setSale(mIsSale);
        mRefreshView.setAdapter(mConsignmentManngerAdapter);
        mRefreshView.setReclyViewSetting(RxRefreshView.ReclyViewSetting.createLinearSetting(getActivity(),1));
        mRefreshView.setDataListner(new RxRefreshView.DataListner<ConsignMentGoodsBean>() {
            @Override
            public Observable<List<ConsignMentGoodsBean>> loadData(int p) {
                return getData(p);
            }
            @Override
            public void compelete(List<ConsignMentGoodsBean> data) {
            }
            @Override
            public void error(Throwable e) {
            }
        });
        mConsignmentManngerAdapter.setOnItemClickListener(this);
        mConsignmentManngerAdapter.setOnItemChildClickListener2(this);
        LiveEventBus.get(MainEvent.MOBIFY_GOODS).observe(getActivity(), new Observer<Object>() {

            @Override
            public void onChanged(Object o) {
               mIsFirstVisible=true;
            }
        });
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(!mIsFirstVisible||!isVisibleToUser){
            return;
        }
        mIsFirstVisible=true;
        if(mRefreshView!=null){
           mRefreshView.initData();
        }
    }
    public void setSale(boolean sale) {
        mIsSale = sale;
    }
    public abstract Observable<List<ConsignMentGoodsBean>> getData(int p);

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        ConsignmentManngerAdapter manngerAdapter= (ConsignmentManngerAdapter) adapter;
        ConsignMentGoodsBean mentGoodsBean=manngerAdapter.getItem(position);
        GoodsDetailActivity.forward(getActivity(),mentGoodsBean.getId());
    }

    @Override
    public void onItemClick(int position, ConsignMentGoodsBean consignMentGoodsBean, View view) {
                if(consignMentGoodsBean==null){
                    return;
                }
                int id=view.getId();
                if(id== R.id.btn_delete){
                   deletePostion(position,consignMentGoodsBean.getId());
                }else if(id== R.id.btn_down){
                    downGoods(position,consignMentGoodsBean.getId());
                }else if(id== R.id.btn_up){
                    upGoods(position,consignMentGoodsBean.getId());
                }
    }

    /*删除*/
    private void deletePostion(final int position,String id) {
        MainAPI.delShelfGoods(id).compose(this.<Boolean>bindToLifecycle()).subscribe(new DefaultObserver<Boolean>() {
            @Override
            public void onNext(Boolean aBoolean) {
                if(aBoolean){
                   if(mConsignmentManngerAdapter!=null){
                      mConsignmentManngerAdapter.remove(position);
                   }
                }
            }
        });
    }

    /*下架商品*/
    protected  void downGoods(final int position, String id){
        MainAPI.downConsignmentGoods(id).compose(this.<Boolean>bindToLifecycle()).subscribe(new DialogObserver<Boolean>(getActivity()) {
            @Override
            public void onNextTo(Boolean aBoolean) {
                if(aBoolean){
                    LiveEventBus.get(MainEvent.MOBIFY_GOODS).post(true);
                    if(mConsignmentManngerAdapter!=null){
                        mConsignmentManngerAdapter.remove(position);
                    }
                }
            }
        });
    }


    /*上架商品*/
    protected  void upGoods(final int position, String id){

    }


}
