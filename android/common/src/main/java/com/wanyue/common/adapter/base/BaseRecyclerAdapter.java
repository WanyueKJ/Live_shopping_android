package com.wanyue.common.adapter.base;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.ViewGroup;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wanyue.common.custom.refresh.RxRefreshView;
import com.wanyue.common.utils.ClickUtil;
import com.wanyue.common.utils.ListUtil;
import com.wanyue.common.utils.ViewUtil;

import org.jetbrains.annotations.NotNull;
import java.util.List;

public abstract class BaseRecyclerAdapter<T,E extends BaseReclyViewHolder> extends BaseQuickAdapter<T,E> implements RxRefreshView.DataAdapter<T> {
    public Context context;
    private DataChangeListner<T> dataChangeListner;
    protected View.OnClickListener mOnClickListener;
    protected BaseMutiRecyclerAdapter.OnItemChildClickListener2<T> mOnItemChildClickListener2;

    public BaseRecyclerAdapter(List<T> data) {
        super(data);
        setLayoutId();
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

    @NotNull
    @Override
    public E onCreateViewHolder(ViewGroup parent, int viewType) {
        E e=super.onCreateViewHolder(parent, viewType);
        bindContext(parent.getContext());
        return e;
    }
    public void bindContext(Context context) {
    this.context=context;
    }



    @Override
    public void setData(List<T> data) {
        if(dataChangeListner!=null){
            dataChangeListner.change(data);
        }
        mData = data;
        notifyDataSetChanged();
    }
    public void setLayoutId() {
        mLayoutResId = getLayoutId();
    }

    public abstract int getLayoutId();

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
        if(mData!=null){
            addData(index,data);
        }else{
            setData(data);
        }
    }

    public T getLastData(){
       return ListUtil.haveData(mData) ?mData.get(mData.size()-1):null;
    }

    @Override
    public int getItemCount() {
        return mData==null?0:super.getItemCount();
    }

    public int size(){
        return mData==null?0:mData.size();
    }
    @Override
    public void notifyReclyDataChange() {
        notifyDataSetChanged();
    }
    @Override
    public List<T> getArray() {
        return mData;
    }


    @Override
    public RecyclerView.Adapter returnRecyclerAdapter() {
        return this;
    }

    /*监听数据源的内存地址变化*/
    public void setDataChangeListner(DataChangeListner<T> dataChangeListner) {
        this.dataChangeListner = dataChangeListner;
    }

    public void setOnItemChildClickListener2(BaseMutiRecyclerAdapter.OnItemChildClickListener2<T> onItemChildClickListener2) {
        mOnItemChildClickListener2 = onItemChildClickListener2;
    }

    public interface DataChangeListner<T>{
        public void change(List<T>t);
    }
}
