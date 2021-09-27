package com.wanyue.live.adapter;

import android.content.Context;
import android.graphics.Paint;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wanyue.common.adapter.RefreshAdapter;
import com.wanyue.common.bean.GoodsBean;
import com.wanyue.common.glide.ImgLoader;
import com.wanyue.common.http.HttpCallback;
import com.wanyue.common.utils.ClickUtil;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.common.utils.WordUtil;
import com.wanyue.live.R;
import com.wanyue.live.http.LiveHttpUtil;
import com.wanyue.live.http.LiveShopAPI;

/**
 * Created by  on 2019/8/29.
 */

public class LiveShopAdapter extends RefreshAdapter<GoodsBean> {

    private View.OnClickListener mOnClickListener;
    private ActionListener mActionListener;
    private String mMoneySymbol;


    public LiveShopAdapter(Context context) {
        super(context);
        mMoneySymbol = WordUtil.getString(R.string.money_symbol);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!canClick()) {
                    return;
                }
                final int position = (int) v.getTag();
                GoodsBean bean = mList.get(position);
                LiveShopAPI.shopSetSale(bean.getId(), 0, new HttpCallback() {
                            @Override
                            public void onSuccess(int code, String msg, String[] info) {
                                if (isSuccess(code)) {
                                    mList.remove(position);
                                    notifyDataSetChanged();
                                    if(mActionListener!=null){
                                        mActionListener.onDeleteSuccess();
                                    }
                                } else {
                                    ToastUtil.show(msg);
                                }
                            }
                        }
                );
            }
        };

    }

    public void setActionListener(ActionListener actionListener){
        mActionListener=actionListener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.item_live_shop, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position) {
        ((Vh) vh).setData(mList.get(position), position);
    }

    class Vh extends RecyclerView.ViewHolder {

        ImageView mThumb;
        TextView mDes;
        TextView mPrice;
        TextView mPriceOrigin;
        View mBtnDel;
         TextView mTvGoodsNum;




        public Vh(View itemView) {
            super(itemView);
            mThumb = itemView.findViewById(R.id.thumb);
            mDes = itemView.findViewById(R.id.des);
            mPrice = itemView.findViewById(R.id.price);
            mPriceOrigin = itemView.findViewById(R.id.price_origin);
            mBtnDel = itemView.findViewById(R.id.btn_delete);
            mBtnDel.setOnClickListener(mOnClickListener);
            mTvGoodsNum = (TextView) itemView.findViewById(R.id.tv_goods_num);

        }

        void setData(GoodsBean bean, int position) {
            mBtnDel.setTag(position);
            ImgLoader.display(mContext, bean.getThumb(), mThumb);
            mPrice.setText(StringUtil.contact(mMoneySymbol, bean.getPriceNow()));
            mTvGoodsNum.setText(Integer.toString((position+1)));
            mDes.setText(bean.getName());
        }
    }


    public interface ActionListener{
        void onDeleteSuccess();
    }


}
