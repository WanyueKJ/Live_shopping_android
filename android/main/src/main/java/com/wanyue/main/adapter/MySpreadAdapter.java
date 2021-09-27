package com.wanyue.main.adapter;

import androidx.annotation.NonNull;
import com.wanyue.common.adapter.base.BaseReclyViewHolder;
import com.wanyue.common.adapter.base.BaseRecyclerAdapter;
import com.wanyue.main.R;
import com.wanyue.main.bean.MySpreadBean;
import java.util.List;

public class MySpreadAdapter  extends BaseRecyclerAdapter<MySpreadBean, BaseReclyViewHolder> {
    public MySpreadAdapter(List<MySpreadBean> data) {
        super(data);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_recly_my_spread;
    }

    @Override
    protected void convert(@NonNull BaseReclyViewHolder helper, MySpreadBean item) {
        helper.setText(R.id.tv_name,item.getTitle());
        helper.setImageDrawable(R.id.img_thumb,item.getIcon());
    }
}
