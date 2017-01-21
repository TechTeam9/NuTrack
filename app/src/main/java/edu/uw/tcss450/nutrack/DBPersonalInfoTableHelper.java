package edu.uw.tcss450.nutrack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;


public class DBPersonalInfoTableHelper  extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "nutrack.db";

    public static final String TABLE_NAME = "personal_info";

    public static final String COLUMN_NAME = "name";

    public static final String COLUMN_HEIGHT = "height";

    public static final String COLUMN_WEIGHT = "weight";

    public static final String COLUMN_DOB = "DOB";

    public static final String COLUMN_GENDER = "gender";

    public DBPersonalInfoTableHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE personal_info(" +
                "name TEXT," +
                "height REAL," +
                "weight REAL," +
                "DOB NUMERIC," +
                "gender TEXT," +
                ")";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertMember(String theName, float theHeight, float theWeight, Date theDOB, String theGender) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues personalInfoValues = new ContentValues();

        Date date = new Date();

        personalInfoValues.put(COLUMN_NAME, theName);
        personalInfoValues.put(COLUMN_HEIGHT, theHeight);
        personalInfoValues.put(COLUMN_WEIGHT, theWeight);
        personalInfoValues.put(COLUMN_DOB, "");
        personalInfoValues.put(COLUMN_GENDER, theGender);

        db.insert(TABLE_NAME, null, personalInfoValues);
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
