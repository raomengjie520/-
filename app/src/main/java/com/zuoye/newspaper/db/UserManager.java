//package com.zuoye.newspaper.db;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.util.Log;
//
//public class UserManager {
//    private final DatabaseHelper dbHelper;
//
//    public UserManager(Context context) {
//        dbHelper = new DatabaseHelper(context);
//    }
//
//    public boolean insertUser(String username, String password) {
//        if (isUserExists(username)) {
//            return false;
//        }
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(DatabaseHelper.COLUMN_USERNAME, username);
//        values.put(DatabaseHelper.COLUMN_PASSWORD, password);
//        db.insert(DatabaseHelper.TABLE_USERS, null, values);
//        db.close();
//        return true;
//    }
//
//    public boolean validateUser(String username, String password) {
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        Cursor cursor = db.query(
//            DatabaseHelper.TABLE_USERS,
//            new String[]{DatabaseHelper.COLUMN_USERNAME},
//            DatabaseHelper.COLUMN_USERNAME + "=? AND " + DatabaseHelper.COLUMN_PASSWORD + "=?",
//            new String[]{username, password},
//            null,
//            null,
//            null
//        );
//        boolean isValid = cursor.getCount() > 0;
//        cursor.close();
//        db.close();
//        return isValid;
//    }
//
//    public boolean isUserExists(String username) {
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        Cursor cursor = db.query(
//            DatabaseHelper.TABLE_USERS,
//            new String[]{DatabaseHelper.COLUMN_USERNAME},
//            DatabaseHelper.COLUMN_USERNAME + "=?",
//            new String[]{username},
//            null,
//            null,
//            null
//        );
//        boolean exists = cursor.getCount() > 0;
//        cursor.close();
//        db.close();
//        return exists;
//    }
//
//
//
//    public String getCurrentUserName() {
//        SQLiteDatabase db = null;
//        Cursor cursor = null;
//        String userName = null;
//
//        try {
//            // 获取数据库实例
//            db = dbHelper.getReadableDatabase();
//
//            // 执行查询
//            cursor = db.query(
//                    DatabaseHelper.TABLE_USERS,
//                    new String[]{DatabaseHelper.COLUMN_USERNAME},
//                    null,  // 可以根据需要添加筛选条件
//                    null,
//                    null,
//                    null,
//                    null
//            );
//
//            // 如果查询到结果，获取用户名
//            if (cursor != null && cursor.moveToFirst()) {
//                int usernameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USERNAME);
//                if (usernameIndex != -1) {
//                    userName = cursor.getString(usernameIndex);
//                } else {
//                    Log.e("UserManager", "Column not found: " + DatabaseHelper.COLUMN_USERNAME);
//                }
//            }
//        } catch (Exception e) {
//            Log.e("UserManager", "Error while fetching user name: ", e);
//        } finally {
//            // 确保 cursor 和 db 被关闭
//            if (cursor != null) {
//                cursor.close();
//            }
//            if (db != null) {
//                db.close();
//            }
//        }
//
//        return userName;
//    }
//
//
//    // 更新用户名
//    public boolean updateUserName(String oldUserName, String newUserName) {
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(DatabaseHelper.COLUMN_USERNAME, newUserName);  // 设置新的用户名
//
//        // 更新数据库中的用户名
//        int rowsUpdated = db.update(
//                DatabaseHelper.TABLE_USERS,
//                values,
//                DatabaseHelper.COLUMN_USERNAME + "=?",
//                new String[]{oldUserName}  // 根据旧用户名来更新
//        );
//
//        // 如果更新成功，保存新的用户名到 SharedPreferences
////        if (rowsUpdated > 0) {
////            saveCurrentUserName(newUserName);  // 更新成功后保存到 SharedPreferences
////            db.close();
////            return true;  // 更新成功
////        } else {
////            db.close();
////            return false;  // 更新失败
////        }
//        return true;
//    }
//
//
//    public void close() {
//        dbHelper.close();
//    }
//
//}

package com.zuoye.newspaper.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private final DatabaseHelper dbHelper;

    public UserManager(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public boolean insertUser(String username, String password) {
        if (isUserExists(username)) {
            return false;
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USERNAME, username);
        values.put(DatabaseHelper.COLUMN_PASSWORD, password);
        db.insert(DatabaseHelper.TABLE_USERS, null, values);
        db.close();
        return true;
    }

    public boolean validateUser(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_USERS,
                new String[]{DatabaseHelper.COLUMN_USERNAME},
                DatabaseHelper.COLUMN_USERNAME + "=? AND " + DatabaseHelper.COLUMN_PASSWORD + "=?",
                new String[]{username, password},
                null,
                null,
                null
        );
        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isValid;
    }

    public boolean isUserExists(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_USERS,
                new String[]{DatabaseHelper.COLUMN_USERNAME},
                DatabaseHelper.COLUMN_USERNAME + "=?",
                new String[]{username},
                null,
                null,
                null
        );
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public String getCurrentUserName() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String userName = null;

        try {
            db = dbHelper.getReadableDatabase();
            cursor = db.query(
                    DatabaseHelper.TABLE_USERS,
                    new String[]{DatabaseHelper.COLUMN_USERNAME},
                    null,
                    null,
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                int usernameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USERNAME);
                if (usernameIndex != -1) {
                    userName = cursor.getString(usernameIndex);
                } else {
                    Log.e("UserManager", "Column not found: " + DatabaseHelper.COLUMN_USERNAME);
                }
            }
        } catch (Exception e) {
            Log.e("UserManager", "Error while fetching user name: ", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return userName;
    }

    // 更新用户名
    public boolean updateUserName(String oldUserName, String newUserName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USERNAME, newUserName);

        int rowsUpdated = db.update(
                DatabaseHelper.TABLE_USERS,
                values,
                DatabaseHelper.COLUMN_USERNAME + "=?",
                new String[]{oldUserName}
        );

        db.close();
        return rowsUpdated > 0;
    }

    // 更新密码
    public boolean updatePassword(String currentPassword, String newPassword, String confirmedPassword) {

        if (!newPassword.equals(confirmedPassword)) {
            System.out.println(newPassword);
            System.out.println(confirmedPassword);
            Log.d("UserManager", "New password and confirmed password do not match.");
            System.out.println("New password and confirmed password do not match.");
            return false;
        }

        String currentUserName = getCurrentUserName();
        if (currentUserName == null) {
            Log.d("UserManager", "Current user is not logged in.");
            System.out.println("Current user is not logged in.");
            return false;
        }

        boolean isValid = validateUser(currentUserName, currentPassword);
        if (isValid) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_PASSWORD, newPassword);

            int rowsUpdated = db.update(
                    DatabaseHelper.TABLE_USERS,
                    values,
                    DatabaseHelper.COLUMN_USERNAME + "=?",
                    new String[]{currentUserName}
            );

            db.close();
            return rowsUpdated > 0;
        } else {
            Log.d("UserManager", "Current password is incorrect.");
            return false;
        }
    }

    // 获取所有注册用户的用户名和密码
    public List<String> getAllUsers() {
        List<String> usersList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // 查询所有用户
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_USERS,
                new String[]{DatabaseHelper.COLUMN_USERNAME, DatabaseHelper.COLUMN_PASSWORD},
                null, null, null, null, null
        );

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String username = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_USERNAME));
                    String password = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PASSWORD));
                    usersList.add("用户名: " + username + " | 密码: " + password);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return usersList;
    }
}
