package com.wanyue.live.adapter;

import androidx.annotation.Nullable;
import com.wanyue.common.adapter.base.BaseReclyViewHolder;
import com.wanyue.common.adapter.base.BaseRecyclerAdapter;
import com.wanyue.live.R;
import com.wanyue.live.bean.LiveUserActionBean;

import java.util.List;

/**
 * 直播间 禁言
 */
public class LiveUserActionAdapter extends BaseRecyclerAdapter<LiveUserActionBean, BaseReclyViewHolder> {
    public LiveUserActionAdapter(@Nullable List<LiveUserActionBean> data) {
        super(data);
    }
    @Override
    public int getLayoutId() {
        return R.layout.item_user_action;
    }
    @Override
    protected void convert(BaseReclyViewHolder helper, LiveUserActionBean item) {
        helper.setText(R.id.text,item.getName());
    }
}
