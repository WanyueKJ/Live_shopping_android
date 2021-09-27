package com.wanyue.live.business.floatwindow;

import android.content.Intent;
import android.net.Uri;

import androidx.fragment.app.Fragment;

import io.reactivex.ObservableEmitter;

import static android.app.Activity.RESULT_OK;

public class SysPermisssonFragment extends Fragment {
    public static final int ACTION_MANAGE_SYS_PERMISSION=10;
    private ObservableEmitter observableEmitter;
    public void startAciton(String action,ObservableEmitter<Boolean> e){
        observableEmitter=e;
        Intent intent = new Intent(action,
                Uri.parse("package:" + getContext().getPackageName()));
        startActivity(intent);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ACTION_MANAGE_SYS_PERMISSION){
            if(observableEmitter!=null){
               observableEmitter.onNext(resultCode==RESULT_OK);
            }
        }
    }
}
