package com.wanyue.shop.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import com.wanyue.common.adapter.base.BaseMutiRecyclerAdapter;
import com.wanyue.common.adapter.base.BaseReclyViewHolder;
import com.wanyue.common.utils.ClickUtil;
import com.wanyue.common.utils.DebugUtil;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.common.utils.ViewUtil;
import com.wanyue.common.utils.WordUtil;
import com.wanyue.shop.R;
import com.wanyue.shop.api.ShopAPI;
import com.wanyue.shop.bean.OrderBean;
import com.wanyue.shop.bean.OrderStatus;
import com.wanyue.shop.business.ShopState;
import com.wanyue.shop.view.widet.linear.ListPool;
import com.wanyue.shop.view.widet.linear.PoolLinearListView;
import java.util.List;

public class BuyerOrderAdaper extends BaseMutiRecyclerAdapter<OrderBean, BaseReclyViewHolder> {
    private ListPool mListPool;
    public BuyerOrderAdaper(List<OrderBean> data) {
        super(data);
        addItemType(ShopState.ORDER_STATE_WAIT_PAY,R.layout.item_recly_buyer_order_1);
        addItemType(ShopState.ORDER_STATE_WAIT_DELIVERED,R.layout.item_recly_buyer_order_2);
        addItemType(ShopState.ORDER_STATE_WAIT_RECEIVE,R.layout.item_recly_buyer_order_2);
        addItemType(ShopState.ORDER_STATE_WAIT_EVALUATE,R.layout.item_recly_buyer_order_3);
        addItemType(ShopState.ORDER_STATE_COMPELETE,R.layout.item_recly_buyer_order_4);
        addItemType(-1,R.layout.empty);

    }

    @Override
    protected void convert(@NonNull BaseReclyViewHolder helper, OrderBean item) {
        switch (helper.getItemViewType()){
            case ShopState.ORDER_STATE_WAIT_PAY:
                commonConvert(helper,item);
                helper.setOnChildClickListner(R.id.btn_cancel,mOnClickListener);
                helper.setOnChildClickListner(R.id.btn_buy,mOnClickListener);
                break;
            case ShopState.ORDER_STATE_WAIT_DELIVERED:
            case ShopState.ORDER_STATE_WAIT_RECEIVE:
                commonConvert(helper,item);
                break;
            case ShopState.ORDER_STATE_WAIT_EVALUATE:
                commonConvert(helper,item);
                helper.setOnChildClickListner(R.id.btn_evaluate,mOnClickListener);
                break;
            case ShopState.ORDER_STATE_COMPELETE:
                commonConvert(helper,item);
                helper.setOnChildClickListner(R.id.btn_buy_again,mOnClickListener);
                helper.setOnChildClickListner(R.id.btn_delete,mOnClickListener);
                break;
            case -1:
                DebugUtil.sendException("出现空数据");
                break;
            default:
                break;
        }
    }

    private void commonConvert(BaseReclyViewHolder helper, OrderBean item) {
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
        OrderStatus orderStatus=item.getOrderStatus();
        if(orderStatus!=null){
           helper.setText(R.id.tv_status,orderStatus.getTitle());
        }
    }


}
