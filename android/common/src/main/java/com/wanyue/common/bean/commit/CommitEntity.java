package com.wanyue.common.bean.commit;


import android.text.TextUtils;

import com.wanyue.common.bean.DataListner;

public abstract class CommitEntity{
  private DataListner dataListner;
  public static final String DEFAUlT_VALUE="0";


  public void observer(){
      if(dataListner!=null){
         dataListner.compelete(observerCondition());
      }
  }

  public boolean fieldNotEmpty(Object object ){
      if(object instanceof String){
          String str= (String) object;
          return !TextUtils.isEmpty(str);
      }else{
          return object!=null;
      }
  }

    public boolean fieldNotEmptyAndNoZero(Object object){
        if(object==null){
          return false;
        }
        else if(object instanceof String){
            String str= (String) object;
            return !TextUtils.isEmpty(str)&&!str.equals(CommitEntity.DEFAUlT_VALUE);
        }else if(object instanceof Integer){
            Integer integer= (Integer) object;
            return integer!=0;
        }
        else{
            return object!=null;
        }
    }

    public void release(){
        dataListner=null;
    }

  public abstract boolean observerCondition();
    public void setDataListner(DataListner dataListner) {
        this.dataListner = dataListner;
    }
}
