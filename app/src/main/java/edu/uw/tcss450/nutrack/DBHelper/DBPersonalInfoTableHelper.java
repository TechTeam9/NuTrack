package edu.uw.tcss450.nutrack.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Retrieving User personal data in database.
 */
public class DBPersonalInfoTableHelper  extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "nutrack.db";

    private static final String TABLE_NAME = "personal_info";

    private static final String COLUMN_NAME = "name";

    private static final String COLUMN_HEIGHT = "height";

    private static final String COLUMN_WEIGHT = "weight";

    private static final String COLUMN_DOB = "date_of_birth";

    private static final String COLUMN_GENDER = "gender";

    private static final String COLUMN_AVATAR_ID = "avatar_id";

    public DBPersonalInfoTableHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS personal_info(" +
                "name TEXT," +
                "gender TEXT," +
                "date_of_birth NUMERIC," +
                "height REAL," +
                "weight REAL," +
                "avatar_id INTEGER" +
                ")";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS personal_info(" +
                "name TEXT," +
                "gender TEXT," +
                "date_of_birth NUMERIC," +
                "height REAL," +
                "weight REAL," +
                "avatar_id INTEGER" +
                ")";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertPersonalInfo(String theName, char theGender, String theDOB, double theHeight, double theWeight, int theAvatarId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues personalInfoValues = new ContentValues();

        Date date = new Date();

        personalInfoValues.put(COLUMN_NAME, theName);
        personalInfoValues.put(COLUMN_GENDER, String.valueOf(theGender));
        personalInfoValues.put(COLUMN_DOB, theDOB);
        personalInfoValues.put(COLUMN_HEIGHT, theHeight);
        personalInfoValues.put(COLUMN_WEIGHT, theWeight);
        personalInfoValues.put(COLUMN_AVATAR_ID, theAvatarId);

        db.insert(TABLE_NAME, null, personalInfoValues);
        return true;
    }


    public int getMemberSize() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM personal_info", null).getCount();
    }

    public Cursor getPersonalInfo() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM personal_info", null);
    }

    public void deletePersonalInfo() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public void closeDB() {
        this.close();
    }
}
