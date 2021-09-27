package com.wanyue.main.store.view.proxy;

import android.view.View;
import android.view.ViewGroup;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.wanyue.common.server.observer.DialogObserver;
import com.wanyue.common.utils.ClickUtil;
import com.wanyue.main.R;
import com.wanyue.main.api.MainAPI;
import com.wanyue.main.business.MainEvent;
import com.wanyue.shop.view.view.BaseGoodsDetailBottomViewProxy;

public class GoodsAddDetailBottomViewProxy extends BaseGoodsDetailBottomViewProxy implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.view_goods_deatail_add_goods;
    }

    @Override
    protected void initView(ViewGroup contentView) {
        super.initView(contentView);
        setOnClickListner(R.id.btn_commit,this);
    }

    @Override
    public void onClick(View v) {
        if(mStoreGoodsBean==null|| !ClickUtil.canClick()){
            return;
        }
        final String id=mStoreGoodsBean.getId();

    }
}
