package com.wanyue.shop.adapter;

import androidx.annotation.NonNull;
import com.wanyue.common.adapter.base.BaseReclyViewHolder;
import com.wanyue.common.adapter.base.BaseRecyclerAdapter;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.shop.R;
import com.wanyue.shop.bean.StoreGoodsBean;
import java.util.List;

public class CollectGoodsAdapter extends BaseRecyclerAdapter<StoreGoodsBean, BaseReclyViewHolder> {

    public CollectGoodsAdapter(List<StoreGoodsBean> data) {
        super(data);
    }
    @Override
    public int getLayoutId() {
        return R.layout.item_relcy_collect_goods;
    }
    @Override
    protected void convert(@NonNull BaseReclyViewHolder helper, StoreGoodsBean item) {
        if(item!=null){
            helper.setText(R.id.tv_title,item.getName());
            helper.setText(R.id.tv_price, StringUtil.getPrice(item.getPrice()));
            helper.setImageUrl(item.getImage(),R.id.img_thumb);
        }
        helper.setOnChildClickListner(R.id.btn_delete,mOnClickListener);
    }
}
