package com.zuoye.newspaper.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.zuoye.newspaper.News;

import java.util.ArrayList;
import java.util.List;

public class HistoryManager {

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;


    public HistoryManager(Context context) {
        dbHelper = new DatabaseHelper(context);
        open();
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void insertNewsHistory(String newsId, String title, String date, String thumbnailUrl, String newsUrl) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NEWS_ID, newsId);
        values.put(DatabaseHelper.COLUMN_TITLE, title);
        values.put(DatabaseHelper.COLUMN_DATE, date);
        values.put(DatabaseHelper.COLUMN_THUMBNAIL_URL, thumbnailUrl);
        values.put(DatabaseHelper.COLUMN_NEWS_URL, newsUrl);

        database.insert(DatabaseHelper.TABLE_NEWS_HISTORY, null, values);
    }

    public List<News> getAllNewsHistory() {
        List<News> newsHistoryList = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_NEWS_HISTORY,
                null, null, null, null, null, DatabaseHelper.COLUMN_NEWS_ID + " DESC");

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String newsId = getColumnString(cursor, DatabaseHelper.COLUMN_NEWS_ID);
                    String title = getColumnString(cursor, DatabaseHelper.COLUMN_TITLE);
                    String date = getColumnString(cursor, DatabaseHelper.COLUMN_DATE);
                    String thumbnailUrl = getColumnString(cursor, DatabaseHelper.COLUMN_THUMBNAIL_URL);
                    String newsUrl = getColumnString(cursor, DatabaseHelper.COLUMN_NEWS_URL);

                    News news = new News(newsId, title, date, thumbnailUrl, newsUrl);
                    newsHistoryList.add(news);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return newsHistoryList;
    }

    public News getNewsHistoryById(String newsId) {
        News news = null;
        Cursor cursor = database.query(DatabaseHelper.TABLE_NEWS_HISTORY,
                null, DatabaseHelper.COLUMN_NEWS_ID + " = ?",
                new String[]{newsId}, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String title = getColumnString(cursor, DatabaseHelper.COLUMN_TITLE);
                String date = getColumnString(cursor, DatabaseHelper.COLUMN_DATE);
                String thumbnailUrl = getColumnString(cursor, DatabaseHelper.COLUMN_THUMBNAIL_URL);
                String newsUrl = getColumnString(cursor, DatabaseHelper.COLUMN_NEWS_URL);

                news = new News(newsId, title, date, thumbnailUrl, newsUrl);
            }
            cursor.close();
        }
        return news;
    }

    public void deleteNewsHistory(String newsId) {
        database.delete(DatabaseHelper.TABLE_NEWS_HISTORY,
                DatabaseHelper.COLUMN_NEWS_ID + " = ?",
                new String[]{newsId});
    }

    public void deleteAllNewsHistory() {
        database.delete(DatabaseHelper.TABLE_NEWS_HISTORY, null, null);
    }

    private String getColumnString(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName);
        if (columnIndex == -1) {
            Log.e("HistoryManager", "Column " + columnName + " not found in database.");
            return "";
        } else {
            return cursor.getString(columnIndex);
        }
    }

    public int getNewsCount() {
        int count = 0;
        Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_NEWS_HISTORY, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0); // 获取第一列的值，即新闻条数
            }
            cursor.close();
        }
        return count;
    }


}
