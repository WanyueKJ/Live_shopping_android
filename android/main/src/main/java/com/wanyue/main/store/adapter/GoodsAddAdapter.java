package com.wanyue.main.store.adapter;


import androidx.annotation.NonNull;

import com.wanyue.common.adapter.base.BaseReclyViewHolder;
import com.wanyue.common.adapter.base.BaseRecyclerAdapter;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.main.R;
import com.wanyue.main.store.bean.ConsignMentGoodsBean;

import java.util.List;

public class GoodsAddAdapter extends BaseRecyclerAdapter<ConsignMentGoodsBean, BaseReclyViewHolder> {
    public GoodsAddAdapter(List<ConsignMentGoodsBean> data) {
        super(data);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_recly_consignment_goods_add;
    }

    @Override
    protected void convert(@NonNull BaseReclyViewHolder helper, ConsignMentGoodsBean item) {
        helper.setText(R.id.tv_title,item.getStore_name());
        helper.setText(R.id.tv_price, StringUtil.getPrice(item.getPriceTip()));
        helper.setImageUrl(item.getImage(), R.id.img_thumb);
        helper.setText(R.id.tv_bring, StringUtil.getPrice(item.getBring_price()));
        helper.setOnChildClickListner(R.id.btn_add,mOnClickListener);

    }
}
