package com.wanyue.shop.view.activty;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.wanyue.common.Constants;
import com.wanyue.common.activity.BaseActivity;
import com.wanyue.common.business.acmannger.ActivityMannger;
import com.wanyue.common.glide.ImgLoader;
import com.wanyue.common.http.ParseHttpCallback;
import com.wanyue.common.utils.ClickUtil;
import com.wanyue.common.utils.ResourceUtil;
import com.wanyue.common.utils.RouteUtil;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.shop.R;
import com.wanyue.shop.api.ShopAPI;
import com.wanyue.shop.bean.OrderBean;
import com.wanyue.shop.bean.OrderStatus;
import com.wanyue.shop.business.ShopState;

public class OrderPayResultActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTvOrderId;
    private TextView mTvOrderCreateTime;
    private TextView mTvPayStyle;
    private TextView mTvOrderPrice;
    private TextView mTvOrderFailed;
    private TextView mBtnPayAgain;
    private ImageView mImgState;
    private TextView mTvPayState;
    private ViewGroup mVpOrderFailed;


    private  boolean isSucc;

    private TextView mBtnBackMain;
    private boolean mIsFromLiveRoom;


    @Override
    public void init() {
        mTvOrderId = (TextView) findViewById(R.id.tv_order_id);
        mTvOrderCreateTime = (TextView) findViewById(R.id.tv_order_create_time);
        mTvPayStyle = (TextView) findViewById(R.id.tv_pay_style);
        mTvOrderPrice = (TextView) findViewById(R.id.tv_order_price);
        mVpOrderFailed = (ViewGroup) findViewById(R.id.vp_order_failed);
        mTvOrderFailed = (TextView) findViewById(R.id.tv_order_failed);
        mBtnPayAgain = (TextView) findViewById(R.id.btn_pay_again);
        mImgState = (ImageView) findViewById(R.id.img_state);
        mTvPayState = (TextView) findViewById(R.id.tv_pay_state);
        mBtnBackMain = (TextView) findViewById(R.id.btn_back_main);

        mBtnBackMain.setOnClickListener(this);
        findViewById(R.id.btn_pay_again).setOnClickListener(this);
        mIsFromLiveRoom=getIntent().getBooleanExtra(Constants.FROM,false);
        if(mIsFromLiveRoom){
            mBtnBackMain.setText("返回直播间");
        }else{
            mBtnBackMain.setText("返回首页");
        }
    }

    @Override
    protected void onFirstResume() {
        super.onFirstResume();
        getOrderDetail();
    }
    private  String mOrderId;
    private void getOrderDetail() {
        mOrderId=getIntent().getStringExtra(Constants.KEY_ID);
        ShopAPI.getOrderDetail(mOrderId, ShopState.ORDER_BUY_SELF,new ParseHttpCallback<JSONObject>() {
            @Override
            public void onSuccess(int code, String msg, JSONObject jsonObject) {
                if(isSuccess(code)&&jsonObject!=null){
                    String json=jsonObject.toJSONString();
                    OrderBean orderBean=jsonObject.toJavaObject(OrderBean.class);
                    checkOrderPayResult(orderBean.getOrderStatus());
                    setOrderDataUI(orderBean);
                  return;
              }else{
                    finish();
                }
            }
        });
    }

    private void checkOrderPayResult(OrderStatus orderStatus) {
        isSucc=orderStatus!=null?orderStatus.getType()!=ShopState.ORDER_STATE_WAIT_PAY:false;
        isPaySucc(isSucc);
    }

    private void setOrderDataUI(OrderBean mOrderBean) {
        if(mOrderBean==null){
            return;
        }
        OrderStatus orderStatus=mOrderBean.getOrderStatus();
        checkOrderPayResult(orderStatus);
        if(orderStatus!=null){
           mTvPayStyle.setText(orderStatus.getPayType());
        }
        mTvOrderId.setText(mOrderBean.getOrderId());
        mTvOrderCreateTime.setText(mOrderBean.getAddTime());
        mTvOrderPrice.setText(StringUtil.getPrice(mOrderBean.getPayPrice()));
    }


    private void isPaySucc(boolean isSucc) {
        if(isSucc){
            ImgLoader.display(this,R.drawable.icon_pay_succ,mImgState);
            mVpOrderFailed.setVisibility(View.GONE);
            mTvPayState.setText("订单支付成功");
            setTabTitle("支付成功");
            mBtnPayAgain.setText("查看订单");
        }else{
            ImgLoader.display(this,R.drawable.icon_pay_fail,mImgState);
            mVpOrderFailed.setVisibility(View.VISIBLE);
            mTvOrderFailed.setText(getIntent().getStringExtra(Constants.KEY_TINT));
            mTvPayState.setText("订单支付失败");
            setTabTitle("支付失败");
            mBtnPayAgain.setText("重新支付");
        }
    }


    public static void forward(Context context, String orderId,boolean isFromLiveRoom){
        forward(context,orderId,isFromLiveRoom,null);
    }

    public static void forward(Context context, String orderId,boolean isFromLiveRoom,String errorString){
        Intent intent=new Intent(context,OrderPayResultActivity.class);
        intent.putExtra(Constants.KEY_ID,orderId);
        intent.putExtra(Constants.FROM,isFromLiveRoom);
        intent.putExtra(Constants.KEY_TINT,errorString);
        context.startActivity(intent);
    }



    @Override
    public int getLayoutId() {
        return R.layout.activity_order_pay_result;
    }

    @Override
    public void onClick(View v) {
        if(!ClickUtil.canClick()){
            return;
        }
        int id=v.getId();
        if(id==R.id.btn_pay_again){
         if(isSucc) {
             OrderDeatailActivity.forward(this, ShopState.ORDER_BUY_SELF,mOrderId);
         }else{
             startActivity(MyOrderActivity.class);
         }
         finish();
        }else if(id==R.id.btn_back_main){
         backToMain();
        }
    }

    private void backToMain() {
        Activity activity=ActivityMannger.getInstance().getTargetActivty();
        if(!mIsFromLiveRoom){
           RouteUtil.forwardMain(this);
        }else{
            ActivityMannger.getInstance().removeTopActivity(ActivityMannger.getInstance().getActivityLinkedHashSet(),activity);
        }
    }
}
