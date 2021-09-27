package com.wanyue.main.adapter;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import com.wanyue.common.adapter.base.BaseReclyViewHolder;
import com.wanyue.common.adapter.base.BaseRecyclerAdapter;
import com.wanyue.common.utils.ResourceUtil;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.main.R;
import com.wanyue.main.bean.BrokerageRankBean;
import java.util.List;

public class BrokerageRankAdapter extends BaseRecyclerAdapter<BrokerageRankBean, BaseReclyViewHolder> {
    private Drawable[]mDrawableRank;

    public BrokerageRankAdapter(List<BrokerageRankBean> data) {
        super(data);
        initDrawable();
    }

    private void initDrawable() {
        if(mDrawableRank==null){
           mDrawableRank=new Drawable[3];
           mDrawableRank[0]= ResourceUtil.getDrawable(R.drawable.medal01,false);
           mDrawableRank[1]= ResourceUtil.getDrawable(R.drawable.medal02,false);
           mDrawableRank[2]= ResourceUtil.getDrawable(R.drawable.medal03,false);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_recly_brokerage_rank;
    }
    @Override
    protected void convert(@NonNull BaseReclyViewHolder helper, BrokerageRankBean item) {
        int position=helper.getLayoutPosition()+1;
        helper.setImageUrl(item.getAvatar(),R.id.img_avator);
        helper.setText(R.id.tv_name,item.getNickname());
        helper.setText(R.id.tv_price, StringUtil.getPrice(item.getBrokeragePrice()));
        helper.setText(R.id.tv_rank_num,Integer.toString(position));

        ImageView imageView=helper.getView(R.id.img_rank_num);
        TextView tvRankNum=helper.getView(R.id.tv_rank_num);
        initDrawable();

        if(position<=mDrawableRank.length){
            imageView.setImageDrawable(mDrawableRank[position-1]);
            tvRankNum.setText(null);
        }else{
            imageView.setImageDrawable(null);
            tvRankNum.setText(Integer.toString(position));
        }

    }

}
