package com.wanyue.main.store.view.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.activity.BaseActivity;
import com.wanyue.common.bean.ConfigBean;
import com.wanyue.common.custom.DrawableTextView;
import com.wanyue.common.server.observer.DefaultObserver;
import com.wanyue.common.utils.ClickUtil;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.main.R;
import com.wanyue.main.api.MainAPI;
import com.wanyue.main.store.bean.MyStoreMessageBean;
import com.wanyue.shop.business.ShopState;

public class MyStoreActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTvOrderTip;
    private TextView mBtnMyOrder;
    private TextView mBtnWaitDelivered;
    private TextView mBtnWaitReceived;
    private TextView mBtnWaitEvaluated;
    private TextView mTvMoneyToday;
    private TextView mTvTotalToday;
    private TextView mTvSettlementedPrice;
    private TextView mTvNosettlementedPrice;
    private TextView mTvHost;
    private TextView mBtnCopy;


    @Override
    public void init() {
        setTabTitle("我的店铺");
        mTvOrderTip = (DrawableTextView) findViewById(R.id.tv_order_tip);
        mBtnMyOrder = (DrawableTextView) findViewById(R.id.btn_my_order);
        mBtnWaitDelivered = (TextView) findViewById(R.id.btn_wait_delivered);
        mBtnWaitReceived = (TextView) findViewById(R.id.btn_wait_received);
        mBtnWaitEvaluated = (TextView) findViewById(R.id.btn_wait_evaluated);
        mBtnMyOrder = (DrawableTextView) findViewById(R.id.btn_my_order);
        mTvMoneyToday = (TextView) findViewById(R.id.tv_money_today);
        mTvTotalToday = (TextView) findViewById(R.id.tv_total_today);
        mTvSettlementedPrice = (TextView) findViewById(R.id.tv_settlemented_price);
        mTvNosettlementedPrice = (TextView) findViewById(R.id.tv_nosettlemented_price);
        mTvHost = (TextView) findViewById(R.id.tv_host);
        mBtnCopy = (TextView) findViewById(R.id.btn_copy);
        mBtnCopy.setOnClickListener(this);

        findViewById(R.id.btn_profit_detail).setOnClickListener(this);
        findViewById(R.id.btn_my_order).setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_store;
    }

    @Override
    protected void onFirstResume() {
        super.onFirstResume();
        MainAPI.getMyStore().compose(this.<MyStoreMessageBean>bindUntilOnDestoryEvent()).subscribe(new DefaultObserver<MyStoreMessageBean>() {
            @Override
            public void onNext(MyStoreMessageBean myStoreMessageBean) {
                setStoreData(myStoreMessageBean);
            }
        });
        CommonAppConfig.getObserverConfig().compose(this.<ConfigBean>bindToLifecycle()).subscribe(new DefaultObserver<ConfigBean>() {

            @Override
            public void onNext(ConfigBean configBean) {
                mTvHost.setText(configBean.getShopUrl());
            }
        });
    }


    private void setStoreData(MyStoreMessageBean myStoreMessageBean) {
        mTvMoneyToday.setText(myStoreMessageBean.getShop_t());
        mTvTotalToday.setText(myStoreMessageBean.getShop_all());
        mTvSettlementedPrice.setText(myStoreMessageBean.getShop_ok());
        mTvNosettlementedPrice.setText(myStoreMessageBean.getShop_no());

        mBtnWaitDelivered.setText(getBuildString(R.string.wait_to_delivered,myStoreMessageBean.getUnshipped()));
        mBtnWaitReceived.setText(getBuildString(R.string.wait_to_received,myStoreMessageBean.getReceived()));
        mBtnWaitEvaluated.setText(getBuildString(R.string.wait_to_evaluated,myStoreMessageBean.getEvaluated()));
    }

    private SpannableStringBuilder getBuildString(int resId,int count) {
        String string=getString(resId);
        SpannableStringBuilder style = new SpannableStringBuilder(string);
        style.append("\n");
        int startIndex=style.length();
        String countString=Integer.toString(count);
        style.append(countString);
        style.setSpan(new AbsoluteSizeSpan(20, true), startIndex, startIndex+countString.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        style.setSpan(new ForegroundColorSpan(Color.BLACK), startIndex, startIndex+countString.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return style;
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

    @Override
    public void onClick(View v) {
        if(!ClickUtil.canClick()){
            return;
        }
        int id=v.getId();
        if(id== R.id.btn_copy){
            copy(mTvHost.getText().toString());
        }else if(id== R.id.btn_profit_detail){
            ProfitActivity.forward(this,ProfitActivity.TYPE_STORE);
        }else if(id== R.id.btn_my_order){
            StoreOrderListActivity.forward(this, ShopState.ORDER_SELL_STORE);
        }
    }
}
