package com.wanyue.shop.view.activty;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.lifecycle.Observer;
import com.alibaba.fastjson.JSONObject;
import com.wanyue.common.Constants;
import com.wanyue.common.activity.BaseActivity;
import com.wanyue.common.activity.WebViewActivity;
import com.wanyue.common.glide.ImgLoader;
import com.wanyue.common.http.ParseHttpCallback;
import com.wanyue.common.http.ParseSingleHttpCallback;
import com.wanyue.common.utils.L;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.shop.R;
import com.wanyue.shop.adapter.BuyerOrderChildAdapter;
import com.wanyue.shop.api.ShopAPI;
import com.wanyue.shop.bean.OrderBean;
import com.wanyue.shop.bean.OrderStatus;
import com.wanyue.shop.bean.ShopCartBean;
import com.wanyue.shop.bean.StoreGoodsBean;
import com.wanyue.shop.business.ShopState;
import com.wanyue.shop.model.OrderModel;
import com.wanyue.shop.view.view.order.AbsOderDetailBottomViewProxy;
import com.wanyue.shop.view.view.order.OrderDetailProgressViewProxy;
import com.wanyue.shop.view.widet.ViewGroupLayoutBaseAdapter;
import com.wanyue.shop.view.widet.linear.PoolLinearListView;
import java.util.List;

public class OrderDeatailActivity extends BaseActivity implements View.OnClickListener {
    private String mOrderId;
    private OrderBean mOrderBean;
    private ImageView mImgStatusGif;
    private TextView mTvOrderTip;
    private TextView mTvTime;
    private ViewGroup mVpProgressContainer;
    private TextView mTvNamePhone;
    private TextView mTvAddress;
    private TextView mTvGoodsNum;
    private PoolLinearListView mListView;
    private TextView mTvOrderId;
    private TextView mBtnCopy;
    private TextView mTvOrderCreateTime;
    private TextView mTvPayType;
    private TextView mTvPayStyle;
    private TextView mTvOrderPrice;
    private TextView mTvYunfei;
    private TextView mTvTotalPrice;
    
    private ViewGroup mVpLogistics;
    private TextView mTvDelivery;
    private TextView mTvLogisticsCompany;
    private TextView mTvExpressNo;
    private ScrollView mScrollView;



    private OrderDetailProgressViewProxy mOrderDetailProgressViewProxy;
    private BuyerOrderChildAdapter mBuyerOrderChildAdapter;
    private AbsOderDetailBottomViewProxy mAbsOderDetailBottomViewProxy;

    private FrameLayout mVpBottom;

    private int mStatus;
    @Override
    public void init() {
        setTabTitle(R.string.order_detail);
        mOrderId=getIntent().getStringExtra(Constants.KEY_ID);
        mImgStatusGif = (ImageView) findViewById(R.id.img_status_gif);
        mTvOrderTip = (TextView) findViewById(R.id.tv_order_tip);
        mTvTime =  findViewById(R.id.tv_time);
        mVpProgressContainer =  findViewById(R.id.vp_progress_container);
        mTvNamePhone = (TextView) findViewById(R.id.tv_name_phone);
        mTvAddress = (TextView) findViewById(R.id.tv_address);
        mTvGoodsNum = (TextView) findViewById(R.id.tv_goods_num);
        mListView = (PoolLinearListView) findViewById(R.id.listView);
        mTvOrderId = (TextView) findViewById(R.id.tv_order_id);
        mBtnCopy = (TextView) findViewById(R.id.btn_copy);
        mTvOrderCreateTime = (TextView) findViewById(R.id.tv_order_create_time);
        mTvPayType = (TextView) findViewById(R.id.tv_pay_type);
        mTvPayStyle = (TextView) findViewById(R.id.tv_pay_style);
        mTvOrderPrice = (TextView) findViewById(R.id.tv_order_price);
        mTvYunfei = (TextView) findViewById(R.id.tv_yunfei);
        mTvTotalPrice = (TextView) findViewById(R.id.tv_total_price);
        mVpBottom = (FrameLayout) findViewById(R.id.vp_bottom);

        mVpLogistics = (ViewGroup) findViewById(R.id.vp_logistics);
        mTvDelivery = (TextView) findViewById(R.id.tv_delivery);
        mTvLogisticsCompany = (TextView) findViewById(R.id.tv_logistics_company);
        mTvExpressNo = (TextView) findViewById(R.id.tv_express_no);
        mScrollView = (ScrollView) findViewById(R.id.scroll_view);

        mOrderDetailProgressViewProxy=new OrderDetailProgressViewProxy();
        getViewProxyMannger().addViewProxy(mVpProgressContainer,mOrderDetailProgressViewProxy,mOrderDetailProgressViewProxy.getDefaultTag());
        mBtnCopy.setOnClickListener(this);
        findViewById(R.id.btn_custom_service).setOnClickListener(this);
        OrderModel.watchOrderChangeEvent(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(StringUtil.equals(s,mOrderId)){
                    finish();
                }
            }
        });
        mStatus=getIntent().getIntExtra(Constants.KEY_STATUS,0);
        if(mStatus!=ShopState.ORDER_BUY_SELF){
            FrameLayout.LayoutParams params= (FrameLayout.LayoutParams) mScrollView.getLayoutParams();
            params.bottomMargin=0;
        }
    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_order_deatail;
    }
    @Override
    protected void onFirstResume() {
        super.onFirstResume();
        ShopAPI.getOrderDetail(mOrderId, mStatus,new ParseHttpCallback<JSONObject>() {
            @Override
            public void onSuccess(int code, String msg, JSONObject info) {
                if(isSuccess(code)){
                   L.e(info.toJSONString());
                    mOrderBean=info.toJavaObject(OrderBean.class);
                    setOrderDataUI();
                }
            }
        });
    }

    private void setOrderDataUI() {
        if(mOrderBean==null){
            return;
        }

        int orderType=-1;
        OrderStatus orderStatus=mOrderBean.getOrderStatus();
        if(orderStatus!=null){
           orderType=orderStatus.getType();
           if(mOrderDetailProgressViewProxy!=null){
              mOrderDetailProgressViewProxy.setStatus(orderType);
           }
           addBottomView(orderType);

            if(orderStatus.getType()== ShopState.ORDER_STATE_WAIT_PAY){
                mTvPayType.setText(R.string.no_pay);
            }else{
                mTvPayType.setText(R.string.payed);
            }
            mTvOrderTip.setText(orderStatus.getMsg());
            mTvPayStyle.setText(orderStatus.getPayType());
        }
        boolean isEvaluate=orderType==ShopState.ORDER_STATE_WAIT_EVALUATE;//是否显示评论

        if(mOrderBean.getStatus()!=0){
            setLogisticsMessage(mOrderBean);
        }

        List<ShopCartBean> array=mOrderBean.getCartInfo();
        if(mBuyerOrderChildAdapter==null){
           mBuyerOrderChildAdapter=new BuyerOrderChildAdapter(array);
           mBuyerOrderChildAdapter.setOpenEvaluate(isEvaluate);
           mBuyerOrderChildAdapter.setOnItemChildClickListener(new ViewGroupLayoutBaseAdapter.OnItemChildClickListener<ShopCartBean>() {
                @Override
                public void onItemClick(int position, ShopCartBean shopCartBean, View view) {
                }
            });
           mListView.setAdapter(mBuyerOrderChildAdapter);
        }else{
            mBuyerOrderChildAdapter.setData(array);
        }
        mTvNamePhone.setText(mOrderBean.getNameAndPhone());
        mTvAddress.setText(mOrderBean.getAddress());
        ImgLoader.display(this,mOrderBean.getStatusPic(),mImgStatusGif);
        mTvTime.setText(mOrderBean.getAddTime());
        mTvOrderId.setText(mOrderBean.getOrderId());
        mTvGoodsNum.setText(getString(R.string.total_num_jian2,Integer.toString(mOrderBean.getTotalNum())));
        mTvOrderCreateTime.setText(mOrderBean.getAddTime());
        mTvTotalPrice.setText(StringUtil.getPrice(mOrderBean.getPayPrice()));
        mTvOrderPrice.setText(StringUtil.getPrice(mOrderBean.getTotalPrice()));
        mTvYunfei.setText(StringUtil.getPrice(mOrderBean.getPayPostage()));
    }

    private void addBottomView(int orderType) {
        if(mAbsOderDetailBottomViewProxy==null&&mStatus==ShopState.ORDER_BUY_SELF){
            AbsOderDetailBottomViewProxy bottomViewProxy=AbsOderDetailBottomViewProxy.create(orderType);
            if(bottomViewProxy!=null){
                mAbsOderDetailBottomViewProxy=bottomViewProxy;
                getViewProxyMannger().addViewProxy(mVpBottom,bottomViewProxy,bottomViewProxy.getDefaultTag());
                bottomViewProxy.setOrderBean(mOrderBean);
            }
        }else{
            mVpBottom.setVisibility(View.GONE);
        }
    }

    private void setLogisticsMessage(OrderBean orderBean) {
        mVpLogistics.setVisibility(View.VISIBLE);
        mTvDelivery.setText(orderBean.getDeliveryTypeName());
        mTvLogisticsCompany.setText(orderBean.getDeliveryName());
        mTvExpressNo.setText(orderBean.getDeliveryId());
    }

    public static void forward(Context context,int status, String orderId){
        Intent intent=new Intent(context,OrderDeatailActivity.class);
        intent.putExtra(Constants.KEY_ID,orderId);
        intent.putExtra(Constants.KEY_STATUS,status);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShopAPI.cancle(ShopAPI.ORDER_DETAIL);
    }


    @Override
    public void onClick(View v) {
        int id=v.getId();
        if(id==R.id.btn_copy){
            if(mOrderBean!=null){
               copy(mOrderBean.getOrderId());
            }
        }else if(id==R.id.btn_custom_service){
            openService();
        }
    }

    private String mServiceLink;
    private void openService() {
        String storeId=mOrderBean.getStoreId();
        if(TextUtils.isEmpty(mServiceLink)){
            ShopAPI.getShopService(storeId, new ParseSingleHttpCallback<String>("service_url") {
                @Override
                public void onSuccess(String data) {
                    mServiceLink=data;
                    WebViewActivity.forward(OrderDeatailActivity.this,data);
                }
            });
        }else{
            WebViewActivity.forward(this,mServiceLink);
        }
    }


    /**
     * 复制到剪贴板
     */

    private void copy(String content) {
        ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("text", content);
        cm.setPrimaryClip(clipData);
        ToastUtil.show(getString(com.wanyue.common.R.string.copy_success));
    }

}
