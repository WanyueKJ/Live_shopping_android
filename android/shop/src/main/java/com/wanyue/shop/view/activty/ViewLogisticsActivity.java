package com.wanyue.shop.view.activty;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wanyue.common.Constants;
import com.wanyue.common.activity.BaseActivity;
import com.wanyue.common.http.ParseHttpCallback;
import com.wanyue.common.utils.DebugUtil;
import com.wanyue.common.utils.JsonUtil;
import com.wanyue.common.utils.ListUtil;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.shop.R;
import com.wanyue.shop.adapter.RefundGoodsListAdaper;
import com.wanyue.shop.adapter.ViewLogisticsAdapter;
import com.wanyue.shop.api.ShopAPI;
import com.wanyue.shop.bean.OrderBean;
import com.wanyue.shop.bean.ViewLogisticsBean;
import com.wanyue.shop.view.view.HotGoodsEmptyViewProxy;
import com.wanyue.shop.view.widet.linear.ListPool;
import com.wanyue.shop.view.widet.linear.PoolLinearListView;
import java.util.List;

public class ViewLogisticsActivity extends BaseActivity implements View.OnClickListener {

    private ViewGroup mVpContainer;
    private PoolLinearListView mGoodsListView;
    private PoolLinearListView mLogisticsListView;
    private TextView mTvLogisticsCompany;
    private TextView mTvExpressNo;
    private TextView mBtnCopy;
    private String mOrderId;

    private HotGoodsEmptyViewProxy mHotGoodsEmptyViewProxy;
    private RefundGoodsListAdaper mGoodsListAdaper;
    private ViewLogisticsAdapter mViewLogisticsAdapter;

    @Override
    public void init() {
        setTabTitle(R.string.view_logistics);
        mVpContainer = (ViewGroup) findViewById(R.id.vp_container);
        mGoodsListView = (PoolLinearListView) findViewById(R.id.goods_list_view);
        mTvLogisticsCompany = (TextView) findViewById(R.id.tv_logistics_company);
        mTvExpressNo = (TextView) findViewById(R.id.tv_express_no);
        mBtnCopy = (TextView) findViewById(R.id.btn_copy);
        mLogisticsListView = (PoolLinearListView) findViewById(R.id.logistics_list_view);
        mLogisticsListView.setListPool(new ListPool());

        mOrderId=getIntent().getStringExtra(Constants.DATA);
        mBtnCopy.setOnClickListener(this);
    }

    @Override
    protected void onFirstResume() {
        super.onFirstResume();
        ShopAPI.viewLogistics(mOrderId, new ParseHttpCallback<JSONObject>() {
            @Override
            public void onSuccess(int code, String msg, JSONObject info) {
                    if(isSuccess(code)&&info!=null){
                      String json=info.toJSONString();
                      DebugUtil.logJson(info);
                      parseOrder(info);
                      parseViewLogistics(info);
                      initEmptyView();

                    }
            }
        });
    }

    private void parseViewLogistics(JSONObject info) {
        List<ViewLogisticsBean> list=null;
        String express=info.getString("express");
        if(JsonUtil.getJsonType(express)==JsonUtil.JSON_TYPE_ARRAY&&DebugUtil.isDeBug()){
            list= ListUtil.asList(new ViewLogisticsBean("包裹正在准备揽收","2020-7-10")
            );
            if(mViewLogisticsAdapter==null){
                mViewLogisticsAdapter=new ViewLogisticsAdapter(list);
                mLogisticsListView.setAdapter(mViewLogisticsAdapter);
            }

            return;
        }else if(JsonUtil.getJsonType(express)==JsonUtil.JSON_TYPE_OBJECT){
            info=info.getJSONObject("express");
            if(info==null){
                return;
            }
            info=info.getJSONObject("result");
            if(info==null){
                return;
            }
            JSONArray jsonArray=info.getJSONArray("list");
             list= JsonUtil.getData(jsonArray,ViewLogisticsBean.class);
            if(mViewLogisticsAdapter==null){
                mViewLogisticsAdapter=new ViewLogisticsAdapter(list);
                mLogisticsListView.setAdapter(mViewLogisticsAdapter);
            }
        }

    }

    private void parseOrder(JSONObject info) {
        JSONObject orderObject=info.getJSONObject("order");
        if(orderObject!=null){
            OrderBean orderBean=orderObject.toJavaObject(OrderBean.class);
            setOrderData(orderBean);
        }
    }

    private void initEmptyView() {
        if(mHotGoodsEmptyViewProxy==null){
            mHotGoodsEmptyViewProxy=new HotGoodsEmptyViewProxy();
            mHotGoodsEmptyViewProxy.setHideConver(true);
            getViewProxyMannger().addViewProxy(mVpContainer,mHotGoodsEmptyViewProxy,mHotGoodsEmptyViewProxy.getDefaultTag());
        }
    }

    private void setOrderData(OrderBean orderBean) {
        if(mGoodsListAdaper==null){
            mGoodsListAdaper=new RefundGoodsListAdaper(orderBean.getCartInfo());
            mGoodsListView.setAdapter(mGoodsListAdaper);
        }
        mTvLogisticsCompany.setText(orderBean.getDeliveryName());
        mTvExpressNo.setText(orderBean.getDeliveryId());
    }

    public static void forward(Context context, String mOrderId){
        if(mOrderId==null){
            DebugUtil.sendException("mOrderId==null");
            return;
        }
        Intent intent=new Intent(context,ViewLogisticsActivity.class);
        intent.putExtra(Constants.DATA,mOrderId);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_view_logistics;
    }

    @Override
    public void onClick(View v) {
        copy(mTvExpressNo.getText().toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShopAPI.cancle(ShopAPI.VIEW_LOGISTICS);
    }

    private void copy(String content) {
        if(TextUtils.isEmpty(content)){
            return;
        }
        ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("text", content);
        cm.setPrimaryClip(clipData);
        ToastUtil.show(getString(com.wanyue.common.R.string.copy_success));
    }

}
