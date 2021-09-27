package com.wanyue.common.business.acmannger;

import android.app.Activity;
import android.content.Intent;

import com.wanyue.common.utils.DebugUtil;
import com.wanyue.common.utils.ListUtil;

import java.util.Iterator;
import java.util.LinkedHashSet;

public class ActivityMannger {
 private static ActivityMannger mActivityMannger;
 /*使用该数据结构是因为要去重*/
 private LinkedHashSet<Activity>mActivityLinkedHashSet;
    /*存储其他栈的activity*/
 private LinkedHashSet<Activity> mOntherStack;
 private Activity mBaseActivity;
 private Activity mTopMainStackActivity;
 private boolean isBackGround;
 private OnLaunchListner mOnLaunchListner;

 private Activity mTargetActivty;

 private ActivityMannger(){
     mActivityLinkedHashSet=new LinkedHashSet<>();
     mOntherStack=new LinkedHashSet<>();
 }

 public static ActivityMannger getInstance(){
        if(mActivityMannger==null){
            synchronized (ActivityMannger.class){
                mActivityMannger=new ActivityMannger();
            }
        }
        return mActivityMannger;
 }


 public void addActivityByNewStack(Activity activity){
     if(activity==null){
         return;
     }
     if(mOntherStack==null){
        mOntherStack=new LinkedHashSet<>();
     }
        mOntherStack.add(activity);
        addActivity(activity);
 }


    public Activity getBaseActivity() {
        return mBaseActivity;
    }

    /*从set集合取出最后的一个数据*/
    private Activity getLastActivity(LinkedHashSet<Activity> linkedHashSet) {
       Activity activity=null;
     if(linkedHashSet!=null){
           for(Activity temp:linkedHashSet){
               activity=temp;
           }
       }
     return activity;
    }


    public boolean  checkStackOpenCondition(){
        if(mOnLaunchListner!=null){
           return mOnLaunchListner.launchFromBackGround();
        }
        return false;
    }

    public void launchOntherStackToTopActivity(boolean shouldLimitBackGround,Activity activity){
        if((shouldLimitBackGround&&!isBackGround)||!checkStackOpenCondition()){
            return;
        }
        Activity lastActivity=getLastActivity(mOntherStack);
        if(lastActivity!=null&&lastActivity!=activity){
           startActivity(lastActivity,Intent.FLAG_ACTIVITY_NEW_TASK);
       }
  }

  public void finishStack(Activity stackActivity){
      if(!removeActiviy(mOntherStack,stackActivity)){
          return;
      }
      Activity lastStackActivity=getLastActivity(mActivityLinkedHashSet);
      if(lastStackActivity==null){
          return;
      }
      //startActivity(lastStackActivity,Intent.FLAG_ACTIVITY_SINGLE_TOP);
  }




    private boolean removeActiviy(LinkedHashSet<Activity> set, Activity activity) {
     if(set!=null&&activity!=null&&set.contains(activity)){
         return set.remove(activity);
     }
     return false;
    }

    public void startActivity(Activity activity,int...flagArray){
     if(mBaseActivity==null){
         return;
     }
      Intent intent=new Intent(mBaseActivity,activity.getClass());
      if(flagArray!=null){
          for(int flag:flagArray){
              intent.addFlags(flag);
          }
      }
      mBaseActivity.startActivity(intent);
  }





    public void addActivity(Activity activity){
        if(mActivityLinkedHashSet==null){
            mActivityLinkedHashSet=new LinkedHashSet<>();
        }
        if(activity==null){
          return;
        }
        mActivityLinkedHashSet.add(activity);
        if(!mOntherStack.contains(activity)){
           mTopMainStackActivity=activity;
        }
    }

    public void setBackGround(boolean backGround) {
        isBackGround = backGround;
    }

    public void removeActivity(Activity activity){
        if(mTopMainStackActivity!=null&&activity!=null&&mTopMainStackActivity==activity){
           mTopMainStackActivity=null;
        }
        removeActiviy(mActivityLinkedHashSet,activity);

        finishStack(activity);
  }

  public void setBaseActivity(Activity baseActivity) {
      mBaseActivity = baseActivity;
  }


    public Activity getTargetActivty() {
        return mTargetActivty;
    }

    public void setTargetActivty(Activity targetActivty) {
        mTargetActivty = targetActivty;
    }

    public void releaseBaseActivity(Activity baseActivity) {
        if(baseActivity!=null&&mBaseActivity!=null&&baseActivity==mBaseActivity){
           mBaseActivity=null;
        }
    }

    public boolean isBackGround() {
        return isBackGround;
    }

   public Activity getMainStackTopActivity(){
     return mTopMainStackActivity;
   }

    public void setOnLaunchListner(OnLaunchListner onLaunchListner) {
        mOnLaunchListner = onLaunchListner;

    }

    /*是否是基类*/
    public boolean isBaseActivity(Activity activity) {
        if(activity!=null&&mBaseActivity!=null&&activity==mBaseActivity){
            return true;
        }
        return false;
    }

    public Activity getFirstClassTypeActivieActivity(Class<? extends Activity>cs){
        if(!ListUtil.haveData(mActivityLinkedHashSet)){
            return null;
        }
        for(Activity activity:mActivityLinkedHashSet){
            if(activity.getClass()==cs&&!activity.isFinishing()){
               return activity;
            }
        }
        return null;
    }


    public void clearAllActivity(){
        if(mActivityLinkedHashSet!=null){
            for(Activity activity:mActivityLinkedHashSet)
            {
                if(activity!=null&&!activity.isFinishing()){
                    activity.finish();
                }
            }
        }
    }




    public void removeTopActivity(LinkedHashSet<Activity>set,Activity activity) {
        if(set==null||activity==null){
            DebugUtil.sendException("移除失败==set=="+set+"&&activity=="+activity);
            return;
        }
        Iterator<Activity> iterator=set.iterator();
        boolean needRemove=false;
        while (iterator.hasNext()){
            Activity temp=iterator.next();
            if(needRemove){
               temp.finish();
            }
            if(temp==activity){
                needRemove=true;
            }

        }
    }

    public LinkedHashSet<Activity> getActivityLinkedHashSet() {
        return mActivityLinkedHashSet;
    }

    public static interface OnLaunchListner{
        public boolean launchFromBackGround();
    }
}
