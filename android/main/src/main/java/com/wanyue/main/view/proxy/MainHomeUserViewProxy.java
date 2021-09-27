package com.wanyue.main.view.proxy;

import android.app.Activity;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.makeramen.roundedimageview.RoundedImageView;
import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.Constants;
import com.wanyue.common.activity.WebViewActivity;
import com.wanyue.common.adapter.base.BaseMutiRecyclerAdapter;
import com.wanyue.common.bean.UserBean;
import com.wanyue.common.custom.DrawableTextView;
import com.wanyue.common.glide.ImgLoader;
import com.wanyue.common.http.ParseHttpCallback;
import com.wanyue.common.http.ParseSingleHttpCallback;
import com.wanyue.common.proxy.RxViewProxy;
import com.wanyue.common.server.RequestFactory;
import com.wanyue.common.server.observer.DefaultObserver;
import com.wanyue.common.utils.ClickUtil;
import com.wanyue.common.utils.DebugUtil;
import com.wanyue.common.utils.ListUtil;
import com.wanyue.common.utils.ViewUtil;
import com.wanyue.main.R;
import com.wanyue.main.adapter.MainUserAdapter;
import com.wanyue.main.adapter.MenuUserAdapter;
import com.wanyue.main.api.MainAPI;
import com.wanyue.main.apply.activity.ApplyStoreActivity;
import com.wanyue.main.bean.MainUserSectionBean;
import com.wanyue.main.bean.MenuBean;
import com.wanyue.main.business.MainEvent;

import com.wanyue.main.store.view.activity.GiftProfitActivity;
import com.wanyue.main.store.view.activity.MyStoreActivity;
import com.wanyue.main.store.view.activity.ProfitActivity;
import com.wanyue.main.store.view.activity.StoreOrderListActivity;
import com.wanyue.main.view.activity.EditProfileActivity;
import com.wanyue.main.view.activity.MainActivity;
import com.wanyue.main.view.activity.MyCoinActivity;
import com.wanyue.main.view.activity.MyDiamondsActivity;
import com.wanyue.main.view.activity.SettingActivity;
import com.wanyue.shop.api.ShopAPI;
import com.wanyue.shop.bean.OrderStatementBean;
import com.wanyue.shop.business.ShopState;
import com.wanyue.shop.model.OrderModel;
import com.wanyue.shop.view.activty.MyAddressActivity;
import com.wanyue.shop.view.activty.MyCollectGoodsActivity;
import com.wanyue.shop.view.activty.MyOrderActivity;
import com.wanyue.shop.view.activty.RefundListActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

public class MainHomeUserViewProxy extends RxViewProxy implements View.OnClickListener, BaseQuickAdapter.OnItemClickListener, BaseMutiRecyclerAdapter.OnItemChildClickListener2<MainUserSectionBean> {
    private static final int OPENSTORE=183; //开通店铺
    private static final int MY_MONEY=138; //我的钱包
    private static final int MY_ADDR=139; //我的地址
    private static final int MY_COLLECT=140; //我的收藏
    private static final int CONNECT_SERVICE=141; //联系客服

    private static final int MY_CONSIGNMENT=142; //我的代销
    private static final int CONSIGNMENT_GOOD_MANNGER=143; //商品管理
    private static final int CONSIGNMENT_ORDER=144; //代销订单
    private static final int CONSIGNMENT_MONEY=145; //代销收益

    private static final int MY_STORE=146; //我的店铺
    private static final int STORE_ORDER=147; //店铺订单
    private static final int STORE_MONEY=148; //店铺收益
    private static final int OPEN=150; //店铺收益

    private static final int SETTING=149; //店铺收益
    private static final int MY_DIAMONDS=182; //我的钻石
    private static final int GIFT_PROFIT=186; //礼物收益

    private TextView mTvUserId;
    private TextView mTitleView;
    private RoundedImageView mImgAvator;
    private TextView mTvUserName;
    private TextView mTvOrderTip;
    private TextView mBtnWaitPay;
    private TextView mBtnWaitDelivered;
    private TextView mBtnWaitReceived;
    private TextView mBtnWaitEvaluated;
    private TextView mBtnRefund;

    private TextView mPointWaitPay;
    private TextView mPointWaitDelivered;
    private TextView mPointWaitReceived;
    private TextView mPointWaitEvaluated;
    private TextView mPointWaitRefund;

    private ParseHttpCallback<JSONObject>mParseMenuHttpCallback;
    private ParseHttpCallback<JSONObject>mParseUserCenterHttpCallback;

    private TextView mBtnCheckDeail;
    private TextView mTvCurrentMoney;
    private TextView mTvYesterdayMoney;
    private TextView mTvSpreadPersonNum;
    private TextView mTvSpreadNum;

    private RecyclerView mReclyView;

    private MainUserAdapter mMenuUserAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.view_main_home_user;
    }

    @Override
    protected void initView(ViewGroup contentView) {
        super.initView(contentView);
        setDefaultStatusBarPadding(R.id.vp_tab);
        mTitleView = (TextView) findViewById(R.id.title_view);
        mImgAvator = (RoundedImageView) findViewById(R.id.img_avator);
        mTvUserId = (TextView) findViewById(R.id.tv_user_id);
        mTvUserName = (TextView) findViewById(R.id.tv_user_name);
        mTvOrderTip = (TextView) findViewById(R.id.tv_order_tip);
        mBtnWaitPay =  findViewById(R.id.btn_wait_pay);
        mBtnWaitDelivered = findViewById(R.id.btn_wait_delivered);
        mBtnWaitReceived = findViewById(R.id.btn_wait_received);
        mBtnWaitEvaluated =  findViewById(R.id.btn_wait_evaluated);
        mBtnRefund = findViewById(R.id.btn_refund);

        mBtnWaitPay.setOnClickListener(this);
        mBtnWaitDelivered.setOnClickListener(this);
        mBtnWaitReceived.setOnClickListener(this);
        mBtnWaitEvaluated.setOnClickListener(this);
        mBtnRefund.setOnClickListener(this);

        mImgAvator.setOnClickListener(this);
        mTvUserName.setOnClickListener(this);
        mTvUserId.setOnClickListener(this);

        findViewById(R.id.btn_my_order).setOnClickListener(this);

        mPointWaitPay = (TextView) findViewById(R.id.point_wait_pay);
        mPointWaitDelivered = (TextView) findViewById(R.id.point_wait_delivered);
        mPointWaitReceived = (TextView) findViewById(R.id.point_wait_received);
        mPointWaitEvaluated = (TextView) findViewById(R.id.point_wait_evaluated);
        mPointWaitRefund = (TextView) findViewById(R.id.point_wait_refund);

        mBtnCheckDeail = (DrawableTextView) findViewById(R.id.btn_check_deail);
        mTvCurrentMoney = (TextView) findViewById(R.id.tv_current_money);
        mTvYesterdayMoney = (TextView) findViewById(R.id.tv_yesterday_money);
        mTvSpreadPersonNum = (TextView) findViewById(R.id.tv_spread_person_num);
        mTvSpreadNum = (TextView) findViewById(R.id.tv_spread_num);
        initReclyView();
        mTvCurrentMoney.setOnClickListener(this);
        mTvYesterdayMoney.setOnClickListener(this);
        mTvSpreadPersonNum.setOnClickListener(this);
        mBtnCheckDeail.setOnClickListener(this);
        mTvSpreadNum.setOnClickListener(this);
        findViewById(R.id.tv_current_money_tip).setOnClickListener(this);
        findViewById(R.id.tv_yesterday_money_tip).setOnClickListener(this);
        findViewById(R.id.tv_spread_person_num_tip).setOnClickListener(this);
        findViewById(R.id.tv_spread_num_tip).setOnClickListener(this);

        LiveEventBus.get(MainEvent.UPDATE_USER_CENTER).observe(getActivity(), new Observer<Object>() {
            @Override
            public void onChanged(Object object){
                requestData();
            }
        });
    }

    private void initReclyView() {
        mReclyView =  findViewById(R.id.reclyView);
        mMenuUserAdapter=new MainUserAdapter(null);
        mMenuUserAdapter.setOnItemChildClickListener2(this);
        mReclyView.setAdapter(mMenuUserAdapter);
        mMenuUserAdapter.setMenuClickListner(this);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        mReclyView.setLayoutManager(linearLayoutManager);
        mMenuUserAdapter.setMenuClickListner(this);
    }

    private void setUserMessage() {
        UserBean userBean=CommonAppConfig.getUserBean();
        if(userBean!=null){
          ImgLoader.display(getActivity(),userBean.getAvatar(),mImgAvator);
           mTvUserName.setText(userBean.getUserNiceName());
           mTvUserId.setText("ID:"+userBean.getId());
        }
    }

    @Override
    public void setUserVisibleHint(final boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(!isVisibleToUser){
          return;
        }
        requestData();
    }

    @Override
    public void onStart() {
        super.onStart();
        requestData();
    }

    private void requestData() {
        RequestFactory.getRequestManager().cancle(MainAPI.USER);
        RequestFactory.getRequestManager().cancle(MainAPI.MENU_USER);
        MainAPI.getUserCenter().map(new Function<JSONObject, List<MainUserSectionBean>>() {
            @Override
            public List<MainUserSectionBean> apply(JSONObject info) throws Exception {
                parseUserCenterOntherData(info);
                boolean isShop= info.getIntValue("isshop")==1;
                List<MainUserSectionBean>list=new ArrayList<>();
                openShop(isShop,list);
                return list;
            }
        }).flatMap(new Function<List<MainUserSectionBean>, ObservableSource<List<MainUserSectionBean>>>() {
            @Override
            public ObservableSource<List<MainUserSectionBean>> apply(List<MainUserSectionBean> list) throws Exception {
                return MainAPI.getMenuUser(list,CONNECT_SERVICE,SETTING);
            }
        }).subscribe(new DefaultObserver<List<MainUserSectionBean>>() {
            @Override
            public void onNext(List<MainUserSectionBean> list) {
                if(mMenuUserAdapter!=null){
                   mMenuUserAdapter.setData(list);
                }
            }
        });
    }

    private JSONObject mUserJsonObject;
    private void parseUserCenterOntherData(JSONObject info) {
        mUserJsonObject=info;
        String userJson=info.toJSONString();
        UserBean userBean= JSON.parseObject(userJson,UserBean.class);
        CommonAppConfig.setUserBean(userBean);
        JSONObject orderStatusJsonObject=info.getJSONObject("orderStatusNum");
        if(orderStatusJsonObject!=null){
            OrderStatementBean userOrderNumBean=orderStatusJsonObject.toJavaObject(OrderStatementBean.class);
            OrderModel.setOrderStatementBean(userOrderNumBean);
            setOrderNumCount(userOrderNumBean);
        }
        setUserMessage();
        String spread_order_count=info.getIntValue("order_count")+"";
        String spread_count=info.getInteger("spread_total")+"";
        String yesterDay=info.getString("yesterDay");
        String brokerage_price=info.getString("brokerage_price");
        if(mTvCurrentMoney!=null){
            mTvCurrentMoney.setText(brokerage_price);
        }
        if(mTvYesterdayMoney!=null){
            mTvYesterdayMoney.setText(yesterDay);
        }
        if(mTvSpreadPersonNum!=null){
            String personString= spread_count+"人";
            int startIndex=personString.indexOf("人");
            SpannableStringBuilder style = new SpannableStringBuilder(personString);
            style.setSpan(new AbsoluteSizeSpan(10, true), startIndex, startIndex+1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            mTvSpreadPersonNum.setText(style);
        }
        if(mTvSpreadNum!=null){
            String numString= spread_order_count+"单";
            int startIndex=numString.indexOf("单");
            SpannableStringBuilder style = new SpannableStringBuilder(numString);
            style.setSpan(new AbsoluteSizeSpan(10, true), startIndex, startIndex+1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            mTvSpreadNum.setText(style);
        }
    }

    private void openShop(boolean isShop, List<MainUserSectionBean> data) {
        if(isShop){
           /* MainUserSectionBean mainUserSectionBean1=new MainUserSectionBean();
            mainUserSectionBean1.setTitle("我的代销");
            mainUserSectionBean1.setId(MY_CONSIGNMENT);
            mainUserSectionBean1.setRightTitle("查看详情");
            List<MenuBean>list=ListUtil.asList(new MenuBean("商品管理",CONSIGNMENT_GOOD_MANNGER,R.drawable.icon_main_user_good_mannger),
            new MenuBean("代销订单",CONSIGNMENT_ORDER,R.drawable.icon_main_user_consignment_order),
            new MenuBean("代销收益",CONSIGNMENT_MONEY,R.drawable.icon_main_user_consignment_money)
            );
            data.add(mainUserSectionBean1);
            mainUserSectionBean1.setMenuBeanList(list);*/
            MainUserSectionBean mainUserSectionBean2=new MainUserSectionBean();
            mainUserSectionBean2.setTitle("我的店铺");
            mainUserSectionBean2.setRightTitle("店铺主页");
            mainUserSectionBean2.setId(MY_STORE);
            List<MenuBean>list2=ListUtil.asList(new MenuBean("店铺订单",STORE_ORDER,R.drawable.icon_main_user_store_order),
                    new MenuBean("店铺收益",STORE_MONEY,R.drawable.icon_main_user_store_money),
                    new MenuBean("我要开播",OPEN,R.drawable.icon_open_live)
            );
            mainUserSectionBean2.setMenuBeanList(list2);
            data.add(mainUserSectionBean2);
        }
    }

    /*设置订单数量*/
    private void setOrderNumCount(OrderStatementBean userOrderNumBean) {
        ViewUtil.setTextAndViewsibleByNumber(mPointWaitPay,userOrderNumBean.getUnpaidCount());
        ViewUtil.setTextAndViewsibleByNumber(mPointWaitDelivered,userOrderNumBean.getUnShippedCount());
        ViewUtil.setTextAndViewsibleByNumber(mPointWaitReceived,userOrderNumBean.getReceivedCount());
        ViewUtil.setTextAndViewsibleByNumber(mPointWaitEvaluated,userOrderNumBean.getEvaluatedCount());
        ViewUtil.setTextAndViewsibleByNumber(mPointWaitRefund,userOrderNumBean.getRefundCount());
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==EditProfileActivity.EDIT&&resultCode== Activity.RESULT_OK){
            String avator=data.getStringExtra(Constants.URL);
            String nickName=data.getStringExtra(Constants.DATA);
            UserBean userBean=CommonAppConfig.getUserBean();
            if(userBean!=null){
                userBean.setAvatar(avator);
                userBean.setUserNiceName(nickName);
                setUserMessage();
            }
        }

    }

    @Override
    public void onClick(View v) {
        if(!ClickUtil.canClick()){
            return;
        }
        int id=v.getId();
        if(id==R.id.btn_wait_pay||id==R.id.btn_my_order){
            toBuyerOder(0);
        }else if(id==R.id.btn_wait_delivered){
            toBuyerOder(1);
        }else if(id==R.id.btn_wait_received){
            toBuyerOder(2);
        }else if(id==R.id.btn_wait_evaluated){
            toBuyerOder(3);
        }else if(id==R.id.btn_refund){
            startActivity(RefundListActivity.class);
        }else if(id==R.id.img_avator||id==R.id.tv_user_name||id==R.id.tv_user_id){
            Intent intent=new Intent(getActivity(),EditProfileActivity.class);
            startActivityForResult(intent,EditProfileActivity.EDIT);
        }else if(id==R.id.btn_check_deail||
                id==R.id.tv_current_money||id==R.id.tv_current_money_tip||
                id==R.id.tv_yesterday_money||id==R.id.tv_yesterday_money_tip)
        {
        }else if(id==R.id.tv_spread_person_num||id==R.id.tv_spread_person_num_tip){
        }else if(id==R.id.tv_spread_num||id==R.id.tv_spread_num_tip){
        }
    }

    private void toBuyerOder(int position) {
        MyOrderActivity.forward(getActivity(),position);
    }

    //个人中心菜单点击
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if(!ClickUtil.canClick()){
            return;
        }
        MenuUserAdapter menuUserAdapter= (MenuUserAdapter) adapter;
        MenuBean menuBean=menuUserAdapter.getItem(position);
        if(menuBean==null){
            DebugUtil.sendException("MenuBean==null");
            return;
        }
        int menuId=menuBean.getId();
        switch (menuId){
            case MY_MONEY:
                toMyCoin();
                break;
            case MY_ADDR:
                toMyAddress();
                break;
            case MY_COLLECT:
                toCollectGoods();
                break;
            case CONNECT_SERVICE:
                openService();
                break;
            case SETTING:
                startActivity(SettingActivity.class);
                break;
            case CONSIGNMENT_GOOD_MANNGER:
                break;
            case STORE_ORDER:
                StoreOrderListActivity.forward(getActivity(), ShopState.ORDER_SELL_STORE);
                break;
            case CONSIGNMENT_ORDER:
                break;
            case STORE_MONEY:
                ProfitActivity.forward(getActivity(),ProfitActivity.TYPE_STORE);
                break;
            case CONSIGNMENT_MONEY:
                ProfitActivity.forward(getActivity(),ProfitActivity.TYPE_CONSIGNMENT);
                break;
            case OPENSTORE:
               startActivity(ApplyStoreActivity.class);
                break;
            case MY_DIAMONDS:
                MyDiamondsActivity.forward(getActivity());
                break;
                case GIFT_PROFIT:
                   GiftProfitActivity.forward(getActivity());
                break;

            case OPEN:
                ( (MainActivity)getActivity()).openLive(null);
                break;
            default:
                break;
        }
    }

    private String mServiceLink;
    private void openService() {
        if(TextUtils.isEmpty(mServiceLink)){
            ShopAPI.getShopService("0", new ParseSingleHttpCallback<String>("service_url") {
                @Override
                public void onSuccess(String data) {
                    mServiceLink=data;
                    WebViewActivity.forward(getActivity(),data);
                }
            });
        }else{
            WebViewActivity.forward(getActivity(),mServiceLink);
        }
    }


    private void toMyCoin() {
        if(mUserJsonObject==null){
            return;
        }
        String nowMyMoney=mUserJsonObject.getString("now_money");
        String sumPrice=null;
        JSONObject jsonObject=mUserJsonObject.getJSONObject("orderStatusNum");
        if(jsonObject!=null){
           sumPrice= jsonObject.getString("sum_price");
        }
        MyCoinActivity.forward(getActivity(),nowMyMoney,sumPrice);
    }

    private void toMyAddress() {
        startActivity(MyAddressActivity.class);
    }
    private void toCollectGoods() {
        startActivity(MyCollectGoodsActivity.class);
    }

    @Override
    public void onItemClick(int position, MainUserSectionBean mainUserSectionBean, View view) {
                int id=mainUserSectionBean.getId();
                switch (id){
                    case MY_CONSIGNMENT:
                        break;
                    case MY_STORE:
                        startActivity(MyStoreActivity.class);
                        break;
                    default:
                        break;

                }
    }
}
