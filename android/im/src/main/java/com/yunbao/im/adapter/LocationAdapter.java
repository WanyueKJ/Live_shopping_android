package com.yunbao.im.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunbao.common.Constants;
import com.yunbao.im.R;
import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.bean.TxLocationPoiBean;

import java.util.List;

/**
 * Created by  on 2018/7/18.
 */

public class LocationAdapter extends RefreshAdapter<TxLocationPoiBean> {

    private int mCheckedPosition;
    private View.OnClickListener mOnClickListener;

    public LocationAdapter(Context context) {
        super(context);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag == null) {
                    return;
                }
                int position = (int) tag;
                TxLocationPoiBean bean = mList.get(position);
                if (mCheckedPosition != position) {
                    mList.get(mCheckedPosition).setChecked(false);
                    bean.setChecked(true);
                    notifyItemChanged(mCheckedPosition, Constants.PAYLOAD);
                    notifyItemChanged(position, Constants.PAYLOAD);
                    mCheckedPosition = position;
                }
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(bean, position);
                }
            }
        };
    }

    public TxLocationPoiBean getCheckedLocationPoiBean() {
        if (mList != null && mCheckedPosition >= 0 && mCheckedPosition < mList.size()) {
            return mList.get(mCheckedPosition);
        }
        return null;
    }


    @Override
    public Vh onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.item_location, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position, @NonNull List payloads) {
        Object payload = payloads.size() > 0 ? payloads.get(0) : null;
        ((Vh) vh).setData(mList.get(position), position, payload);
    }

    @Override
    public void refreshData(List<TxLocationPoiBean> list) {
        if (list.size() > 0) {
            list.get(0).setChecked(true);
        }
        mCheckedPosition = 0;
        super.refreshData(list);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class Vh extends RecyclerView.ViewHolder {

        TextView mTitle;
        TextView mAddress;
        ImageView mRadioButton;
        View mLine;

        public Vh(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.title);
            mAddress = (TextView) itemView.findViewById(R.id.address);
            mRadioButton = (ImageView) itemView.findViewById(R.id.radioButton);
            mLine = itemView.findViewById(R.id.line);
            itemView.setOnClickListener(mOnClickListener);
        }

        void setData(TxLocationPoiBean bean, int position, Object payload) {
            itemView.setTag(position);
            if (payload == null) {
                mTitle.setText(bean.getTitle());
                mAddress.setText(bean.getAddress());
                if (position == mList.size() - 1) {
                    if (mLine.getVisibility() == View.VISIBLE) {
                        mLine.setVisibility(View.INVISIBLE);
                    }
                } else {
                    if (mLine.getVisibility() != View.VISIBLE) {
                        mLine.setVisibility(View.VISIBLE);
                    }
                }
            }
            if (bean.isChecked()) {
                mRadioButton.setImageResource(R.mipmap.icon_checked);
            } else {
                mRadioButton.setImageDrawable(null);
            }

        }
    }


}
