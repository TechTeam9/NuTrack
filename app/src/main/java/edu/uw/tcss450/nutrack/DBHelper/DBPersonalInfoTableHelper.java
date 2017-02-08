package edu.uw.tcss450.nutrack.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;


public class DBPersonalInfoTableHelper  extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "nutrack.db";

    private static final String TABLE_NAME = "personal_info";

    private static final String COLUMN_NAME = "name";

    private static final String COLUMN_HEIGHT = "height";

    private static final String COLUMN_WEIGHT = "weight";

    private static final String COLUMN_DOB = "DOB";

    private static final String COLUMN_GENDER = "gender";

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

    public boolean insertMember(String theName, double theHeight, double theWeight, Date theDOB, String theGender) {
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
        return db.rawQuery("SELECT * FROM personal_info", null);
    }

    public int getMemberSize() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM personal_info", null).getCount();
    }

    public void closeDB() {
        this.close();
    }
}
