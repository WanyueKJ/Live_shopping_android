package com.wanyue.shop.view.view;

import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.to.aboomy.pager2banner.Banner;
import com.to.aboomy.pager2banner.IndicatorView;
import com.wanyue.common.adapter.base.BaseReclyViewHolder;
import com.wanyue.common.adapter.base.BaseRecyclerAdapter;
import com.wanyue.common.bean.GoodsBean;
import com.wanyue.common.custom.ItemDecoration;
import com.wanyue.common.proxy.RxViewProxy;
import com.wanyue.common.utils.ListUtil;
import com.wanyue.common.utils.ResourceUtil;
import com.wanyue.shop.R;
import com.wanyue.shop.view.activty.GoodsDetailActivity;
import java.util.ArrayList;
import java.util.List;

/*优品推荐*/
public class GoodsDetailRecommendViewProxy extends BaseGoodItemViewProxy implements BaseQuickAdapter.OnItemClickListener {
    private Banner mBannner;
    private int mSpanCount=6;
    private List<List<GoodsBean>>mData;
    private BannerAdapter mBannerAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.view_goods_detail_recommend;
    }

    @Override
    protected void initView(ViewGroup contentView) {
        super.initView(contentView);
        mBannner = findViewById(R.id.bannner);
        mBannerAdapter=new BannerAdapter(null);
        mBannner.setAutoPlay(false).setIndicator(defaultIndicator())
                .setAdapter(mBannerAdapter);

    }

    private IndicatorView defaultIndicator() {
        IndicatorView indicator = new IndicatorView(getActivity())
                .setIndicatorColor(ResourceUtil.getColor(getActivity(),R.color.gray1));
        indicator.setIndicatorRatio(1F)
                .setIndicatorSelectedRatio(1F)
                .setIndicatorSelectorColor(ResourceUtil.getColor(getActivity(),R.color.global))
                .setIndicatorStyle(IndicatorView.IndicatorStyle.INDICATOR_CIRCLE);
        return indicator;
    }

    public void setData(List<GoodsBean>data){
        if(!ListUtil.haveData(data)||mBannerAdapter==null){
            return;
        }
        if(mData!=null){
           mData.clear();
        }else{
            mData=new ArrayList<>();
        }
        int size=data.size();
        int totalCount=size/mSpanCount;
        int remainder=size%mSpanCount;
        if(remainder>0){
           totalCount=totalCount+1;
        }
        for(int i=0;i<totalCount;i++){
            int tempStart=i*mSpanCount;
            if(tempStart<0){
               tempStart=0;
            }
            int tempEnd=(i+1)*mSpanCount;
            if(tempEnd>=size){
               tempEnd=size;
            }
            List<GoodsBean>subList=data.subList(tempStart,tempEnd);
            mData.add(subList);
        }
        mBannerAdapter.setData(mData);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
          GoodsAdapter goodsAdapter= (GoodsAdapter) adapter;
          GoodsBean goodsBean=goodsAdapter.getItem(position);
          GoodsDetailActivity.forward(getActivity(),goodsBean.getId());
    }

    public class BannerAdapter extends BaseRecyclerAdapter<List<GoodsBean>,BaseReclyViewHolder> {
        public BannerAdapter(List<List<GoodsBean>> data) {
            super(data);
        }
        @Override
        protected void convert(@NonNull BaseReclyViewHolder helper, List<GoodsBean> item) {
            RecyclerView recyclerView= helper.getView(R.id.reclyView);
            GoodsAdapter adapter= (GoodsAdapter) recyclerView.getAdapter();
            if(adapter==null){
               adapter= initReclyView(recyclerView);
            }
            adapter.setData(item);
        }
        @Override
        public int getLayoutId() {
            return R.layout.item_relcy;
        }
    }

    private GoodsAdapter initReclyView(RecyclerView recyclerView) {
        GoodsAdapter goodsAdapter=new GoodsAdapter(null);
        recyclerView.setAdapter(goodsAdapter);
        GridLayoutManager gridLayoutManager= new GridLayoutManager(getActivity(),3){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        ItemDecoration decoration = new ItemDecoration(getActivity(), 0xffdd00, 10, 10);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(decoration);
        goodsAdapter.setOnItemClickListener(GoodsDetailRecommendViewProxy.this);
        return goodsAdapter;
    }

    public class GoodsAdapter extends BaseRecyclerAdapter<GoodsBean,BaseReclyViewHolder>{
        public GoodsAdapter(List<GoodsBean> data) {
            super(data);
        }

        @Override
        public int getLayoutId() {
            return R.layout.item_recly_goods_recommend;
        }
        @Override
        protected void convert(@NonNull BaseReclyViewHolder helper, GoodsBean item) {
            helper.setImageUrl(item.getThumb(),R.id.img_cover);
            helper.setText(R.id.tv_title,item.getName());
            helper.setText(R.id.tv_price,item.getUnitPrice());
        }
    }

}
