package com.wanyue.common.proxy;
import java.util.LinkedList;
import io.reactivex.annotations.Nullable;

public class ViewProxyStack  {
    private LinkedList<BaseViewProxy> mProxyLinkedList;

    public ViewProxyStack() {
    }

    public boolean isContain(BaseViewProxy baseViewProxy){
        return mProxyLinkedList!=null&&mProxyLinkedList.contains(baseViewProxy);
    }
    public int popStack(@Nullable BaseViewProxy baseViewProxy){
        if(mProxyLinkedList==null){
           return 0;
        }
      if(!isContain(baseViewProxy)){
          return mProxyLinkedList.size();
      }
        mProxyLinkedList.remove(baseViewProxy);

      int size=mProxyLinkedList.size();
      if(size==0){
         return size;
      }
        BaseViewProxy mProxyLinkedListLast=mProxyLinkedList.getLast();
        if(mProxyLinkedListLast!=null){
           mProxyLinkedListLast.onAddAtParent();
        }
        return mProxyLinkedList.size();
    }

    public void addStack(@Nullable BaseViewProxy viewProxy){
        if(mProxyLinkedList==null){
           mProxyLinkedList= new LinkedList<BaseViewProxy>();
        }
         if(mProxyLinkedList.size()>0){
            mProxyLinkedList.getLast().onRemoveAtParent();
          }
            mProxyLinkedList.add(viewProxy);
    }

}
