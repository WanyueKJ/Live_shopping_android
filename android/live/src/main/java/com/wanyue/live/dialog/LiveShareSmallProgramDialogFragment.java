package com.wanyue.live.dialog;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.Constants;
import com.wanyue.common.bean.UserBean;
import com.wanyue.common.dialog.AbsDialogFragment;
import com.wanyue.common.glide.ImgLoader;
import com.wanyue.common.mob.MobBean;
import com.wanyue.common.mob.MobCallback;
import com.wanyue.common.mob.MobShareUtil;
import com.wanyue.common.mob.ShareData;
import com.wanyue.common.utils.ImageUtil;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.live.R;
import com.wanyue.live.adapter.LiveShareSmallAdapter;
import com.wanyue.live.bean.LiveBean;
import com.wanyue.live.custom.RoundTextView;
import com.wanyue.live.model.LiveModel;
import java.util.List;

public class LiveShareSmallProgramDialogFragment extends AbsDialogFragment {
    private ViewGroup mRootView;
    private ImageView mImgUserThumb;
    private ImageView mImgAvator;
    private RoundTextView mTvLiveUserName;
    private TextView mTvLiveTile;
    private TextView mTvShareName;
    private TextView mTvInviteTip;
    private TextView mTvTip;
    private RecyclerView mReclyView;
    private ImageView mImgQrCode;

    private LiveShareSmallAdapter mLiveShareSmallAdapter;
    private String mQrCode;
    private String mThumbPath;
    private MobShareUtil mMobShareUtil;

    @Override
    protected int getLayoutId() {
        return R.layout.dilog_live_share_small_program;
    }
    @Override
    protected int getDialogStyle() {
        return R.style.dialog;
    }
    @Override
    protected boolean canCancel() {
        return true;
    }

    @Override
    protected void setWindowAttributes(Window window) {
        window.setWindowAnimations(R.style.bottomToTopAnim);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
    }

    @Override
    public void init() {
        super.init();
        mRootView = findViewById(R.id.rootView);
        mImgUserThumb = findViewById(R.id.img_user_thumb);
        mImgAvator =  findViewById(R.id.img_avator);
        mTvLiveUserName =  findViewById(R.id.tv_live_user_name);
        mTvLiveTile = findViewById(R.id.tv_live_tile);
        mTvShareName =  findViewById(R.id.tv_share_name);
        mTvInviteTip = findViewById(R.id.tv_invite_tip);
        mTvTip =  findViewById(R.id.tv_tip);
        mTvTip.setText(getString(R.string.playbill_share_tip,getString(R.string.app_name)));
        mReclyView =  findViewById(R.id.reclyView);
        mImgQrCode = (ImageView) findViewById(R.id.img_qr_code);

        mLiveShareSmallAdapter=new LiveShareSmallAdapter(getLiveShareData());
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mReclyView.setLayoutManager(linearLayoutManager);
        mReclyView.setAdapter(mLiveShareSmallAdapter);
        mLiveShareSmallAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
               if(mLiveShareSmallAdapter==null){
                   return;
               }
               MobBean mobBean=mLiveShareSmallAdapter.getItem(position);
               String type= mobBean.getType();

               if(mThumbPath==null){
                  mThumbPath=saveThumbToLocal();
               }
               if(TextUtils.isEmpty(mThumbPath)){
                   return;
               }
               if(type.equals(Constants.MOB_DOWNLOAD)){
                   ToastUtil.show(getString(R.string.save_succ));
               }else{
                   share(mobBean);
               }
            }
        });
         setUIData();
    }

    private void share(MobBean mobBean) {
        if(mMobShareUtil==null){
           mMobShareUtil=new MobShareUtil();
        }
        ShareData shareData=new ShareData();
        shareData.setFilePath(mThumbPath);
        mMobShareUtil.execute(mobBean.getType(), shareData, new MobCallback() {
            @Override
            public void onSuccess(Object data) {
            }
            @Override
            public void onError() {
            }
            @Override
            public void onCancel() {

            }
            @Override
            public void onFinish() {

            }
        });
    }

    private String saveThumbToLocal() {
        Bitmap bitmap= ImageUtil.convertViewToBitmap(mRootView);
       return ImageUtil.saveAlbum(getActivity(),bitmap, System.currentTimeMillis()+"_pill.jpg");
    }

    private void setUIData() {
        LiveBean liveBean=LiveModel.getContextLiveBean(getActivity());
       if(liveBean==null){
           dismiss();
           return;
       }
        mTvLiveTile.setText(liveBean.getTitle());
        ImgLoader.display(getActivity(),liveBean.getAvatar(),mImgAvator);
        mTvLiveUserName.setText(liveBean.getUserNiceName());
        UserBean userBean= CommonAppConfig.getUserBean();
        mTvShareName.setText(userBean.getUserNiceName());
        ImgLoader.display(getActivity(),mQrCode,mImgQrCode);
        ImgLoader.display(getActivity(),liveBean.getThumb(),mImgUserThumb);

    }

    private List<MobBean> getLiveShareData() {
        String[]array={Constants.MOB_WX,Constants.MOB_WX_PYQ};
        List<MobBean> list = MobBean.getLiveShareTypeList(array);
        MobBean downLoadMobBean = new MobBean();
        downLoadMobBean.setIcon1(R.mipmap.icon_download);
        downLoadMobBean.setType(Constants.MOB_DOWNLOAD);
        list.add(downLoadMobBean);
        return list;
    }

    public void setQrCode(String qrCode) {
        mQrCode = qrCode;
    }
}
