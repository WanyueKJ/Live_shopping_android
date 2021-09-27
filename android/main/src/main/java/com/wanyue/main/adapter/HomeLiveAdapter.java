package com.wanyue.main.adapter;

import com.wanyue.common.adapter.base.BaseReclyViewHolder;
import com.wanyue.common.adapter.base.BaseRecyclerAdapter;
import com.wanyue.common.utils.WordUtil;
import com.wanyue.live.bean.LiveBean;
import com.wanyue.main.R;
import java.util.List;

public class HomeLiveAdapter extends BaseRecyclerAdapter<LiveBean, BaseReclyViewHolder> {

    public HomeLiveAdapter(List<LiveBean> data) {
        super(data);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_recly_home_live;
    }

    @Override
    protected void convert(BaseReclyViewHolder helper, LiveBean item) {
        helper.setImageUrl(item.getThumb(),R.id.img_conver)
        .setText(R.id.tv_title,item.getTitle());
        helper.setImageUrl(item.getAvatar(),R.id.img_user_avatar)
        .setText(R.id.tv_watch_count, WordUtil.getString(R.string.good_watch,item.getNums()))
        .setText(R.id.tv_user_name,item.getUserNiceName())
        .setText(R.id.tv_zan,item.getLikes())
        .setText(R.id.tv_goods_num, WordUtil.getString(R.string.goods_nums,item.getGoodNum()))
        ;
        helper.setImageUrl(item.getGoodsCover(),R.id.img_good_cover);
    }
}
