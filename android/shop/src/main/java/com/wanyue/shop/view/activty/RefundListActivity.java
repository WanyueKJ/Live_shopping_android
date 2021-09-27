package com.wanyue.shop.view.activty;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wanyue.common.activity.BaseActivity;
import com.wanyue.common.custom.refresh.RxRefreshView;
import com.wanyue.shop.R;
import com.wanyue.shop.adapter.RefundListAdapter;
import com.wanyue.shop.api.ShopAPI;
import com.wanyue.shop.bean.OrderBean;
import com.wanyue.shop.business.ShopState;
import java.util.List;
import io.reactivex.Observable;

public class RefundListActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener {
    private RxRefreshView mRefreshView;
    private RefundListAdapter mRefundListAdapter;
    @Override
    public void init() {
        setTabTitle("退货列表");
        mRefreshView =  findViewById(R.id.refreshView);
        mRefreshView.setIconId(R.drawable.icon_empty_no_order);
        mRefundListAdapter=new RefundListAdapter(null);
        mRefreshView.setAdapter(mRefundListAdapter);
        mRefreshView.setDataListner(new RxRefreshView.DataListner<OrderBean>() {
            @Override
            public Observable<List<OrderBean>> loadData(int p) {
                return getData(p);
            }
            @Override
            public void compelete(List<OrderBean> data) {
            }
            @Override
            public void error(Throwable e) {
            }
        });
        mRefreshView.setReclyViewSetting(RxRefreshView.ReclyViewSetting.createLinearSetting(this,10));
        mRefundListAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void onFirstResume() {
        super.onFirstResume();
        if(mRefreshView!=null){
           mRefreshView.initData();
        }
    }
    private Observable<List<OrderBean>> getData(int p) {
        return ShopAPI.getOrderList(ShopState.ORDER_STATE_REFUND,p,"",ShopState.ORDER_BUY_SELF);
    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_refund_list;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if(mRefundListAdapter==null){
            return;
        }
        OrderBean orderBean=mRefundListAdapter.getItem(position);
        if(orderBean!=null){
           RefundOrderDetailActivity.forward(this,ShopState.ORDER_BUY_SELF,orderBean.getOrderId());
        }
    }
}
