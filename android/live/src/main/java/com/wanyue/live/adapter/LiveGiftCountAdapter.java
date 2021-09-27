package com.wanyue.live.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wanyue.common.interfaces.OnItemClickListener;
import com.wanyue.live.R;

/**
 * Created by  on 2018/10/13.
 */

public class LiveGiftCountAdapter extends RecyclerView.Adapter<LiveGiftCountAdapter.Vh> {

    private String[] mArray;
    private LayoutInflater mInflater;
    private View.OnClickListener mOnClickListener;
    private OnItemClickListener<String> mOnItemClickListener;

    public LiveGiftCountAdapter(Context context) {
        mArray = new String[]{"1", "10", "66", "88", "100", "520", "1314"};
        mInflater = LayoutInflater.from(context);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null && mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick((String) tag, 0);
                }
            }
        };
    }

    public void setOnItemClickListener(OnItemClickListener<String> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.item_live_gift_count, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Vh vh, int position) {
        vh.setData(mArray[position]);
    }

    @Override
    public int getItemCount() {
        return mArray.length;
    }

    class Vh extends RecyclerView.ViewHolder {

        public Vh(View itemView) {
            super(itemView);
            itemView.setOnClickListener(mOnClickListener);
        }

        void setData(String text) {
            itemView.setTag(text);
            ((TextView) itemView).setText(text);
        }
    }
}
