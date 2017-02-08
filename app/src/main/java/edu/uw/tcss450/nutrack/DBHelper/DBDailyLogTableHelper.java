package edu.uw.tcss450.nutrack.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;


public class DBDailyLogTableHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "nutrack.db";

    private static final String TABLE_NAME = "daily_log";

    private static final String COLUMN_EMAIL = "email";

    private static final String COLUMN_PASSWORD = "password";

    private static final String COLUMN_JOINDATE = "join_date";

    public DBDailyLogTableHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE daily_log(" +
                "email TEXT PRIMARY KEY ASC," +
                "password TEXT," +
                "join_date NUMERIC" +
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

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        memberValues.put(COLUMN_EMAIL, theEmail);
        memberValues.put(COLUMN_PASSWORD, thePassword);
        memberValues.put(COLUMN_JOINDATE, dateFormat.format(date));

        db.insert(TABLE_NAME, null, memberValues);
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
