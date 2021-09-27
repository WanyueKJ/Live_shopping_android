package com.wanyue.shop.view.activty;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wanyue.common.Constants;
import com.wanyue.common.activity.BaseActivity;
import com.wanyue.common.adapter.base.BaseMutiRecyclerAdapter;
import com.wanyue.common.custom.ItemLinearLayout;
import com.wanyue.common.server.observer.DefaultObserver;
import com.wanyue.common.server.observer.DialogObserver;
import com.wanyue.common.upload.ImageUploader;
import com.wanyue.common.utils.ClickUtil;
import com.wanyue.common.utils.DebugUtil;
import com.wanyue.common.utils.DialogUitl;
import com.wanyue.common.utils.ListUtil;
import com.wanyue.common.utils.ProcessResultUtil;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.shop.R;
import com.wanyue.shop.adapter.RefundGoodsListAdaper;
import com.wanyue.shop.api.ShopAPI;
import com.wanyue.shop.bean.OrderBean;
import com.wanyue.shop.bean.RefundCommitBean;
import com.wanyue.shop.model.OrderModel;
import com.wanyue.shop.view.dialog.GalleryDialogFragment;
import com.wanyue.shop.view.widet.linear.PoolLinearListView;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class OrderRefundActivity extends BaseActivity implements View.OnClickListener {
    private static final int MAX_PHOTO_LENGTH=3;
    private TextView mTvRefundNum;
    private TextView mTvRefundMoney;
    private TextView mTvRefundReason;
    private EditText mTvRefundBeizhu;
    private Button mBtnCommit;
    private RecyclerView mRecyclerView;
    private PoolLinearListView mListView;
    private RefundGoodsListAdaper mRefundGoodsListAdaper;
    private SparseArray<String> mRefundReasonList;
    private ProcessResultUtil mProcessResultUtil;

    private RefundCommitBean mRefundCommitBean;
    private String mOrderId;
    private ImageUploader mImageUploader;
    private Disposable mDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void init() {
        setTabTitle("申请退款");
        mTvRefundNum = (TextView) findViewById(R.id.tv_refund_num);
        mTvRefundMoney = (TextView) findViewById(R.id.tv_refund_money);
        mTvRefundReason = (TextView) findViewById(R.id.tv_refund_reason);
        mTvRefundBeizhu = (EditText) findViewById(R.id.tv_refund_beizhu);
        mBtnCommit = (Button) findViewById(R.id.btn_commit);

        mListView =  findViewById(R.id.listView);
        mRecyclerView=findViewById(R.id.reclyView);
        findViewById(R.id.btn_refund_reason).setOnClickListener(this);


        mBtnCommit.setOnClickListener(this);

        mRefundCommitBean=new RefundCommitBean();
        mTvRefundBeizhu.addTextChangedListener(new ItemLinearLayout.TextAfterChanger() {
            @Override
            public void afterTextChanged(Editable s) {
                String text=s.toString();
                if(mRefundCommitBean!=null){
                   mRefundCommitBean.setRefund_reason_wap_explain(text);
                }
            }
        });
        mTvRefundReason.addTextChangedListener(new ItemLinearLayout.TextAfterChanger() {
            @Override
            public void afterTextChanged(Editable s) {
                String text=s.toString();
                if(mRefundCommitBean!=null){
                   mRefundCommitBean.setText(text);
                }
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        mProcessResultUtil = new ProcessResultUtil(this);
        String json=getIntent().getStringExtra(Constants.DATA);
        OrderBean orderBean=JSON.parseObject(json,OrderBean.class);
        mOrderId=orderBean.getOrderId();
        mRefundCommitBean.setUni(orderBean.getOrderId());
        setOrderData(orderBean);
    }


    private void setOrderData(OrderBean orderBean) {
        mRefundGoodsListAdaper=new RefundGoodsListAdaper(orderBean.getCartInfo());
        mListView.setAdapter(mRefundGoodsListAdaper);
        mTvRefundNum.setText(Integer.toString(orderBean.getTotalNum()));
        mTvRefundMoney.setText(StringUtil.getPrice(orderBean.getTotalPrice()));
    }

    public static void forward(Context context,OrderBean orderBean){
        if(orderBean==null){
            DebugUtil.sendException("OrderBean!=null");
            return;
        }
        Intent intent=new Intent(context,OrderRefundActivity.class);
        String json= JSON.toJSONString(orderBean);
        intent.putExtra(Constants.DATA,json);
        context.startActivity(intent);
    }



    /*先验证权限*/
    private void judgePermisson(Runnable runnable) {
        String[] permissonArray = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO};
        mProcessResultUtil.requestPermissions(permissonArray
                , runnable);
    }


    /*选择照片*/
    private void goToSelectPhoto() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_order_refund;
    }


    @Override
    public void onClick(View v) {
        if(!ClickUtil.canClick()){
            return;
        }
        int id=v.getId();
        if(id==R.id.btn_refund_reason){
            selectReason();
        }else if(id==R.id.btn_commit){
            commit();
        }
    }


    private void commit() {
        if(mRefundCommitBean==null|| TextUtils.isEmpty(mRefundCommitBean.getUni())){
            DebugUtil.sendException("mEvaluateCommitBean=="+mRefundCommitBean+"&&mEvaluateCommitBean.getUni()="+mRefundCommitBean.getUni());
            return;
        }
        String check=mRefundCommitBean.check();
        if(!TextUtils.isEmpty(check)){
            ToastUtil.show(check);
            return;
        }
        getRequest().subscribe(new DialogObserver<Boolean>(this) {
            @Override
            public void onNextTo(Boolean aBoolean) {
                if(aBoolean){
                    OrderModel.sendOrderChangeEvent(mOrderId);
                    finish();
                }
            }
        });
    }


    private void upLoadImage(List<String> commitList) {
        if(mImageUploader==null){
            mImageUploader=new ImageUploader(this);
        }
        final Dialog dialog=DialogUitl.loadingDialog(this);
        dialog.show();
        Observable<String>observable= mImageUploader.uploadFileArraycompress(commitList);
        mDisposable =mImageUploader.collect(observable).subscribe(new Consumer<StringBuilder>() {
            @Override
            public void accept(StringBuilder stringBuilder) throws Exception {
                DialogUitl.dismissDialog(dialog);
                if(stringBuilder.length()>0){
                   stringBuilder.deleteCharAt(stringBuilder.length()-1);
                }
                if(mRefundCommitBean!=null){
                    mRefundCommitBean.setRefund_reason_wap_img(stringBuilder.toString());
                }
                getRequest().subscribe(new DefaultObserver<Boolean>() {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        DialogUitl.dismissDialog(dialog);
                    }
                    @Override
                    public void onComplete() {
                        super.onComplete();
                        DialogUitl.dismissDialog(dialog);
                    }
                    @Override
                    public void onNext(Boolean aBoolean) {
                        if(aBoolean){
                            finish();
                            OrderModel.sendOrderChangeEvent(mOrderId);
                        }
                    }
                });
            }
        });
    }

    /*提交退款*/
    private Observable<Boolean> getRequest() {
        return ShopAPI.refundOrder(mRefundCommitBean);
    }

    /*选择理由*/
    private void selectReason() {
        if(mRefundReasonList!=null&&mRefundReasonList.size()>0){
            openRefundDialog();
        }else{
            ShopAPI.refundReason().compose(this.<List<String>>bindUntilOnDestoryEvent()).subscribe(new DialogObserver<List<String>>(this) {
                @Override
                public void onNextTo(List<String> list) {
                    int size=list.size();
                    mRefundReasonList=new SparseArray<>();
                    for(int i=0;i<size;i++){
                        mRefundReasonList.put(i,list.get(i));
                    }
                    openRefundDialog();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }




    private void openRefundDialog() {
        DialogUitl.showStringArrayDialog(this ,mRefundReasonList, new DialogUitl.StringArrayDialogCallback() {
            @Override
            public void onItemClick(String text, int tag) {
                if(mTvRefundReason!=null){
                    mTvRefundReason.setText(text);
                }
            }
        });
    }





}
