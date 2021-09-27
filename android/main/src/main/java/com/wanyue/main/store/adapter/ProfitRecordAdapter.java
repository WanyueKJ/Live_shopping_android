package com.wanyue.main.store.adapter;

import androidx.annotation.NonNull;

import com.wanyue.common.adapter.base.BaseReclyViewHolder;
import com.wanyue.common.adapter.base.BaseRecyclerAdapter;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.main.R;
import com.wanyue.main.store.bean.ProfitRecordBean;

import java.util.List;

public class ProfitRecordAdapter extends BaseRecyclerAdapter<ProfitRecordBean, BaseReclyViewHolder> {
    public ProfitRecordAdapter(List<ProfitRecordBean> data) {
        super(data);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_recly_profit_record;
    }


    @Override
    protected void convert(@NonNull BaseReclyViewHolder helper, ProfitRecordBean item) {
        helper.setText(R.id.tv_order,item.getTitle());
        helper.setText(R.id.tv_time,item.getAddtime());
        helper.setText(R.id.tv_price, StringUtil.getPrice(item.getMoney()));
        helper.setText(R.id.tv_status, item.getStatus_txt());
    }
}
