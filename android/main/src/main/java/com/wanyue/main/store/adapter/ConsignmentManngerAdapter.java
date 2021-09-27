package com.wanyue.main.store.adapter;

import androidx.annotation.NonNull;

import com.wanyue.common.adapter.base.BaseReclyViewHolder;
import com.wanyue.common.adapter.base.BaseRecyclerAdapter;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.common.utils.WordUtil;
import com.wanyue.main.R;
import com.wanyue.main.store.bean.ConsignMentGoodsBean;

import java.util.List;

public class ConsignmentManngerAdapter extends BaseRecyclerAdapter<ConsignMentGoodsBean, BaseReclyViewHolder> {
    private boolean mSale;
    public ConsignmentManngerAdapter(List<ConsignMentGoodsBean> data) {
        super(data);
    }

    @Override
    public int getLayoutId() {
        if(mSale){
            return R.layout.item_recly_consignment_mannger_goods_saled;
        }else{
            return R.layout.item_recly_consignment_mannger_goods_unsaled;
        }
    }
    public void setSale(boolean sale) {
        mSale = sale;
        setLayoutId();
    }
    @Override
    protected void convert(@NonNull BaseReclyViewHolder helper, ConsignMentGoodsBean item) {

        helper.setText(R.id.tv_title,item.getStore_name());
        helper.setText(R.id.tv_price, item.getPriceTip());
        helper.setImageUrl(item.getImage(), R.id.img_thumb);
        helper.setText(R.id.tv_bring, StringUtil.getPrice(item.getBring_price()));
        helper.setOnChildClickListner(R.id.btn_add,mOnClickListener);
        helper.setText(R.id.tv_sale, WordUtil.getString(R.string.saled_num,item.getSalenums()));

        if(mSale){
            helper.setOnChildClickListner(R.id.btn_down,mOnClickListener);
        }else{
            helper.setOnChildClickListner(R.id.btn_delete,mOnClickListener);
            helper.setOnChildClickListner(R.id.btn_up,mOnClickListener);
        }
    }
}
