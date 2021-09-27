package com.wanyue.live.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.wanyue.common.adapter.RefreshAdapter;
import com.wanyue.common.bean.GoodsBean;
import com.wanyue.common.glide.ImgLoader;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.common.utils.WordUtil;
import com.wanyue.live.R;

/**
 * Created by  on 2019/8/29.
 */

public class LiveGoodsAdapter extends RefreshAdapter<GoodsBean> {

    private View.OnClickListener mOnClickListener;
    private String mMoneySymbol;

    public LiveGoodsAdapter(Context context) {
        super(context);
        mMoneySymbol = WordUtil.getString(R.string.money_symbol);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null && mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick((GoodsBean) tag, 0);
                }
            }
        };
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.item_live_goods, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position) {
        ((Vh) vh).setData(mList.get(position));
    }

    class Vh extends RecyclerView.ViewHolder {

        ImageView mThumb;
        TextView mTitle;
        TextView mPrice;
        View mBtnBuy;

        public Vh(View itemView) {
            super(itemView);
            mThumb = itemView.findViewById(R.id.thumb);
            mTitle = itemView.findViewById(R.id.title);
            mPrice = itemView.findViewById(R.id.price);
            mBtnBuy = itemView.findViewById(R.id.btn_buy);
            mBtnBuy.setOnClickListener(mOnClickListener);
        }

        void setData(GoodsBean bean) {
            mBtnBuy.setTag(bean);
            ImgLoader.display(mContext, bean.getThumb(), mThumb);
            mPrice.setText(StringUtil.contact(mMoneySymbol,bean.getPriceNow()));
            mTitle.setText(bean.getName());
        }
    }
}
