package com.example.surveimy.datatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.surveimy.models.NotificationItem;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class NotificationTable {
    private final WeakReference<Context> mContext;
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    public NotificationTable(Context context) {
        mContext = new WeakReference<>(context);
    }

    //open connection sqlite
    private void open() {
        mDbHelper = new DatabaseHelper(mContext.get());
        mDb = mDbHelper.getWritableDatabase();
    }

    //close connection sqlite
    private void close() {
        mDbHelper.close();
    }

    //store new notification
    public void insert(String title, String subtitle, String created, int surveyId) {
        ContentValues values = new ContentValues();
        values.put(AppDB.NotificationTable.COLUMN_TITLE, title);
        values.put(AppDB.NotificationTable.COLUMN_SUB_TITLE, subtitle);
        values.put(AppDB.NotificationTable.COLUMN_CREATED, created);
        values.put(AppDB.NotificationTable.COLUMN_SURVEY, surveyId);
        open();
        mDb.beginTransaction();
        mDb.insert(AppDB.NotificationTable.TABLE_NAME, null, values);
        mDb.setTransactionSuccessful();
        mDb.endTransaction();
        close();
    }

    public void clearAll() {
        open();
        mDb.beginTransaction();
        mDb.delete(AppDB.NotificationTable.TABLE_NAME, null, null);
        mDb.setTransactionSuccessful();
        mDb.endTransaction();
        close();
    }

    public void updateStatus(int id, int status) {
        ContentValues values = new ContentValues();
        values.put(AppDB.NotificationTable.COLUMN_STATUS, status);
        open();
        mDb.beginTransaction();
        mDb.update(AppDB.NotificationTable.TABLE_NAME, values, AppDB.NotificationTable.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        mDb.setTransactionSuccessful();
        mDb.endTransaction();
        close();
    }

    public List<NotificationItem> getAllItems() {
        List<NotificationItem> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + AppDB.NotificationTable.TABLE_NAME;
        selectQuery += " ORDER BY " + AppDB.NotificationTable.COLUMN_ID + " DESC ";
        open();
        Cursor cursor = mDb.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                NotificationItem item = new NotificationItem();
                item.setId(id);
                item.setTitle(cursor.getString(1));
                item.setSubTitle(cursor.getString(2));
                item.setCreatedAt(cursor.getString(3));
                item.setStatus(cursor.getInt(4));
                item.setKuesionerId(cursor.getInt(5));
                list.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        close();
        return list;
    }
}
