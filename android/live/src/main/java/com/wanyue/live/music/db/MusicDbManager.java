package com.wanyue.live.music.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.wanyue.live.music.LiveMusicBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by  on 2017/9/4.
 */

public class MusicDbManager {
    private static MusicDbManager sInstace;
    private MusicDbHelper mDbHelper;

    private MusicDbManager() {
        mDbHelper = new MusicDbHelper();
    }

    public static MusicDbManager getInstace() {
        if (sInstace == null) {
            synchronized (MusicDbManager.class) {
                if (sInstace == null) {
                    sInstace = new MusicDbManager();
                }
            }
        }
        return sInstace;
    }


    /**
     * 保存数据
     *
     * @param bean
     */
    public void save(LiveMusicBean bean) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        if (db.isOpen()) {
            Cursor c = db.rawQuery(MusicDbHelper.QUERY + "'" + bean.getId() + "'", null);
            if (c.moveToNext()) {//如果存在，执行update
                SQLiteStatement st = db.compileStatement(MusicDbHelper.UPDATE);
                db.beginTransaction();
                try {
                    st.bindString(1, bean.getName());
                    st.bindString(2, bean.getArtist());
                    st.bindString(3, bean.getId());
                    st.executeUpdateDelete();
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                    db.close();
                }
            } else {//如果不存在，执行insert
                SQLiteStatement st = db.compileStatement(MusicDbHelper.INSERT);
                db.beginTransaction();
                try {
                    st.bindString(1, bean.getId());
                    st.bindString(2, bean.getName());
                    st.bindString(3, bean.getArtist());
                    st.executeInsert();
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                    db.close();
                }
            }
        }
    }

    /**
     * 查询列表
     *
     * @return
     */
    public List<LiveMusicBean> queryList() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        if (db.isOpen()) {
            List<LiveMusicBean> list = new ArrayList<>();
            Cursor c = null;
            try {
                c = db.rawQuery(MusicDbHelper.QUERY_LIST, null);
                while (c.moveToNext()) {
                    LiveMusicBean bean = new LiveMusicBean();
                    bean.setId(c.getString(c.getColumnIndex(MusicDbHelper.ID)));
                    bean.setName(c.getString(c.getColumnIndex(MusicDbHelper.NAME)));
                    bean.setArtist(c.getString(c.getColumnIndex(MusicDbHelper.ARTIST)));
                    bean.setProgress(100);
                    list.add(bean);
                }
            } finally {
                if (c != null) {
                    c.close();
                }
                db.close();
            }
            return list;
        }
        return null;
    }


    /**
     * 删除歌曲
     *
     * @param id
     */
    public void delete(String id) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        if (db.isOpen()) {
            try {
                db.execSQL(MusicDbHelper.DELETE + "'" + id + "'");
            } finally {
                db.close();
            }
        }
    }
}
