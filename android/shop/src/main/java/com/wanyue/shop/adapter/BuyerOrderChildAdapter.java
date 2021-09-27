package com.wanyue.shop.adapter;

import android.view.View;
import android.widget.TextView;

import com.wanyue.common.adapter.base.BaseReclyViewHolder;
import com.wanyue.common.bean.GoodsBean;
import com.wanyue.common.bean.SpecsValueBean;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.common.utils.ViewUtil;
import com.wanyue.shop.R;
import com.wanyue.shop.bean.ShopCartBean;
import com.wanyue.shop.view.widet.ViewGroupLayoutBaseAdapter;
import java.util.List;

public class BuyerOrderChildAdapter extends ViewGroupLayoutBaseAdapter<ShopCartBean> {
    private boolean openEvaluate;
    public BuyerOrderChildAdapter(List<ShopCartBean> list) {
        super(list);
    }

    @Override
    public void convert(BaseReclyViewHolder helper, ShopCartBean item) {
        int position=helper.getObjectPosition();
        ShopCartBean shopCartBean=  item;
        GoodsBean goodsBean=shopCartBean.getProductInfo();
        helper.setText(R.id.tv_goods_num,"x"+item.getCartNum());
        if(goodsBean!=null){
            helper.setText(R.id.tv_title,goodsBean.getName());
            helper.setText(R.id.tv_price, StringUtil.getFormatPrice(item.getProductPrice()));
            helper.setImageUrl(goodsBean.getThumb(),R.id.img_thumb);
            SpecsValueBean specsValueBean=goodsBean.getAttrInfo();
            if(specsValueBean!=null){
                helper.setText(R.id.tv_field,specsValueBean.getSuk());
            }
            TextView tvEvaluate=helper.getView(R.id.btn_evaluate);
            if(openEvaluate&&!item.isReply()){
               ViewUtil.setVisibility(tvEvaluate, View.VISIBLE);
               tvEvaluate.setTag(position);
               tvEvaluate.setOnClickListener(mOnChildClickListener);
            }
        }
    }
    public void setOpenEvaluate(boolean openEvaluate) {
        this.openEvaluate = openEvaluate;
       // notifyDataChanged();
    }

    @Override
    protected int getLayoutId(ShopCartBean shopCartBean) {
        return R.layout.item_relcy_commit_order_child;
    }
}
