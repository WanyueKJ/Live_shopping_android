package com.wanyue.main.apply.view;

import com.wanyue.common.proxy.RxViewProxy;

public abstract class AbsApplyStoreViewProxy extends RxViewProxy {
    public static final int NOAPPLY=-2;   //没有申请
    public static final int REVIEW_ING=0; //审核中
    public static final int REVIEW_ERROR=-1; //审核失败
    public static final int REVIEW_SUCCESS=1;//审核成功



}
