package com.wanyue.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.trello.rxlifecycle2.components.support.RxDialogFragment;
import com.wanyue.common.utils.ClickUtil;
import com.wanyue.common.utils.L;

import java.lang.ref.WeakReference;

/**
 * Created by  on 2018/9/29.
 */

public abstract class AbsDialogFragment extends RxDialogFragment {
    private LifeCycleListener mLifeCycleListener;
    protected Context mContext;
    protected View mRootView;
    private DissMissLisnter mDissMissLisnter;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mContext = new WeakReference<>(getActivity()).get();
        mRootView = LayoutInflater.from(mContext).inflate(getLayoutId(), null);
        Dialog dialog =null;
        if(isNeedDialogListner()){
            dialog= new Dialog(mContext, getDialogStyle());
        }else{
            dialog= createNoListnerDialog();
        }

        Window window=dialog.getWindow();
        dialog.setContentView(mRootView);
        dialog.setCancelable(canCancel());
        dialog.setCanceledOnTouchOutside(canCancel());
        setWindowAttributes(window);

      /* */
        return dialog;
    }

    private Dialog createNoListnerDialog() {
        return new Dialog(mContext,getDialogStyle()){
            @Override
            public void setOnDismissListener(@Nullable OnDismissListener listener) {
            }
            @Override
            public void setOnCancelListener(@Nullable OnCancelListener listener) {
            }
            @Override
            public void setOnShowListener(@Nullable OnShowListener listener) {
            }
        };
    }

    public boolean isNeedDialogListner(){
        return true;
  }

    public void init(){

    }


    public void show(FragmentManager manager) {
         super.show(manager,getName());
    }

    public String getName(){
        return this.getClass().getName();
    }

    protected abstract int getLayoutId();


    protected abstract  int getDialogStyle();

    protected abstract boolean canCancel();

    protected abstract void setWindowAttributes(Window window);

    protected <T extends View>T findViewById(int id) {
        if (mRootView != null) {
            return mRootView.findViewById(id);
        }
        return null;
    }
    public void initData() {
    }

    public void setDissMissLisnter(DissMissLisnter dissMissLisnter) {
        mDissMissLisnter = dissMissLisnter;
    }

    public void setOnClickListener(int id, View.OnClickListener clickListener){
        View view=findViewById(id);
        if(view!=null){
            view.setOnClickListener(clickListener);
        }
    }
    protected boolean canClick() {
        return ClickUtil.canClick();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mLifeCycleListener != null) {
            mLifeCycleListener.onDialogFragmentShow(this);
        }
        init();
       /* if (this.getShowsDialog()) {
            View view = this.getView();
            if (view != null) {
                if (view.getParent() != null) {
                    throw new IllegalStateException("DialogFragment can not be attached to a container view");
                }

                this.getDialog().setContentView(view);
            }

            Activity activity = this.getActivity();
            if (activity != null) {
                this.getDialog().setOwnerActivity(activity);
            }
            this.getDialog().setCancelable(this.isCancelable());
            this.getDialog().setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {

                }
            });
            this.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {

                }
            });
            if (savedInstanceState != null) {
                Bundle dialogState = savedInstanceState.getBundle("android:savedDialogState");
                if (dialogState != null) {
                    this.getDialog().onRestoreInstanceState(dialogState);
                }
            }*/

        //}
    }





    @Override
    public void onDismiss(DialogInterface dialog) {
        if(mDissMissLisnter!=null){
            mDissMissLisnter.close();
            mDissMissLisnter=null;
        }
        super.onDismiss(dialog);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext=null;
        if (mLifeCycleListener != null) {
            mLifeCycleListener.onDialogFragmentHide(this);
        }
        mLifeCycleListener = null;
        L.e("dialog销毁了=="+this.getName());
    }




    public void setLifeCycleListener(LifeCycleListener lifeCycleListener) {
        mLifeCycleListener = lifeCycleListener;
    }

    public interface LifeCycleListener {
        void onDialogFragmentShow(AbsDialogFragment fragment);
        void onDialogFragmentHide(AbsDialogFragment fragment);
    }



    public static interface  DissMissLisnter{
        public void close();
    }
}
