package com.example.dungtt.dailyreport.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.media.Image;

import java.util.ArrayList;

public class SQLiteHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "DailyReport";
    private static final int VERSION = 1;
    public static final String TABLE_NAME = "Reports";

    public static final String KEY_ID = "id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_TIME = "time";
    public static final String KEY_WRITER = "writer";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_IMAGE = "image";


    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String scrip = "CREATE TABLE IF NOT EXISTS Reports(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title VARCHAR(100), writer VARCHAR(50), time VARCHAR(16), content VARCHAR(1000), image BLOB)";
        sqLiteDatabase.execSQL(scrip);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String scrip = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
        sqLiteDatabase.execSQL(scrip);
        onCreate(sqLiteDatabase);
    }

    public void addReport(String reportname, String reportwriter, String time,String content, byte[] Hinh) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "INSERT INTO " + TABLE_NAME + " VALUES(null, ?, ?, ?, ?, ?)";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1, reportname);
        statement.bindString(2, reportwriter);
        statement.bindString(3, time);
        statement.bindString(4, content);
        statement.bindBlob(5, Hinh);
        statement.execute();
        db.close();
    }

    public ArrayList<Report> getListReport() {
        ArrayList<Report> list = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            int ids = cursor.getInt(0);
            String name = cursor.getString(1);
            String writer = cursor.getString(2);
            String time = cursor.getString(3);
            String content = cursor.getString(4);
            byte[] hinh = cursor.getBlob(5);
            Report report = new Report(ids, name, writer, time, content, hinh);
            list.add(report);
            cursor.moveToNext();
        }
        return list;
    }

    public void updateReport(Report report){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE " + TABLE_NAME + " SET title = ?, writer = ?, time = ?, content = ?, image =  ? WHERE id =?";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1, report.getReportName());
        statement.bindString(2, report.getWriter());
        statement.bindString(3, report.getTimeCreate());
        statement.bindString(4, report.getContentReport());
        statement.bindBlob(5, report.getImage());

        statement.execute();
    }

    public void deleteReport(Report report){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?", new String[] { String.valueOf(report.getId()) });
        db.close();
    }
}
