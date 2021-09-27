package com.wanyue.shop.adapter;

import androidx.annotation.NonNull;

import com.wanyue.common.adapter.base.BaseReclyViewHolder;
import com.wanyue.common.adapter.base.BaseRecyclerAdapter;
import com.wanyue.common.bean.GoodsBean;
import com.wanyue.common.utils.WordUtil;
import com.wanyue.shop.R;
import java.util.List;

public class GreateGoodsSearchAdapter extends BaseRecyclerAdapter<GoodsBean, BaseReclyViewHolder> {
    public GreateGoodsSearchAdapter(List<GoodsBean> data) {
        super(data);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_recly_greate_goods_search;
    }


    @Override
    protected void convert(@NonNull BaseReclyViewHolder helper, GoodsBean item) {
        helper.setText(R.id.tv_title,item.getName());
        helper.setText(R.id.tv_price,item.getUnitPrice());
        helper.setText(R.id.tv_sale_num, WordUtil.getString(R.string.sale_num_2,item.getSales()));
        helper.setImageUrl(item.getThumb(),R.id.img_goods_cover);
    }
}
