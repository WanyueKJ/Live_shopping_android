package com.wanyue.main.store.adapter;

import androidx.annotation.NonNull;

import com.wanyue.common.adapter.base.BaseReclyViewHolder;
import com.wanyue.common.adapter.base.BaseRecyclerAdapter;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.common.utils.WordUtil;
import com.wanyue.main.R;
import com.wanyue.shop.adapter.BuyerOrderChildAdapter;
import com.wanyue.shop.bean.OrderBean;
import com.wanyue.shop.bean.OrderStatus;
import com.wanyue.shop.view.widet.linear.ListPool;
import com.wanyue.shop.view.widet.linear.PoolLinearListView;

import java.util.List;

public class StoreOrderAdapter extends BaseRecyclerAdapter<OrderBean, BaseReclyViewHolder> {
    private boolean isStore=true;
    private ListPool mListPool;

    public StoreOrderAdapter(List<OrderBean> data) {
        super(data);

    }
    @Override
    public int getLayoutId() {
        if(isStore){
           return R.layout.item_recly_store_order;
        }else{
            return R.layout.item_recly_consignment_order;
        }
    }

    public void setIsStore(boolean isStore) {
        this.isStore = isStore;
        setLayoutId();
    }

    @Override
    protected void convert(@NonNull BaseReclyViewHolder helper, OrderBean item) {
              if(isStore){
                convertStore(helper,item);
              }else{
                convertConsignment(helper,item);
              }
    }

    private void convertStore(BaseReclyViewHolder helper, OrderBean item) {
        helper.setText(com.wanyue.shop.R.id.tv_time,item.getAddTime());
        helper.setText(com.wanyue.shop.R.id.tv_price, StringUtil.getPrice(item.getTotalPrice()));
        helper.setText(com.wanyue.shop.R.id.tv_total_num, WordUtil.getString(com.wanyue.shop.R.string.total_num_jian2,item.getTotalNum()));
        PoolLinearListView linearListView=helper.getView(com.wanyue.shop.R.id.listView);
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
            helper.setText(com.wanyue.shop.R.id.tv_status,orderStatus.getTitle());
        }
    }

    private void convertConsignment(BaseReclyViewHolder helper, OrderBean item) {
        convertStore(helper,item);
        helper.setText(R.id.tv_bring_price,item.getBringPriceTip());

    }
}
