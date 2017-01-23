package edu.uw.tcss450.nutrack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;


public class DBDailyLogTableHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "nutrack.db";

    public static final String TABLE_NAME = "daily_log";

    public static final String COLUMN_DATE = "date";

    public static final String COLUMN_WEIGHT = "weight";

    public static final String COLUMN_CALORIES = "calories";

    public static final String COLUMN_PROTEIN = "protein";

    public DBDailyLogTableHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE daily_log(" +
                "date NUMERIC PRIMARY KEY ASC," +
                "weight REAL," +
                "calories REAL," +
                "protein REAL" +
                ")";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertMember(String theEmail, String thePassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues memberValues = new ContentValues();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();


        db.insert(TABLE_NAME, null, memberValues);
        return true;
    }


    public boolean createTodayLog() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues logValues = new ContentValues();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        logValues.put(COLUMN_DATE, dateFormat.format(date));

        db.insert(TABLE_NAME, null, logValues);

        return true;
    }

    public boolean updateTodayWeight(Date theDate, float theWeight) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues logValues = new ContentValues();
        logValues.put(COLUMN_WEIGHT, theWeight);

        db.rawQuery("UPDATE " + TABLE_NAME + " SET " + COLUMN_WEIGHT + "=" + theWeight + "WHERE date='" + theDate + "'", null);

        return true;
    }

    public boolean updateTodayCalories(Date theDate, float theCalories) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.rawQuery("UPDATE " + TABLE_NAME + " SET " + COLUMN_WEIGHT + "=" + theCalories + "WHERE date='" + theDate + "'", null);
        db.close();
        return true;
    }

    public boolean updateTodayProtein(Date theDate, float theProtein) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.rawQuery("UPDATE " + TABLE_NAME + " SET " + COLUMN_WEIGHT + "=" + theProtein + "WHERE date='" + theDate + "'", null);
        db.close();
        return true;
    }



    public Cursor getData() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM memberTable", null);
    }

    public int getMemberSize() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM memberTable", null).getCount();
    }
}
