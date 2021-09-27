package com.wanyue.common.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.Constants;
import com.wanyue.common.bean.ConfigBean;
import com.wanyue.common.interfaces.OnItemClickListener;
import com.wanyue.common.mob.MobBean;
import com.wanyue.common.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by  on 2018/10/19.
 * 直播分享
 */

public class CommonShareAdapter extends RecyclerView.Adapter<CommonShareAdapter.Vh> {

    private List<MobBean> mList;
    private LayoutInflater mInflater;
    private View.OnClickListener mOnClickListener;
    private OnItemClickListener<MobBean> mOnItemClickListener;

    public CommonShareAdapter(Context context) {
        mList = new ArrayList<>();
        ConfigBean configBean = CommonAppConfig.getConfig();
        if (configBean != null) {
            List<MobBean> list = MobBean.getLiveShareTypeList(configBean.getShareType());
            mList.addAll(list);
        }


        mInflater = LayoutInflater.from(context);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick((MobBean) tag, 0);
                    }
                }
            }
        };
    }
    public void addLink(){
        MobBean linkBean = new MobBean();
        linkBean.setType(Constants.COPY);
        linkBean.setName(R.string.copy_link);
        linkBean.setIcon1(R.mipmap.icon_live_link);
        mList.add(linkBean);
    }




    public void setOnItemClickListener(OnItemClickListener<MobBean> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.item_live_share, parent, false));
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

        ImageView mIcon;
        TextView mName;

        public Vh(View itemView) {
            super(itemView);
            mIcon = (ImageView) itemView.findViewById(R.id.icon);
            mName = (TextView) itemView.findViewById(R.id.name);
            itemView.setOnClickListener(mOnClickListener);
        }

        void setData(MobBean bean) {
            itemView.setTag(bean);
            mIcon.setImageResource(bean.getIcon1());
            mName.setText(bean.getName());
        }
    }
}
