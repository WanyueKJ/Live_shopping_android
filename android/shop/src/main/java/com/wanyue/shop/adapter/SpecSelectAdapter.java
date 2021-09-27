package com.wanyue.shop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.wanyue.common.adapter.base.BaseReclyViewHolder;
import com.wanyue.common.utils.DebugUtil;
import com.wanyue.common.utils.ListUtil;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.shop.R;
import com.wanyue.shop.bean.SpecsBean;
import com.wanyue.shop.view.widet.FlexRadioGroup;
import com.wanyue.shop.view.widet.ViewGroupLayoutBaseAdapter;
import java.util.List;

public class SpecSelectAdapter extends ViewGroupLayoutBaseAdapter<SpecsBean> {
    private LayoutInflater mLayoutInflater;
    private String[]specKeyArray;
    private OnKeyChangeListnter mOnKeyChangeListnter;
    public SpecSelectAdapter(List<SpecsBean> list,String selectKey,LayoutInflater layoutInflater) {
        super(list);
        if(selectKey!=null){
          specKeyArray=selectKey.split(",");
        }
        mLayoutInflater=layoutInflater;
    }

    /*这是不复用item的模式*/
    @Override
    public void convert(BaseReclyViewHolder helper, SpecsBean item) {
         helper.setText(R.id.tv_title,item.getName());
         int position=helper.getObjectPosition();
         List<String>labelList=item.getValue();
         FlexRadioGroup flexRadioGroup=helper.getView(R.id.flex);

        flexRadioGroup.setFlexWrap(FlexWrap.WRAP);
        flexRadioGroup.setFlexDirection(FlexDirection.ROW);
        initChild(position,flexRadioGroup,labelList);
    }
        private int getChildPosition(View view) {
           Object object= view.getTag();
           if(object!=null&&object instanceof Integer){
               return (int) object;
           }
           return -1;
    }

    private void initChild(final int layoutPosition,final FlexRadioGroup flexRadioGroup,final  List<String> labelList) {
       if(!ListUtil.haveData(labelList)||mLayoutInflater==null){
           return;
       }

        flexRadioGroup.setOnCheckedChangeListener(new FlexRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(int checkedId) {
                View view= flexRadioGroup.findViewById(checkedId);
                if(view==null){
                    return;
                }
                int childPosition=getChildPosition(view);
                int size=ListUtil.getSize(specKeyArray);
                if(childPosition!=-1&&size>layoutPosition){
                    specKeyArray[layoutPosition]=labelList.get(childPosition);
                    specKeyChange();
                }else{
                    DebugUtil.sendException("specKeyArray大小必须大于layoutPosition");
                }
            }
        });

       int size=labelList.size();
       String key= ListUtil.getArrayData(specKeyArray,layoutPosition);
       for(int i=0;i<size;i++){
         String label=labelList.get(i);
         boolean isChecked=false;
         if(StringUtil.equals(key,label)){
             isChecked=true;
         }
         RadioButton radioButton= (RadioButton) mLayoutInflater.inflate(R.layout.item_relcy_spec_child,flexRadioGroup,false);
         radioButton.setText(label);
         radioButton.setTag(i);
         flexRadioGroup.addView(radioButton);
         radioButton.setChecked(isChecked);
       }
    }
    /*选择改变了*/
    private void specKeyChange() {
        if(specKeyArray!=null&&mOnKeyChangeListnter!=null){
           mOnKeyChangeListnter.change(specKeyArray);
        }
    }
    public void setOnKeyChangeListnter(OnKeyChangeListnter onKeyChangeListnter) {
        mOnKeyChangeListnter = onKeyChangeListnter;
    }

    public interface OnKeyChangeListnter{
        public void change(String[]keyArray);
    }

    public String[] getSpecKeyArray() {
        return specKeyArray;
    }

    public void setSpecKeyArray(String[] specKeyArray) {
        this.specKeyArray = specKeyArray;
    }

    @Override
    protected int getLayoutId(SpecsBean o) {
        return R.layout.item_relcly_spec_select;
    }

}
