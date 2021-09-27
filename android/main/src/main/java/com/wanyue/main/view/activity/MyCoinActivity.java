package com.wanyue.main.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.wanyue.common.activity.BaseActivity;
import com.wanyue.main.R;
import com.wanyue.shop.view.view.HotGoodsEmptyViewProxy;

/**
 * 我的余额
 */
public class MyCoinActivity extends BaseActivity implements View.OnClickListener {
    private ViewGroup mContainer;
    private TextView mTvLeftTop;
    private TextView mTvLeftBottom;
    private TextView mBtnBillingRecords;
    private TextView mBtnPurchaseHistory;

    private String mTotalBalance;
    private String mTotalConsumption;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void init() {
        setTabTitle("我的账号");
        mContainer = (ViewGroup) findViewById(R.id.container);
        mTvLeftTop = (TextView) findViewById(R.id.tv_left_top);
        mTvLeftBottom = (TextView) findViewById(R.id.tv_left_bottom);
        mBtnBillingRecords = (TextView) findViewById(R.id.btn_billing_records);
        mBtnPurchaseHistory = (TextView) findViewById(R.id.btn_purchase_history);
        mBtnBillingRecords.setOnClickListener(this);
        mBtnPurchaseHistory.setOnClickListener(this);

        HotGoodsEmptyViewProxy emptyViewProxy=new HotGoodsEmptyViewProxy();
        emptyViewProxy.setHideConver(true);
        getViewProxyMannger().addViewProxy(mContainer,emptyViewProxy,emptyViewProxy.getDefaultTag());

        Intent intent=getIntent();
        mTotalBalance=intent.getStringExtra("data1");
        mTotalConsumption=intent.getStringExtra("data2");

        mTvLeftTop.setText(mTotalBalance);
        mTvLeftBottom.setText(mTotalConsumption);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_coin;
    }

    public static void forward(Context context,String totalBalance, String consumption){
        Intent intent=new Intent(context,MyCoinActivity.class);
        intent.putExtra("data1",totalBalance);
        intent.putExtra("data2",consumption);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if(id==R.id.btn_billing_records){
            openBillingRecords();
        }else if(id==R.id.btn_purchase_history){
            openPurchaseHistory();
        }
    }
    private void openPurchaseHistory() {
      // CoinRecordActivity.forward(this,1);
    }
    private void openBillingRecords() {
       // CoinRecordActivity.forward(this,0);
    }
}
