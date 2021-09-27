package com.wanyue.main.adapter;

import android.widget.TextView;
import androidx.annotation.NonNull;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.wanyue.common.CommonApplication;
import com.wanyue.common.adapter.base.BaseMutiRecyclerAdapter;
import com.wanyue.common.adapter.base.BaseReclyViewHolder;
import com.wanyue.common.utils.ResourceUtil;
import com.wanyue.main.R;
import com.wanyue.main.bean.CommissionBean;
import com.wanyue.main.bean.CommissionSectionBean;
import java.util.List;

public class CommissionDetailAdapter extends BaseMutiRecyclerAdapter<MultiItemEntity, BaseReclyViewHolder> {
    private boolean mIsMustUnit;//必须使用符号
    public CommissionDetailAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(CommissionSectionBean.TYPE, R.layout.item_recly_commission_detail_head);
        addItemType(CommissionBean.TYPE, R.layout.item_recly_commission_detail_normal);
    }
    @Override
    protected void convert(@NonNull BaseReclyViewHolder helper, MultiItemEntity item) {
            switch (helper.getItemViewType()){
                case CommissionSectionBean.TYPE:
                    convertHead(helper,item);
                    break;
                case CommissionBean.TYPE:
                    convertNormal(helper,item);
                    break;
                default:
                    break;
            }
    }

    private void convertNormal(BaseReclyViewHolder helper, MultiItemEntity item) {
        CommissionBean commissionBean= (CommissionBean) item;
        helper.setText(R.id.tv_name,commissionBean.getTitle());
        helper.setText(R.id.tv_add_time,commissionBean.getTime());
        TextView tvResult=helper.getView(R.id.tv_result);
        tvResult.setText(commissionBean.getResult(mIsMustUnit));
        if(commissionBean.getPm()==0){
            tvResult.setTextColor(ResourceUtil.getColor(CommonApplication.sInstance,R.color.global));
        }else{
            tvResult.setTextColor(ResourceUtil.getColor(CommonApplication.sInstance,R.color.global));
        }
    }


    private void convertHead(BaseReclyViewHolder helper, MultiItemEntity item) {
        CommissionSectionBean sectionBean= (CommissionSectionBean) item;
        helper.setText(R.id.tv_title,sectionBean.getTime());
    }

    public void setMustUnit(boolean mustUnit) {
        mIsMustUnit = mustUnit;
    }
}
