package com.wanyue.live.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.wanyue.common.bean.LiveClassBean;
import com.wanyue.common.custom.MyRadioButton;
import com.wanyue.common.glide.ImgLoader;
import com.wanyue.common.interfaces.OnItemClickListener;
import com.wanyue.live.R;
import java.util.List;

/**
 * Created by  on 2018/10/7.
 */

public class LiveReadyClassAdapter extends RecyclerView.Adapter<LiveReadyClassAdapter.Vh> {

    private Context mContext;
    private List<LiveClassBean> mList;
    private LayoutInflater mInflater;
    private View.OnClickListener mOnClickListener;
    private OnItemClickListener<LiveClassBean> mOnItemClickListener;

    public LiveReadyClassAdapter(Context context, List<LiveClassBean> list) {
        mContext=context;
        mList = list;
        mInflater = LayoutInflater.from(context);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null && mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick((LiveClassBean) tag, 0);
                }
            }
        };
    }

    public void setOnItemClickListener(OnItemClickListener<LiveClassBean> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.item_live_ready_class, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Vh vh, int position) {
        vh.setData(mList.get(position));
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    class Vh extends RecyclerView.ViewHolder {
        TextView mName;
        MyRadioButton mRadioButton;

        public Vh(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.tv_name);
            mRadioButton = (MyRadioButton) itemView.findViewById(R.id.radioButton);
            itemView.setOnClickListener(mOnClickListener);
        }

        void setData(LiveClassBean bean) {
            itemView.setTag(bean);
            mName.setText(bean.getName());
            mRadioButton.doChecked(bean.isChecked());
        }
    }
}
