package com.wanyue.shop.view.view.buyer;

import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.Observer;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lxj.xpopup.XPopup;
import com.wanyue.common.adapter.base.BaseMutiRecyclerAdapter;
import com.wanyue.common.custom.refresh.RxRefreshView;
import com.wanyue.common.http.HttpCallback;
import com.wanyue.common.http.ParseSingleHttpCallback;
import com.wanyue.common.proxy.RxViewProxy;
import com.wanyue.common.server.observer.DialogObserver;
import com.wanyue.common.utils.ClickUtil;
import com.wanyue.common.utils.DebugUtil;
import com.wanyue.common.utils.DialogUitl;
import com.wanyue.common.utils.ListUtil;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.shop.R;
import com.wanyue.shop.adapter.BuyerOrderAdaper;
import com.wanyue.shop.api.ShopAPI;
import com.wanyue.shop.bean.OrderBean;
import com.wanyue.shop.business.ShopState;
import com.wanyue.shop.model.OrderModel;
import com.wanyue.shop.view.activty.CommitOrderActivity;
import com.wanyue.shop.view.activty.OrderDeatailActivity;
import com.wanyue.shop.view.pop.PayOrderPopView;

import java.util.List;
import io.reactivex.Observable;

public abstract class BuyerOderViewProxy extends RxViewProxy implements BaseQuickAdapter.OnItemClickListener, BaseMutiRecyclerAdapter.OnItemChildClickListener2<OrderBean> {
    private RxRefreshView<OrderBean> mRefreshView;
    private BuyerOrderAdaper mAdapter;
    @Override
    protected void initView(ViewGroup contentView) {
        super.initView(contentView);
        mRefreshView = findViewById(R.id.refreshView);
        mRefreshView.setIconId(R.drawable.icon_empty_no_order);
        mAdapter=new BuyerOrderAdaper(null);
        mAdapter.setOnItemClickListener(this);
        mRefreshView.setAdapter(mAdapter);
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
        mAdapter.setOnItemChildClickListener2(this);
        mRefreshView.setReclyViewSetting(RxRefreshView.ReclyViewSetting.createLinearSetting(getActivity(),10));
        OrderModel.watchOrderChangeEvent(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String id) {
                checkNeedRefresh(id);
            }
        });
    }

    //判断是否需要刷新订单
    private void checkNeedRefresh(String id) {
        if(mAdapter==null){
            return;
        }
        List<OrderBean>list= mAdapter.getArray();
        if(!ListUtil.haveData(list))     {
            return;
        }
        for(OrderBean orderBean:list)     {
            if(StringUtil.equals(orderBean.getOrderId(),id)){
                if(mRefreshView!=null)   {
                    mRefreshView.initData();
                }
                return;
            }
        }
    }
    public abstract Observable<List<OrderBean>> getData(int p);

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            autoRefresh();
        }
    }

    private void autoRefresh() {
        if(mRefreshView!=null){
           mRefreshView.initData();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_single_refresh;
    }
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
           if(mAdapter==null){
               DebugUtil.sendException("onItemClick=="+null);
               return;
           }
          OrderBean orderBean=mAdapter.getItem(position);
          OrderDeatailActivity.forward(getActivity(), ShopState.ORDER_BUY_SELF,orderBean.getOrderId());
    }

    @Override
    public void onItemClick(int position, OrderBean orderBean, View view) {
            int id=view.getId();
            if(id==R.id.btn_cancel){
              openCancleOrderDialog(orderBean.getOrderId(),position);
            }else if(id==R.id.btn_buy){
                buyOrder(orderBean.getOrderId(),position);
            }else if(id==R.id.btn_evaluate){
             toEvaluate(orderBean);
            }else if(id==R.id.btn_buy_again){
               againOrder(orderBean);
            }else if(id==R.id.btn_delete){
              openDeleteConfrimDialog(orderBean);
            }
    }


    private void openCancleOrderDialog(final String orderId, final int position) {
        DialogUitl.showSimpleDialog(getActivity(), "是否取消订单?", new DialogUitl.SimpleCallback() {
            @Override
            public void onConfirmClick(Dialog dialog, String content) {
                cancleOrder(orderId,position);
            }
        });
    }


    private void openDeleteConfrimDialog(final OrderBean orderBean) {
        DialogUitl.showSimpleDialog(getActivity(), "是否要删除订单?", new DialogUitl.SimpleCallback() {
            @Override
            public void onConfirmClick(Dialog dialog, String content) {
                deleteOrder(orderBean);
            }
        });
    }


    /*删除订单*/
    private void deleteOrder(OrderBean orderBean) {
       final  String orderId=orderBean.getOrderId();
        ShopAPI.deleteOrder(orderId).compose(this.<Boolean>bindToLifecycle())
                .subscribe(new DialogObserver<Boolean>(getActivity()) {
                    @Override
                    public void onNextTo(Boolean aBoolean) {
                        if(aBoolean){
                          OrderModel.sendOrderChangeEvent(orderId);
                        }
                    }
                });
    }



    protected  void againOrder(OrderBean orderBean){
        ShopAPI.againOrder(orderBean.getOrderId(), new ParseSingleHttpCallback<String>("cateId") {
            @Override
            public void onSuccess(String data) {
                CommitOrderActivity.forward(getActivity(),data);
            }
        });
    }


    private void toEvaluate(OrderBean orderBean) {
        OrderDeatailActivity.forward(getActivity(),ShopState.ORDER_BUY_SELF,orderBean.getOrderId());
    }

    private PayOrderPopView mPayOrderPopView;
    private void buyOrder(String id, int position) {
        if(mPayOrderPopView!=null&&mPayOrderPopView.isShow()){
            return;
        }
        mPayOrderPopView=new PayOrderPopView(getActivity()){
            @Override
            protected void onDismiss() {
                super.onDismiss();
                mPayOrderPopView=null;
            }
        };
        mPayOrderPopView.setId(id);
        new XPopup.Builder(getActivity())
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .asCustom(mPayOrderPopView)
                .show();
    }
    
    private void cancleOrder(String id,final int position) {
        ShopAPI.cancleOrder(id, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                ToastUtil.show(msg);
                if(isSuccess(code)){
                    if(mAdapter!=null){
                       mAdapter.remove(position);
                    }
                }
            }
        });
    }
}
