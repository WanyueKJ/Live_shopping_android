package com.wanyue.main.view.proxy;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.lifecycle.Observer;
import com.wanyue.common.proxy.Arg;
import com.wanyue.common.proxy.RxViewProxy;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.common.utils.ViewUtil;
import com.wanyue.main.R;
import com.wanyue.shop.model.ShopCartModel;
import com.wanyue.shop.view.activty.ShopCartActivity;
import java.util.Map;

public abstract class MainDefaultHeadViewProxy extends RxViewProxy implements View.OnClickListener {
    private ImageView mBtnMessage;
    private TextView mTvMessagePoint;
    private View mBtnSearch;
    private TextView mEtSearch;
    private ImageView mBtnShopCart;
    private TextView mTvShopPoint;

    @Override
    public int getLayoutId() {
        return R.layout.view_main_default_head;
    }

    @Override
    protected void initView(ViewGroup contentView) {
        super.initView(contentView);
        mBtnMessage = (ImageView) findViewById(R.id.btn_message);
        mTvMessagePoint = (TextView) findViewById(R.id.tv_message_point);
        mBtnSearch = (FrameLayout) findViewById(R.id.btn_search);
        mEtSearch = (TextView) findViewById(R.id.et_search);
        mBtnShopCart = (ImageView) findViewById(R.id.btn_shop_cart);
        mTvShopPoint = (TextView) findViewById(R.id.tv_shop_point);

        Map<String,Object> map= getArgMap();
        String title= Arg.getParmString(this,"title");
        mEtSearch.setHint(title);
        mEtSearch.setEnabled(false);
        mEtSearch.setClickable(false);
        mEtSearch.setFocusableInTouchMode(false);
        mBtnSearch.setOnClickListener(this);
        mBtnShopCart.setOnClickListener(this);
        mBtnMessage.setOnClickListener(this);
        ShopCartModel.obserShopCartNum2(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
              ViewUtil.setTextAndViewsibleByNumber(mTvShopPoint,integer);
            }
        });
        ShopCartModel.requestShopcartCount();
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if(id==R.id.btn_search){
            foward();
        }else if(id==R.id.btn_shop_cart){
            toShopCart();
        }else if(id==R.id.btn_message){
            ToastUtil.show(R.string.coming_soon);
        }
    }
    private void toShopCart() {
        startActivity(ShopCartActivity.class);
    }

    public abstract void foward();
}
