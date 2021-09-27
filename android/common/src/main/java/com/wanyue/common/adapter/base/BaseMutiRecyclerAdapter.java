package com.wanyue.common.adapter.base;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.wanyue.common.custom.refresh.RxRefreshView;
import com.wanyue.common.utils.ClickUtil;
import com.wanyue.common.utils.ListUtil;
import com.wanyue.common.utils.ViewUtil;

import java.util.List;

public abstract class BaseMutiRecyclerAdapter<T extends MultiItemEntity, K  extends BaseViewHolder> extends BaseMultiItemQuickAdapter<T, K> implements RxRefreshView.DataAdapter<T> {
    protected View.OnClickListener mOnClickListener;
    protected OnItemChildClickListener2<T> mOnItemChildClickListener2;
    public BaseMutiRecyclerAdapter(List<T> data) {
        super(data);
        mOnClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer integer= ViewUtil.getTag(v,Integer.class);
                if(integer==null||!ClickUtil.canClick()){
                    return;
                }
                int headCount=getHeaderLayoutCount();
                int position=integer-headCount;
                T t=getItem(position);
                if(mOnItemChildClickListener2!=null){
                    mOnItemChildClickListener2.onItemClick(position,t,v);
                }
            }
        };
    }

    @Override
    public void setData(List<T> data) {
        setNewData(data);
        notifyDataSetChanged();
    }


    @Override
    public void appendData(List<T> data) {
        if(mData!=null){
            addData(data);
        }else{
            setData(data);
        }
    }

    @Override
    public void appendData(int index, List<T> data) {
        if(data!=null){
            addData(index,data);
        }else{
            setData(data);
        }
    }


    public T getLastData(){
        return ListUtil.haveData(mData) ?mData.get(mData.size()-1):null;
    }

    public int size(){
        return mData==null?0:mData.size();
    }

    @Override
    public List<T> getArray() {
        return mData;
    }

    @Override
    public RecyclerView.Adapter returnRecyclerAdapter() {
        return this;
    }

    @Override
    public void notifyReclyDataChange() {
        notifyDataSetChanged();
    }

    public void setOnItemChildClickListener2(OnItemChildClickListener2<T> onItemChildClickListener2) {
        mOnItemChildClickListener2 = onItemChildClickListener2;
    }

    public interface OnItemChildClickListener2<T>{
        public void onItemClick(int position,T t,View view);
    }
}