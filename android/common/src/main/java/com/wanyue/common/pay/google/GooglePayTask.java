package com.wanyue.common.pay.google;

import android.app.Activity;
import androidx.annotation.Nullable;
import android.text.TextUtils;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.lzy.okgo.model.Response;
import com.wanyue.common.R;
import com.wanyue.common.api.CommonAPI;
import com.wanyue.common.http.CommonHttpUtil;
import com.wanyue.common.http.HttpCallback;
import com.wanyue.common.http.JsonBean;
import com.wanyue.common.utils.L;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.common.utils.WordUtil;

import java.util.ArrayList;
import java.util.List;


public class GooglePayTask implements PurchasesUpdatedListener {

    private static final String TAG = "GooglePay";

    private Activity mActivity;
    private GooglePayCallback mGooglePayCallback;
    private BillingClient mBillingClient;
    private String mCheckUrl;
    private String mGoogleGoodsId;//谷歌play上面的商品id
    private String mOrder;

    public GooglePayTask(Activity activity, String checkUrl, String googleGoodsId, GooglePayCallback callback) {
        mActivity = activity;
        mCheckUrl = checkUrl;
        mGoogleGoodsId = googleGoodsId;
        mGooglePayCallback = callback;
        mBillingClient = BillingClient.newBuilder(activity).setListener(this).build();
    }

    @Override
    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
        if (responseCode == BillingClient.BillingResponse.OK
                && purchases != null) {
            L.e(TAG, "支付成功");
            for (Purchase purchase : purchases) {
                checkPay(purchase);
            }
        } else if (responseCode == BillingClient.BillingResponse.USER_CANCELED) {
            L.e(TAG, "支付取消");
            // Handle an error caused by a user cancelling the purchase flow.
        } else if (responseCode == BillingClient.BillingResponse.ITEM_ALREADY_OWNED) {
            L.e(TAG, "存在未消耗商品");
            checkAllowItem();
        } else {
            L.e(TAG, "支付出现其他问题 code=" + responseCode);
            // Handle any other error codes.
        }
    }

    private void checkAllowItem() {
        mBillingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.INAPP, new PurchaseHistoryResponseListener() {
            @Override
            public void onPurchaseHistoryResponse(int responseCode, List<Purchase> purchasesList) {
                if (responseCode == BillingClient.BillingResponse.OK
                        && purchasesList != null) {
                    for (Purchase purchase : purchasesList) {
//                        if (purchase.getSku().equals(mBean.getGoogle_shopid())){
                        if (purchase.getSku().equals(mGoogleGoodsId)) {
                            UseItem(purchase.getPurchaseToken(), true);
                            break;
                        }
                    }
                }
            }
        });

    }

    private void checkPay(final Purchase purchase) {
        L.e(TAG, "消费商品");
        L.e(TAG, purchase.getOrderId());
        L.e(TAG, purchase.getOriginalJson());
        L.e(TAG, purchase.getPurchaseToken());
        L.e(TAG, purchase.getSignature());
        ToastUtil.show("checkPay--->" + purchase.getOrderId() + purchase.getOriginalJson() + purchase.getPurchaseToken() + purchase.getSignature());
        CommonAPI.checkGooglePay(mCheckUrl, purchase.getOriginalJson(), purchase.getSignature(), purchase.getOrderId(), mOrder, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    UseItem(purchase.getPurchaseToken(), false);
                } else {
                    ToastUtil.show(WordUtil.getString(R.string.order_error));
                    release();
                    if (mGooglePayCallback != null) {
                        mGooglePayCallback.onFailed();
                    }
                }
            }

            @Override
            public void onError(Response<JsonBean> response) {
                super.onError(response);
                ToastUtil.show(WordUtil.getString(R.string.pay_failure));
            }
        });
    }

    private void UseItem(String purchaseToken, final boolean isCheck) {
        mBillingClient.consumeAsync(purchaseToken, new ConsumeResponseListener() {
            @Override
            public void onConsumeResponse(int responseCode, String purchaseToken) {
                if (responseCode == BillingClient.BillingResponse.OK) {
                    L.e("googlePay", "已消耗商品");
                    if (isCheck) {
                        showPay();
                    } else {
                        if (mGooglePayCallback != null) {
                            mGooglePayCallback.onSuccess();
                        }
                        release();
                    }

                }
            }
        });
    }


    private void queryShopItem() {
        List<String> skuList = new ArrayList<>();
        skuList.add(mGoogleGoodsId);
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);

        mBillingClient.querySkuDetailsAsync(params.build(),
                new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(int responseCode, List<SkuDetails> skuDetailsList) {
                        //L.e("googlePay","查询商品信息错误，code = "+responseCode+skuDetailsList);
                        if (responseCode == BillingClient.BillingResponse.OK
                                && skuDetailsList != null) {
                            L.e(TAG, "存在商品信息");
                            ToastUtil.show("存在商品信息");
                            for (SkuDetails skuDetails : skuDetailsList) {
                                String sku = skuDetails.getSku();
                                String price = skuDetails.getPrice();
                                if (!TextUtils.isEmpty(mGoogleGoodsId) && mGoogleGoodsId.equals(sku)) {
                                    L.e(TAG, price);
                                }
                            }
                            showPay();
                        } else {
                            L.e(TAG, "查询商品信息错误，code = " + responseCode);
//                            ToastUtil.show(WordUtil.getString(R.string.query_shopitem_err));
                            if (mGooglePayCallback != null) {
                                mGooglePayCallback.onFailed();
                            }
                            release();
                        }
                    }
                });
    }

    private void showPay() {
        BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                .setSku(mGoogleGoodsId)
                .setType(BillingClient.SkuType.INAPP)
                .build();
        int responseCode = mBillingClient.launchBillingFlow((mActivity), flowParams);
        L.e("googlePay", String.valueOf(responseCode));
        ToastUtil.show("showPay" + String.valueOf(responseCode));
    }

    public void start(String googleOrderId) {

        mOrder = googleOrderId;
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(int responseCode) {
                L.e(TAG, "连接到谷歌市场");
                queryShopItem();
            }

            @Override
            public void onBillingServiceDisconnected() {
                //ToastUtil.show(WordUtil.getString(R.string.cannot_conn_google));
                if (mGooglePayCallback != null) {
                    mGooglePayCallback.onServiceDissconnected();
                }
            }
        });

    }


    private void release() {
        mBillingClient.endConnection();
    }


    public interface GooglePayCallback {

        void onServiceDissconnected();

        void onSuccess();

        void onFailed();
    }

}
