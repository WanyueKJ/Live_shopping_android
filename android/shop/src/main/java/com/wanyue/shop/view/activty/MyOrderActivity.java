package com.wanyue.shop.view.activty;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;
import com.wanyue.common.Constants;
import com.wanyue.common.activity.BaseActivity;
import com.wanyue.common.custom.ShadowContainer;
import com.wanyue.common.proxy.ViewProxyPageAdapter;
import com.wanyue.common.utils.DebugUtil;
import com.wanyue.common.utils.ListUtil;
import com.wanyue.shop.R;
import com.wanyue.shop.adapter.BuyerOrderIndicatorAdapter;
import com.wanyue.shop.api.ShopAPI;
import com.wanyue.shop.bean.OrderBean;
import com.wanyue.shop.bean.OrderStatementBean;
import com.wanyue.shop.business.ShopState;
import com.wanyue.shop.model.OrderModel;
import com.wanyue.shop.view.view.buyer.BuyerOderViewProxy;
import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import java.util.List;
import io.reactivex.Observable;


public class MyOrderActivity extends BaseActivity {
    private TextView mTgTvOrderMessage;
    private TextView mTvOrderMessage;
    private ShadowContainer mVpOrderContainer;
    private ViewPager mViewPager;
    private List<BuyerOderViewProxy> mBuyerOderViewProxyList;
    private MagicIndicator mIndicator;
    private BuyerOrderIndicatorAdapter mIndicatorAdapter;
    private String[] mTitleArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void init() {
        setTabTitle(R.string.my_order);
        mTgTvOrderMessage = findViewById(R.id.tg_tv_order_message);
        mTvOrderMessage = findViewById(R.id.tv_order_message);
        mVpOrderContainer =  findViewById(R.id.vp_order_container);
        mViewPager =  findViewById(R.id.viewPager);
        mBuyerOderViewProxyList=initViewArray();
        mIndicator =  findViewById(R.id.indicator);
        mViewPager.setOffscreenPageLimit(mBuyerOderViewProxyList.size());
        mTitleArray=new String[5];
        initTitleArray("","","","","");
        CommonNavigator commonNavigator = new CommonNavigator(this);
        mIndicatorAdapter = new BuyerOrderIndicatorAdapter(mTitleArray, this, mViewPager);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(mIndicatorAdapter);

        mIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mIndicator, mViewPager);
        ViewProxyPageAdapter pageAdapter = new ViewProxyPageAdapter(getViewProxyMannger(),mBuyerOderViewProxyList);
        int position=getIntent().getIntExtra(Constants.POSITION,0);
        pageAdapter.attachViewPager(mViewPager,position);
        OrderModel.watchOrderStatementChange(this, new Observer<OrderStatementBean>() {
            @Override
            public void onChanged(OrderStatementBean orderStatementBean) {
                setOrderStatementToUI(orderStatementBean);
            }
        });
        OrderModel.freshOrderStatement();
    }


    private void initTitleArray(String temp1,String temp2,String temp3,String temp4,String temp5) {
        if(mTitleArray==null){
           mTitleArray=new String[5];
        }
        mTitleArray[0]=getString(R.string.mail_001,temp1);
        mTitleArray[1]=getString(R.string.mail_002,temp2);
        mTitleArray[2]=getString(R.string.mail_003,temp3);
        mTitleArray[3]=getString(R.string.mail_004,temp4);
        mTitleArray[4]=getString(R.string.mail_006,temp5);
    }

    private void setOrderStatementToUI(OrderStatementBean orderStatementBean) {
        mTvOrderMessage.setText(getString(R.string.user_order_total,Integer.toString(orderStatementBean.getOrderCount()),orderStatementBean.getSumPrice()));
        initTitleArray(
                Integer.toString(orderStatementBean.getUnpaidCount()),
                Integer.toString(orderStatementBean.getUnShippedCount()),
                Integer.toString(orderStatementBean.getReceivedCount()),
                Integer.toString(orderStatementBean.getEvaluatedCount()),
                Integer.toString(orderStatementBean.getCompleteCount()));
        if(mIndicatorAdapter!=null){
           mIndicatorAdapter.notifyTitle(mTitleArray);
        }
    }

    private List<BuyerOderViewProxy> initViewArray() {
        BuyerOderViewProxy viewProxy1=new BuyerOderViewProxy() {
            @Override
            public Observable<List<OrderBean>> getData(int p) {
                OrderModel.freshOrderStatement();
              return ShopAPI.getOrderList(ShopState.ORDER_STATE_WAIT_PAY,p,"",ShopState.ORDER_BUY_SELF);
            }
        };
        BuyerOderViewProxy viewProxy2=new BuyerOderViewProxy() {
            @Override
            public Observable<List<OrderBean>> getData(int p) {
                OrderModel.freshOrderStatement();
                return ShopAPI.getOrderList(ShopState.ORDER_STATE_WAIT_DELIVERED,p,"",ShopState.ORDER_BUY_SELF);
            }
        };
        BuyerOderViewProxy viewProxy3=new BuyerOderViewProxy() {
            @Override
            public Observable<List<OrderBean>> getData(int p) {
                OrderModel.freshOrderStatement();
                return ShopAPI.getOrderList(ShopState.ORDER_STATE_WAIT_RECEIVE,p,"",ShopState.ORDER_BUY_SELF);
            }
        };
        BuyerOderViewProxy viewProxy4=new BuyerOderViewProxy() {
            @Override
            public Observable<List<OrderBean>> getData(int p) {
                OrderModel.freshOrderStatement();
                return ShopAPI.getOrderList(ShopState.ORDER_STATE_WAIT_EVALUATE,p,"",ShopState.ORDER_BUY_SELF);
            }
        };
        BuyerOderViewProxy viewProxy5=new BuyerOderViewProxy() {
            @Override
            public Observable<List<OrderBean>> getData(int p) {
                OrderModel.freshOrderStatement();
                return ShopAPI.getOrderList(ShopState.ORDER_STATE_COMPELETE,p,"",ShopState.ORDER_BUY_SELF);
            }
        };
        List<BuyerOderViewProxy>viewArray=ListUtil.asList(viewProxy1,viewProxy2,viewProxy3,viewProxy4,viewProxy5);
        return viewArray;
    }


    //状态类型|0=未支付,1=未发货,2=待收货,3=待评价,4=交易完成,-1=申请退款中,-2=已退款,11=拼团中
    public static void forward(Context context,int position){
        if(context==null){
            DebugUtil.sendException("context不能为null");
            return;
        }
        Intent intent=new Intent(context,MyOrderActivity.class);
        intent.putExtra(Constants.POSITION,position);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_order;
    }

}
