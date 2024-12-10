package com.zuoye.newspaper.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "newsapp.db";
    public static final int DATABASE_VERSION = 2;

    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_REGISTER_TIME = "register_time"; // 新字段

    public static final String TABLE_NEWS_HISTORY = "news_history";
    public static final String COLUMN_NEWS_ID = "news_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_THUMBNAIL_URL = "thumbnail_url";
    public static final String COLUMN_NEWS_URL = "news_url";

    private static final String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " (" +
            COLUMN_USERNAME + " TEXT PRIMARY KEY, " +
            COLUMN_PASSWORD + " TEXT)";

    private static final String CREATE_NEWS_HISTORY_TABLE = "CREATE TABLE " + TABLE_NEWS_HISTORY + " (" +
            COLUMN_NEWS_ID + " TEXT PRIMARY KEY, " +
            COLUMN_TITLE + " TEXT, " +
            COLUMN_DATE + " TEXT, " +
            COLUMN_THUMBNAIL_URL + " TEXT, " +
            COLUMN_NEWS_URL + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_NEWS_HISTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWS_HISTORY);
        onCreate(db);
    }

    // 新增：查询所有用户的用户名和密码
    public List<String> getAllUsers() {
        List<String> usersList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // 查询用户表中的所有用户信息
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_USERNAME, COLUMN_PASSWORD},
                null, null, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String username = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME));
                    String password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));
                    usersList.add("用户名: " + username + " | 密码: " + password);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return usersList;
    }


}
