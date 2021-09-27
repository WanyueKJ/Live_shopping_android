package com.wanyue.live.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wanyue.common.Constants;
import com.wanyue.common.activity.BaseActivity;
import com.wanyue.common.bean.GoodsBean;
import com.wanyue.common.bean.UserBean;
import com.wanyue.common.custom.refresh.RxRefreshView;
import com.wanyue.common.glide.ImgLoader;
import com.wanyue.common.http.ParseSingleHttpCallback;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.live.R;
import com.wanyue.live.adapter.LiveStoreAdapter;
import com.wanyue.live.http.LiveHttpConsts;
import com.wanyue.live.http.LiveHttpUtil;
import com.wanyue.live.http.LiveShopAPI;
import com.wanyue.shop.api.ShopAPI;
import com.wanyue.shop.view.activty.GoodsDetailActivity;
import com.wanyue.shop.view.view.GoodsHandleNoCartViewProxy;
import java.util.List;
import io.reactivex.Observable;

public class LiveStoreActivity extends BaseActivity implements View.OnClickListener, BaseQuickAdapter.OnItemClickListener {
    private ImageView mAvatar;
    private TextView mTvName;
    private ImageView mBtnCustomService;
    private RxRefreshView<GoodsBean> mRefreshView;
    private UserBean mUserBean;
    private LiveStoreAdapter mLiveStoreAdapter;
    private TextView mTvGoodsCount;

    @Override
    public void init() {
        mAvatar =findViewById(R.id.avatar);
        mTvName =  findViewById(R.id.tv_name);
        mBtnCustomService =  findViewById(R.id.btn_custom_service);
        mRefreshView =  findViewById(R.id.refreshView);
        mUserBean=getIntent().getParcelableExtra(Constants.USER_BEAN);
        mBtnCustomService.setOnClickListener(this);
        mTvGoodsCount = (TextView) findViewById(R.id.tv_goods_count);

        mLiveStoreAdapter=new LiveStoreAdapter(null);
        mRefreshView.setAdapter(mLiveStoreAdapter);
        mRefreshView.setReclyViewSetting(RxRefreshView.ReclyViewSetting.createGridSetting(this,2,10));
        mRefreshView.setDataListner(new RxRefreshView.DataListner<GoodsBean>() {
            @Override
            public Observable<List<GoodsBean>> loadData(int p) {
                return getData(p);
            }
            @Override
            public void compelete(List<GoodsBean> data) {
            }
            @Override
            public void error(Throwable e) {
                e.printStackTrace();
            }
        });
        mLiveStoreAdapter.setOnItemClickListener(this);
        setData();
    }

    private Observable<List<GoodsBean>> getData(int p) {
        String uid=mUserBean!=null?mUserBean.getId():null;
        return ShopAPI.getSaleList(uid,p);
    }

    @Override
    protected void onFirstResume() {
        super.onFirstResume();
        if(mRefreshView!=null){
           mRefreshView.initData();
        }
    }


    private void setData() {
        if(mUserBean!=null){
           ImgLoader.display(this,mUserBean.getAvatar(),mAvatar);
           mTvName.setText(mUserBean.getUserNiceName()+"的小店");
            LiveShopAPI.getShopSaleNum(mUserBean.getId(), new ParseSingleHttpCallback<String>("nums") {
                @Override
                public void onSuccess(String data) {
                    mTvGoodsCount.setText(data+"件");
                }
            });
        }

    }

    public static void forward(Context context, UserBean userBean){
        Intent intent=new Intent(context,LiveStoreActivity.class);
        intent.putExtra(Constants.USER_BEAN,userBean);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_live_store;
    }
    @Override
    public void onClick(View v) {
        ToastUtil.show(R.string.coming_soon);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LiveHttpUtil.cancel(LiveHttpConsts.SHOP_SALE_NUMS);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        LiveStoreAdapter liveStoreAdapter= (LiveStoreAdapter) adapter;
        GoodsBean goodsBean=liveStoreAdapter.getItem(position);
        if(goodsBean==null||mUserBean==null){
            return;
        }
        GoodsDetailActivity.forward(mContext,goodsBean.getId(),mUserBean.getId(), GoodsHandleNoCartViewProxy.class);
    }
}
