package com.wanyue.shop.view.activty;

import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wanyue.common.activity.BaseActivity;
import com.wanyue.common.bean.GoodsBean;
import com.wanyue.common.custom.refresh.RxRefreshView;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.shop.R;
import com.wanyue.shop.adapter.GreateGoodsSearchAdapter;
import com.wanyue.shop.api.ShopAPI;
import com.wanyue.shop.view.view.HotGoodsEmptyViewProxy;
import com.wanyue.shop.view.view.HotKeywordSearchGoodViewProxy;
import com.wanyue.shop.view.view.SearchViewProxy;
import java.util.List;
import io.reactivex.Observable;

public class GreateGoodsSearchActivity extends BaseActivity implements SearchViewProxy.SeacherListner, BaseQuickAdapter.OnItemClickListener {
    private RxRefreshView<GoodsBean>mRefreshView;
    private HotKeywordSearchGoodViewProxy mSearchGoodViewProxy;
    private GreateGoodsSearchAdapter mGreateGoodsSearchAdapter;
    private FrameLayout mVpSearchContainer;

    private String mKeyword;


    @Override
    public void init() {
        setTabTitle("搜索商品");
        mRefreshView = findViewById(R.id.refreshView);
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
            }
        });
        mGreateGoodsSearchAdapter=new GreateGoodsSearchAdapter(null);
        mGreateGoodsSearchAdapter.setOnItemClickListener(this);
        mRefreshView.setAdapter(mGreateGoodsSearchAdapter);
        mSearchGoodViewProxy=new HotKeywordSearchGoodViewProxy();
        mSearchGoodViewProxy.setSeacherListner(this);
        mVpSearchContainer = (FrameLayout) findViewById(R.id.vp_search_container);
        getViewProxyMannger().addViewProxy(mVpSearchContainer,mSearchGoodViewProxy,mSearchGoodViewProxy.getDefaultTag());
        mRefreshView.setReclyViewSetting(RxRefreshView.ReclyViewSetting.createLinearSetting(this,0));

        HotGoodsEmptyViewProxy hotGoodsEmptyViewProxy=new HotGoodsEmptyViewProxy();
        hotGoodsEmptyViewProxy.setEmptyIconId(R.drawable.icon_empty_no_search);
        mRefreshView.setEmptyViewProxy(getViewProxyMannger(),hotGoodsEmptyViewProxy);
        mRefreshView.setRefreshEnable(false);
    }
    private Observable<List<GoodsBean>> getData(int p) {
        return ShopAPI.getProductList(mKeyword,p).compose(this.<List<GoodsBean>>bindUntilOnDestoryEvent());
    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_greate_goods_search;
    }
    @Override
    public void search(String keyward) {
        mKeyword=keyward;
        if(TextUtils.isEmpty(mKeyword)){
           ToastUtil.show("请输入关键词");
           return;
        }

        if(mRefreshView!=null){
          mRefreshView.initData();
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        GreateGoodsSearchAdapter greateGoodsSearchAdapter= (GreateGoodsSearchAdapter) adapter;
        GoodsBean goodsBean=greateGoodsSearchAdapter.getItem(position);
        if(goodsBean!=null){
           GoodsDetailActivity.forward(this,goodsBean.getId());
        }

    }
}
