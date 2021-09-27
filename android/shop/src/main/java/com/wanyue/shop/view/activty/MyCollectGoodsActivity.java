package com.wanyue.shop.view.activty;

import android.view.View;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wanyue.common.activity.BaseActivity;
import com.wanyue.common.adapter.base.BaseMutiRecyclerAdapter;
import com.wanyue.common.custom.refresh.RxRefreshView;
import com.wanyue.common.server.observer.DialogObserver;
import com.wanyue.common.utils.ClickUtil;
import com.wanyue.common.utils.ProcessResultUtil;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.shop.R;
import com.wanyue.shop.adapter.CollectGoodsAdapter;
import com.wanyue.shop.api.ShopAPI;
import com.wanyue.shop.bean.StoreGoodsBean;
import java.util.List;
import io.reactivex.Observable;

public class MyCollectGoodsActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener, BaseMutiRecyclerAdapter.OnItemChildClickListener2<StoreGoodsBean> {
    private RxRefreshView<StoreGoodsBean> mRefreshView;
    private CollectGoodsAdapter mCollectGoodsAdapter;
    @Override
    public void init() {
        setTabTitle(R.string.collect_goods);
        mRefreshView =findViewById(R.id.refreshView);
        mRefreshView.setIconId(R.drawable.icon_empty_no_collection);

        mRefreshView.setDataListner(new RxRefreshView.DataListner<StoreGoodsBean>() {
            @Override
            public Observable<List<StoreGoodsBean>> loadData(int p) {
                return getData(p);
            }
            @Override
            public void compelete(List<StoreGoodsBean> data) {
            }
            @Override
            public void error(Throwable e) {
            }
        });
        mCollectGoodsAdapter=new CollectGoodsAdapter(null);
        mRefreshView.setAdapter(mCollectGoodsAdapter);
        mRefreshView.setReclyViewSetting(RxRefreshView.ReclyViewSetting.createLinearSetting(this,0));
        mCollectGoodsAdapter.setOnItemChildClickListener2(this);
        mCollectGoodsAdapter.setOnItemClickListener(this);

    }

    private Observable<List<StoreGoodsBean>> getData(int p) {
        return ShopAPI.getCollectGoodsList(p);
    }

    @Override
    protected void onFirstResume() {
        super.onFirstResume();
        if(mRefreshView!=null){
           mRefreshView.initData();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_collect_goods;
    }
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if(mCollectGoodsAdapter==null|| !ClickUtil.canClick()){
            return;
        }
        StoreGoodsBean goodsBean=mCollectGoodsAdapter.getItem(position);
        GoodsDetailActivity.forward(this,goodsBean.getPid());
    }
    @Override
    public void onItemClick(final int position, StoreGoodsBean goodsBean, View view) {
        if(mCollectGoodsAdapter==null){
            return;
        }
        String category=goodsBean.getCategory();
        ShopAPI.cancleCollectGoods(goodsBean.getPid(),category).
                compose(this.<Boolean>bindUntilOnDestoryEvent())
                .subscribe(new DialogObserver<Boolean>(this) {
                    @Override
                    public void onNextTo(Boolean aBoolean) {
                        if(aBoolean){
                            ToastUtil.show("取消收藏成功");
                            if(mRefreshView!=null){
                               mRefreshView.initData();
                            }
                        }
                    }
       });
    }
}
