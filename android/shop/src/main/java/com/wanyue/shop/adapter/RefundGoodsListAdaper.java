package com.wanyue.shop.adapter;

import com.wanyue.common.adapter.base.BaseReclyViewHolder;
import com.wanyue.common.bean.GoodsBean;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.shop.R;
import com.wanyue.shop.bean.ShopCartBean;
import com.wanyue.shop.view.widet.ViewGroupLayoutBaseAdapter;
import java.util.List;

public class RefundGoodsListAdaper extends ViewGroupLayoutBaseAdapter<ShopCartBean> {
    public RefundGoodsListAdaper(List<ShopCartBean> list) {
        super(list);
    }
    @Override
    public void convert(BaseReclyViewHolder helper, ShopCartBean item) {
        GoodsBean goodsBean = item.getProductInfo();
        helper.setText(R.id.tv_goods_num,"x" + item.getCartNum());

        if (goodsBean != null) {
            helper.setImageUrl(goodsBean.getThumb(),R.id.img_thumb);
            helper.setText(R.id.tv_title,goodsBean.getName());
            helper.setText(R.id.tv_price, StringUtil.getFormatPrice(item.getProductPrice()));
        }
    }
    @Override
    protected int getLayoutId(ShopCartBean o) {
        return R.layout.item_recly_order_goods_about;
    }
}
