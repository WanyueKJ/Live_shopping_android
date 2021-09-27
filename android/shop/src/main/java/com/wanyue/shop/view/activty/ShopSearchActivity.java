package com.wanyue.shop.view.activty;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wanyue.common.Constants;
import com.wanyue.common.activity.BaseActivity;
import com.wanyue.common.custom.CheckImageView;
import com.wanyue.common.custom.ItemDecoration;
import com.wanyue.common.custom.refresh.RxRefreshView;
import com.wanyue.common.utils.DpUtil;
import com.wanyue.shop.R;
import com.wanyue.shop.adapter.ShopListAdapter;
import com.wanyue.shop.api.ShopAPI;
import com.wanyue.shop.bean.GoodsSearchArgs;
import com.wanyue.shop.bean.GoodsTypeBean;
import com.wanyue.shop.view.view.HotGoodsEmptyViewProxy;
import com.wanyue.shop.view.view.SearchViewProxy;
import com.wanyue.shop.view.view.SortGoodsViewProxy;
import java.util.List;
import io.reactivex.Observable;

public class ShopSearchActivity extends BaseActivity implements  CheckImageView.OnCheckClickListner{
    private ViewGroup mVpSearchContainer;
    private CheckImageView mBtnChangeMannger;
    private ViewGroup mVpSortContainer;
    private View mVTopBg;
    private RxRefreshView<GoodsTypeBean>mRefreshView;

    private LinearLayoutManager mLinearLayoutManager;
    private GridLayoutManager mGridLayoutManager;
    private ItemDecoration mItemDecoration;

    private ShopListAdapter mShopListAdapter;
    private GoodsSearchArgs mGoodsSearchArgs;

    @Override
    public void init() {
        mGoodsSearchArgs=getIntent().getParcelableExtra(Constants.DATA);
        if(mGoodsSearchArgs==null){
           finish();
           return;
        }
        setDefaultStatusBarPadding(R.id.vp_title);
        setTabTitle(R.string.goods_list);
        mVpSearchContainer =  findViewById(R.id.vp_search_container);
        mBtnChangeMannger =   findViewById(R.id.btn_change_mannger);
        mVpSortContainer =    findViewById(R.id.vp_sort_container);
        mVTopBg =  findViewById(R.id.v_top_bg);
        mRefreshView =  findViewById(R.id.refreshView);
        switchLayoutMannager(false);
        mBtnChangeMannger.setCheckClickListner(this);
        mShopListAdapter=new ShopListAdapter(null);
        mShopListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(mShopListAdapter==null){
                    return;
                }
                GoodsTypeBean goodsTypeBean=mShopListAdapter.getItem(position);
                GoodsDetailActivity.forward(mContext,goodsTypeBean.getId());
            }
        });

        mRefreshView.setAdapter(mShopListAdapter);
        HotGoodsEmptyViewProxy hotGoodsEmptyViewProxy=new HotGoodsEmptyViewProxy();
        hotGoodsEmptyViewProxy.setEmptyIconId(R.drawable.bg_empty_good_list);
        mRefreshView.setEmptyViewProxy(getViewProxyMannger(),hotGoodsEmptyViewProxy);
        mRefreshView.setDataListner(new RxRefreshView.DataListner<GoodsTypeBean>() {
            @Override
            public Observable<List<GoodsTypeBean>> loadData(int p) {
                return getData(p);
            }
            @Override
            public void compelete(List<GoodsTypeBean> data) {

            }
            @Override
            public void error(Throwable e) {
            }
        });
        initSearch();
        initSortView();

    }

    @Override
    protected void onFirstResume() {
        super.onFirstResume();
        mRefreshView.initData();
    }

    /*初始化排序*/
    private void initSortView() {
        SortGoodsViewProxy sortGoodsViewProxy=new SortGoodsViewProxy();
        sortGoodsViewProxy.setArgs(mGoodsSearchArgs);
        sortGoodsViewProxy.setParameterChangeListner(new SortGoodsViewProxy.ParameterChangeListner() {
            @Override
            public void argsChange(GoodsSearchArgs args) {
                if(mRefreshView!=null){
                   mRefreshView.initData();
                }
            }
        });
        getViewProxyMannger().addViewProxy(mVpSortContainer,sortGoodsViewProxy,sortGoodsViewProxy.getDefaultTag());
    }

    private Observable<List<GoodsTypeBean>> getData(int p) {
            return ShopAPI.getProductList(mGoodsSearchArgs,p);
    }

    /*切换布局方向*/
    private void switchLayoutMannager(boolean isLinear) {
        if(mShopListAdapter!=null){
           mShopListAdapter.setIsLinear(isLinear);
        }
        if(isLinear){
         if(mLinearLayoutManager==null){
            mLinearLayoutManager=new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
          }
            mRefreshView.setLayoutManager(mLinearLayoutManager);
            if(mItemDecoration!=null){
              mRefreshView.removeItemDecoration(mItemDecoration);
            }

            RecyclerView recyclerView=mRefreshView.getRecyclerView();
            if(recyclerView!=null){
                recyclerView.setPadding(0,0,0,0);
            }
        }else{
          RecyclerView recyclerView=mRefreshView.getRecyclerView();
          if(recyclerView!=null){
              int padding= DpUtil.dp2pxResource(this,R.dimen.default_padding);
              recyclerView.setPadding(padding,0,padding,0);
          }

         if(mGridLayoutManager==null){
            mGridLayoutManager=new GridLayoutManager(this,2);
          }
          if(mItemDecoration==null){
             mItemDecoration= new ItemDecoration(this,0x00000000, 10, 10);
          }
            mRefreshView.addItemDecoration(mItemDecoration);
            mRefreshView.setLayoutManager(mGridLayoutManager);
        }
    }


    /*初始化查询控件*/
    private void initSearch() {
        SearchViewProxy searchViewProxy=new SearchViewProxy();
        searchViewProxy.setHint(getString(R.string.search_goods));
        searchViewProxy.setEnableAutoSearch(false);
        searchViewProxy.setTitle(mGoodsSearchArgs.keyword);
        searchViewProxy.setSeacherListner(new SearchViewProxy.SeacherListner() {
            @Override
            public void search(String keyward) {
                if(mGoodsSearchArgs!=null&&mRefreshView!=null){
                   mGoodsSearchArgs.keyword=keyward;
                   mRefreshView.initData();
                }
            }
        });
        getViewProxyMannger().addViewProxy(mVpSearchContainer,searchViewProxy,searchViewProxy.getDefaultTag());
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_shop_search;
    }

    public static void forward(Context context, GoodsSearchArgs goodsSearchArgs){
        Intent intent=new Intent(context,ShopSearchActivity.class);
        intent.putExtra(Constants.DATA,goodsSearchArgs);
        context.startActivity(intent);
    }

    @Override
    public void onCheckClick(CheckImageView view, boolean isChecked) {
        switchLayoutMannager(isChecked);
    }
}
