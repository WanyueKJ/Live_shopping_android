package com.wanyue.live.adapter;

import android.content.Context;
import android.graphics.Paint;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wanyue.common.Constants;
import com.wanyue.common.adapter.RefreshAdapter;
import com.wanyue.common.bean.GoodsBean;
import com.wanyue.common.custom.MyRadioButton;
import com.wanyue.common.glide.ImgLoader;
import com.wanyue.common.http.HttpCallback;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.common.utils.WordUtil;
import com.wanyue.live.R;
import com.wanyue.live.event.LiveGoodsChangeEvent;
import com.wanyue.live.http.LiveHttpUtil;
import com.wanyue.live.http.LiveShopAPI;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by  on 2019/8/29.
 */

public class LiveGoodsAddAdapter extends RefreshAdapter<GoodsBean> {

    private View.OnClickListener mAddClickListener;
    private String mMoneySymbol;
    private String mAddString;
    private String mAddedString;


    public LiveGoodsAddAdapter(Context context) {
        super(context);
        mAddClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!canClick()) {
                    return;
                }
                final int position = (int) v.getTag();
                final GoodsBean bean = mList.get(position);
                final int isSale = bean.getIssale() == 1 ? 0 : 1;
                LiveShopAPI.shopSetSale(bean.getId(), isSale, new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        if (isSuccess(code)) {
                            bean.setIssale(isSale);
                            EventBus.getDefault().post(new LiveGoodsChangeEvent());
                            notifyItemChanged(position, Constants.PAYLOAD);
                        }
                        ToastUtil.show(msg);
                    }

                });
            }
        };
        mMoneySymbol = WordUtil.getString(R.string.money_symbol);
        mAddString = WordUtil.getString(R.string.add);
        mAddedString = WordUtil.getString(R.string.added);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.item_live_goods_add, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position) {

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position, @NonNull List payloads) {
        Object payload = payloads.size() > 0 ? payloads.get(0) : null;
        ((Vh) vh).setData(mList.get(position), position, payload);
    }

    class Vh extends RecyclerView.ViewHolder {

        ImageView mThumb;
        TextView mTitle;
        TextView mPrice;
        TextView mTvProfit;
        MyRadioButton mBtnAdd;

        public Vh(View itemView) {
            super(itemView);
            mThumb = itemView.findViewById(R.id.thumb);
            mTitle = itemView.findViewById(R.id.title);
            mPrice = itemView.findViewById(R.id.price);
            mTvProfit = itemView.findViewById(R.id.tv_profit);
            mBtnAdd = itemView.findViewById(R.id.btn_add);
            mBtnAdd.setOnClickListener(mAddClickListener);
        }

        void setData(GoodsBean bean, int position, Object payload) {
            if (payload == null) {
                mBtnAdd.setTag(position);
                ImgLoader.display(mContext, bean.getThumb(), mThumb);
                mTvProfit.setText(StringUtil.contact(mMoneySymbol, bean.getConsignment()));
                mPrice.setText(StringUtil.contact(mMoneySymbol, bean.getPriceNow()));
                mTitle.setText(bean.getName());
            }

            if (bean.getIssale() == 1) {
                mBtnAdd.setText(mAddedString);
                mBtnAdd.doChecked(true);

            } else {
                mBtnAdd.setText(mAddString);
                mBtnAdd.doChecked(false);
            }
        }
    }
}
