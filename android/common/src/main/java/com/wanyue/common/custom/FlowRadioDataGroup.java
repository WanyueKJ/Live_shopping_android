package com.wanyue.common.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tencent.cos.xml.utils.StringUtils;
import com.wanyue.common.bean.ExportNamer;
import com.wanyue.common.utils.ListUtil;
import com.wanyue.common.utils.StringUtil;

import java.util.List;

public class FlowRadioDataGroup<T extends ExportNamer> extends FlowRadioGroup {
    private List<T> mData;
    private RadioButtonFactory radioButtonFactory;
    private SelectDataChangeListner selectDataChangeListner;
    private int defaultSelectPosition=-1;

    public FlowRadioDataGroup(Context context) {
        super(context);
    }
    public FlowRadioDataGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {

        super.init();
        setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(selectDataChangeListner!=null){
                    selectDataChangeListner.select(FlowRadioDataGroup.this, getSelectData());
                }
            }
        });
    }

    public void setData(List<T> data) {
        this.mData = data;
    }

    private T getSelectData() {
        int index=getCheckedRadioButtonIndex();
        if(index!=-1&&mData!=null&&mData.size()>index){
            return mData.get(index);
        }
        return null;
    }


    public void createChildByData(){
        removeAllViews();
        if(mData!=null&&radioButtonFactory!=null){
            ViewGroup.LayoutParams layoutParams=radioButtonFactory.getLayoutParm();
            for(T t:mData){
              RadioButton radioButton=radioButtonFactory.createRadioButton(getContext(),t,this,layoutParams);
              if(layoutParams==null){
               addView(radioButton);
              }else{
               addView(radioButton,layoutParams);
              }
            }
        }
        if(defaultSelectPosition!=-1){
            checkByPosition(0);
        }
    }

    public void clearAllData(){
        removeAllViews();
        mData=null;
    }



    /*初始化子控件之后*/
    public void  setCancleSelf(boolean cancleSelf){
         int size=getChildCount();
         for(int i=0;i<size;i++){
             View view=getChildAt(i);
             if(view instanceof CancleSelfRadioButton){
                 CancleSelfRadioButton radioButton= (CancleSelfRadioButton) view;
                 radioButton.setCanCancleSelf(cancleSelf);
             }
         }
    }


    public void setSelectDataChangeListner(SelectDataChangeListner<T> selectDataChangeListner) {
        this.selectDataChangeListner = selectDataChangeListner;
    }


    public void setRadioButtonFactory(RadioButtonFactory radioButtonFactory) {
        this.radioButtonFactory = radioButtonFactory;
    }


    public void setSelect(String id) {
        if(ListUtil.haveData(mData)&&getChildCount()>0){
          int size=mData.size();
          for(int i=0;i<size;i++){
             ExportNamer exportNamer= mData.get(i);
             if(StringUtil.equals(exportNamer.exportId(),id)){
                select(i);
                return;
             }else if(StringUtils.isEmpty(id)){
                 select(0);
             }
          }
        }
    }

    private void select(int position) {
        CompoundButton compoundButton= (CompoundButton) getChildAt(position);
        compoundButton.setChecked(true);
    }

    public  interface SelectDataChangeListner<T>{
        public void select(View view,T t);
    }
    public void checkByPosition(int position){
        int count=getChildCount();
        if(position<0||position>=count){
            return;
        }
        check(getChildAt(position).getId());
    }
    /*设置默认选中的radioButton*/
    public void setDefaultSelectPosition(int defaultSelectPosition) {
        this.defaultSelectPosition = defaultSelectPosition;
    }

    public List<T> getData() {
        return mData;
    }

    public interface RadioButtonFactory{
        public  RadioButton createRadioButton(Context context,ExportNamer t, ViewGroup viewGroup,ViewGroup.LayoutParams params);
        public ViewGroup.LayoutParams getLayoutParm();
    }
}
