package com.wanyue.shop.adapter;

import com.wanyue.common.adapter.base.BaseReclyViewHolder;
import com.wanyue.shop.R;
import com.wanyue.shop.bean.KeywordBean;
import com.wanyue.shop.view.widet.ViewGroupLayoutBaseAdapter;
import java.util.List;

public class KeywordAdapter extends ViewGroupLayoutBaseAdapter<String> {
    public KeywordAdapter(List<String> list) {
        super(list);
    }
    @Override
    public void convert(BaseReclyViewHolder helper, String item) {
        helper.setText(R.id.tv_title,item);
    }

    @Override
    protected int getLayoutId(String keywordBean) {
        return R.layout.item_relcy_keyword;
    }
}
