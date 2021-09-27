package com.wanyue.main.store.adapter;

import androidx.annotation.NonNull;

import com.wanyue.common.adapter.base.BaseReclyViewHolder;
import com.wanyue.common.adapter.base.BaseRecyclerAdapter;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.main.R;
import com.wanyue.main.store.bean.SettlementRecordBean;

import java.util.List;

public class SettlementRecordAdapter extends BaseRecyclerAdapter<SettlementRecordBean, BaseReclyViewHolder> {
    public SettlementRecordAdapter(List<SettlementRecordBean> data) {
        super(data);
    }
    @Override
    public int getLayoutId() {
        return R.layout.item_recly_settlement_record;
    }

    @Override
    protected void convert(@NonNull BaseReclyViewHolder helper, SettlementRecordBean item) {
        helper.setText(R.id.tv_order,item.getOrder());
        helper.setText(R.id.tv_time,item.getSettle_time());
        helper.setText(R.id.tv_price, StringUtil.getPrice(item.getMoney()));
        helper.setText(R.id.tv_status, item.getStatus());
    }
}
