package com.wanyue.shop.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.wanyue.common.adapter.base.BaseMutiRecyclerAdapter;
import com.wanyue.common.adapter.base.BaseReclyViewHolder;
import com.wanyue.common.adapter.base.BaseRecyclerAdapter;
import com.wanyue.common.custom.CheckImageView;
import com.wanyue.common.server.observer.DialogObserver;
import com.wanyue.common.utils.ClickUtil;
import com.wanyue.common.utils.DialogUitl;
import com.wanyue.common.utils.ResourceUtil;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.shop.R;
import com.wanyue.shop.api.ShopAPI;
import com.wanyue.shop.bean.AddressInfoBean;
import com.wanyue.shop.business.ShopEvent;
import com.wanyue.shop.view.activty.CreateAddressActivity;

import java.util.List;

public  class AddressAdapter extends BaseRecyclerAdapter<AddressInfoBean, BaseReclyViewHolder> {
    private int mSelectPosition=-1;
    private Context mContext;
    public AddressAdapter(List<AddressInfoBean> data, Context context) {
        super(data);
        mContext=context;
        setOnItemChildClickListener2(new BaseMutiRecyclerAdapter.OnItemChildClickListener2<AddressInfoBean>() {
            @Override
            public void onItemClick(int position, AddressInfoBean addressInfoBean, View view) {
                int id=view.getId();
                if(id==R.id.btn_delete){
                  openDeleteAddress(addressInfoBean.getId());
                }else if(id==R.id.btn_edit){
                    editAddress(addressInfoBean);
                }else if(id==R.id.btn_set_default){
                    setDefaultAddress(position);
                }
            }
        });

    }

    private void openDeleteAddress(final int id) {
        if(mContext==null){
            deleteAddress(id);
        }else{
            DialogUitl.showSimpleDialog(mContext, "是否要删除该地址?", new DialogUitl.SimpleCallback() {
                @Override
                public void onConfirmClick(Dialog dialog, String content) {
                    deleteAddress(id);
                }
            });
        }

    }

    /*删除地址*/
    private void deleteAddress(int id) {
        ShopAPI.deleteAddress(id).subscribe(new DialogObserver<Boolean>(mContext) {
            @Override
            public void onNextTo(Boolean aBoolean) {
                if(aBoolean){
                    ToastUtil.show("删除成功");
                   LiveEventBus.get(ShopEvent.ADDRESS_CHANGE, AddressInfoBean.class).post(new AddressInfoBean());
                }
            }
        });
    }


   /*修改地址*/
    private void editAddress(AddressInfoBean addressInfoBean) {
        CreateAddressActivity.forward((Activity) mContext,addressInfoBean);
    }

    private void setDefaultAddress(final int position) {
        final AddressInfoBean addressInfoBean=getItem(position);
        ShopAPI.setDefaultAddress(addressInfoBean.getId()).subscribe(new DialogObserver<Boolean>(context) {
            @Override
            public void onNextTo(Boolean aBoolean) {
                if(aBoolean){
                    ToastUtil.show("设置成功");
                  changeSelect(position);
                }
            }
        });
    }

    private void changeSelect(int position) {
        if(mSelectPosition!=-1){
            AddressInfoBean oldSelectInfo=getItem(mSelectPosition);
            oldSelectInfo.setIsDefault(0);
            notifyItemChanged(mSelectPosition);
        }
        AddressInfoBean newInfo=getItem(position);
        newInfo.setIsDefault(1);
        notifyItemChanged(position);
        mSelectPosition=position;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_recly_address;
    }

    @Override
    protected void convert(BaseReclyViewHolder helper, AddressInfoBean item) {
        helper.setText(R.id.tv_name_phone, item.getNamePhoneShowInfo());
        helper.setText(R.id.tv_address,item.getDetailArea());
        helper.setOnChildClickListner(R.id.btn_edit,mOnClickListener);
        helper.setOnChildClickListner(R.id.btn_delete,mOnClickListener);
        helper.setOnChildClickListner(R.id.btn_set_default,mOnClickListener);
        CheckImageView btnSetDefault=helper.getView(R.id.btn_set_default);
        boolean isDefault=item.getIsDefault()==1;
        if(isDefault){
           mSelectPosition=helper.getLayoutPosition();
        }
        btnSetDefault.setChecked(isDefault);
    }
}
