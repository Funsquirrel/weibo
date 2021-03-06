package com.fy.weibo.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.fy.weibo.App;
import com.fy.weibo.sdk.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fan on 2018/8/30.
 * Fighting!!!
 */
public final  class DataBaseHelper {

    private static volatile DataBaseHelper dataBaseHelper;

    private SQLiteDatabase sqLiteDatabase;
    private DataBase instance = new DataBase(App.getAppInstance().getApplicationContext(), "UserData.db", null, 1);


    private DataBaseHelper() {
        sqLiteDatabase = instance.getWritableDatabase();
    }

    public static DataBaseHelper getDataBaseHelper() {

        if (dataBaseHelper == null) {
            synchronized (DataBaseHelper.class) {
                if (dataBaseHelper == null) {
                    dataBaseHelper = new DataBaseHelper();
                }
            }
        }
        return dataBaseHelper;
    }

    public void checkAccount() {

        Cursor cursor = instance.getWritableDatabase().query("User", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String s = cursor.getString(cursor.getColumnIndex("account"));
                String a = cursor.getString(cursor.getColumnIndex("password"));
                String d = cursor.getString(cursor.getColumnIndex("token"));
                String u = cursor.getString(cursor.getColumnIndex("uid"));
                Log.e("TAG", s);
                Log.e("TAG", a);
                Log.e("TAG", d);
                Log.e("TAG", u);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    public boolean checkAccount(String account) {
        Cursor cursor = instance.getWritableDatabase().query("User", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String a = cursor.getString(cursor.getColumnIndex("account"));
                Log.e("TAG", "验证账户");
                if (a.equals(account))
                    return true;
            } while (cursor.moveToNext());
        }
        cursor.close();
        return false;
    }

    public boolean checkPassword(String account, String password) {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT password FROM User WHERE account=" + account, null);
        cursor.moveToFirst();
        Log.e("TAG", cursor.getString(0));
        if (password.equals(cursor.getString(0)))
            return true;
        cursor.close();
        return false;
    }

    public void updateToken(String account, String password, String token) {
        Log.e("TAG", token + "----------------" + Constants.ACCESS_TOKEN);

        if (!token.equals(Constants.ACCESS_TOKEN)){
            sqLiteDatabase.execSQL("UPDATE User SET token = ? WHERE account = ? AND password= ?", new String[]{token, account, password});
            Log.e("TAG", token + "----------------" + Constants.ACCESS_TOKEN);
        }
    }

    public String[] getUserTokenUid(String account, String password) {

        Cursor cursor = sqLiteDatabase.query("User", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String dbAccount = cursor.getString(cursor.getColumnIndex("account"));
                String dbPass = cursor.getString(cursor.getColumnIndex("password"));
                if (account.equals(dbAccount) && password.equals(dbPass)){
                    Log.e("TAG", "相同账户");
                    String token = cursor.getString(cursor.getColumnIndex("token"));
                    String uid = cursor.getString(cursor.getColumnIndex("uid"));
                    Log.e("TAG", "这是token和uid" + token + "-----" + uid);
                    return new String[]{token, uid};
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return null;
    }





    public void saveUserToken(String account, String password, String token, String uid) {

        sqLiteDatabase = instance.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("account", account);
        contentValues.put("password", password);
        contentValues.put("token", token);
        contentValues.put("uid", uid);
        sqLiteDatabase.insert("User", null, contentValues);
        contentValues.clear();

    }

    public boolean checkRegisterAccount(String account) {

        Cursor cursor = sqLiteDatabase.query("User", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String dbAccount = cursor.getString(cursor.getColumnIndex("account"));
                if (account.equals(dbAccount)){
                    Log.e("TAG", "相同账户");
                    return false;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return true;
    }

    public List<String[]> getTokenInfoList() {

        List<String[]> tokenInfoList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query("User", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String token = cursor.getString(cursor.getColumnIndex("token"));
                String uid = cursor.getString(cursor.getColumnIndex("uid"));
                tokenInfoList.add(new String[]{token, uid});
            } while (cursor.moveToNext());
        }
        cursor.close();
        return tokenInfoList;
    }

}
