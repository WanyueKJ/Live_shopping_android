package com.wanyue.common.adapter.radio;

import android.view.View;
import android.widget.Checkable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tencent.cos.xml.utils.StringUtils;
import com.wanyue.common.R;
import com.wanyue.common.adapter.base.BaseReclyViewHolder;
import com.wanyue.common.adapter.base.BaseRecyclerAdapter;
import com.wanyue.common.utils.ListUtil;
import com.wanyue.common.utils.StringUtil;
import java.util.List;

public class RadioAdapter<T extends IRadioChecker> extends BaseRecyclerAdapter<T, BaseReclyViewHolder> {
    private Checkable selectCheckAble;
    private int selectPosition=-1;

    public RadioAdapter(List<T> data) {
        super(data);
        setOnItemClickListener(onItemClickListener);
    }
    @Override
    public int getLayoutId() {
        return R.layout.item_recly_radio;
    }

    @Override
    protected void convert(BaseReclyViewHolder helper, IRadioChecker item) {
        helper.setText(contentViewId(),item.getContent());
        Checkable checkable=helper.getView(checkId());
        int position=helper.getLayoutPosition();
        if(selectPosition==position&&(selectCheckAble==null||selectCheckAble!=checkable)){
           setCheck(checkable,position);
        }
    }



    private OnItemClickListener onItemClickListener=new OnItemClickListener() {
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            Checkable checkable= view.findViewById(checkId());
            setCheck(checkable,position);

        }
    };

    private void setCheck(Checkable checkable, int position) {
        if(selectCheckAble!=null&&selectCheckAble!=checkable){
            selectCheckAble.setChecked(false);
        }
        selectCheckAble=checkable;
        checkable.setChecked(true);
        selectPosition=position;
    }

    @Override
    public void setData(List<T> data) {
        super.setData(data);

    }

    public int contentViewId(){
        return R.id.tv_content;
    }
    public int checkId(){
        return R.id.check_image;
    }

    public T getSelectData(){
        if(selectPosition==-1) {
            return null;
        }
        return ListUtil.safeGetData(mData,selectPosition);
    }

    public int setDefaultSelect(String id){
        if(!ListUtil.haveData(mData)|| StringUtils.isEmpty(id)){
            return -1;
        }
        int size=mData.size();
        for(int i=0;i<size;i++){
          if(StringUtil.equals(mData.get(i).getId(),id)){
              selectPosition=i;
              break;
          }
        }
        notifyItemChanged(selectPosition);
        return selectPosition;
    }

    public String getId(){
        if(size()==0||selectPosition==-1) {
            return null;
        }
        return mData.get(selectPosition).getId();
    }

}
