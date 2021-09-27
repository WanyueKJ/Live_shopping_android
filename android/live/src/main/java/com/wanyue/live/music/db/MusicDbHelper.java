package com.wanyue.live.music.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.wanyue.common.CommonApplication;
import com.wanyue.common.utils.L;

/**
 * Created by  on 2017/9/4.
 */

public class MusicDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "yunbao.music";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "music";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String ARTIST = "artist";
    public static final String INSERT = "INSERT INTO " + TABLE_NAME + " VALUES(?, ?, ?)";
    public static final String UPDATE = "UPDATE " + TABLE_NAME + " SET " + NAME + "=?," + ARTIST + "=? WHERE " + ID + "=?";
    public static final String QUERY_LIST = "SELECT * FROM " + TABLE_NAME;
    public static final String DELETE = "DELETE FROM " + TABLE_NAME + " WHERE id=";
    public static final String QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE id=";


    public MusicDbHelper() {
        super(CommonApplication.sInstance, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + ID + " VARCHAR PRIMARY KEY ," + NAME + " VARCHAR, " + ARTIST + " VARCHAR)";
        L.e("MusicDbHelper----sql--->" + sql);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
