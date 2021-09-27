package com.wanyue.live.adapter;

import android.view.View;
import android.widget.ImageView;

import com.wanyue.common.adapter.base.BaseReclyViewHolder;
import com.wanyue.common.adapter.base.BaseRecyclerAdapter;
import com.wanyue.common.mob.MobBean;
import com.wanyue.common.utils.ResourceUtil;
import com.wanyue.common.utils.ViewUtil;
import com.wanyue.live.R;
import java.util.List;

public class LiveShareSmallAdapter extends BaseRecyclerAdapter<MobBean, BaseReclyViewHolder> {
    public LiveShareSmallAdapter(List<MobBean> data) {
        super(data);
    }
    @Override
    public int getLayoutId() {
        return R.layout.item_recly_live_share_small;
    }
    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */

    @Override
    protected void convert(BaseReclyViewHolder helper, MobBean item) {

        helper.setImageDrawable(R.id.img_avator,item.getIcon1());
        ImageView imageView=helper.getView(R.id.img_avator);
        imageView.setImageDrawable(ResourceUtil.getDrawable(item.getIcon1(),true));
    }
}
