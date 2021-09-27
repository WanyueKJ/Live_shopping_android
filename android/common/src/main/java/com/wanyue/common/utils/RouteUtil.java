package com.wanyue.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.alibaba.android.arouter.facade.callback.NavigationCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.wanyue.common.Constants;
import com.wanyue.common.bean.UserBean;
import static com.wanyue.common.Constants.CALL_TYPE;
import static com.wanyue.common.Constants.DATA;
import static com.wanyue.common.Constants.ROLE;
import static com.wanyue.common.Constants.ROOM_ID;

/**
 * Created by  on 2019/2/25.
 */

public class RouteUtil {
    public static final String PATH_LAUNCHER = "/app/LauncherActivity";
    public static final String PATH_LOGIN_INVALID = "/main/LoginInvalidActivity";
    public static final String PATH_USER_HOME = "/main/UserHomeActivity";
    public static final String PATH_MAIN = "/main/MainActivity";
    public static final String PATH_COIN = "/main/MyDiamondsActivity";
    public static final String PATH_VIP = "/main/VipActivity";
    public static final String PATH_LOGIN = "/main/LoginActivity";
    public static final String MAIN_ORDER_COMMENT = "/main/OrderCommentActivity";
    public static final String MAIN_ORDER_COMMENT_ANCHOR = "/main/OrderCommentActivity3";
    public static final String PUB_DYNAMIC = "/dynamics/PublishDynamicsActivity";
    public static final String PATH_CALL_SERVICE = "/im/CallService";
    public static final String PATH_CALL_ACTIVITY = "/im/CallActivity";
    public static final String PATH_TEACHER_HOME = "/main/TeacherHomeActivity";
    public static final String PATH_SHOP_CART = "/shop/ShopCartActivity";
    public static final String PATH_CASH_ACCOUNT = "/main/CashActivity";

    /**
     * 启动页
     */

    public static void forwardLauncher() {
        ARouter.getInstance().build(PATH_LAUNCHER)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .navigation();
    }

    public static void forwardLogin() {
        ARouter.getInstance().build(PATH_LOGIN)
                .navigation();
    }


    public static void forwardMain(Activity activity) {
        ARouter.getInstance().build(PATH_MAIN)
                .navigation(activity);

    }

    /**
     * 登录过期
     */

    public static void forwardLoginInvalid(String tip) {
        ARouter.getInstance().build(PATH_LOGIN_INVALID)
                .withString(Constants.TIP, tip)
                .navigation();
    }

    /**
     * 跳转到个人主页
     */

    public static void forwardUserHome(String toUid) {
        ARouter.getInstance().build(PATH_USER_HOME)
                .withString(Constants.TO_UID, toUid)
                .navigation();
    }

    /**
     * 跳转到充值页面
     */

    public static void forwardMyCoin() {
        ARouter.getInstance().build(PATH_COIN).navigation();
    }

    /**
     * 跳转到订单评论 用户评价主播
     */

    public static void forwardOrderComment(String orderId) {
        ARouter.getInstance().build(MAIN_ORDER_COMMENT)
                .withString(Constants.ORDER_ID, orderId)
                .navigation();
    }

    /**
     * 跳转到订单评论 主播评价用户
     */
    public static void forwardOrderCommentAnchor(String orderId) {
        ARouter.getInstance().build(MAIN_ORDER_COMMENT_ANCHOR)
                .withString(Constants.ORDER_ID, orderId)
                .navigation();
    }


    public static void forwardPubDynamics() {
        ARouter.getInstance().build(PUB_DYNAMIC)
                .navigation();
    }

    /**
     * 跳转到VIP
     */

    public static void forwardVip() {
        ARouter.getInstance().build(PATH_VIP)
                .navigation();
    }

    public static void forwardTeacherHome( Activity activity,String toUid) {
        ARouter.getInstance().build(PATH_TEACHER_HOME)
                .withString(Constants.KEY_ID,toUid)
                .navigation(activity)
        ;
    }

    /**
     * 跳转到通话
     */

    private static void forwardCall(String path,int role, int roomId, int callType, UserBean userBean) {
        ARouter.getInstance().build(path)
                .withInt(ROLE,role)
                .withInt(ROOM_ID,roomId)
                .withInt(CALL_TYPE,callType)
                .withParcelable(DATA,userBean)
                .navigation();
    }

    public static void forwardCallService(int role, int roomId, int callType, UserBean userBean){
        forwardCall(PATH_CALL_SERVICE,role,roomId,callType,userBean);
    }

    public static void forwardCallActivity(int role, int roomId, int callType, UserBean userBean){
        forwardCall(PATH_CALL_ACTIVITY,role,roomId,callType,userBean);
    }

    public static void forwardShopCart(Context context){
        ARouter.getInstance().build(PATH_SHOP_CART)
                .navigation(context);
    }


    public static void forwardCashAccount(String accountID) {
        ARouter.getInstance().build(PATH_CASH_ACCOUNT)
                .withString(Constants.CASH_ACCOUNT_ID, accountID)
                .navigation();
    }

    public static void forwardGoodsDetail(String id, boolean b) {

    }
}
