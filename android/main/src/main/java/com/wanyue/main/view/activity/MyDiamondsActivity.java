package com.wanyue.main.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.Constants;
import com.wanyue.common.HtmlConfig;
import com.wanyue.common.activity.BaseActivity;
import com.wanyue.common.activity.WebViewActivity;
import com.wanyue.common.api.CommonAPI;
import com.wanyue.common.bean.CoinBean;
import com.wanyue.common.bean.CoinPayBean;
import com.wanyue.common.custom.ItemDecoration;
import com.wanyue.common.event.CoinChangeEvent;
import com.wanyue.common.http.CommonHttpConsts;
import com.wanyue.common.http.CommonHttpUtil;
import com.wanyue.common.http.HttpCallback;
import com.wanyue.common.interfaces.OnItemClickListener;
import com.wanyue.common.pay.PayCallback;
import com.wanyue.common.pay.PayPresenter;
import com.wanyue.common.utils.RouteUtil;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.common.utils.WordUtil;
import com.wanyue.main.R;
import com.wanyue.main.adapter.CoinAdapter;
import com.wanyue.main.adapter.CoinPayAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的钻石
 */
@Route(path = RouteUtil.PATH_COIN)
public class MyDiamondsActivity extends BaseActivity implements OnItemClickListener<CoinBean>, View.OnClickListener {

    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView mPayRecyclerView;
    private CoinAdapter mAdapter;
    private CoinPayAdapter mPayAdapter;
    private TextView mBalance;
    private long mBalanceValue;
    private boolean mFirstLoad = true;
    private PayPresenter mPayPresenter;
    private String mCoinName;
    /*private TextView mTip1;
    private TextView mTip2;
    private TextView mCoin2;*/
    private String mChargeH5Url;

    public static void forward(Context context) {
        context.startActivity(new Intent(context, MyDiamondsActivity.class));
    }

    @Override
    public void init() {
        setTabTitle(WordUtil.getString(R.string.wallet, CommonAppConfig.getCoinName()));
        /*mTip1 = findViewById(R.id.tip_1);
        mTip2 = findViewById(R.id.tip_2);
        mCoin2 = findViewById(R.id.coin_2);*/
        mRefreshLayout = findViewById(R.id.refreshLayout);
        mRefreshLayout.setColorSchemeResources(R.color.global);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        mCoinName = CommonAppConfig.getCoinName();
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) {
                    return 3;
                }
                return 1;
            }
        });
        mRecyclerView.setLayoutManager(gridLayoutManager);
        ItemDecoration decoration = new ItemDecoration(mContext, 0x00000000, 5, 20);
        decoration.setOnlySetItemOffsetsButNoDraw(true);
        mRecyclerView.addItemDecoration(decoration);

        TextView coinNameTextView = findViewById(R.id.coin_name);
        coinNameTextView.setText(WordUtil.getString(R.string.wallet_coin_name, mCoinName));
        mBalance = findViewById(R.id.coin);

        mAdapter = new CoinAdapter(mContext, mCoinName);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setContactView(findViewById(R.id.top));
        mRecyclerView.setAdapter(mAdapter);
        findViewById(R.id.btn_tip).setOnClickListener(this);
        findViewById(R.id.btn_charge).setOnClickListener(this);
        View headView = mAdapter.getHeadView();
        mPayRecyclerView = headView.findViewById(R.id.pay_recyclerView);
        ItemDecoration decoration2 = new ItemDecoration(mContext, 0x00000000, 14, 10);
        decoration2.setOnlySetItemOffsetsButNoDraw(true);
        mPayRecyclerView.addItemDecoration(decoration2);
        mPayRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false));
        mPayAdapter = new CoinPayAdapter(mContext);
        mPayRecyclerView.setAdapter(mPayAdapter);
        mPayPresenter = new PayPresenter(this);
        mPayPresenter.setServiceNameAli(Constants.PAY_BUY_COIN_ALI);
        mPayPresenter.setServiceNameWx(Constants.PAY_BUY_COIN_WX);
        mPayPresenter.setAliCallbackUrl(HtmlConfig.ALI_PAY_COIN_URL);
        mPayPresenter.setPayCallback(new PayCallback() {
            @Override
            public void onSuccess() {
                if (mPayPresenter != null) {
                    mPayPresenter.checkPayResult();
                }
            }

            @Override
            public void onFailed(int error) {

            }
        });
        EventBus.getDefault().register(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_diamonds;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mFirstLoad) {
            mFirstLoad = false;
            loadData();
        }
    }

    private void loadData() {
        CommonAPI.getBalance(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (isSuccess(code) && info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    String coin = obj.getString("coin");
                    mBalanceValue = Long.parseLong(coin);
                    mBalance.setText(coin);
                    //mTip1.setText(obj.getString("tip_t"));
                    //mTip2.setText(obj.getString("tip_d"));
                    //mCoin2.setText(obj.getString("score"));

                    List<CoinBean> list = JSON.parseArray(obj.getString("list"), CoinBean.class);
                    if (mAdapter != null) {
                        mAdapter.setList(list);
                    }
                    mPayPresenter.setBalanceValue(mBalanceValue);
                    JSONObject objConfig = obj.getJSONObject("config");
                    mPayPresenter.setAliPartner(objConfig.getString("aliapp_partner"));
                    mPayPresenter.setAliSellerId(objConfig.getString("aliapp_seller_id"));
                    mPayPresenter.setAliPrivateKey(objConfig.getString("aliapp_key_android"));
                    mPayPresenter.setWxAppID(objConfig.getString("wx_appid"));
                    mPayPresenter.setServiceNameAli("getorderbyali");
                    mPayPresenter.setServiceNameWx("getorderbywx");
                    List<CoinPayBean> payList = new ArrayList<>();
                    if (objConfig.getIntValue("aliapp_switch") == 1) {
                        CoinPayBean coinPayBean = new CoinPayBean();
                        coinPayBean.setChecked(true);
                        coinPayBean.setName(WordUtil.getString(R.string.alipay));
                        coinPayBean.setId(Constants.PAY_TYPE_ALI);
                        coinPayBean.setIcon(R.mipmap.icon_cash_ali);
                        payList.add(coinPayBean);
                    }
                    if (objConfig.getIntValue("wx_switch") == 1) {
                        CoinPayBean coinPayBean = new CoinPayBean();
                        coinPayBean.setChecked(payList.size() <= 0);
                        coinPayBean.setName(WordUtil.getString(R.string.wxpay));
                        coinPayBean.setId(Constants.PAY_TYPE_WX);
                        coinPayBean.setIcon(R.mipmap.icon_cash_wx);
                        payList.add(coinPayBean);
                    }
                    if (mPayAdapter != null) {
                        mPayAdapter.setList(payList);
                    }
                    mChargeH5Url = obj.getString("h5url");
                }
            }

            @Override
            public void onFinish() {
                if (mRefreshLayout != null) {
                    mRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onItemClick(CoinBean bean, int position) {
        /*if (mPayPresenter == null) {
            return;
        }
        if (mPayAdapter == null) {
            ToastUtil.show(R.string.wallet_tip_5);
            return;
        }
        CoinPayBean coinPayBean = mPayAdapter.getPayCoinPayBean();
        if (coinPayBean == null) {
            ToastUtil.show(R.string.wallet_tip_5);
            return;
        }
        String href = coinPayBean.getHref();
        if (TextUtils.isEmpty(href)) {
            String money = bean.getMoney();
            String goodsName = StringUtil.contact(bean.getCoin(), mCoinName);
            String orderParams = StringUtil.contact(
                    "&uid=", CommonAppConfig.getUid(),
                    "&money=", money,
                    "&ruleid=", bean.getId(),
                    "&coin=", bean.getCoin(),
                    "&type=", "1");
            mPayPresenter.pay(coinPayBean.getId(), money, goodsName, orderParams);
        } else {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(href));
            mContext.startActivity(intent);
        }*/
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCoinChangeEvent(CoinChangeEvent e) {
        if (mBalance != null) {
            mBalance.setText(e.getCoin());
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_tip) {
            if (!TextUtils.isEmpty(mChargeH5Url)) {
                WebViewActivity.forward(mContext, mChargeH5Url);
            }
        }else if (i==R.id.btn_charge){
            charge();
        }
    }

    private void charge(){
        if (mPayPresenter == null) {
            return;
        }
        if (mPayAdapter == null) {
            ToastUtil.show(R.string.wallet_tip_5);
            return;
        }
        CoinPayBean coinPayBean = mPayAdapter.getPayCoinPayBean();
        if (coinPayBean == null) {
            ToastUtil.show(R.string.wallet_tip_5);
            return;
        }
        if (mAdapter == null) {
            ToastUtil.show(R.string.wallet_tip_6);
            return;
        }
        CoinBean bean = mAdapter.getCoinBean();
        if (bean == null) {
            ToastUtil.show(R.string.wallet_tip_6);
            return;
        }
        String href = coinPayBean.getHref();
        if (TextUtils.isEmpty(href)) {
            String money = bean.getMoney();
            String goodsName = StringUtil.contact(bean.getCoin(), mCoinName);
            String orderParams = StringUtil.contact(
                    "&uid=", CommonAppConfig.getUid(),
                    "&money=", money,
                    "&ruleid=", bean.getId(),
                    "&coin=", bean.getCoin(),
                    "&type=", "1");
            mPayPresenter.pay(coinPayBean.getId(), money, goodsName, orderParams);
        } else {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(href));
            mContext.startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        CommonHttpUtil.cancel(CommonHttpConsts.GET_BALANCE);
        CommonHttpUtil.cancel(CommonHttpConsts.GET_ALI_ORDER);
        CommonHttpUtil.cancel(CommonHttpConsts.GET_WX_ORDER);
        if (mRefreshLayout != null) {
            mRefreshLayout.setOnRefreshListener(null);
        }
        mRefreshLayout = null;
        if (mPayPresenter != null) {
            mPayPresenter.release();
        }
        mPayPresenter = null;
        super.onDestroy();
    }


}
