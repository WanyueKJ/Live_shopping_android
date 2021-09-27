package com.wanyue.main.adapter;

import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.wanyue.common.adapter.base.BaseReclyViewHolder;
import com.wanyue.common.adapter.base.BaseRecyclerAdapter;
import com.wanyue.common.utils.ViewUtil;
import com.wanyue.main.R;
import com.wanyue.main.bean.ClassifyBean;
import java.util.List;

public class ClassifyIndexAdapter extends BaseRecyclerAdapter<ClassifyBean, BaseReclyViewHolder> {
    private static final int DEFAULT_INDEX=0;
    private int mSelectIndex=DEFAULT_INDEX;
    private RadioButton mSelectRatioButton;

    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener;

    public ClassifyIndexAdapter(List<ClassifyBean> data) {
        super(data);
        initOnCheckListner();
    }

    private void initOnCheckListner() {
        if(mOnCheckedChangeListener==null){
            mOnCheckedChangeListener=new CompoundButton.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked&&!ViewUtil.equalsView(buttonView,mSelectRatioButton)){
                       ViewUtil.setChecked(mSelectRatioButton,false);
                        mSelectRatioButton= (RadioButton) buttonView;
                    }
                }
            };
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_recly_index_classify;
    }

    @Override
    protected void convert(BaseReclyViewHolder helper, ClassifyBean item) {
        RadioButton radioButton=helper.getView(R.id.radio);
        radioButton.setOnCheckedChangeListener(mOnCheckedChangeListener);
        radioButton.setText(item.getName());
        int layoutPosition=helper.getLayoutPosition();
        if(mSelectIndex==layoutPosition){
           if(!ViewUtil.equalsView(radioButton,mSelectRatioButton)){
               ViewUtil.setChecked(mSelectRatioButton,false);
               mSelectRatioButton=radioButton;
           }
            ViewUtil.setChecked(radioButton,true);
        }else{
            ViewUtil.setChecked(radioButton,false);
        }
    }

    public void  setSelectIndex(int position){
        if(position>=size()||mSelectIndex==position){
            return;
        }
        mSelectIndex=position;
        notifyItemChanged(mSelectIndex);


    }
}
