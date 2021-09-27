package com.wanyue.shop.view.view;

import android.view.ViewGroup;
import androidx.lifecycle.Observer;
import com.wanyue.shop.bean.ShopCartBean;
import com.wanyue.shop.model.ShopCartModel;

/*购物车相关的数量修改器*/
public class ShopCartGoodsNumViewProxy extends GoodsNumViewProxy {
    private ShopCartBean mShopCartBean;
    public void setShopCartBean(ShopCartBean shopCartBean) {
        mShopCartBean = shopCartBean;
        if(mShopCartBean!=null&&mTvGoodsNum!=null){
           mTvGoodsNum.setText(Integer.toString(mShopCartBean.getCartNum()));
        }
    }

    public ShopCartBean getShopCartBean() {
        return mShopCartBean;
    }

    public boolean haveData(){
        return mShopCartBean!=null;
    }

    @Override
    protected void initView(ViewGroup contentView) {
        super.initView(contentView);
        ShopCartModel.obsverNumberBackEvent(getActivity(), new Observer<ShopCartBean>() {
            @Override
            public void onChanged(ShopCartBean shopCartBean) {
                if(mShopCartBean!=null&&mShopCartBean==shopCartBean){
                   mTvGoodsNum.setText(Integer.toString(shopCartBean.getCartNum()));
                }
            }
        });
    }
}
