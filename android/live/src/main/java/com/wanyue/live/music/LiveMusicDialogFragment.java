package com.wanyue.live.music;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.Constants;
import com.wanyue.common.dialog.AbsDialogFragment;
import com.wanyue.common.http.HttpCallback;
import com.wanyue.common.utils.DownloadUtil;
import com.wanyue.common.utils.DpUtil;
import com.wanyue.common.utils.FileUtil;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.live.R;
import com.wanyue.live.http.LiveHttpConsts;
import com.wanyue.live.http.LiveHttpUtil;
import com.wanyue.live.http.MusicUrlCallback;
import com.wanyue.live.music.db.MusicDbManager;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Created by  on 2017/9/2.
 */

public class LiveMusicDialogFragment extends AbsDialogFragment implements View.OnClickListener, LiveMusicAdapter.ActionListener {

    private EditText mEditText;
    private RecyclerView mRecyclerView;
    private View mNoLocalMusic;//没有本地歌曲
    private String mLastKey;
    private LiveMusicAdapter mAdapter;
    private DownloadUtil mDownloadUtil;
    private ActionListener mActionListener;

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_live_music;
    }

    @Override
    protected int getDialogStyle() {
        return R.style.dialog2;
    }

    @Override
    protected boolean canCancel() {
        return true;
    }

    @Override
    protected void setWindowAttributes(Window window) {
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = DpUtil.dp2px(280);
        params.height = DpUtil.dp2px(360);
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mEditText = mRootView.findViewById(R.id.edit);
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search();
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
                if (s.length() == 0) {
                    queryDownloadMusic();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mNoLocalMusic = mRootView.findViewById(R.id.no_local_music);
        mRecyclerView = mRootView.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mAdapter = new LiveMusicAdapter(mContext);
        mAdapter.setActionListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mRootView.findViewById(R.id.btn_search).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_close).setOnClickListener(this);
        queryDownloadMusic();
    }

    @Override
    public void onClick(View v) {
        if (!canClick()) {
            return;
        }
        int i = v.getId();
        if (i == R.id.btn_search) {
            search();

        } else if (i == R.id.btn_close) {
            dismiss();

        }
    }

    /**
     * 获取已经下载的歌曲
     */
    private void queryDownloadMusic() {
        List<LiveMusicBean> list = MusicDbManager.getInstace().queryList();
        if (list.size() > 0) {
            if (mNoLocalMusic.getVisibility() == View.VISIBLE) {
                mNoLocalMusic.setVisibility(View.INVISIBLE);
            }
            mAdapter.setList(list);
        } else {
            if (mNoLocalMusic.getVisibility() != View.VISIBLE) {
                mNoLocalMusic.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 搜索歌曲
     */
    private void search() {
        String key = mEditText.getText().toString().trim();
        if (TextUtils.isEmpty(key)) {
            ToastUtil.show(getString(R.string.content_empty));
            return;
        }
        if (!TextUtils.isEmpty(mLastKey) && mLastKey.equals(key)) {
            return;
        }
        mLastKey = key;
        LiveHttpUtil.searchMusic(key, mSearchCallback);
    }

    private HttpCallback mSearchCallback = new HttpCallback() {
        @Override
        public void onSuccess(int code, String msg, String[] info) {
            if (code == 0) {
                if (mNoLocalMusic.getVisibility() == View.VISIBLE) {
                    mNoLocalMusic.setVisibility(View.INVISIBLE);
                }
                List<LiveMusicBean> list = JSON.parseArray(Arrays.toString(info), LiveMusicBean.class);
                mAdapter.setList(list);
            }
        }
    };


    @Override
    public void onChoose(LiveMusicBean bean) {
        dismiss();
        if (mActionListener != null) {
            mActionListener.onChoose(bean.getId());
        }
    }

    @Override
    public void onDownLoad(LiveMusicBean bean) {
        mMusicUrlCallback.setLiveMusicBean(bean);
        //获取选中歌曲的下载地址和歌词
        LiveHttpUtil.getMusicUrl(bean.getId(), mMusicUrlCallback);
    }

    @Override
    public void onItemRemoved(String musicId,int listSize) {
        MusicDbManager.getInstace().delete(musicId);
        File mp3File = new File(CommonAppConfig.MUSIC_PATH, musicId + ".mp3");
        if (mp3File.exists()) {
            mp3File.delete();
        }
        File lrcFile = new File(CommonAppConfig.MUSIC_PATH, musicId + ".lrc");
        if (lrcFile.exists()) {
            lrcFile.delete();
        }
        if(listSize==0){
            queryDownloadMusic();
        }
    }

    /**
     * 获取歌曲下载地址的回调
     */
    private MusicUrlCallback mMusicUrlCallback = new MusicUrlCallback() {

        @Override
        public void onSuccess(int code, String msg, String[] info) {
            if (code == 0 && info.length > 0) {
                JSONObject obj = JSON.parseObject(info[0]);
                String url = obj.getString("audio_link");
                String lrcContent = obj.getString("lrc_content");
                if (!TextUtils.isEmpty(url)) {
                    final LiveMusicBean bean = getLiveMusicBean();
                    if (bean != null) {
                        if (mDownloadUtil == null) {
                            mDownloadUtil = new DownloadUtil();
                        }
                        final String musicId = bean.getId();
                        File dir = new File(CommonAppConfig.MUSIC_PATH);
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        if (!TextUtils.isEmpty(lrcContent)) {
                            FileUtil.saveStringToFile(dir, lrcContent, musicId + ".lrc");
                        }
                        mDownloadUtil.download(Constants.DOWNLOAD_MUSIC, dir, musicId + ".mp3", url, new DownloadUtil.Callback() {

                            @Override
                            public void onSuccess(File file) {
                                MusicDbManager.getInstace().save(bean);
                            }

                            @Override
                            public void onProgress(int progress) {
                                if (mAdapter != null) {
                                    mAdapter.updateItem(musicId, progress);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        });
                    }
                } else {
                    ToastUtil.show(R.string.music_url_empty);
                }
            } else {
                ToastUtil.show(msg);
            }
        }
    };

    public void setActionListener(ActionListener actionListener) {
        mActionListener = actionListener;
    }

    public interface ActionListener {
        void onChoose(String musicId);
    }

    @Override
    public void onDestroy() {
        LiveHttpUtil.cancel(LiveHttpConsts.SEARCH_MUSIC);
        LiveHttpUtil.cancel(LiveHttpConsts.GET_MUSIC_URL);
        if (mAdapter != null) {
            mAdapter.release();
        }
        super.onDestroy();
    }

}
