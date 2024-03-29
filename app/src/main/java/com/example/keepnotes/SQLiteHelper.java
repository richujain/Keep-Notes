package com.example.keepnotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import androidx.annotation.Nullable;

public class SQLiteHelper extends SQLiteOpenHelper {
    public SQLiteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void queryData(String sql){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL(sql);
    }


    public void insertData(String name,byte[] image){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sql = "INSERT INTO CATEGORY VALUES (NULL,?,?)";
        SQLiteStatement sqLiteStatement = sqLiteDatabase.compileStatement(sql);
        sqLiteStatement.clearBindings();
        sqLiteStatement.bindString(1, name);
        sqLiteStatement.bindBlob(2, image);
        sqLiteStatement.executeInsert();
    }
    public void insertData(Integer cid, String title,String description,byte[] image,String lat, String lon, String datetime){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sql = "INSERT INTO NOTES VALUES (NULL,?,?,?,?,?,?,?)";
        SQLiteStatement sqLiteStatement = sqLiteDatabase.compileStatement(sql);
        sqLiteStatement.clearBindings();
        sqLiteStatement.bindLong(1, cid);
        sqLiteStatement.bindString(2, title);
        sqLiteStatement.bindString(3, description);
        sqLiteStatement.bindBlob(4, image);
        sqLiteStatement.bindString(5, lat);
        sqLiteStatement.bindString(6, lon);
        sqLiteStatement.bindString(7, datetime);
        sqLiteStatement.executeInsert();
    }

    public void updateData(Integer noteId, String title,String description,byte[] image){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sql = "UPDATE NOTES SET title = '"+ title +"', description = '"+ description +"' where id = '"+ noteId +"'";
        SQLiteStatement sqLiteStatement = sqLiteDatabase.compileStatement(sql);
        sqLiteStatement.executeUpdateDelete();
        SQLiteDatabase database = this.getWritableDatabase();

        final String[] whereArgs = { Long.toString(noteId) };


        database.update("NOTES",createContentValues(image), "id = ?",whereArgs);
        /*ContentValues cv = new ContentValues();
        cv.put("title",    title);
        //cv.put(KEY_IMAGE,   image);
        database.insert( "NOTES", null, cv );*/
    }
    private ContentValues createContentValues(byte[] image) {
        ContentValues cv = new ContentValues();
        cv.put("image", image);
        return cv;
    }

    public Cursor getData(String sql){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.rawQuery(sql, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
