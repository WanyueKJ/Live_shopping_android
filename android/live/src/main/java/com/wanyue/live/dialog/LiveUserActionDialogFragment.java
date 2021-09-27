package com.wanyue.live.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wanyue.common.Constants;
import com.wanyue.common.bean.UserBean;
import com.wanyue.common.dialog.AbsDialogFragment;
import com.wanyue.common.http.HttpCallback;
import com.wanyue.common.interfaces.CommonCallback;
import com.wanyue.common.interfaces.OnItemClickListener;
import com.wanyue.common.server.observer.DefaultObserver;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.common.utils.WordUtil;
import com.wanyue.live.R;
import com.wanyue.live.activity.LiveActivity;
import com.wanyue.live.adapter.LiveUserActionAdapter;
import com.wanyue.live.bean.LiveUserActionBean;
import com.wanyue.live.http.LiveHttpUtil;

import java.util.List;

public class LiveUserActionDialogFragment extends AbsDialogFragment {
    private TextView title;
    private RecyclerView recyclerView;
     interface OnActionClickListener{
         void onItemClick(DialogFragment dialogFragment,LiveUserActionBean bean);
     };
    private OnActionClickListener mCallBack;
    @Override
    protected int getLayoutId() {
        return R.layout.dialog_chat_action;
    }

    @Override
    protected int getDialogStyle() {
        return R.style.dialog;
    }

    @Override
    protected boolean canCancel() {
        return false;
    }

    @Override
    protected void setWindowAttributes(Window window) {
        window.setWindowAnimations(R.style.bottomToTopAnim);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        title = findViewById(R.id.title);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        title.setText(WordUtil.getString(R.string.chat_shutup_time));
        loadData();
    }

    private void loadData(){
        LiveHttpUtil.getShutList().compose(this.<List<LiveUserActionBean>>bindToLifecycle()).subscribe(new DefaultObserver<List<LiveUserActionBean>>() {
            @Override
            public void onNext(List<LiveUserActionBean> liveUserActionBeans) {
                loadView(liveUserActionBeans);
            }
        });
    }

    private void loadView(List<LiveUserActionBean> list) {
        if (list != null) {
            LiveUserActionAdapter chatActionAdapter = new LiveUserActionAdapter(list);
            chatActionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    action((LiveUserActionBean) adapter.getData().get(position));
                }
            });
            recyclerView.setAdapter(chatActionAdapter);
        }

    }

    private void action(LiveUserActionBean bean) {
        if (bean == null) {
            return;
        }
        if (mCallBack!=null){
            mCallBack.onItemClick(this,bean);
        }
    }

    public void setCallBack(OnActionClickListener callBack) {
        mCallBack = callBack;
    }

    @Override
    public void onDestroy() {
        recyclerView=null;
        super.onDestroy();
    }
}
