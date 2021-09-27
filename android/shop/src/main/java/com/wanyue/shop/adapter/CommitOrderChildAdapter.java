package com.wanyue.shop.adapter;

import android.view.View;

import androidx.annotation.NonNull;

import com.wanyue.common.adapter.base.BaseReclyViewHolder;
import com.wanyue.common.adapter.base.BaseRecyclerAdapter;
import com.wanyue.common.bean.GoodsBean;
import com.wanyue.common.bean.SpecsValueBean;
import com.wanyue.shop.R;
import com.wanyue.shop.bean.ShopCartBean;
import java.util.List;

public class CommitOrderChildAdapter extends BaseRecyclerAdapter<ShopCartBean, BaseReclyViewHolder> {

    public CommitOrderChildAdapter(List<ShopCartBean> data) {
        super(data);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_relcy_commit_order_child;
    }

    @Override
    protected void convert(@NonNull BaseReclyViewHolder helper, ShopCartBean item) {
        ShopCartBean shopCartBean=  item;
        GoodsBean goodsBean=shopCartBean.getProductInfo();
        helper.setText(R.id.tv_goods_num,"x"+item.getCartNum());
        if(goodsBean!=null){
            helper.setText(R.id.tv_title,goodsBean.getName());
            helper.setText(R.id.tv_price,goodsBean.getUnitPrice());
            helper.setImageUrl(goodsBean.getThumb(),R.id.img_thumb);
            SpecsValueBean specsValueBean=goodsBean.getAttrInfo();
            if(specsValueBean!=null){
              helper.setText(R.id.tv_field,specsValueBean.getSuk());
            }
        }
    }
}
