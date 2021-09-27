package com.wanyue.shop.view.view.order;

import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.wanyue.common.proxy.RxViewProxy;
import com.wanyue.common.utils.ResourceUtil;
import com.wanyue.shop.R;
import com.wanyue.shop.business.ShopState;

public class OrderDetailProgressViewProxy extends RxViewProxy {
    private TextView mBtnWaitPay;
    private TextView mBtnWaitDelivered;
    private TextView mBtnWaitReceived;
    private TextView mBtnWaitEvaluated;
    private TextView mBtnCompeleted;
    private ImageView mPointWaitPay;
    private ImageView mPointWaitDelivered;
    private ImageView mPointWaitReceived;
    private ImageView mPointWaitEvaluated;
    private ImageView mPointCompeleted;

    private Drawable mDefaultDrawable;
    private Drawable mSelectDrawable;
    private int mDefaultTextColor;
    private int mSelectTextColor;

    @Override
    protected void initView(ViewGroup contentView) {
        super.initView(contentView);
        mBtnWaitPay = (TextView) findViewById(R.id.btn_wait_pay);
        mBtnWaitDelivered = (TextView) findViewById(R.id.btn_wait_delivered);
        mBtnWaitReceived = (TextView) findViewById(R.id.btn_wait_received);
        mBtnWaitEvaluated = (TextView) findViewById(R.id.btn_wait_evaluated);
        mBtnCompeleted = (TextView) findViewById(R.id.btn_compeleted);
        mPointWaitPay = (ImageView) findViewById(R.id.point_wait_pay);
        mPointWaitDelivered = (ImageView) findViewById(R.id.point_wait_delivered);
        mPointWaitReceived = (ImageView) findViewById(R.id.point_wait_received);
        mPointWaitEvaluated = (ImageView) findViewById(R.id.point_wait_evaluated);
        mPointCompeleted = (ImageView) findViewById(R.id.point_compeleted);
        mDefaultDrawable=ResourceUtil.getDrawable(R.drawable.icon_order_status_default,false);
        mSelectDrawable=ResourceUtil.getDrawable(R.drawable.icon_order_status_select,false);
        mDefaultTextColor=ResourceUtil.getColor(getActivity(),R.color.textColor);
        mSelectTextColor=ResourceUtil.getColor(getActivity(),R.color.global);
    }
    public void setStatus(int status){
        if(!isInit()){
           return;
        }
        switch (status){
            case ShopState.ORDER_STATE_WAIT_PAY:
                mBtnWaitPay.setTextColor(mSelectTextColor);
                mBtnWaitDelivered.setTextColor(mDefaultTextColor);
                mBtnWaitReceived.setTextColor(mDefaultTextColor);
                mBtnWaitEvaluated.setTextColor(mDefaultTextColor);
                mBtnCompeleted.setTextColor(mDefaultTextColor);

                mPointWaitPay.setImageDrawable(mSelectDrawable);
                mPointWaitDelivered.setImageDrawable(mDefaultDrawable);
                mPointWaitReceived.setImageDrawable(mDefaultDrawable);
                mPointWaitEvaluated.setImageDrawable(mDefaultDrawable);
                mPointCompeleted.setImageDrawable(mDefaultDrawable);
                break;
            case ShopState.ORDER_STATE_WAIT_DELIVERED:
                mBtnWaitPay.setTextColor(mDefaultTextColor);
                mBtnWaitDelivered.setTextColor(mSelectTextColor);
                mBtnWaitReceived.setTextColor(mDefaultTextColor);
                mBtnWaitEvaluated.setTextColor(mDefaultTextColor);
                mBtnCompeleted.setTextColor(mDefaultTextColor);

                mPointWaitPay.setImageDrawable(mDefaultDrawable);
                mPointWaitDelivered.setImageDrawable(mSelectDrawable);
                mPointWaitReceived.setImageDrawable(mDefaultDrawable);
                mPointWaitEvaluated.setImageDrawable(mDefaultDrawable);
                mPointCompeleted.setImageDrawable(mDefaultDrawable);

                break;
            case ShopState.ORDER_STATE_WAIT_RECEIVE:
                mBtnWaitPay.setTextColor(mDefaultTextColor);
                mBtnWaitDelivered.setTextColor(mDefaultTextColor);
                mBtnWaitReceived.setTextColor(mSelectTextColor);
                mBtnWaitEvaluated.setTextColor(mDefaultTextColor);
                mBtnCompeleted.setTextColor(mDefaultTextColor);

                mPointWaitPay.setImageDrawable(mDefaultDrawable);
                mPointWaitDelivered.setImageDrawable(mDefaultDrawable);
                mPointWaitReceived.setImageDrawable(mSelectDrawable);
                mPointWaitEvaluated.setImageDrawable(mDefaultDrawable);
                mPointCompeleted.setImageDrawable(mDefaultDrawable);
                break;
            case ShopState.ORDER_STATE_WAIT_EVALUATE:
                mBtnWaitPay.setTextColor(mDefaultTextColor);
                mBtnWaitDelivered.setTextColor(mDefaultTextColor);
                mBtnWaitReceived.setTextColor(mDefaultTextColor);
                mBtnWaitEvaluated.setTextColor(mSelectTextColor);
                mBtnCompeleted.setTextColor(mDefaultTextColor);

                mPointWaitPay.setImageDrawable(mDefaultDrawable);
                mPointWaitDelivered.setImageDrawable(mDefaultDrawable);
                mPointWaitReceived.setImageDrawable(mDefaultDrawable);
                mPointWaitEvaluated.setImageDrawable(mSelectDrawable);
                mPointCompeleted.setImageDrawable(mDefaultDrawable);
                break;
            case ShopState.ORDER_STATE_COMPELETE:
                mBtnWaitPay.setTextColor(mDefaultTextColor);
                mBtnWaitDelivered.setTextColor(mDefaultTextColor);
                mBtnWaitReceived.setTextColor(mDefaultTextColor);
                mBtnWaitEvaluated.setTextColor(mDefaultTextColor);
                mBtnCompeleted.setTextColor(mSelectTextColor);

                mPointWaitPay.setImageDrawable(mDefaultDrawable);
                mPointWaitDelivered.setImageDrawable(mDefaultDrawable);
                mPointWaitReceived.setImageDrawable(mDefaultDrawable);
                mPointWaitEvaluated.setImageDrawable(mDefaultDrawable);
                mPointCompeleted.setImageDrawable(mSelectDrawable);
                break;
            default:
                break;
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.view_order_detail_progress;
    }
}
