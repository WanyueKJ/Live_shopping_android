package com.wanyue.shop.view.activty;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.wanyue.common.Constants;
import com.wanyue.common.activity.BaseActivity;
import com.wanyue.common.custom.CheckImageView;
import com.wanyue.common.custom.refresh.RxRefreshView;
import com.wanyue.common.server.observer.DefaultObserver;
import com.wanyue.common.utils.ClickUtil;
import com.wanyue.common.utils.DialogUitl;
import com.wanyue.common.utils.ListUtil;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.common.utils.ViewUtil;
import com.wanyue.shop.R;
import com.wanyue.shop.adapter.ShopCartAdapter;
import com.wanyue.shop.api.ShopAPI;
import com.wanyue.shop.bean.ShopCartBean;
import com.wanyue.shop.bean.ShopcartParseBean;
import com.wanyue.shop.business.ShopState;
import com.wanyue.shop.model.ShopCartModel;
import com.wanyue.shop.view.view.HotGoodsEmptyViewProxy;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class ShopCartActivity extends BaseActivity implements View.OnClickListener, ShopCartAdapter.DeleteInvaildListner {
    private static final int STATE_NORMAL=1; //普通状态
    private static final int STATE_EDIT=2;  //编辑状态
    private int mState=STATE_NORMAL;

    private TextView mBtnMannger;
    private RxRefreshView<MultiItemEntity> mRefreshView;
    private ViewGroup mVpBottom;
    private CheckImageView mCheckTotalImage;
    private ViewGroup mVpToolManger;
    private TextView mBtnCollect;
    private TextView mBtnDelete;
    private ViewGroup mVpToolBuy;
    private TextView mTvTotalPrice;
    private TextView mBtnCommit;
    private TextView mTvGoodsNum;
    private TextView mTvTotalNum;

    private ShopCartAdapter mShopCartAdapter;
    private ShopCartModel mShopCartModel;
    private ShopcartParseBean mShopcartParseBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);//键盘顶起布局
        super.onCreate(savedInstanceState);
    }

    @Override
    public void init() {
        setTabTitle(R.string.shop_cart);
        mShopCartModel= ViewModelProviders.of(this).get(ShopCartModel.class);
        mBtnMannger = (TextView) findViewById(R.id.btn_mannger);
        mTvGoodsNum = (TextView) findViewById(R.id.tv_goods_num);
        mRefreshView = (RxRefreshView) findViewById(R.id.refreshView);
        mVpBottom =  findViewById(R.id.vp_bottom);
        mCheckTotalImage = (CheckImageView) findViewById(R.id.check_total_image);
        mVpToolManger =  findViewById(R.id.vp_tool_manger);
        mBtnCollect = (TextView) findViewById(R.id.btn_collect);
        mBtnDelete = (TextView) findViewById(R.id.btn_delete);
        mVpToolBuy =  findViewById(R.id.vp_tool_buy);
        mTvTotalPrice = (TextView) findViewById(R.id.tv_total_price);
        mBtnCommit = (TextView) findViewById(R.id.btn_commit);
        mTvTotalNum = (TextView) findViewById(R.id.tv_total_num);
        mRefreshView.setLoadMoreEnable(false);
        mRefreshView.setHasFixedSize(true);
        HotGoodsEmptyViewProxy hotGoodsEmptyViewProxy=new HotGoodsEmptyViewProxy();
        hotGoodsEmptyViewProxy.setEmptyIconId(R.drawable.bg_empty_no_cart);
        mRefreshView.setEmptyViewProxy(getViewProxyMannger(),hotGoodsEmptyViewProxy);

        mShopCartAdapter=new ShopCartAdapter(null,getViewProxyMannger(),this,mShopCartModel);
        mRefreshView.setAdapter(mShopCartAdapter);
        mRefreshView.setRefreshEnable(false);
        mRefreshView.setReclyViewSetting(RxRefreshView.ReclyViewSetting.createLinearSetting(this,0));
        mRefreshView.setDataListner(new RxRefreshView.DataListner<MultiItemEntity>() {
            @Override
            public Observable<List<MultiItemEntity>> loadData(int p) {
                return getData();
            }
            @Override
            public void compelete(List<MultiItemEntity> data) {
                mShopCartAdapter.expandAll();
                if(ListUtil.haveData(data)){
                   ViewUtil.setVisibility(mVpBottom,View.VISIBLE);
                }else{
                   ViewUtil.setVisibility(mVpBottom,View.GONE);
                }
                 notifyAllSelectButton();
            }
            @Override
            public void error(Throwable e) {
            }
        });
        freshStateUI();
        int shopCartNum=ShopCartModel.getShopCartNum();
        String goodsNum=Integer.toString(shopCartNum);
        mTvGoodsNum.setText(goodsNum);
        mTvTotalNum.setText(getString(R.string.all_select,goodsNum));
        mBtnMannger.setOnClickListener(this);
        mBtnCollect.setOnClickListener(this);
        mBtnDelete.setOnClickListener(this);
        mCheckTotalImage.setOnClickListener(this);
        mBtnCommit.setOnClickListener(this);
        initShopCartDataObserver();
        setTabBackGound(R.color.white);
    }

    /*查看所有商品是否是选中状态*/
    private void notifyAllSelectButton() {
        if(mShopCartModel!=null){
           mCheckTotalImage.setChecked(mShopCartModel.isAllSelect());
        }
    }


    private void initShopCartDataObserver() {
        /*观察价格变化*/
        mShopCartModel.getPriceData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@NotNull String price) {
                mTvTotalPrice.setText(price);
            }
        });

        /*观察购物车数量变化*/
        ShopCartModel.obserShopCartNum(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                String numString=integer.toString();
                mTvGoodsNum.setText(numString);
                mTvTotalNum.setText(getString(R.string.all_select,numString));
            }
        });

        //观察是有选中的状态
        mShopCartModel.getCheckLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                mBtnCommit.setEnabled(aBoolean);
            }
        });

        
        mShopCartModel.getNotifyItemChangeLiveData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                notifyAllSelectButton();
                if(integer==ShopCartModel.NOTIFY_DEFAULT){
                    if(mRefreshView!=null){
                       mRefreshView.initData();
                    }
                }else if(integer==ShopCartModel.NOTIFY_ALL){
                    if(mShopCartAdapter!=null){
                       mShopCartAdapter.notifyDataSetChanged();
                       if(mShopCartModel!=null){
                          mShopCartModel.measureTotalPrice();
                       }
                    }
                }else if(integer==ShopCartModel.NOTIFY_LOCK_SCROLL){
                    if(mRefreshView!=null){
                       mRefreshView.setCanScroll(false);
                    }
                }else if(integer==ShopCartModel.NOTIFY_UNLOCK_SCROLL){
                    if(mRefreshView!=null){
                       mRefreshView.setCanScroll(true);
                    }
                }
            }
        });
    }


    private void freshStateUI() {
        if(mState==STATE_NORMAL){
            mBtnMannger.setText(R.string.mannger);
            mVpToolBuy.setVisibility(View.VISIBLE);
            mVpToolManger.setVisibility(View.GONE);
        }else{
            mBtnMannger.setText(R.string.cancel);
            mVpToolBuy.setVisibility(View.GONE);
            mVpToolManger.setVisibility(View.VISIBLE);
        }
    }

    /*设置状态*/
    private void judgeState() {
        if(mState==STATE_NORMAL){
            mState=STATE_EDIT;
            mShopCartModel.setEditMode(true);
        }else if(mState==STATE_EDIT){
            mState=STATE_NORMAL;
            mShopCartModel.setEditMode(false);
        }
        freshStateUI();
    }


    /*网络请求*/
    private Observable<List<MultiItemEntity>> getData() {
        return ShopAPI.getShopCartData().map(new Function<ShopcartParseBean, List<MultiItemEntity>>() {
            @Override
            public List<MultiItemEntity> apply(ShopcartParseBean shopcartParseBean) throws Exception {
                mShopcartParseBean=shopcartParseBean;
                List<MultiItemEntity>list=ShopCartModel.transFormListData(shopcartParseBean);
                if(mShopCartModel!=null){
                   mShopCartModel.setShopCartData(shopcartParseBean.getValid());
                }
                return list;
            }
        }).compose(this.<List<MultiItemEntity>>bindUntilOnDestoryEvent());
    }

    @Override
    protected void onFirstResume() {
        super.onFirstResume();
        if(mRefreshView!=null){
           mRefreshView.initData();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_shop_cart;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShopAPI.cancle(ShopAPI.SHOP_CART_LIST);
    }
    @Override
    public void onClick(View v) {
        if(!ClickUtil.canClick()){
            return;
        }
        int id=v.getId();
        if(id==R.id.btn_mannger){
            judgeState();
        }else if(id==R.id.btn_collect){
            collect();
        }else if(id==R.id.btn_delete){
           deleteGoods();
        }else if(id==R.id.check_total_image){
            judgeAllSelect();
        }else if(id==R.id.btn_commit){
            commit();
        }
    }

    private void commit() {
        if(mShopCartModel==null){
            return;
        }

        String[] selectId=mShopCartModel.getAllSelectCartId();
        if(selectId==null||selectId.length<=0){
            ToastUtil.show(getString(R.string.select_goods_tip));
            return;
        }
        String idArray=StringUtil.splitJoint(selectId);
        CommitOrderActivity.forward(this,idArray);

    }

    private void judgeAllSelect() {
        if(mCheckTotalImage==null){
            return;
        }
        final boolean isTargetCheck=!mCheckTotalImage.isChecked();
        mShopCartModel.setAllSelected(isTargetCheck);
        mCheckTotalImage.setChecked(isTargetCheck);
    }

    /*删除商品*/
    private void deleteGoods() {
      final String[]allSelectId=mShopCartModel.getAllSelectCartId();
        if(allSelectId==null||allSelectId.length<=0) {
            ToastUtil.show(R.string.select_goods_tip);
            return;
        }
        DialogUitl.showSimpleDialog(this, "是否要删除商品?", new DialogUitl.SimpleCallback() {
            @Override
            public void onConfirmClick(Dialog dialog, String content) {
                if(mShopCartModel!=null){
                    mShopCartModel.deleteGoodsArray(allSelectId,ShopCartActivity.this);
                }
            }
        });
    }

   /*批量收藏商品*/
    private void collect() {
      String[] allSelectId=getAllSelectGoodsId();
       if(allSelectId==null||allSelectId.length<=0) {
           ToastUtil.show(getString(R.string.select_goods_tip));
           return;
       }
       ShopAPI.batchCollect(allSelectId, ShopState.PRODUCT_DEFAULT).
               compose(this.<Boolean>bindUntilOnDestoryEvent())
               .subscribe(new DefaultObserver<Boolean>() {
                   @Override
                   public void onNext(Boolean aBoolean) {
                       if(aBoolean){
                         ToastUtil.show(R.string.collect_succ);
                       }
                   }
               });
    }

    private String[] getAllSelectGoodsId() {
        if(mShopCartModel==null){
            return null;
        }
        String[] allSelectId=mShopCartModel.getAllSelectGoodsId();
        return allSelectId;
    }

    @Override
    public void deleteInvaild() {
     if(mShopcartParseBean==null||mShopCartModel==null){
         return;
     }
     List<ShopCartBean>list=mShopcartParseBean.getInvalid();
     if(!ListUtil.haveData(list)){
         return;
     }
     int size=list.size();
     String[]allId=new String[size];
     for(int i=0;i<size;i++){
         ShopCartBean shopCartBean= list.get(i);
         allId[i]=shopCartBean.getId();
     }
        mShopCartModel.deleteGoodsArray(allId,this);
    }
}
