package com.wanyue.main.store.view.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RadioGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lxj.xpopup.XPopup;
import com.wanyue.common.Constants;
import com.wanyue.common.activity.BaseActivity;
import com.wanyue.common.custom.refresh.RxRefreshView;
import com.wanyue.common.server.observer.DefaultObserver;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.main.R;
import com.wanyue.main.store.adapter.StoreOrderAdapter;
import com.wanyue.main.view.pop.SelectOrderTypePop;
import com.wanyue.main.view.widet.UnderlineRadioButton;
import com.wanyue.shop.api.ShopAPI;
import com.wanyue.shop.bean.OrderBean;
import com.wanyue.shop.bean.OrderStatementBean;
import com.wanyue.shop.business.ShopState;
import com.wanyue.shop.view.activty.OrderDeatailActivity;
import com.wanyue.shop.view.activty.RefundOrderDetailActivity;

import java.util.List;

import io.reactivex.Observable;

public class StoreOrderListActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, BaseQuickAdapter.OnItemClickListener {
    private int mStatus;
    private UnderlineRadioButton mBtnWaitPay;
    private UnderlineRadioButton mBtnWaitToDelivered;
    private UnderlineRadioButton mBtnDelivered;
    private UnderlineRadioButton mBtnOnther;
    private RxRefreshView<OrderBean> mRefreshView;
    private RadioGroup mRadioGroup;
    private int mOntherType=-10;
    private int mSelectType;
    private StoreOrderAdapter mStoreOrderAdapter;
    private OrderStatementBean mOrderStatementBean;
    private int mCheckId;

    @Override
    public void init() {
        mStatus=getIntent().getIntExtra(Constants.TYPE,0);
        if(mStatus==ShopState.ORDER_SELL_STORE){
            setTabTitle("店铺订单");
        }else{
            setTabTitle("代销订单");
        }

        mRadioGroup =  findViewById(R.id.radio_group);
        mBtnWaitPay =  findViewById(R.id.btn_wait_pay);
        mBtnWaitToDelivered = findViewById(R.id.btn_wait_to_delivered);
        mBtnDelivered = findViewById(R.id.btn_delivered);
        mBtnOnther =  findViewById(R.id.btn_onther);
        mRefreshView =  findViewById(R.id.refreshView);
        mRadioGroup.setOnCheckedChangeListener(this);

        mBtnWaitPay.setOnClickListener(this);
        mBtnWaitPay.setOnClickListener(this);
        mBtnWaitToDelivered.setOnClickListener(this);
        mBtnDelivered.setOnClickListener(this);
        mBtnOnther.setOnClickListener(this);
        mStoreOrderAdapter=new StoreOrderAdapter(null);
        mRefreshView.setAdapter(mStoreOrderAdapter);
        mRefreshView.setIconId(R.drawable.icon_empty_no_order);
        mRefreshView.setReclyViewSetting(RxRefreshView.ReclyViewSetting.createLinearSetting(this,10));
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
                e.printStackTrace();
            }
        });

        mStoreOrderAdapter.setIsStore(mStatus==ShopState.ORDER_SELL_STORE);
        mRadioGroup.check(R.id.btn_wait_pay);
        mStoreOrderAdapter.setOnItemClickListener(this);
        getOrderNumData();
        refreshList();
    }
    private void getOrderNumData() {
        ShopAPI.getOrderStatement(mStatus).compose(this.<OrderStatementBean>bindToLifecycle()).subscribe(new DefaultObserver<OrderStatementBean>() {
            @Override
            public void onNext(OrderStatementBean orderStatementBean) {
                mOrderStatementBean=orderStatementBean;
                String text1= StringUtil.contact(getString(R.string.wait_pay)," ",orderStatementBean.getUnpaidCount());
                mBtnWaitPay.setText(text1);
                String text2= StringUtil.contact(getString(R.string.wait_to_delivered)," ",orderStatementBean.getUnShippedCount());
                mBtnWaitToDelivered.setText(text2);
                String text3= StringUtil.contact(getString(R.string.delivered)," ",orderStatementBean.getReceivedCount());
                mBtnDelivered.setText(text3);

            }
        });
    }

    private Observable<List<OrderBean>> getData(int p) {
        return ShopAPI.getOrderList(mSelectType,p,"",mStatus);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_store_order_list;
    }
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if(mCheckId==checkedId){
            return;
        }
        mCheckId=checkedId;
        if(checkedId== R.id.btn_wait_pay){
            mSelectType= ShopState.ORDER_STATE_WAIT_PAY;
        }else if(checkedId== R.id.btn_wait_to_delivered){
            mSelectType= ShopState.ORDER_STATE_WAIT_DELIVERED;
        }else if(checkedId== R.id.btn_delivered){
            mSelectType= ShopState.ORDER_STATE_WAIT_RECEIVE;
        }else if(checkedId== R.id.btn_onther){
            mSelectType= mOntherType;
        }
        refreshList();
    }

    private void refreshList() {
        if(mRefreshView!=null){
           mRefreshView.initData();
        }
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if(id== R.id.btn_wait_pay){
            mRadioGroup.check(id);
        }else if(id== R.id.btn_wait_to_delivered){
            mRadioGroup.check(id);
        }else if(id== R.id.btn_delivered){
            mRadioGroup.check(id);
        }else if(id== R.id.btn_onther){
            if(mOntherType==-10||mBtnOnther.isChecked()){
               openSelectOrderType();
            }else{
               mRadioGroup.check(id);
            }
        }
    }

    public static void forward(Context context,int type){
        Intent intent=new Intent(context,StoreOrderListActivity.class);
        intent.putExtra(Constants.TYPE,type);
        context.startActivity(intent);
    }


    private SelectOrderTypePop mSelectOrderTypePop;
    private void openSelectOrderType() {
        if(mSelectOrderTypePop!=null&&mSelectOrderTypePop.isShow()){
            return;
        }
        if(mSelectOrderTypePop==null){
          mSelectOrderTypePop=new SelectOrderTypePop(this);
        }
        mSelectOrderTypePop.setOrderStatementBean(mOrderStatementBean);
        mSelectOrderTypePop.setSelectListner(new SelectOrderTypePop.SelectListner() {
            @Override
            public void select(String title, int type) {
                mBtnOnther.setText(title);
                mOntherType=type;
                if(!mBtnOnther.isChecked()){
                    mRadioGroup.check(R.id.btn_onther);
                }else{
                    refreshList();
                }
            }
        });
        new XPopup.Builder(this)
                .atView(mRadioGroup)
                .asCustom(mSelectOrderTypePop)
                .show();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        StoreOrderAdapter storeOrderAdapter= (StoreOrderAdapter) adapter;
        OrderBean orderBean=storeOrderAdapter.getItem(position);
        if(orderBean==null){
            return;
        }
        boolean isRefund=ShopState.isRefundOrder(orderBean);
        if(isRefund){
            RefundOrderDetailActivity.forward(this,mStatus,orderBean.getOrderId());
        }else{
            OrderDeatailActivity.forward(this,mStatus,orderBean.getOrderId());
        }
    }
}
