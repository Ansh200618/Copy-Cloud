package com.copycloud.app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.copycloud.app.models.HistoryItem;

import java.util.ArrayList;
import java.util.List;

public class HistoryDatabase extends SQLiteOpenHelper {
    
    private static final String DATABASE_NAME = "copycloud_history.db";
    private static final int DATABASE_VERSION = 1;
    
    private static final String TABLE_HISTORY = "history";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_CODE = "code";
    private static final String COLUMN_CONTENT = "content";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_TIMESTAMP = "timestamp";
    private static final String COLUMN_DEVICE_CODE = "device_code";
    
    private static final int MAX_HISTORY_ITEMS = 20;
    private static final long EXPIRE_TIME_MS = 24 * 60 * 60 * 1000; // 24 hours
    
    public HistoryDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_HISTORY + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CODE + " TEXT, " +
                COLUMN_CONTENT + " TEXT, " +
                COLUMN_TYPE + " TEXT, " +
                COLUMN_TIMESTAMP + " INTEGER, " +
                COLUMN_DEVICE_CODE + " TEXT" +
                ")";
        db.execSQL(createTable);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        onCreate(db);
    }
    
    public long addHistoryItem(HistoryItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        // No auto-cleanup - local history persists until user deletes
        if (getHistoryCount() >= MAX_HISTORY_ITEMS) {
            removeOldestItem();
        }
        ContentValues values = new ContentValues();
        values.put(COLUMN_CODE, item.getCode());
        values.put(COLUMN_CONTENT, item.getContent());
        values.put(COLUMN_TYPE, item.getType());
        values.put(COLUMN_TIMESTAMP, item.getTimestamp());
        values.put(COLUMN_DEVICE_CODE, item.getDeviceCode());
        long id = db.insert(TABLE_HISTORY, null, values);
        db.close();
        return id;
    }
    
    public List<HistoryItem> getAllHistory() {
        List<HistoryItem> historyList = new ArrayList<>();
        // No auto-cleanup - local history stays until user deletes
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_HISTORY, null, null, null, null, null,
                COLUMN_TIMESTAMP + " DESC", String.valueOf(MAX_HISTORY_ITEMS));
        if (cursor.moveToFirst()) {
            do {
                HistoryItem item = new HistoryItem();
                item.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                item.setCode(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CODE)));
                item.setContent(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT)));
                item.setType(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE)));
                item.setTimestamp(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_TIMESTAMP)));
                item.setDeviceCode(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DEVICE_CODE)));
                historyList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return historyList;
    }
    
    public void deleteHistoryItem(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HISTORY, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
    
    public void clearAllHistory() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HISTORY, null, null);
        db.close();
    }
    
    /**
     * Clean expired items - DISABLED FOR LOCAL STORAGE
     * Local history persists until user manually deletes
     * Only cloud storage expires after 24 hours
     */
    public void cleanExpiredItems() {
        // No-op: Local storage doesn't auto-expire
    }
    
    private int getHistoryCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_HISTORY, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        db.close();
        return count;
    }
    
    private void removeOldestItem() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_HISTORY + " WHERE " + COLUMN_ID + " IN " +
                "(SELECT " + COLUMN_ID + " FROM " + TABLE_HISTORY + " ORDER BY " +
                COLUMN_TIMESTAMP + " ASC LIMIT 1)");
        db.close();
    }
}
