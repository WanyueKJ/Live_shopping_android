package com.wanyue.main.adapter;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.wanyue.common.adapter.base.BaseReclyViewHolder;
import com.wanyue.main.R;
import com.wanyue.main.bean.ClassifyBean;
import com.wanyue.main.bean.ClassifySectionBean;
import java.util.List;

public class ClassifyAdapter extends BaseSectionQuickAdapter<ClassifySectionBean, BaseReclyViewHolder> {
    public ClassifyAdapter(int layoutResId, int sectionHeadResId, List<ClassifySectionBean> data) {
        super(layoutResId, sectionHeadResId, data);
    }

    @Override
    protected void convertHead(BaseReclyViewHolder helper, ClassifySectionBean item) {
        helper.setText(R.id.tv_title,item.header);
    }

    @Override
    protected void convert(BaseReclyViewHolder helper, ClassifySectionBean item) {
        ClassifyBean classifyBean=item.t;
        if(classifyBean!=null){
            helper.setText(R.id.tv_name,classifyBean.getName());
            helper.setImageUrl(classifyBean.getPic(),R.id.img_conver);
        }
    }

    public void setData(List<ClassifySectionBean> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public int transformRealPosition(int position){
        return -1;
    }

    public int size(){
        return mData==null?0:mData.size();
    }



}
