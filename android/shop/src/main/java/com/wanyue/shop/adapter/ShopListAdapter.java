package com.wanyue.shop.adapter;

import com.wanyue.common.adapter.base.BaseMutiRecyclerAdapter;
import com.wanyue.common.adapter.base.BaseReclyViewHolder;
import com.wanyue.common.utils.ListUtil;
import com.wanyue.common.utils.WordUtil;
import com.wanyue.shop.R;
import com.wanyue.shop.bean.GoodsTypeBean;
import java.util.List;

public class ShopListAdapter extends BaseMutiRecyclerAdapter<GoodsTypeBean, BaseReclyViewHolder> {
    private boolean misLinear;

    public ShopListAdapter(List<GoodsTypeBean> data) {
        super(data);
        addItemType(GoodsTypeBean.TYPE_SINGLE, R.layout.itemt_recly_goods_list_single);
        addItemType(GoodsTypeBean.TYPE_DOULE, R.layout.itemt_recly_goods_list_double);
    }

    @Override
    protected void convert(BaseReclyViewHolder helper, GoodsTypeBean item) {
            switch (helper.getItemViewType()){
                case GoodsTypeBean.TYPE_DOULE:
                    convertDouble(helper,item);
                    break;
                case GoodsTypeBean.TYPE_SINGLE:
                    convertSingle(helper,item);
                    break;
                default:
            }
    }

    public void setIsLinear(boolean misLinear) {
        if(this.misLinear==misLinear){
            return;
        }
        this.misLinear = misLinear;
        changeItemtype(mData);
    }

    @Override
    public void setData(List<GoodsTypeBean> data) {
        changeItemtype(data);
        super.setData(data);
    }

    @Override
    public void appendData(List<GoodsTypeBean> data) {
        changeItemtype(data);
        super.appendData(data);
    }


    private void changeItemtype(List<GoodsTypeBean> data) {
        if(ListUtil.haveData(data)){
            int type=misLinear?GoodsTypeBean.TYPE_SINGLE:GoodsTypeBean.TYPE_DOULE;
            for(GoodsTypeBean goodsTypeBean:data){
                goodsTypeBean.setItemType(type);
            }
        }
    }


    private void convertSingle(BaseReclyViewHolder helper, GoodsTypeBean item) {
        commonConver(helper,item);
    }

    private void commonConver(BaseReclyViewHolder helper, GoodsTypeBean item) {
        helper.setText(R.id.tv_title,item.getName());
        helper.setText(R.id.tv_price,item.getUnitPrice());
        helper.setText(R.id.tv_sale_num, WordUtil.getString(R.string.sale_num_2,item.getSales()));
        helper.setImageUrl(item.getThumb(),R.id.img_goods_cover);
    }

    private void convertDouble(BaseReclyViewHolder helper, GoodsTypeBean item) {
        commonConver(helper,item);
     }
}
