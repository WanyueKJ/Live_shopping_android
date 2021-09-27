package com.wanyue.shop.view.view.order;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;
import com.alibaba.fastjson.JSONObject;
import com.lxj.xpopup.XPopup;
import com.wanyue.common.http.HttpCallback;
import com.wanyue.common.http.ParseHttpCallback;
import com.wanyue.common.http.ParseSingleHttpCallback;
import com.wanyue.common.proxy.RxViewProxy;
import com.wanyue.common.server.observer.DialogObserver;
import com.wanyue.common.utils.ClickUtil;
import com.wanyue.common.utils.DialogUitl;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.shop.R;
import com.wanyue.shop.api.ShopAPI;
import com.wanyue.shop.bean.OrderBean;
import com.wanyue.shop.business.ShopState;
import com.wanyue.shop.model.OrderModel;
import com.wanyue.shop.view.activty.CommitOrderActivity;
import com.wanyue.shop.view.activty.OrderRefundActivity;
import com.wanyue.shop.view.activty.ViewLogisticsActivity;
import com.wanyue.shop.view.pop.GetCodePop;
import com.wanyue.shop.view.pop.PayOrderPopView;

public abstract class AbsOderDetailBottomViewProxy extends RxViewProxy implements View.OnClickListener {
    private OrderBean mOrderBean;
    private PayOrderPopView mPayOrderPopView;


    public void setOrderBean(OrderBean orderBean) {
       mOrderBean = orderBean;
    }
    public static AbsOderDetailBottomViewProxy create(int state) {
        switch (state) {
            case ShopState.ORDER_STATE_WAIT_PAY:
                return new WaitPayODBottomViewProxy();
            case ShopState.ORDER_STATE_WAIT_DELIVERED:
                return new WaitDeliveredOdBottomViewProxy();
            case ShopState.ORDER_STATE_WAIT_RECEIVE:
                return new WaitReceiveOdViewProxy();
            case ShopState.ORDER_STATE_WAIT_EVALUATE:
                return new WaitEvaluateODViewProxy();
            case ShopState.ORDER_STATE_COMPELETE:
                return new CompeleteODViewProxy();
            default:
                return null;
        }
    }
    @Override
    protected void initView(ViewGroup contentView) {
        super.initView(contentView);
        setOnClickListner(R.id.btn_cancel,this);
        setOnClickListner(R.id.btn_buy,this);

        setOnClickListner(R.id.btn_apply_refund,this);
        setOnClickListner(R.id.btn_check_wuliu,this);
        setOnClickListner(R.id.btn_buy_again,this);
        setOnClickListner(R.id.btn_delete,this);
        setOnClickListner(R.id.btn_take_goods,this);

    }

    public void toRefund(){
        OrderRefundActivity.forward(getActivity(),mOrderBean);
    }


    @Override
    public void onClick(View v) {
            if(!ClickUtil.canClick()||mOrderBean==null){
                return;
            }

            int id=v.getId();
            if(id==R.id.btn_apply_refund){
                toRefund();
            }else if(id==R.id.btn_check_wuliu){
                toViewLogistics();
            }else if(id==R.id.btn_buy_again){
                againOrder();
            }else if(id==R.id.btn_delete){
              openDeleteConfrimDialog(mOrderBean);
            }else if(id==R.id.btn_cancel){
                openCancleOrderDialog(mOrderBean);
            }else if(id==R.id.btn_buy){
                buy(mOrderBean.getOrderId());
            }else if(id==R.id.btn_take_goods){
                openTakeGoodsDialog(mOrderBean.getOrderId());
            }
    }


    private void openTakeGoodsDialog(final String orderId) {
        DialogUitl.showSimpleDialog(getActivity(), "为保障权益请收到货确认无误后,再确认收货",new DialogUitl.SimpleCallback() {
            @Override
            public void onConfirmClick(Dialog dialog, String content) {
                takeGoods(orderId);
            }
        });
    }

    private void takeGoods(final String orderId) {
        ShopAPI.orderTake(orderId, new ParseHttpCallback<JSONObject>() {
            @Override
            public void onSuccess(int code, String msg, JSONObject info) {
                if(isSuccess(code)&&info!=null){
                    int gain_integral= info.getIntValue("gain_integral");
                    if(gain_integral==0){
                        ToastUtil.show(msg);
                        OrderModel.sendOrderChangeEvent(orderId);
                    }else{
                        openGetPopDialog(gain_integral);
                    }
                }else {
                    ToastUtil.show(msg);
                }

            }
        });
    }

    private void openGetPopDialog(int gain_integral) {
        Activity activity=getActivity();
        GetCodePop getCodePop=new GetCodePop(activity);
        getCodePop.setCodeTip(Integer.toString(gain_integral));
        getCodePop.setOrderId(mOrderBean.getOrderId());
        new XPopup.Builder(activity)
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .asCustom(getCodePop)
                .show();
    }

    private void buy(String id) {
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


    private void openCancleOrderDialog(final OrderBean orderBean) {
        DialogUitl.showSimpleDialog(getActivity(), "是否取消订单?", new DialogUitl.SimpleCallback() {
            @Override
            public void onConfirmClick(Dialog dialog, String content) {
                cancleOrder(orderBean.getOrderId());
            }
        });
    }

    private void cancleOrder(final String id) {
        ShopAPI.cancleOrder(id, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                ToastUtil.show(msg);
                if(isSuccess(code)){
                    OrderModel.sendOrderChangeEvent(id);
                }
            }
        });
    }

    private void openDeleteConfrimDialog(final OrderBean orderBean) {
        DialogUitl.showSimpleDialog(getActivity(), "是否删除订单?", new DialogUitl.SimpleCallback() {
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
                           ToastUtil.show("删除成功");
                           OrderModel.sendOrderChangeEvent(orderId);
                        }
                    }
                });
    }

    protected  void againOrder(){
        ShopAPI.againOrder(mOrderBean.getOrderId(), new ParseSingleHttpCallback<String>("cateId") {
            @Override
            public void onSuccess(String data) {
                CommitOrderActivity.forward(getActivity(),data);
            }
        });
    }

    private void toViewLogistics() {
       ViewLogisticsActivity.forward(getActivity(),mOrderBean.getOrderId());
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        ShopAPI.cancle(ShopAPI.ORDER_AGAIN);
        ShopAPI.cancle(ShopAPI.ORDER_TAKE);
    }
}
