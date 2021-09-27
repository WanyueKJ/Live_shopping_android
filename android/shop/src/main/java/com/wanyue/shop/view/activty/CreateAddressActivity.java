package com.wanyue.shop.view.activty;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.wanyue.common.Constants;
import com.wanyue.common.activity.BaseActivity;
import com.wanyue.common.api.CommonAPI;
import com.wanyue.common.bean.DataListner;
import com.wanyue.common.custom.CheckImageView;
import com.wanyue.common.interfaces.CommonCallback;
import com.wanyue.common.server.observer.DialogObserver;
import com.wanyue.common.utils.CityUtil;
import com.wanyue.common.utils.DialogUitl;
import com.wanyue.common.utils.ListUtil;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.common.utils.ValidatePhoneUtil;
import com.wanyue.shop.R;
import com.wanyue.shop.R2;
import com.wanyue.shop.api.ShopAPI;
import com.wanyue.shop.bean.AddressCommitBean;
import com.wanyue.shop.bean.AddressInfoBean;
import com.wanyue.shop.business.ShopEvent;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.AddressPicker;

public class CreateAddressActivity extends BaseActivity {
    private static final int TYPE_ADD=0;
    private static final int TYPE_UPDATE=2;

    @BindView(R2.id.tv_right_title)
    TextView mTvRightTitle;
    @BindView(R2.id.tv_name)
    EditText mTvName;
    @BindView(R2.id.tv_phone)
    EditText mTvPhone;
    @BindView(R2.id.tv_area)
    TextView mTvArea;
    @BindView(R2.id.btn_select_area)
    LinearLayout mBtnSelectArea;
    @BindView(R2.id.tv_detail_address)
    EditText mTvDetailAddress;
    @BindView(R2.id.btn_set_default)
    CheckImageView mBtnSetDefault;
    @BindView(R2.id.btn_commit)
    View mBtnCommit;

    private AddressCommitBean mAddressCommitBean;

    private int mType;

    @Override
    public void init() {
        setTabBackGound(R.color.white);
        AddressInfoBean addressInfoBean=getIntent().getParcelableExtra(Constants.DATA);
        mAddressCommitBean = new AddressCommitBean();
        if(addressInfoBean==null){ //新建地址
            setTabTitle(getString(R.string.new_address));
            mType=TYPE_ADD;
        }else{  //修改地址
            setTabTitle(getString(R.string.edit_address));
            mTvName.setText(addressInfoBean.getName());
            mTvPhone.setText(addressInfoBean.getPhone());
            mTvDetailAddress.setText(addressInfoBean.getAddress());
            mAddressCommitBean.copy(addressInfoBean);
            mTvArea.setText(mAddressCommitBean.getCityInfo());
            mType=TYPE_UPDATE;
        }

        mAddressCommitBean.setDataListner(new DataListner() {
            @Override
            public void compelete(boolean isCompelete) {
                if(isCompelete){
                    mBtnCommit.setAlpha(1);
                }else{
                    mBtnCommit.setAlpha(0.5F);
                }
                mBtnCommit.setEnabled(isCompelete);
            }
        });
        mBtnSetDefault.setChecked(mAddressCommitBean.getIsDefault()==1);
        mBtnSetDefault.setCheckClickListner(new CheckImageView.OnCheckClickListner() {
            @Override
            public void onCheckClick(CheckImageView view, boolean isChecked) {
                if(mAddressCommitBean==null){
                    return;
                }
                int isDefault=isChecked?1:0;
                mAddressCommitBean.setIsDefault(isDefault);
                if(mAddressCommitBean.observerCondition()){
                   mAddressCommitBean.observer();
                }
            }
        });
    }

    @OnClick(R2.id.btn_select_area)
    public void selectArea() {
        final ArrayList<Province> list = CityUtil.getInstance().getCityList();
        if (!ListUtil.haveData(list)) {
            final Dialog loading = DialogUitl.loadingDialog(mContext);
            loading.show();
            CityUtil.getInstance().getCityListFromRemote(new CommonCallback<ArrayList<Province>>() {
                @Override
                public void callback(ArrayList<Province> newList) {
                    loading.dismiss();
                    if (newList != null) {
                        showChooseCityDialog(newList);
                    }
                }
            });
        } else {
            showChooseCityDialog(list);
        }
    }

    /*监听地址变化*/
    @OnTextChanged(value = R2.id.tv_detail_address, callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void watchAddressChange(CharSequence sequence, int start, int before, int count) {
        if (mAddressCommitBean != null) {
            mAddressCommitBean.setAddress(sequence.toString());
        }
    }
    /*监听手机号变化*/
    @OnTextChanged(value = R2.id.tv_phone, callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void watchPhoneChange(CharSequence sequence, int start, int before, int count) {
        if (mAddressCommitBean != null) {
            mAddressCommitBean.setPhone(sequence.toString());
        }
    }

    /*监听名称变化*/
    @OnTextChanged(value = R2.id.tv_name, callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void watchNameChange(CharSequence sequence, int start, int before, int count) {
        if (mAddressCommitBean != null) {
            mAddressCommitBean.setName(sequence.toString());
        }
    }
    private void showChooseCityDialog(ArrayList<Province> list) {
        DialogUitl.showCityChooseDialog(this, list, null, null, null, new AddressPicker.OnAddressPickListener() {
            @Override
            public void onAddressPicked(Province province, City city, County county) {
                String provinceName = province.getAreaName();
                String cityName = city.getAreaName();
                String countyName = county.getAreaName();
                mAddressCommitBean.setProvince(provinceName);
                mAddressCommitBean.setCity(cityName);
                mAddressCommitBean.setArea(countyName);
                mAddressCommitBean.setCityId(city.getAreaId());
                mTvArea.setText(mAddressCommitBean.getCityInfo());
            }
        });
    }

    public static void forward(Activity context, AddressInfoBean addressInfoBean){
        Intent intent=new Intent(context,CreateAddressActivity.class);
        if(addressInfoBean!=null){
            intent.putExtra(Constants.DATA,addressInfoBean);
        }
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_create_address;
    }

    @Override
    protected boolean shouldBindButterKinfe() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CommonAPI.cancel(CommonAPI.CITY_LIST);
    }

    @OnClick(R2.id.btn_commit)
    public void commit() {
        if (mAddressCommitBean == null) {
            return;
        }

        String phoneString=mAddressCommitBean.getPhone();
        if(!ValidatePhoneUtil.validateMobileNumber(phoneString)){
            ToastUtil.show(R.string.login_phone_error);
            return;
        }


        DialogObserver dialogObserver=new DialogObserver<Boolean>(this) {
            @Override
            public void onNextTo(Boolean aBoolean) {
                if(aBoolean){
                    if(mType==TYPE_ADD){
                        ToastUtil.show("添加成功");
                    }else if(mType==TYPE_UPDATE){
                        ToastUtil.show("保存成功");
                    }
                    LiveEventBus.get(ShopEvent.ADDRESS_CHANGE,AddressInfoBean.class).post(new AddressInfoBean());
                    finish();
                }
            }
        };
        ShopAPI.addAddress(mAddressCommitBean).compose(this.<Boolean>bindUntilOnDestoryEvent()).subscribe(dialogObserver);
    }
}
