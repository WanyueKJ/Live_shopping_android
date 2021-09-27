package com.wanyue.main.adapter;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import com.wanyue.common.adapter.base.BaseReclyViewHolder;
import com.wanyue.common.adapter.base.BaseRecyclerAdapter;
import com.wanyue.common.bean.GoodsBean;
import com.wanyue.common.utils.ResourceUtil;
import com.wanyue.common.utils.WordUtil;
import com.wanyue.main.R;
import java.util.List;

public class GreateSelectAdapter extends BaseRecyclerAdapter<GoodsBean, BaseReclyViewHolder> {
    public GreateSelectAdapter(List<GoodsBean> data) {
        super(data);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_recly_goods_select;
    }

    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */


    @Override
    protected void convert(BaseReclyViewHolder helper, GoodsBean item) {

        helper.setImageUrl(item.getThumb(),R.id.img_goods_cover);
        helper.setText(R.id.tv_title,item.getName());
        helper.setText(R.id.tv_price,item.getUnitPrice());
        helper.setText(R.id.tv_buy_num, WordUtil.getString(R.string.sale_num,item.getSales()));

        ImageView imageView=helper.getView(R.id.img_tag);
        if(imageView!=null&&imageView.getDrawable()==null){
           Drawable drawable=ResourceUtil.getDrawable(R.drawable.icon_popular_today,true);
           imageView.setImageDrawable(drawable);
        }

    }
}
