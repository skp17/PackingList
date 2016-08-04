package com.example.packinglist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PackingListDb extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "PackingList.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + PackingListContract.FeedEntry.TABLE_NAME + " (" +
                    PackingListContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    PackingListContract.FeedEntry.ITEM + " TEXT," +
                    PackingListContract.FeedEntry.IS_CHECKED + " INTEGER" + " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + PackingListContract.FeedEntry.TABLE_NAME;

    public PackingListDb(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public boolean insertData(String item, Integer isChecked) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PackingListContract.FeedEntry.ITEM, item);
        if (isChecked == 0 || isChecked == 1) // 0 means false, 1 means true
            values.put(PackingListContract.FeedEntry.IS_CHECKED, isChecked);

        long result = db.insert(PackingListContract.FeedEntry.TABLE_NAME, null, values);

        return (result != -1);
    }

    public Cursor getAllData() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + PackingListContract.FeedEntry.TABLE_NAME, null);
        return res;
    }

    public boolean updateData(int id, String item, Integer isChecked) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PackingListContract.FeedEntry.ITEM, item);
        if (isChecked == 0 || isChecked == 1) // 0 means false, 1 means true
            values.put(PackingListContract.FeedEntry.IS_CHECKED, isChecked);

        db.update(PackingListContract.FeedEntry.TABLE_NAME, values, "_ID = ?",
                new String[]{Integer.toString(id)});
        return true;
    }

    public boolean updateData(int id, Integer isChecked) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        if (isChecked == 0 || isChecked == 1) // 0 means false, 1 means true
            values.put(PackingListContract.FeedEntry.IS_CHECKED, isChecked);

        db.update(PackingListContract.FeedEntry.TABLE_NAME, values, "_ID = ?",
                new String[]{Integer.toString(id)});
        return true;
    }

    public Integer deleteData(int id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(PackingListContract.FeedEntry.TABLE_NAME, "_ID = ?",
                new String[] { Integer.toString(id) });
    }

    public Boolean deleteAllData() {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("delete from " + PackingListContract.FeedEntry.TABLE_NAME);
        return true;
    }
}
