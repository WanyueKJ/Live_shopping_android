package com.wanyue.live.model;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import com.wanyue.live.bean.LiveBean;

public class LiveModel extends ViewModel {
    private LiveBean mLiveBean;
    private String mLiveUid;
    private String mStream;
    private int mLikeNum;
    private boolean mIsShutUp;

    public LiveBean getLiveBean() {
        return mLiveBean;
    }

    public void setLiveBean(LiveBean liveBean) {
        mLiveBean = liveBean;
        if(mLiveBean!=null){
           mLiveUid=mLiveBean.getUid();
        }
    }

    public boolean isShutUp() {
        return mIsShutUp;
    }

    public void setShutUp(boolean shutUp) {
        mIsShutUp = shutUp;
    }

    public String getLiveUid() {
        return mLiveUid;
    }

    public void setLiveUid(String liveUid) {
        mLiveUid = liveUid;
    }

    public void setStream(String stream) {
        mStream = stream;
    }

    public String getStream() {
        return mStream;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mLiveBean=null;
        mLiveUid=null;
    }



    public static boolean getContextShutUp(FragmentActivity appCompatActivity){
        if(appCompatActivity==null){
            return false;
        }
        LiveModel liveModel= ViewModelProviders.of(appCompatActivity).get(LiveModel.class);
        if(liveModel!=null){
            return liveModel.isShutUp();
        }
        return false;
    }


    public static void setShutUpContext(FragmentActivity appCompatActivity,boolean isShutUp){
        if(appCompatActivity==null){
            return ;
        }
        LiveModel liveModel= ViewModelProviders.of(appCompatActivity).get(LiveModel.class);
        if(liveModel!=null){
            liveModel.setShutUp(isShutUp);
        }
    }





   public static LiveBean getContextLiveBean(FragmentActivity appCompatActivity){
       if(appCompatActivity==null){
           return null;
       }
       LiveModel liveModel= ViewModelProviders.of(appCompatActivity).get(LiveModel.class);
       if(liveModel!=null){
           return liveModel.getLiveBean();
       }
       return null;
   }

    public static String getContextLiveUid(FragmentActivity appCompatActivity){
        if(appCompatActivity==null){
            return null;
        }
        LiveModel liveModel= ViewModelProviders.of(appCompatActivity).get(LiveModel.class);
        if(liveModel!=null){
            return liveModel.getLiveUid();
        }
        return null;
    }
    public static String getContextStream(FragmentActivity appCompatActivity){
        if(appCompatActivity==null){
            return null;
        }
        LiveModel liveModel= ViewModelProviders.of(appCompatActivity).get(LiveModel.class);
        if(liveModel!=null){
            return liveModel.getStream();
        }
        return null;
    }

}
