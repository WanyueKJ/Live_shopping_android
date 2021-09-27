package com.yunbao.im.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tencent.mapsdk.raster.model.CameraPosition;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.TencentMap;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.Constants;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.bean.TxLocationPoiBean;
import com.yunbao.common.custom.CommonRefreshView;
import com.yunbao.common.event.LocationEvent;
import com.yunbao.common.http.CommonHttpConsts;
import com.yunbao.common.http.CommonHttpUtil;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.interfaces.OnItemClickListener;
import com.yunbao.common.utils.LocationUtil;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.im.R;
import com.yunbao.im.adapter.LocationAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by  on 2018/7/18.
 */

public class LocationActivity extends AbsActivity implements View.OnClickListener {

    private MapView mMapView;
    private TencentMap mTencentMap;
    private CommonRefreshView mRefreshView;
    private LocationAdapter mAdapter;
    private CommonRefreshView mRefreshViewSearch;
    private LocationAdapter mAdapterSearch;
    private double mLng;
    private double mLat;
    private boolean mMapLoaded;
    private EditText mEditText;
    private View mSearchResultGroup;
    private Handler mHandler;
    private static final int WHAT = 0;
    private InputMethodManager imm;
    private String mCurKeyWord;//搜索的关键字
    private boolean mClickItem;
    private boolean mFirstGrag = true;//是否是第一次拖动

    @Override
    protected int getLayoutId() {
        return R.layout.activity_location;
    }

    @Override
    protected void main(Bundle savedInstanceState) {
        setTitle(WordUtil.getString(R.string.location));
        mRefreshView = (CommonRefreshView) findViewById(R.id.refreshView);
        mRefreshView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRefreshViewSearch = (CommonRefreshView) findViewById(R.id.refreshView_search);
        mRefreshViewSearch.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRefreshView.setDataHelper(new CommonRefreshView.DataHelper<TxLocationPoiBean>() {
            @Override
            public RefreshAdapter<TxLocationPoiBean> getAdapter() {
                if (mAdapter == null) {
                    mAdapter = new LocationAdapter(mContext);
                    mAdapter.setOnItemClickListener(new OnItemClickListener<TxLocationPoiBean>() {
                        @Override
                        public void onItemClick(TxLocationPoiBean bean, int position) {
                            mClickItem = true;
                            TxLocationPoiBean.Location location = bean.getLocation();
                            mTencentMap.setCenter(new LatLng(location.getLat(), location.getLng()));
                        }
                    });
                }
                return mAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                CommonHttpUtil.getAddressInfoByTxLocaitonSdk(mLng, mLat, 1, p, CommonHttpConsts.GET_MAP_INFO, callback);
            }

            @Override
            public List<TxLocationPoiBean> processData(String[] info) {
                JSONObject obj = JSON.parseObject(info[0]);
                if (obj != null) {
                    return JSON.parseArray(obj.getString("pois"), TxLocationPoiBean.class);
                }
                return null;
            }

            @Override
            public void onRefreshSuccess(List<TxLocationPoiBean> list, int listCount) {

            }

            @Override
            public void onRefreshFailure() {

            }

            @Override
            public void onLoadMoreSuccess(List<TxLocationPoiBean> loadItemList, int loadItemCount) {

            }

            @Override
            public void onLoadMoreFailure() {

            }
        });

        mRefreshViewSearch.setDataHelper(new CommonRefreshView.DataHelper<TxLocationPoiBean>() {
            @Override
            public RefreshAdapter<TxLocationPoiBean> getAdapter() {
                if (mAdapterSearch == null) {
                    mAdapterSearch = new LocationAdapter(mContext);
                    mAdapterSearch.setOnItemClickListener(new OnItemClickListener<TxLocationPoiBean>() {
                        @Override
                        public void onItemClick(TxLocationPoiBean bean, int position) {
                            imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
                            if (mSearchResultGroup != null && mSearchResultGroup.getVisibility() == View.VISIBLE) {
                                mSearchResultGroup.setVisibility(View.INVISIBLE);
                            }
                            if (mAdapterSearch != null) {
                                mAdapterSearch.clearData();
                            }
                            if (mEditText != null) {
                                mEditText.setText("");
                            }
                            TxLocationPoiBean.Location location = bean.getLocation();
                            mTencentMap.setCenter(new LatLng(location.getLat(), location.getLng()));
                        }
                    });
                }
                return mAdapterSearch;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                if (!TextUtils.isEmpty(mCurKeyWord)) {
                    CommonHttpUtil.searchAddressInfoByTxLocaitonSdk(mLng, mLat, mCurKeyWord, p, callback);
                }
            }

            @Override
            public List<TxLocationPoiBean> processData(String[] info) {
                return JSON.parseArray(info[0], TxLocationPoiBean.class);
            }

            @Override
            public void onRefreshSuccess(List<TxLocationPoiBean> list, int listCount) {

            }

            @Override
            public void onRefreshFailure() {

            }

            @Override
            public void onLoadMoreSuccess(List<TxLocationPoiBean> loadItemList, int loadItemCount) {

            }

            @Override
            public void onLoadMoreFailure() {

            }
        });

        mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mTencentMap = mMapView.getMap();
        mTencentMap.setZoom(16);
        mTencentMap.setOnMapLoadedListener(new TencentMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                mMapLoaded = true;
            }
        });
        mTencentMap.setOnMapCameraChangeListener(new TencentMap.OnMapCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                if (mFirstGrag) {
                    mFirstGrag = false;
                } else {
                    mClickItem = false;
                }
            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                if (!mClickItem) {
                    LatLng latLng = cameraPosition.getTarget();
                    mLng = latLng.getLongitude();
                    mLat = latLng.getLatitude();
                    CommonHttpUtil.cancel(CommonHttpConsts.GET_MAP_INFO);
                    if (mRefreshView != null) {
                        mRefreshView.initData();
                    }
                }
                mFirstGrag = true;
                mClickItem = false;
            }
        });
        mLng = CommonAppConfig.getInstance().getLng();
        mLat = CommonAppConfig.getInstance().getLat();
        if (mLng == 0 || mLat == 0) {
            LocationUtil.getInstance().setNeedPostLocationEvent(true);
            LocationUtil.getInstance().startLocation();
        } else {
            showMyLocation();
        }
        EventBus.getDefault().register(this);
        findViewById(R.id.btn_send).setOnClickListener(this);
        findViewById(R.id.btn_my_location).setOnClickListener(this);
        mSearchResultGroup = findViewById(R.id.search_result_group);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mEditText = (EditText) findViewById(R.id.search_input);
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    CommonHttpUtil.cancel(CommonHttpConsts.GET_MAP_SEARCH);
                    if (mHandler != null) {
                        mHandler.removeMessages(WHAT);
                    }
                    if (mLng == 0 || mLat == 0) {
                        ToastUtil.show(WordUtil.getString(R.string.im_location_failed));
                        return true;
                    }
                    String key = mEditText.getText().toString().trim();
                    if (!TextUtils.isEmpty(key)) {
                        if (mSearchResultGroup.getVisibility() != View.VISIBLE) {
                            mSearchResultGroup.setVisibility(View.VISIBLE);
                        }
                        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
                        mCurKeyWord = key;
                        if (mRefreshViewSearch != null) {
                            mRefreshViewSearch.initData();
                        }
                    } else {
                        ToastUtil.show(WordUtil.getString(R.string.content_empty));
                    }
                    return true;
                }
                return false;
            }
        });
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CommonHttpUtil.cancel(CommonHttpConsts.GET_MAP_SEARCH);
                if (!TextUtils.isEmpty(s)) {
                    if (mHandler != null) {
                        mHandler.removeMessages(WHAT);
                        mHandler.sendEmptyMessageDelayed(WHAT, 500);
                    }
                } else {
                    if (mSearchResultGroup.getVisibility() == View.VISIBLE) {
                        mSearchResultGroup.setVisibility(View.INVISIBLE);
                    }
                    if (mAdapterSearch != null) {
                        mAdapterSearch.clearData();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                CommonHttpUtil.cancel(CommonHttpConsts.GET_MAP_SEARCH);
                String key = mEditText.getText().toString().trim();
                if (!TextUtils.isEmpty(key)) {
                    if (mSearchResultGroup.getVisibility() != View.VISIBLE) {
                        mSearchResultGroup.setVisibility(View.VISIBLE);
                    }
                    //搜索地点
                    mCurKeyWord = key;
                    if (mRefreshViewSearch != null) {
                        mRefreshViewSearch.initData();
                    }
                } else {
                    if (mSearchResultGroup.getVisibility() == View.VISIBLE) {
                        mSearchResultGroup.setVisibility(View.INVISIBLE);
                    }
                    if (mAdapterSearch != null) {
                        mAdapterSearch.clearData();
                    }
                }
            }
        };

    }

    /**
     * 在地图上显示自己的位置
     */
    private void showMyLocation() {
        mTencentMap.setCenter(new LatLng(mLat, mLng));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLocationEvent(LocationEvent e) {
        mLng = e.getLng();
        mLat = e.getLat();
        showMyLocation();
    }

    @Override
    public void onBackPressed() {
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        CommonHttpUtil.cancel(CommonHttpConsts.GET_MAP_SEARCH);
        CommonHttpUtil.cancel(CommonHttpConsts.GET_MAP_INFO);
        LocationUtil.getInstance().setNeedPostLocationEvent(false);
        EventBus.getDefault().unregister(this);
        if (mMapView != null) {
            mMapView.stopAnimation();
            mMapView.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (mMapView != null) {
            mMapView.onPause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (mMapView != null) {
            mMapView.onResume();
        }
        super.onResume();
    }

    @Override
    protected void onStop() {
        if (mMapView != null) {
            mMapView.onStop();
        }
        super.onStop();
    }

    @Override
    protected void onRestart() {
        if (mMapView != null) {
            mMapView.onRestart();
        }
        super.onRestart();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMapView != null) {
            mMapView.onSaveInstanceState(outState);
        }
        super.onSaveInstanceState(outState);
    }

    private void sendLocationInfo() {
        if (!mMapLoaded) {
            ToastUtil.show(WordUtil.getString(R.string.im_map_not_loaded));
            return;
        }
        if (mAdapter != null) {
            TxLocationPoiBean bean = null;
            if (mSearchResultGroup.getVisibility() == View.VISIBLE) {
                bean = mAdapterSearch.getCheckedLocationPoiBean();
            } else {
                bean = mAdapter.getCheckedLocationPoiBean();
            }
            if (bean != null) {
                Intent intent = new Intent();
                TxLocationPoiBean.Location location = bean.getLocation();
                intent.putExtra(Constants.LAT, location.getLat());
                intent.putExtra(Constants.LNG, location.getLng());
                intent.putExtra(Constants.SCALE, mTencentMap.getZoomLevel());
                String address = "{\"name\":\"" + bean.getTitle() + "\",\"info\":\"" + bean.getAddress() + "\"}";
                intent.putExtra(Constants.ADDRESS, address);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                ToastUtil.show(R.string.im_address_failed);
            }
        } else {
            ToastUtil.show(R.string.im_address_failed);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_send) {
            sendLocationInfo();
        } else if (i == R.id.btn_my_location) {
            moveToMyLocation();
        }
    }

    private void moveToMyLocation() {
        mLng = CommonAppConfig.getInstance().getLng();
        mLat = CommonAppConfig.getInstance().getLat();
        showMyLocation();
    }
}
