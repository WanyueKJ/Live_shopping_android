package com.wanyue.shop.adapter;
import android.util.ArrayMap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.wanyue.common.adapter.base.BaseReclyViewHolder;
import com.wanyue.common.adapter.base.BaseRecyclerAdapter;
import com.wanyue.common.custom.refresh.ControllLayoutManager;
import com.wanyue.common.utils.ListUtil;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.common.utils.WordUtil;
import com.wanyue.shop.R;
import com.wanyue.shop.bean.PriceGroup;
import com.wanyue.shop.bean.ShopCartBean;
import com.wanyue.shop.bean.ShopCartStoreBean;
import com.wanyue.shop.bean.StoreBean;
import com.wanyue.shop.view.widet.DataEditTextView;
import java.util.List;

public class CommitOrderAdapter extends BaseRecyclerAdapter<ShopCartStoreBean, BaseReclyViewHolder> {
    public CommitOrderAdapter(List<ShopCartStoreBean> data) {
        super(data);
    }
    @Override
    public int getLayoutId() {
        return R.layout.item_relcy_commit_order;
    }
    @Override
    protected void convert(@NonNull BaseReclyViewHolder helper, ShopCartStoreBean item) {
        helper.setText(R.id.tv_name,item.getName());

        PriceGroup priceGroup=item.getPriceGroup();
        if(priceGroup!=null){
           priceGroup.setStoreId(item.getId());
           String postagePrice=priceGroup.getFormatStorePostage();
           helper.setText(R.id.tv_freight,postagePrice);
           helper.setText(R.id.tv_price, StringUtil.getFormatPrice(priceGroup.getTotalPrice()));
           helper.setText(R.id.tv_total_num, WordUtil.getString(R.string.total_num_jian,item.getTotalCartNum()));
        }
        DataEditTextView<ShopCartStoreBean> textView=helper.getView(R.id.et_remark);
        if(textView!=null){
           textView.setData(item);
           textView.setText(item.getMarks());
           textView.setOnTextChangeListner(mOnTextChangeListner);
        }
        RecyclerView recyclerView=helper.getView(R.id.reclyView);
        List<ShopCartBean> dataList=item.getList();
        if(recyclerView.getAdapter()==null){
           CommitOrderChildAdapter adapter=new CommitOrderChildAdapter(dataList);
           ControllLayoutManager linearLayoutManager=new ControllLayoutManager(context, LinearLayoutManager.VERTICAL, false);
           linearLayoutManager.setScrollEnabled(false);
           recyclerView.setLayoutManager(linearLayoutManager);
           recyclerView.setAdapter(adapter);
        }else{
           CommitOrderChildAdapter adapter= (CommitOrderChildAdapter) recyclerView.getAdapter();
           adapter.setData(dataList);
        }
        helper.addOnClickListener(R.id.btn_coupon);
    }


    private DataEditTextView.OnTextChangeListner<ShopCartStoreBean> mOnTextChangeListner=new DataEditTextView.OnTextChangeListner<ShopCartStoreBean>() {
        @Override
        public void textChange(ShopCartStoreBean data, String text) {
            data.setMarks(text);
        }
    };
}
