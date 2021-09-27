package com.wanyue.main.store.view.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.wanyue.common.Constants;
import com.wanyue.common.activity.BaseActivity;
import com.wanyue.common.custom.refresh.RxRefreshView;
import com.wanyue.common.server.observer.DefaultObserver;
import com.wanyue.common.utils.ClickUtil;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.common.utils.ViewUtil;
import com.wanyue.main.R;
import com.wanyue.main.api.MainAPI;
import com.wanyue.main.store.adapter.SettlementRecordAdapter;
import com.wanyue.main.store.bean.SettlementRecordBean;
import com.wanyue.shop.business.ShopState;

import java.util.List;

import io.reactivex.Observable;

public class ProfitActivity extends BaseActivity implements View.OnClickListener {
    public static final  int TYPE_STORE= ShopState.ORDER_SELL_STORE;
    public static final  int TYPE_CONSIGNMENT=ShopState.ORDER_CONSIGNMENT;
    private int mType;
    private TextView mTvLeftTop;
    private TextView mTvLeftBottom;
    private TextView mTvRightTop;
    private TextView mTvRightBottom;
    private RxRefreshView<SettlementRecordBean> mRefreshView;
    private TextView mTvAllowMoney;
    private TextView mBtnRecord;
    private TextView mBtnGetMoney;
    private SettlementRecordAdapter mSettlementRecordAdapter;
    private String mAllowMoney;
    @Override
    public void init() {
        mType=getIntent().getIntExtra(Constants.TYPE,0);
        if(mType==TYPE_STORE){
            setTabTitle("店铺收益");
        }else{
            setTabTitle("代销收益");
        }
        mTvLeftTop = (TextView) findViewById(R.id.tv_left_top);
        mTvLeftBottom = (TextView) findViewById(R.id.tv_left_bottom);
        mTvRightTop = (TextView) findViewById(R.id.tv_right_top);
        mTvRightBottom = (TextView) findViewById(R.id.tv_right_bottom);
        mRefreshView = (RxRefreshView) findViewById(R.id.refreshView);
        mTvAllowMoney = (TextView) findViewById(R.id.tv_allow_money);
        mBtnRecord = (TextView) findViewById(R.id.btn_record);
        ViewUtil.setVisibility(mBtnRecord,View.GONE);
        mBtnGetMoney = (TextView) findViewById(R.id.btn_get_money);

        mSettlementRecordAdapter=new SettlementRecordAdapter(null);
        mRefreshView.setAdapter(mSettlementRecordAdapter);
        mRefreshView.setReclyViewSetting(RxRefreshView.ReclyViewSetting.createLinearSetting(this,1));
        mRefreshView.setDataListner(new RxRefreshView.DataListner<SettlementRecordBean>() {
            @Override
            public Observable<List<SettlementRecordBean>> loadData(int p) {
                return getData(p);
            }
            @Override
            public void compelete(List<SettlementRecordBean> data) {

            }
            @Override
            public void error(Throwable e) {

            }
        });
        mBtnRecord.setOnClickListener(this);
        mBtnGetMoney.setOnClickListener(this);
    }
    private Observable<List<SettlementRecordBean>> getData(int p) {
       String route=mType==TYPE_STORE?MainAPI.SHOP_SETTLE:MainAPI.BRING_SETTLE;
       return MainAPI.getSettlementRecords(route,p);
    }

    @Override
    protected void onFirstResume() {
        super.onFirstResume();
        if(mType==TYPE_STORE){
          getStoreProfit();  
        }else{
          getBringProfit();
        }

        if(mRefreshView!=null){
           mRefreshView.initData();
        }
    }


    /*
    * ├─ shop	string	非必须		可提现收益
├─ shop_t	string	非必须		今日收益
├─ shop_total	string	非必须		总收益
├─ shop_ok	string	非必须		已结算
├─ shop_no	string	非必须		未结算
    * */
    private void getStoreProfit() {
        MainAPI.getStoreProfit().compose(this.<JSONObject>bindToLifecycle()).subscribe(new DefaultObserver<JSONObject>() {

            @Override
            public void onNext(JSONObject jsonObject) {
                   setData(jsonObject.getString("shop_t"),
                           jsonObject.getString("shop_total"),
                           jsonObject.getString("shop_ok"),
                           jsonObject.getString("shop_no"),
                           jsonObject.getString("shop")
                           );
            }
        });
    }

    private void setData(String shop_t, String shop_total, String shop_ok, String shop_no,String shop) {
        mAllowMoney=shop;
        mTvLeftTop.setText(shop_t);
        mTvRightTop.setText(shop_total);
        mTvLeftBottom.setText(shop_ok);
        mTvRightBottom.setText(shop_no);
        mTvAllowMoney.setText(StringUtil.getPrice(shop));
    }


    private void getBringProfit() {
        MainAPI.getBringProfit().compose(this.<JSONObject>bindToLifecycle()).subscribe(new DefaultObserver<JSONObject>() {

            @Override
            public void onNext(JSONObject jsonObject) {
                setData(jsonObject.getString("bring_t"),
                        jsonObject.getString("bring_total"),
                        jsonObject.getString("bring_ok"),
                        jsonObject.getString("bring_no"),
                        jsonObject.getString("bring")
                );
            }
        });
    }

  /*  ├─ bring	string	非必须		可提现金额
├─ bring_t	string	非必须		今日收益
├─ bring_ok	string	非必须		已结算
├─ bring_no	string	非必须		未结算
├─ bring_total	string	非必须		总收益*/

    public static void forward(Context context, int type){
        Intent intent=new Intent(context,ProfitActivity.class);
        intent.putExtra(Constants.TYPE,type);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_profit;
    }

    @Override
    public void onClick(View v) {
        if(!ClickUtil.canClick()){
            return;
        }
        int id=v.getId();
        if(id== R.id.btn_record){
            toRecord();
        }else if(id== R.id.btn_get_money){
            toGetMoney();
        }
    }

    private void toGetMoney() {
        String routerUrl=mType==TYPE_STORE?MainAPI.CASH_SHOP:MainAPI.CASH_PROXY;
        //GetProfitActivity.forward(this,mAllowMoney,routerUrl);
    }

    private void toRecord() {
        String routerUrl=mType==TYPE_STORE?MainAPI.SHOP_CASH_LIST:MainAPI.BRING_LIST;
       // ProiftRecordActivity.forward(this,routerUrl);
    }
}
