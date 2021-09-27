package com.wanyue.shop.view.activty;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.lxj.xpopup.XPopup;
import com.wanyue.common.Constants;
import com.wanyue.common.activity.BaseActivity;
import com.wanyue.common.bean.UserBean;
import com.wanyue.common.custom.refresh.RxRefreshView;
import com.wanyue.common.http.ParseArrayHttpCallBack;
import com.wanyue.common.http.ParseHttpCallback;
import com.wanyue.common.pay.PayCallback;
import com.wanyue.common.pay.PayPresenter;
import com.wanyue.common.utils.ClickUtil;
import com.wanyue.common.utils.DebugUtil;
import com.wanyue.common.utils.DialogUitl;
import com.wanyue.common.utils.JsonUtil;
import com.wanyue.common.utils.L;
import com.wanyue.common.utils.ListUtil;
import com.wanyue.common.utils.ObjectUtil;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.shop.R;
import com.wanyue.shop.adapter.CommitOrderAdapter;
import com.wanyue.shop.api.ShopAPI;
import com.wanyue.shop.bean.AddressInfoBean;
import com.wanyue.shop.bean.OrderConfirmBean;
import com.wanyue.shop.bean.PriceGroup;
import com.wanyue.shop.bean.ShopCartStoreBean;
import com.wanyue.shop.business.ShopEvent;
import com.wanyue.shop.view.view.CommitOrderBottomViewProxy;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class CommitOrderActivity extends BaseActivity implements View.OnClickListener, BaseQuickAdapter.OnItemChildClickListener {
    private AddressInfoBean mAddressInfoBean;
    private ViewGroup mVpAddress;
    private ViewGroup mVpNoAddr;
    private ViewGroup mVpHaveAddress;
    private TextView mTvNamePhone;
    private TextView mTvAddress;
    private RecyclerView mReclyView;
    private TextView mTvTotalPrice;
    private TextView mBtnCommit;
    private CommitOrderAdapter mCommitOrderAdapter;
    private CommitOrderBottomViewProxy mCommitOrderBottomViewProxy;

    private String orderIdArray;
    private OrderConfirmBean mOrderConfirmBean;
    private ParseHttpCallback<JSONObject> mComputeCallBack;

    private PayPresenter mPayPresenter;

    @Override
    public void init() {
        setTabTitle(R.string.order_submit);
        mVpAddress = (ViewGroup) findViewById(R.id.vp_address);
        mVpNoAddr = (ViewGroup) findViewById(R.id.vp_no_addr);
        mVpHaveAddress = (ViewGroup) findViewById(R.id.vp_have_address);
        mTvNamePhone = (TextView) findViewById(R.id.tv_name_phone);
        mTvAddress = (TextView) findViewById(R.id.tv_address);
        mReclyView =findViewById(R.id.reclyView);
        mTvTotalPrice = (TextView) findViewById(R.id.tv_total_price);
        mBtnCommit = (TextView) findViewById(R.id.btn_commit);
        mCommitOrderAdapter=new CommitOrderAdapter(null);
        mCommitOrderAdapter.setOnItemChildClickListener(this);
        mReclyView.setAdapter(mCommitOrderAdapter);
        RxRefreshView.ReclyViewSetting.createLinearSetting(this,10).settingRecyclerView(mReclyView);
        orderIdArray=getIntent().getStringExtra(Constants.KEY_ID);
        mVpAddress.setOnClickListener(this);
        LiveEventBus.get(ShopEvent.ADDRESS_CHANGE, AddressInfoBean.class).observe(this, new Observer<AddressInfoBean>() {
            @Override
            public void onChanged(@NotNull AddressInfoBean addressInfoBean) {
                if(addressInfoBean.getId()==0){
                    getDefaultAddr();
                }else{
                    setAddress(addressInfoBean);
                    
                    changeAddrUIState();
                }
            }
        });

        mBtnCommit.setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_commit_order;
    }

    @Override
    protected void onFirstResume() {
        super.onFirstResume();
        //getDefaultAddr();
        getOrderInfo();
    }

    private void getOrderInfo() {
        ShopAPI.orderConfirm(orderIdArray, new ParseHttpCallback<JSONObject>() {
            @Override
            public void onSuccess(int code, String msg, JSONObject info) {
                if(isSuccess(code)&&info!=null){
                 mBtnCommit.setEnabled(true);
                 String json=info.toJSONString();
                 mOrderConfirmBean =info.toJavaObject(OrderConfirmBean.class);
                 mOrderConfirmBean.setLiveUid(getIntent().getStringExtra(Constants.LIVE_UID));
                 setAboutOrderData(mOrderConfirmBean);
                 L.e("json=="+json);
                }
            }
        });
    }


    private void setAboutOrderData(OrderConfirmBean orderConfirmBean) {
        mAddressInfoBean=orderConfirmBean.getAddressInfo();
        changeAddrUIState();

        UserBean userBean=orderConfirmBean.getUserInfo();
        if(userBean!=null){
            initBottomView();
            mCommitOrderBottomViewProxy.setData(orderConfirmBean);
        }
        if(mCommitOrderAdapter!=null){
            mCommitOrderAdapter.setData(orderConfirmBean.getCartInfo());
        }
        mTvTotalPrice.setText(StringUtil.getFormatPrice(orderConfirmBean.getTotalPrice()));
    }


    private void initBottomView() {
        if(mCommitOrderBottomViewProxy==null){
            FrameLayout frameLayout=new FrameLayout(this);
            mCommitOrderAdapter.addFooterView(frameLayout);
            mCommitOrderBottomViewProxy= new CommitOrderBottomViewProxy() {
                @Override
                public void selectChange() {
                    priceChangeComputed();
                }
            };
            getViewProxyMannger().addViewProxy(frameLayout,mCommitOrderBottomViewProxy,mCommitOrderBottomViewProxy.getDefaultTag());
        }
    }

    public static void forward(Context context,String orderIdArray){
        forward(context,orderIdArray,null);
    }

    public static void forward(Context context,String orderIdArray,String liveUid){
        Intent intent=new Intent(context,CommitOrderActivity.class);
        intent.putExtra(Constants.KEY_ID,orderIdArray);
        intent.putExtra(Constants.LIVE_UID,liveUid);
        context.startActivity(intent);
    }

    /*获取默认地址*/
    private void getDefaultAddr() {
        ShopAPI.getDefaultAddress(new ParseArrayHttpCallBack<AddressInfoBean>() {
            @Override
            public void onSuccess(int code, String msg, List<AddressInfoBean> info) {
                if(isSuccess(code)&& ListUtil.haveData(info)){
                    mAddressInfoBean=info.get(0);
                    setAddress(mAddressInfoBean);
                    changeAddrUIState();
                }else{
                    setAddress(null);
                    changeAddrUIState();
                }
            }
        });
    }

    private void changeAddrUIState() {
        if (mAddressInfoBean == null) { //没有地址的时候
            mVpHaveAddress.setVisibility(View.INVISIBLE);
            mVpNoAddr.setVisibility(View.VISIBLE);
        } else {        //有地址的时候
            mVpHaveAddress.setVisibility(View.VISIBLE);
            mVpNoAddr.setVisibility(View.INVISIBLE);
            mTvNamePhone.setText(StringUtil.contact(mAddressInfoBean.getName(), "\t\t", mAddressInfoBean.getPhone()));
            String info= StringUtil.contact(mAddressInfoBean.getProvince(),mAddressInfoBean.getCity(),mAddressInfoBean.getArea(),mAddressInfoBean.getAddress());
            mTvAddress.setText(info);
        }
    }

    public void toAddress() {
        if (mAddressInfoBean == null) {
            startActivity(MyAddressActivity.class);
        } else {
            startActivity(MyAddressActivity.class);
        }
    }


    @Override
    public void onClick(View v) {
        int id=v.getId();
        if(id==R.id.vp_address){
            toAddress();
        }else if(id==R.id.btn_commit){
            commit();
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if(mCommitOrderAdapter==null||!ClickUtil.canClick()){
            return;
        }
        ShopCartStoreBean storeBean=mCommitOrderAdapter.getItem(position);
        int id=view.getId();

    }




    private void priceChangeComputed() {
        if(mOrderConfirmBean==null){
            return;
        }
        int addrId=mAddressInfoBean==null?0:mAddressInfoBean.getId();
        String payType=mOrderConfirmBean.getPayType();
        int useCode= mOrderConfirmBean.getIsUseCode();
        String couponJson=null;
        if(mCommitOrderAdapter!=null){
           couponJson=mOrderConfirmBean.getCouponJson();
        }else{
            DebugUtil.sendException("mCommitOrderAdapter==null了,检查一下");
        }
        initComputeCallBack();
        ShopAPI.orderComputed(mOrderConfirmBean.getOrderKey(),addrId,couponJson,payType,useCode,mComputeCallBack);
    }

    /*初始化计算回调*/
    private void initComputeCallBack() {
        if(mComputeCallBack==null){
            mComputeCallBack= new ParseHttpCallback<JSONObject>() {
                @Override
                public void onSuccess(int code, String msg, JSONObject info) {
                    if(isSuccess(code)&&info!=null){
                        orderComputedSucc(info);
                    }
                }
            };
        }
    }


    //价格计算成功
    private void orderComputedSucc(JSONObject info) {
        JSONObject result= info.getJSONObject("result");
        JSONArray priceArray=result.getJSONArray("priceGroup");
        List<PriceGroup>groupList= JsonUtil.getData(priceArray,PriceGroup.class);
        if(groupList!=null&&mOrderConfirmBean!=null&&mOrderConfirmBean.haveGoods()){
            List<ShopCartStoreBean>list=mOrderConfirmBean.getCartInfo();
            for(ShopCartStoreBean temp :list){
                PriceGroup priceGroup=temp.getPriceGroup();
                int index=ListUtil.index(groupList,priceGroup);
                if(index!=-1){
                    temp.setPriceGroup(groupList.get(index));
                }
            }
            if(mCommitOrderAdapter!=null){
               mCommitOrderAdapter.notifyReclyDataChange();
            }
            if(mOrderConfirmBean!=null){
               mOrderConfirmBean.setTotalPrice(result.getDouble("pay_price"));
               mTvTotalPrice.setText(StringUtil.getFormatPrice(mOrderConfirmBean.getTotalPrice()));
            }
        }else{
            DebugUtil.sendException("groupList=="+groupList+"mOrderConfirmBean=="+mOrderConfirmBean);
        }
    }

    /*订单创建*/
    private void commit() {
        if(mOrderConfirmBean==null){
            return;
        }
        if(StringUtil.equals(mOrderConfirmBean.getPayType(), Constants.PAY_TYPE_COIN)){
            boolean isHaveFullCoin= mOrderConfirmBean.checkBalance();
            if(!isHaveFullCoin){
                ToastUtil.show(getString(R.string.no_full_monty));
                return;
            }
        }
        ShopAPI.orderCreate(mOrderConfirmBean,  new ParseHttpCallback<JSONObject>() {
            @Override
            public void onSuccess(int code, String msg, JSONObject info) {
                ToastUtil.show(msg);
                if(isSuccess(code)&&info!=null){
                  info=info.getJSONObject("result");
                  DebugUtil.logJson(info);
                  if(mOrderConfirmBean.checkIsWxPay()){
                      callByWx(info);
                  }else{
                      callSucc(info);
                  }
                }
            }
            @Override
            public boolean showLoadingDialog() {
                return true;
            }

            @Override
            public Dialog createLoadingDialog() {
                return DialogUitl.loadingDialog(CommitOrderActivity.this);
            }
        });
    }

    private void callSucc(JSONObject info) {
        String orderId=info.getString("orderId");
        OrderPayResultActivity.forward(CommitOrderActivity.this,orderId,!TextUtils.isEmpty(mOrderConfirmBean.getLiveUid()));
        finish();
    }

    private void callByWx(final JSONObject info) {
        initPayPrestner();
        mPayPresenter.setPayCallback(new PayCallback() {
            @Override
            public void onSuccess() {
                callSucc(info);
            }
            @Override
            public void onFailed(int errorCode) {
                String errorString=errorCode==PayCallback.CANCLE_PAY?"取消支付":"支付失败";
                String orderId=info.getString("orderId");
                OrderPayResultActivity.forward(CommitOrderActivity.this,orderId,!TextUtils.isEmpty(mOrderConfirmBean.getLiveUid()),errorString);
                finish();
            }
        });
        JSONObject wxConfig=info.getJSONObject("jsConfig");
        if(wxConfig!=null){
            mPayPresenter.setWxAppID(wxConfig.getString("appid"));
            mPayPresenter.wxPay2(wxConfig);
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShopAPI.cancle(ShopAPI.ORDER_CONFIRM);
        ShopAPI.cancle(ShopAPI.DEFAULT_ADDRESS);
        ShopAPI.cancle(ShopAPI.ORDER_COMPUTED);
    }


    private void initPayPrestner() {
        if(mPayPresenter==null){
           mPayPresenter=new PayPresenter(this);
        }
    }

    public void setAddress(AddressInfoBean address) {
        mAddressInfoBean = address;
        if(mOrderConfirmBean!=null){
            mOrderConfirmBean.setAddressInfo(mAddressInfoBean);
        }
    }
   
}
