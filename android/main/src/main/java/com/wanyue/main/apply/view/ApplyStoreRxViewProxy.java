package com.wanyue.main.apply.view;

import android.app.Dialog;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.wanyue.common.Constants;
import com.wanyue.common.glide.ImgLoader;
import com.wanyue.common.interfaces.ImageResultCallback;
import com.wanyue.common.proxy.RxViewProxy;
import com.wanyue.common.server.observer.DefaultObserver;
import com.wanyue.common.upload.FileBundle;
import com.wanyue.common.upload.ImageUploader;
import com.wanyue.common.utils.ClickUtil;
import com.wanyue.common.utils.DialogUitl;
import com.wanyue.common.utils.ProcessImageUtil;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.main.R;
import com.wanyue.main.R2;
import com.wanyue.main.api.MainAPI;
import com.wanyue.main.apply.bean.ApplyStoreBean;
import com.wanyue.main.bean.ApplyAnthorInfo;

import java.io.File;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class ApplyStoreRxViewProxy extends AbsApplyStoreViewProxy {

    @BindView(R2.id.et_name)
    EditText mEtName;
    @BindView(R2.id.et_phone)
    EditText mEtPhone;
    @BindView(R2.id.et_user_code)
    EditText mEtUserCode;
    @BindView(R2.id.img_code_before)
    ImageView mImgCodeBefore;
    @BindView(R2.id.img_code_backgound)
    ImageView mImgCodeBackgound;
    @BindView(R2.id.img_code_hand)
    ImageView mImgCodeHand;
    @BindView(R2.id.img_business_license)
    ImageView mImgBusinessLicense;
    @BindView(R2.id.img_licence)
    ImageView mImgLicence;
    @BindView(R2.id.img_onther_document)
    ImageView mImgOntherDocument;
    @BindView(R2.id.btn_commit)
    Button mBtnCommit;

    ApplyStoreBean mApplyStoreBean;
    private ProcessImageUtil mImageUtil;
    private ImageView mSelectImage;

    private ImageUploader mImageUploader;
    private Disposable mDisposable;

    @Override
    public int getLayoutId() {
        return R.layout.view_apply_store;
    }

    @OnTextChanged(value = R2.id.et_phone, callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void watchPhoneTextChange(CharSequence sequence, int start, int before, int count) {
        initCommitData();
        String phoneString = sequence.toString();
        if (mApplyStoreBean != null) {
            mApplyStoreBean.setTel(phoneString);
        }
    }

    @OnTextChanged(value = R2.id.et_name, callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void watchNameTextChange(CharSequence sequence, int start, int before, int count) {
        initCommitData();
        String name = sequence.toString();
        if (mApplyStoreBean != null) {
            mApplyStoreBean.setRealname(name);
        }
    }

    @OnTextChanged(value = R2.id.et_user_code, callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void watchCodeTextChange(CharSequence sequence, int start, int before, int count) {
        initCommitData();
        String string = sequence.toString();
        if (mApplyStoreBean != null) {
            mApplyStoreBean.setCer_no(string);
        }
    }

    @Override
    protected void initView(ViewGroup contentView) {
        super.initView(contentView);
        initCommitData();
        mImageUtil = new ProcessImageUtil(getActivity());
        mImageUtil.setImageResultCallback(new ImageResultCallback() {
            @Override
            public void beforeCamera() {
            }
            @Override
            public void onSuccess(File file) {
                if(file!=null){
                   loadFile(file);
                   ImgLoader.display(getActivity(), file, mSelectImage);
                }
            }
            @Override
            public void onFailure() {

            }
        });
    }

    private void initCommitData() {
        if(mApplyStoreBean==null){
           mApplyStoreBean = new ApplyStoreBean();
        }
    }

    private void loadFile(File file) {
        initCommitData();
        if(mSelectImage==null){
            return;
        }
        int id=mSelectImage.getId();
        if(id==R.id.img_code_before){
            mApplyStoreBean.getCer_f().file=file;
        }else if(id==R.id.img_code_backgound){
            mApplyStoreBean.getCer_b().file=file;
        }else if(id==R.id.img_code_hand){
            mApplyStoreBean.getCer_h().file=file;
        }else if(id==R.id.img_business_license){
            mApplyStoreBean.getBusiness().file=file;
        }else if(id==R.id.img_licence){
            mApplyStoreBean.getLicense().file=file;
        }else if(id==R.id.img_onther_document){
            mApplyStoreBean.getOther().file=file;
        }
    }
    @Override
    protected boolean shouldBindButterKinfe() {
        return true;
    }
    private Dialog mLoadingDialog;
    /*提交*/
    @OnClick(R2.id.btn_commit)
    public void commit(){
        if (mApplyStoreBean==null||!ClickUtil.canClick()){
            return;
       }
        String tip=mApplyStoreBean.check();
        if(!TextUtils.isEmpty(tip)){
            ToastUtil.show(tip);
            return;
        }
        if(mApplyStoreBean.isHaveUploadThumb()){
            apply();
        }else{
            uploadStoreThumb();
        }
    }

    private void uploadStoreThumb() {
        if(mImageUploader==null){
           mImageUploader=new ImageUploader(getActivity());
        }
        DialogUitl.dismissDialog(mLoadingDialog);
        mLoadingDialog=DialogUitl.loadingDialog(getActivity());
        mLoadingDialog.show();
        final List<FileBundle>list=mApplyStoreBean.getFileBundle();
        mDisposable=mImageUploader.compressFileBundle(list).flatMap(new Function<List<FileBundle>, ObservableSource<FileBundle>>() {
            @Override
            public ObservableSource<FileBundle> apply(List<FileBundle> bundleList) throws Exception {
                return mImageUploader.uploadFileBundleArray(bundleList);
            }
        }).lastElement().doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                DialogUitl.dismissDialog(mLoadingDialog);
            }
        }).subscribe(new Consumer<FileBundle>() {
            @Override
            public void accept(FileBundle fileBundle) throws Exception {
                apply();
            }
        });
    }

    private void apply() {
        MainAPI.applyOpenStore(mApplyStoreBean).compose(this.<Boolean>bindUntilOnDestoryEvent()).subscribe(new DefaultObserver<Boolean>() {
            @Override
            public void onNext(Boolean aBoolean) {
                if(aBoolean){
                  toResultView();
                }
            }
            @Override
            public void onError(Throwable e) {
              super.onError(e);
              DialogUitl.dismissDialog(mLoadingDialog);
            }
            @Override
            public void onComplete() {
                super.onComplete();
                DialogUitl.dismissDialog(mLoadingDialog);
            }
        });
    }

    private void toResultView() {
        ArrayMap<String,Object>map=new ArrayMap<>();
        ApplyAnthorInfo info=new ApplyAnthorInfo();
        info.setStatus(REVIEW_ING);
        map.put(Constants.DATA,info);
        startViewProxy(map,ApplyResultViewProxy.class,null);
    }

    @Override
    public boolean onBackPressed() {
        finish();
        return true;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mDisposable!=null&&!mDisposable.isDisposed()){
           mDisposable.dispose();
        }
    }

    @OnClick({R2.id.img_code_before, R2.id.img_code_backgound, R2.id.img_code_hand, R2.id.img_business_license, R2.id.img_licence, R2.id.img_onther_document})
    public void onViewClicked(View view) {
        if(!ClickUtil.canClick()){
            return;
        }
        DialogUitl.showStringArrayDialog(getActivity(), new Integer[]{
                R.string.camera, R.string.alumb}, new DialogUitl.StringArrayDialogCallback() {
            @Override
            public void onItemClick(String text, int tag) {
                if (tag == R.string.camera) {
                    mImageUtil.getImageByCamera();
                } else {
                    mImageUtil.getImageByAlumb();
                }
            }
        });
        mSelectImage= (ImageView) view;
    }
}
