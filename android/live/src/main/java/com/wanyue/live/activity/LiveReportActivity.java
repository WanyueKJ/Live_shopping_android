package com.wanyue.live.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.wanyue.common.activity.BaseActivity;
import com.wanyue.common.adapter.radio.CheckEntity;
import com.wanyue.common.adapter.radio.RadioAdapter;
import com.wanyue.common.custom.refresh.RxRefreshView;
import com.wanyue.common.server.observer.DefaultObserver;
import com.wanyue.common.server.observer.DialogObserver;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.live.R;
import com.wanyue.live.http.LiveHttpUtil;
import java.util.List;
import static android.os.Build.ID;

public class LiveReportActivity extends BaseActivity implements View.OnClickListener {
    private RecyclerView mReclyView;
    private EditText mTvDes;
    private Button mBtnConfirm;
    private String mUid;
    private RadioAdapter<CheckEntity> mRatioAdapter;


    @Override
    public void init() {
        mReclyView = (RecyclerView) findViewById(R.id.reclyView);
        mTvDes = (EditText) findViewById(R.id.tv_des);
        mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
        setTabTitle(getString(R.string.report));
        mUid=getIntent().getStringExtra(ID);
        if(TextUtils.isEmpty(mUid)){
            finish();
            return;
        }

        mReclyView = (RecyclerView) findViewById(R.id.reclyView);
        mTvDes = findViewById(R.id.tv_des);
        findViewById(R.id.btn_confirm).setOnClickListener(this);
        RxRefreshView.ReclyViewSetting.createLinearSetting(this,1).settingRecyclerView(mReclyView);
        mRatioAdapter=new RadioAdapter<CheckEntity>(null){
            @Override
            public int getLayoutId() {
                return R.layout.item_relcy_live_report;
            }
        };
        mReclyView.setAdapter(mRatioAdapter);
        getData();
    }



    private void getData() {
        LiveHttpUtil.getUserReportList().compose(this.<List<CheckEntity>>bindToLifecycle()).subscribe(new DefaultObserver<List<CheckEntity>>() {
            @Override
            public void onNext(List<CheckEntity> data) {
                mRatioAdapter.setData(data);
            }
        });
    }







    @Override
    public int getLayoutId() {
        return R.layout.activity_live_report;
    }

    public static void forward(Context context, String uid){
        Intent intent=new Intent(context, LiveReportActivity.class);
        intent.putExtra(ID,uid);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if(id== R.id.btn_confirm){
            commit();
        }
    }


    private void commit() {
        if(mRatioAdapter==null||TextUtils.isEmpty(mRatioAdapter.getId())){
            ToastUtil.show(R.string.please_sel_report_reason);
            return;
        }
        String des=mTvDes.getText().toString();
        String content=mRatioAdapter.getSelectData().getContent();
        if(!TextUtils.isEmpty(des)){
            content=content+"\t"+des;
        }
        LiveHttpUtil.setUserReport(mUid,content).compose(this.<Boolean>bindToLifecycle()).subscribe(new DialogObserver<Boolean>(this) {
            @Override
            public void onNextTo(Boolean aBoolean) {
                if(aBoolean){
                    finish();
                }
            }
        });
    }
}
