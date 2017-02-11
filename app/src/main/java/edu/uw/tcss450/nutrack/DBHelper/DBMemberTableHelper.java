package edu.uw.tcss450.nutrack.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Database helper class for connecting to member table in the database
 */
public class DBMemberTableHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "nutrack.db";

    private static final String TABLE_NAME = "account_info";

    private static final String COLUMN_EMAIL = "email";

    private static final String COLUMN_PASSWORD = "password";

    public static final String COLUMN_JOINDATE = "join_date";

    public DBMemberTableHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE account_info(" +
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

        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //Date date = new Date();

        memberValues.put(COLUMN_EMAIL, theEmail);
        memberValues.put(COLUMN_PASSWORD, thePassword);
        //memberValues.put(COLUMN_JOINDATE, dateFormat.format(date));

        db.insert(TABLE_NAME, null, memberValues);
        return true;
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM account_info", null);
    }

    public int getMemberSize() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM account_info", null).getCount();
    }

    public boolean deleteData() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, "1", null);
        db.close();
        return true;
    }

    public void closeDB() {
        this.close();
    }
}
