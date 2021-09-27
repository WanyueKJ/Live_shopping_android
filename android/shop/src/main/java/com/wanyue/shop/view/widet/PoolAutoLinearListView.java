package com.wanyue.shop.view.widet;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.wanyue.common.adapter.base.BaseReclyViewHolder;
import com.wanyue.common.utils.DebugUtil;
import com.wanyue.shop.view.widet.ViewGroupLayoutBaseAdapter;
import com.wanyue.shop.view.widet.linear.IListView;
import com.wanyue.shop.view.widet.linear.ListPool;
import com.wanyue.shop.view.widet.linear.MyAutoLineFeedLinearLayout;

public class PoolAutoLinearListView extends MyAutoLineFeedLinearLayout implements IListView {
    private ViewGroupLayoutBaseAdapter adapter;
    private int itemPadding;
    private ListPool mListPool;

    public PoolAutoLinearListView(Context context) {
        super(context);
    }

    public PoolAutoLinearListView(Context context, int horizontalSpacing, int verticalSpacing) {
        super(context, horizontalSpacing, verticalSpacing);
    }

    public PoolAutoLinearListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void setAdapter(ViewGroupLayoutBaseAdapter adapter) {
        this.adapter = adapter;
        if(adapter!= null){
            adapter.attach(this);
        }
        // setAdapter 时添加 view
        bindView();
    }

    @Override
    public ViewGroup getGroup() {
        return this;
    }

    @Override
    public ViewGroupLayoutBaseAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void bindView() {
        if (adapter == null) {
            return;
        }
        if(mListPool==null){
            noPoolSet();
        }else{
            havePoolSet();
        }
    }

    private void havePoolSet() {
      int adapterCount=adapter.getCount();
      int viewChildCount=getChildCount();
      int offectSize=adapterCount-viewChildCount;
      if(adapterCount==0&&viewChildCount==0){
          return;
      }
      if(viewChildCount>adapterCount){
      for(int index=viewChildCount-1;index<=adapterCount-1;index--){
          View view= getChildAt(index);
          BaseReclyViewHolder baseReclyViewHolder=adapter.getAndRemoveViewHolder(view);
          if(baseReclyViewHolder==null){
             DebugUtil.sendException("BaseReclyViewHolder not Null");
             mListPool.removeToParent(view);
          }else{
              mListPool.put(view,baseReclyViewHolder);
          }
        }
      } else if(adapterCount>viewChildCount){
          for(int i=0;i<offectSize;i++){
           BaseReclyViewHolder baseReclyViewHolder=mListPool.getCacheHolder();
           if(baseReclyViewHolder==null||baseReclyViewHolder.convertView==null){
               View v = adapter.getView(i);
               addViewSetPadding(v);
           }else{
               View view=baseReclyViewHolder.convertView;
               addView(view);
               adapter.putViewHolder(view,baseReclyViewHolder);
           }
          }
      }
        for(int i = 0; i < adapterCount; i++){
            View v = getChildAt(i);
            adapter.bindView(v,i,adapter.getItem(i));
        }
    }

    private void noPoolSet() {
        int adapterCount=adapter.getCount();
        removeAllViews();
        for (int i = 0; i < adapterCount; i++) {
            final View v = adapter.getView(i);
            addViewSetPadding(v);
            adapter.bindView(v,i,adapter.getItem(i));
        }
    }

    public void setListPool(ListPool listPool) {
        mListPool = listPool;
    }
    private void addViewSetPadding(View v) {
        addView(v);
        LinearLayout.LayoutParams layoutParams= (LayoutParams) v.getLayoutParams();
        int orientation=getOrientation();
        if(orientation==VERTICAL){
           layoutParams.bottomMargin=layoutParams.bottomMargin+itemPadding;
        }else if(orientation==HORIZONTAL){
           layoutParams.rightMargin=layoutParams.rightMargin+itemPadding;
        }
    }
}
