package com.wanyue.shop.view.widet;

import android.content.Context;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;

import com.wanyue.common.adapter.base.BaseMutiRecyclerAdapter;
import com.wanyue.common.adapter.base.BaseReclyViewHolder;
import com.wanyue.common.utils.ClickUtil;
import com.wanyue.common.utils.ViewUtil;
import com.wanyue.shop.view.activty.OrderDeatailActivity;
import com.wanyue.shop.view.widet.linear.IListView;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public abstract class ViewGroupLayoutBaseAdapter<T> {
    private List<T> mData;
    private Context context;
    private IListView mViewGroup;
    private OnItemClickListener<T> onItemClickListener;
    private  boolean mIsSingleItemtype;
    private ArrayMap<View,BaseReclyViewHolder>mViewHolderMap;
    private OnItemChildClickListener<T> mOnItemChildClickListener;

    protected View.OnClickListener mOnChildClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(!ClickUtil.canClick()){
                return;
            }
            Integer position= ViewUtil.getTag(v,Integer.class);
            if(position==null){
               return;
            }
            T t=getItem(position);
            if(mOnItemChildClickListener!=null){
               mOnItemChildClickListener.onItemClick(position,t,v);
            }
        }
    };

    public ViewGroupLayoutBaseAdapter(List<T> list) {
        this.mData = list;
    }
    public void attach(IListView viewGroup) {
        mViewGroup = viewGroup;
        this.context = mViewGroup.getContext();
    }

    public LayoutInflater getLayoutInflater() {
        if (context != null) {
            return LayoutInflater.from(context);
        }
        return null;
    }

    public int getCount() {
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }

    public T getItem(int position) {
        if (mData != null) {
            return mData.get(position);
        }
        return null;
    }

    public abstract void convert(BaseReclyViewHolder helper, T item);

    /*布局layout*/
    protected abstract int getLayoutId(T t);

    public View getView(final int position) {
        final T t = getItem(position);
        View view = getLayoutInflater().inflate(getLayoutId(t),mViewGroup.getGroup(), false);
        bindView(view,position,t);
        return view;
    }
    public void bindView(View view,int position,T t) {
        if(mViewHolderMap==null){
           mViewHolderMap=new ArrayMap<>();
        }
        view.setTag(position);
        if(onItemClickListener!=null){
           view.setOnClickListener(mOnClickListener);
        }

        BaseReclyViewHolder baseReclyViewHolder =mViewHolderMap.get(view);
        if( baseReclyViewHolder==null){
            baseReclyViewHolder=new BaseReclyViewHolder(view);
            mViewHolderMap.put(view,baseReclyViewHolder);
        }
        baseReclyViewHolder.setObjectPosition(position);
        convert(baseReclyViewHolder,t);
    }

    public BaseReclyViewHolder getAndRemoveViewHolder(@NotNull View view) {
        if(mViewHolderMap==null||mViewHolderMap.size()<=0){
            return null;
        }
        BaseReclyViewHolder baseReclyViewHolder=mViewHolderMap.get(view);
        if(baseReclyViewHolder!=null){
           mViewHolderMap.remove(baseReclyViewHolder);
        }
        return baseReclyViewHolder;
    }

    public void putViewHolder(View view,BaseReclyViewHolder baseReclyViewHolder){
        if(mViewHolderMap==null){
           mViewHolderMap=new ArrayMap<>();
        }
        mViewHolderMap.put(view,baseReclyViewHolder);
    }


    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private View.OnClickListener mOnClickListener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Integer position= (Integer) v.getTag();
            if (onItemClickListener != null&&position!=null) {
                onItemClickListener.onItemClicked(ViewGroupLayoutBaseAdapter.this,v, getItem(position), position);
            }
        }
    };

    public void setData(List<T> data) {
        mData = data;
        ViewGroupLayoutBaseAdapter adapter = mViewGroup.getAdapter();
        if (adapter != null && adapter == this) {
            mViewGroup.bindView();
        }
    }

    public void notifyDataChanged(){
        ViewGroupLayoutBaseAdapter adapter = mViewGroup.getAdapter();
        if (adapter != null && adapter == this) {
            mViewGroup.bindView();
        }
    }

    public void setOnItemChildClickListener(OnItemChildClickListener<T> onItemChildClickListener) {
        mOnItemChildClickListener = onItemChildClickListener;
    }

    public void setSingleItemtype(boolean singleItemtype) {
        mIsSingleItemtype = singleItemtype;
    }


    public List<T> getData() {
        return mData;
    }

    public boolean isSingleItemtype() {
        return mIsSingleItemtype;
    }

    /**
     * 回调接口
     */



    public interface OnItemChildClickListener<T>{
        public void onItemClick(int position,T t,View view);
    }

    public interface OnItemClickListener<T> {
        public void onItemClicked(ViewGroupLayoutBaseAdapter<T> adapter,View v, T item, int position);
    }
}