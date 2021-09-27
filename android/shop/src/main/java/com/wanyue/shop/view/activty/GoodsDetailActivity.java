package com.wanyue.shop.view.activty;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.wanyue.common.Constants;
import com.wanyue.common.activity.BaseActivity;
import com.wanyue.common.bean.GoodsBean;
import com.wanyue.common.custom.refresh.RxRefreshView;
import com.wanyue.common.http.ParseHttpCallback;
import com.wanyue.common.utils.ClickUtil;
import com.wanyue.common.utils.DebugUtil;
import com.wanyue.common.utils.L;
import com.wanyue.common.utils.ViewUtil;
import com.wanyue.shop.R;
import com.wanyue.shop.api.ShopAPI;
import com.wanyue.shop.bean.GoodsParseBean;
import com.wanyue.shop.bean.StoreGoodsBean;
import com.wanyue.shop.model.GoodDetailModel;
import com.wanyue.shop.view.view.BaseGoodItemViewProxy;
import com.wanyue.shop.view.view.BaseGoodsDetailBottomViewProxy;
import com.wanyue.shop.view.view.GoodsDetailRecommendViewProxy;
import com.wanyue.shop.view.view.GoodsEvaluateViewProxy;
import com.wanyue.shop.view.view.GoodsHandleViewProxy;
import com.wanyue.shop.view.view.GoodsPannelViewProxy;
import com.wanyue.shop.view.view.GoodsWebViewProxy;
import com.wanyue.shop.view.view.SpecsSelectViewProxy;
import java.util.List;

public class GoodsDetailActivity extends BaseActivity implements View.OnClickListener {
    private String mGoodsId;
    private ViewGroup mVpBottom;
    private GoodsPannelViewProxy mPannelViewProxy;
    private GoodsEvaluateViewProxy mGoodsEvaluateViewProxy;
    private GoodsDetailRecommendViewProxy mGoodsRecommendViewProxy;
    private GoodsWebViewProxy mGoodsWebViewProxy;
    private BaseGoodsDetailBottomViewProxy mGoodsHandleViewProxy;
    private ViewGroup mContainer;
    private FrameLayout mVpWindow;
    private GoodDetailModel mGoodDetailModel;

    private FrameLayout mVTab;
    private FrameLayout mTab0;
    private FrameLayout mTab1;
    private FrameLayout mTab2;
    private FrameLayout mTab3;

    private ScrollView mScrollView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);//键盘顶起布局
        super.onCreate(savedInstanceState);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void init() {
        setDefaultStatusBarPadding(R.id.v_tab);
        mGoodsId=getIntent().getStringExtra(Constants.KEY_ID);
        mVpBottom =  findViewById(R.id.vp_bottom);
        mContainer=  findViewById(R.id.container);
        mVpWindow =  findViewById(R.id.vp_window);
        mVTab = (FrameLayout) findViewById(R.id.v_tab);
        mTab0 = (FrameLayout) findViewById(R.id.tab_0);
        mTab1 = (FrameLayout) findViewById(R.id.tab_1);
        mTab2 = (FrameLayout) findViewById(R.id.tab_2);
        mTab3 = (FrameLayout) findViewById(R.id.tab_3);

        findViewById(R.id.btn_0).setOnClickListener(this);
        findViewById(R.id.btn_1).setOnClickListener(this);
        findViewById(R.id.btn_2).setOnClickListener(this);
        findViewById(R.id.btn_3).setOnClickListener(this);
        selectTabLine(0);
        try {
          mGoodsHandleViewProxy=createHandViewProxy();
        }catch (Exception e){
            e.printStackTrace();
        }

        mScrollView = (ScrollView) findViewById(R.id.scroll_view);
        mScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(mPannelViewProxy!=null){
                 if(mPannelViewProxy.isScrollTop(scrollY)){ //每一个view都去监听scrollview的滚动位置
                     selectTabLine(0);
                     if(mPannelViewProxy.getOffectTabHeight()==0){
                         //将tab栏高度传入,加入偏移量,目的是控制view滑动到tab栏正下面属于复合条件
                        mPannelViewProxy.setOffectTabHeight(mVTab.getHeight());
                     }
                  }
                }

                if(mGoodsEvaluateViewProxy!=null){
                  if(mGoodsEvaluateViewProxy.getOffectTabHeight()==0){
                     mGoodsEvaluateViewProxy.setOffectTabHeight(mVTab.getHeight());
                  }

                 if(mGoodsEvaluateViewProxy.isScrollTop(scrollY)){
                     selectTabLine(1);
                 }
                }

                if(mGoodsRecommendViewProxy!=null){
                    if(mGoodsRecommendViewProxy.isScrollTop(scrollY)){
                        selectTabLine(2);
                    }
                    if(mGoodsRecommendViewProxy.getOffectTabHeight()==0){
                       mGoodsRecommendViewProxy.setOffectTabHeight(mVTab.getHeight());
                    }
                }
                if(mGoodsWebViewProxy!=null){
                    if(mGoodsWebViewProxy.isScrollTop(scrollY)){
                       selectTabLine(3);
                    }
                    if(mGoodsWebViewProxy.getOffectTabHeight()==0){
                        mGoodsWebViewProxy.setOffectTabHeight(mVTab.getHeight());
                    }
                }
                float alpha=(float)scrollY/(float)300; //aplpha值控制范围在0-300
                ViewUtil.setAlpha(mVTab,alpha); //设置导航栏的透明度
            }
        });

        addGoodsBottomView();
        mGoodDetailModel= ViewModelProviders.of(this).get(GoodDetailModel.class);
        mGoodDetailModel.setLiveUid(getIntent().getStringExtra(Constants.LIVE_UID));
    }

    protected BaseGoodsDetailBottomViewProxy createHandViewProxy() throws InstantiationException, IllegalAccessException {
        Class<?extends BaseGoodsDetailBottomViewProxy>cs= (Class<? extends BaseGoodsDetailBottomViewProxy>) getIntent().getSerializableExtra(Constants.KEY_CLASS);
        if(cs==null){
           return new GoodsHandleViewProxy();
        }else{
           return cs.newInstance();
        }
    }


    public void addGoodsBottomView() {
        if(mGoodsHandleViewProxy!=null){
            getViewProxyMannger().addViewProxy(mVpBottom,mGoodsHandleViewProxy,mGoodsHandleViewProxy.getDefaultTag());
        }
    }

    private void checkDffect(int item) { //item 0,1,2,3 分别对应的4个模块
        if(mCurrentItem==item){
            return;
        }
        selectTabLine(item);
        switch (item){
            case 0:
                scroll(mPannelViewProxy);
                break;
            case 1:
                scroll(mGoodsEvaluateViewProxy);
                break;
            case 2:
                scroll(mGoodsRecommendViewProxy);
                break;
            case 3:
                scroll(mGoodsWebViewProxy);
                break;
            default:
                break;
     }
    }


    private void scroll(final BaseGoodItemViewProxy goodItemViewProxy) {//goodItemViewProxy是view的管理器
        if(mScrollView!=null&&goodItemViewProxy!=null&&goodItemViewProxy.getTop()!=-1){
            mScrollView.post(new Runnable() { //通过post方法延迟执行
                @Override
                public void run() {
                    mScrollView.scrollTo(0,goodItemViewProxy.getTop());
                }
            });
        }else {
            DebugUtil.sendException("scroll报错了");
        }
    }

    private int mCurrentItem=-1;
    private void selectTabLine(int item) {
        if(mCurrentItem==item){
            return;
        }
        mCurrentItem=item;
        switch (item){
            case 0:
                ViewUtil.setVisibility(mTab0,View.VISIBLE);
                ViewUtil.setVisibility(mTab1,View.INVISIBLE);
                ViewUtil.setVisibility(mTab2,View.INVISIBLE);
                ViewUtil.setVisibility(mTab3,View.INVISIBLE);
                break;
            case 1:
                ViewUtil.setVisibility(mTab0,View.INVISIBLE);
                ViewUtil.setVisibility(mTab1,View.VISIBLE);
                ViewUtil.setVisibility(mTab2,View.INVISIBLE);
                ViewUtil.setVisibility(mTab3,View.INVISIBLE);
                break;
            case 2:
                ViewUtil.setVisibility(mTab0,View.INVISIBLE);
                ViewUtil.setVisibility(mTab1,View.INVISIBLE);
                ViewUtil.setVisibility(mTab2,View.VISIBLE);
                ViewUtil.setVisibility(mTab3,View.INVISIBLE);
                break;
            case 3:
                ViewUtil.setVisibility(mTab0,View.INVISIBLE);
                ViewUtil.setVisibility(mTab1,View.INVISIBLE);
                ViewUtil.setVisibility(mTab2,View.INVISIBLE);
                ViewUtil.setVisibility(mTab3,View.VISIBLE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onFirstResume() {
        super.onFirstResume();
        ShopAPI.getGoodsDetail(mGoodsId, new ParseHttpCallback<JSONObject>() {
            @Override
            public void onSuccess(int code, String msg, JSONObject info) {
               if(isSuccess(code)){
                   String json=info.toJSONString();
                   GoodsParseBean goodsParseBean= info.toJavaObject(GoodsParseBean.class);
                   if(mGoodDetailModel!=null){
                      mGoodDetailModel.transFormData(goodsParseBean,info);
                      mGoodDetailModel.setGoodsParseBean(goodsParseBean);
                   }
                   List<GoodsBean> goodsBeanList=goodsParseBean.getGoodsList();
                   StoreGoodsBean storeGoodsBean=goodsParseBean.getGoodsInfo();
                   if(mGoodsHandleViewProxy!=null&&storeGoodsBean!=null){
                      mGoodsHandleViewProxy.setStoreGoodsBean(storeGoodsBean);
                   }
                   /*顺序一定不要乱*/
                   initGoodsPannelViewProxy(goodsParseBean);
                   initGoodsEvaluate(goodsParseBean);
                   initRecommendViewProxy(goodsBeanList);
                   initWebViewProxy(storeGoodsBean.getDescription());
               }
            }
        });
    }

    /*初始化评价相关*/
    private void initGoodsEvaluate(GoodsParseBean goodsParseBean) {
        if(mGoodsEvaluateViewProxy==null){
           mGoodsEvaluateViewProxy=new GoodsEvaluateViewProxy();
           getViewProxyMannger().addViewProxy(mContainer,mGoodsEvaluateViewProxy,mGoodsEvaluateViewProxy.getDefaultTag());
        }
        mGoodsEvaluateViewProxy.setData(goodsParseBean);
    }


    /*初始化产品介绍的html*/
    private void initWebViewProxy(String description) {
            if(mGoodsWebViewProxy==null){
               mGoodsWebViewProxy=new GoodsWebViewProxy();
               getViewProxyMannger().addViewProxy(mContainer,mGoodsWebViewProxy,mGoodsWebViewProxy.getDefaultTag());
            }
            mGoodsWebViewProxy.loadHtml(description);
    }

    /*初始化商品部分布局哦*/
    private void initGoodsPannelViewProxy(GoodsParseBean storeGoodsBean){
        if(mPannelViewProxy==null){
           mPannelViewProxy=new GoodsPannelViewProxy();
           getViewProxyMannger().addViewProxy(mContainer,mPannelViewProxy,mPannelViewProxy.getDefaultTag());
        }
        mPannelViewProxy.setData(storeGoodsBean);
    }

    /*初始化推荐布局*/
    private void initRecommendViewProxy(List<GoodsBean> goodsBeanList) {
        if(mGoodsRecommendViewProxy==null){
           mGoodsRecommendViewProxy=new GoodsDetailRecommendViewProxy();
           getViewProxyMannger().addViewProxy(mContainer,mGoodsRecommendViewProxy,mGoodsRecommendViewProxy.getDefaultTag());
        }
        mGoodsRecommendViewProxy.setData(goodsBeanList);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_goods_detail;
    }
    /*显示选择框*/
    public void showSpecsSelectWindow() {
        String tag="SpecsSelectViewProxy";
        if(getViewProxyMannger().getViewProxyByTag(tag)!=null){
            return;
        }

        SpecsSelectViewProxy specsSelectViewProxy=new SpecsSelectViewProxy(){
            /*@Override
            public void addViewToParent(ViewGroup container) {
                LayoutTransition transition = new LayoutTransition();
                transition.enableTransitionType(LayoutTransition.APPEARING);
                container.setLayoutTransition(transition);
                FrameLayout.LayoutParams layoutParams=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.gravity= Gravity.BOTTOM;
                container.addView(mContentView,layoutParams);
            }*/
        };
        getViewProxyMannger().addViewProxy(mVpWindow,specsSelectViewProxy,tag);
    }

    public static void forward(Context context,String goodsId){
        forward(context,goodsId,null);
    }
    public static void forward(Context context,String goodsId,Class<?extends BaseGoodsDetailBottomViewProxy>cs){
        forward(context,goodsId,null,cs);
    }

    public static void forward(Context context,String goodsId,String liveUid,Class<?extends BaseGoodsDetailBottomViewProxy>cs){
        Intent intent=new Intent(context,GoodsDetailActivity.class);
        intent.putExtra(Constants.KEY_ID,goodsId);
        intent.putExtra(Constants.LIVE_UID,liveUid);
        if(cs!=null){
            intent.putExtra(Constants.KEY_CLASS,cs);
        }
        context.startActivity(intent);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */

    @Override
    public void onClick(View v) {
        if(!ClickUtil.canClick()){
            return;
        }

        int checkedId=v.getId();
        if(checkedId==R.id.btn_0){
            checkDffect(0);
        }else if(checkedId==R.id.btn_1){
            checkDffect(1);
        }else if(checkedId==R.id.btn_2){
            checkDffect(2);
        }else if(checkedId==R.id.btn_3){
            checkDffect(3);
        }
    }
}
