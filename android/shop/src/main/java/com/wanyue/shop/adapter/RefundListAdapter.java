package com.wanyue.shop.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.wanyue.common.adapter.base.BaseReclyViewHolder;
import com.wanyue.common.adapter.base.BaseRecyclerAdapter;
import com.wanyue.common.utils.ResourceUtil;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.common.utils.WordUtil;
import com.wanyue.shop.R;
import com.wanyue.shop.bean.OrderBean;
import com.wanyue.shop.bean.OrderStatus;
import com.wanyue.shop.business.ShopState;
import com.wanyue.shop.view.widet.linear.ListPool;
import com.wanyue.shop.view.widet.linear.PoolLinearListView;
import java.util.List;

public class RefundListAdapter extends BaseRecyclerAdapter<OrderBean, BaseReclyViewHolder> {
    private ListPool mListPool;
    public RefundListAdapter(List<OrderBean> data) {
        super(data);
    }
    @Override
    public int getLayoutId() {
        return R.layout.item_recly_order_refund;
    }

    @Override
    protected void convert(@NonNull BaseReclyViewHolder helper, OrderBean item) {
        helper.setText(R.id.tv_name,item.getShopName());
        helper.setText(R.id.tv_price, StringUtil.getPrice(item.getTotalPrice()));
        helper.setText(R.id.tv_total_num, WordUtil.getString(R.string.total_num_jian2,item.getTotalNum()));
        PoolLinearListView linearListView=helper.getView(R.id.listView);
        if(linearListView.getAdapter()==null){
            if(mListPool==null){
               mListPool=new ListPool();
            }
            linearListView.setOrientation(PoolLinearListView.VERTICAL);
            linearListView.setListPool(mListPool);
            BuyerOrderChildAdapter childAdapter=new BuyerOrderChildAdapter(item.getCartInfo());
            linearListView.setAdapter(childAdapter);
        }else{
            BuyerOrderChildAdapter childAdapter= (BuyerOrderChildAdapter) linearListView.getAdapter();
            childAdapter.setData(item.getCartInfo());
        }

        int refundStatus=item.getRefundStatus();
        ImageView refundStatusImg=helper.getView(R.id.img_refund_state);
        if(refundStatus== ShopState.REFUND_STATE_COMPELETE){
           refundStatusImg.setImageDrawable(ResourceUtil.getDrawable(R.drawable.icon_refund_state_compeleted,true));
        }else{
            refundStatusImg.setImageDrawable(ResourceUtil.getDrawable(R.drawable.icon_refund_state_ing,true));
        }
    }
}
