package com.wanyue.shop.view.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wanyue.common.bean.GoodsBean;
import com.wanyue.common.custom.ItemDecoration;
import com.wanyue.common.glide.ImgLoader;
import com.wanyue.common.proxy.RxViewProxy;
import com.wanyue.common.server.observer.DefaultObserver;
import com.wanyue.shop.R;
import com.wanyue.shop.adapter.HotGoodsAdapter;
import com.wanyue.shop.api.ShopAPI;
import com.wanyue.shop.view.activty.GoodsDetailActivity;

import java.util.List;

public class HotGoodsEmptyViewProxy extends RxViewProxy implements BaseQuickAdapter.OnItemClickListener {
    private RecyclerView mReclyView;
    private HotGoodsAdapter mHotGoodsAdapter;
    private int mEmptyIconId;

    private boolean isHideConver;

    @Override
    public int getLayoutId() {
        return R.layout.view_empty_hot_goods;
    }
    @Override
    protected void initView(ViewGroup contentView) {
        super.initView(contentView);
        mReclyView =findViewById(R.id.reclyView);
        mHotGoodsAdapter=new HotGoodsAdapter(null);
        mReclyView.setAdapter(mHotGoodsAdapter);
        GridLayoutManager gridLayoutManager= new GridLayoutManager(getActivity(),2);
        GridLayoutManager.SpanSizeLookup spanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                //5个时为前两个为2列、后三个为3列
                if (position ==0) {
                    return 2;
                } else {
                    return 1;
                }
            }
        };
        gridLayoutManager.setSpanSizeLookup(spanSizeLookup);
        ItemDecoration decoration = new ItemDecoration(getActivity(), 0xffdd00, 10, 10);
        mReclyView.setLayoutManager(gridLayoutManager);
        mReclyView.addItemDecoration(decoration);

        LayoutInflater inflater=getViewProxyMannger().getLayoutInflater();
        if(inflater!=null){
            View headView=inflater.inflate(R.layout.item_head_hot_goods_empty,mContentView,false);
            ImageView imageView= headView.findViewById(R.id.img_cover);
            if(isHideConver){
                imageView.setVisibility(View.GONE);
            }else{
                ImgLoader.display(getActivity(),mEmptyIconId,imageView);
            }
            mHotGoodsAdapter.setHeaderView(headView);

        }
        mHotGoodsAdapter.setOnItemClickListener(this);



        initData();
    }

    public void setHideConver(boolean hideConver) {
        isHideConver = hideConver;
    }

    private void initData() {
        ShopAPI.getProductHotList(1,8).compose(this.<List<GoodsBean>>bindToLifecycle()).subscribe(new DefaultObserver<List<GoodsBean>>() {
            @Override
            public void onNext(List<GoodsBean> goodsBeans) {
                mHotGoodsAdapter.setData(goodsBeans);
            }
        });
    }

    public void setEmptyIconId(int emptyIconId) {
        mEmptyIconId = emptyIconId;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            if(mHotGoodsAdapter==null){
                return;
            }
        String goodId= mHotGoodsAdapter.getItem(position).getId();
        GoodsDetailActivity.forward(getActivity(),goodId);
    }
}
