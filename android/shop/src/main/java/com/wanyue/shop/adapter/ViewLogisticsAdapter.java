package com.wanyue.shop.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.wanyue.common.adapter.base.BaseReclyViewHolder;
import com.wanyue.common.utils.ResourceUtil;
import com.wanyue.shop.R;
import com.wanyue.shop.bean.ViewLogisticsBean;
import com.wanyue.shop.view.widet.ViewGroupLayoutBaseAdapter;
import java.util.List;

public class ViewLogisticsAdapter extends ViewGroupLayoutBaseAdapter<ViewLogisticsBean> {
    public ViewLogisticsAdapter(List<ViewLogisticsBean> list) {
        super(list);
    }

    @Override
    public void convert(BaseReclyViewHolder helper, ViewLogisticsBean item) {
        int size=getCount();
        int position=helper.getObjectPosition();

        View lineUp=helper.getView(R.id.v_up_line);
        View lineDown=helper.getView(R.id.v_down_line);
        View node=helper.getView(R.id.v_node);
        Context context=lineUp.getContext();
        int selectColor=ResourceUtil.getColor(context,R.color.global);
        int normalColor=ResourceUtil.getColor(context,R.color.gray_dc);


        TextView tvStatus=helper.getView(R.id.tv_status);
        TextView tvTime=helper.getView(R.id.tv_time);

        tvStatus.setText(item.getStatus());
        tvTime.setText(item.getTime());

        if(size==1){
            lineUp.setBackground(null);
            lineDown.setBackground(null);
            node.setBackground(ResourceUtil.getDrawable(R.drawable.round_graydc,true));
            tvStatus.setTextColor(normalColor);
            tvTime.setTextColor(normalColor);
        }else if(size==2){
            if(position==0){
                lineDown.setBackground(null);
                tvStatus.setTextColor(selectColor);
                tvTime.setTextColor(selectColor);
                lineDown.setBackgroundColor(selectColor);
                node.setBackground(ResourceUtil.getDrawable(R.drawable.round_global,true));
                lineUp.setBackground(null);
            }else if(position==1){
                node.setBackground(ResourceUtil.getDrawable(R.drawable.round_graydc,true));
                tvStatus.setTextColor(normalColor);
                tvTime.setTextColor(normalColor);
                lineUp.setBackgroundColor(selectColor);
                lineDown.setBackground(null);
            }
        }else if(size>2){
            if(position==0){
                lineDown.setBackground(null);
                tvStatus.setTextColor(selectColor);
                tvTime.setTextColor(selectColor);
                lineDown.setBackgroundColor(selectColor);
                node.setBackground(ResourceUtil.getDrawable(R.drawable.round_global,true));
                lineUp.setBackground(null);
            }else if(position==1){
                node.setBackground(ResourceUtil.getDrawable(R.drawable.round_graydc,true));
                tvStatus.setTextColor(normalColor);
                tvTime.setTextColor(normalColor);
                lineUp.setBackgroundColor(selectColor);
                lineDown.setBackgroundColor(normalColor);
            }else if(position==size-1){
                node.setBackground(ResourceUtil.getDrawable(R.drawable.round_graydc,true));
                tvStatus.setTextColor(normalColor);
                tvTime.setTextColor(normalColor);
                lineUp.setBackgroundColor(normalColor);
                lineDown.setBackground(null);
            }else{
                node.setBackground(ResourceUtil.getDrawable(R.drawable.round_graydc,true));
                tvStatus.setTextColor(normalColor);
                tvTime.setTextColor(normalColor);
                lineUp.setBackgroundColor(normalColor);
                lineDown.setBackgroundColor(normalColor);
            }
        }




    }

    @Override
    protected int getLayoutId(ViewLogisticsBean viewLogisticsBean) {
        return R.layout.item_recly_view_logistics;
    }
}
