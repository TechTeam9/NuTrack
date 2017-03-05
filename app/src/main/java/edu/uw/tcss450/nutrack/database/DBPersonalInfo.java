package edu.uw.tcss450.nutrack.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

import edu.uw.tcss450.nutrack.model.Profile;

/**
 * Retrieving User personal data in database.
 */
public class DBPersonalInfo extends SQLiteOpenHelper {

    /**
     * Database name.
     */
    private static final String DATABASE_NAME = "nutrack.db";

    /**
     * Table's name.
     */
    private static final String TABLE_NAME = "personal_info";

    /**
     * Column Name.
     */
    public static final String COLUMN_NAME = "name";

    /**
     * Column Height.
     */
    public static final String COLUMN_HEIGHT = "height";

    /**
     * Column Weight.
     */
    public static final String COLUMN_WEIGHT = "weight";

    /**
     * Column Data of Birth.
     */
    public static final String COLUMN_DOB = "date_of_birth";
    /**
     * Column gender.
     */
    public static final String COLUMN_GENDER = "gender";

    /**
     * Avatar id.
     */
    public static final String COLUMN_AVATAR_ID = "avatar_id";

    /**
     * Tables helper.
     *
     * @param context context
     */
    public DBPersonalInfo(Context context) {
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

    /**
     * Put user info to database.
     * @param theName name.
     * @param theGender gender.
     * @param theDOB date of birth.
     * @param theHeight height.
     * @param theWeight weight,
     * @param theAvatarId avatar id.
     * @return ture if successfully added, false otherwise.
     */
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

    /**
     * Get the member size.
     * @returna number of members.
     */
    public int getMemberSize() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM personal_info", null).getCount();
    }

    /**
     * Get personal information.
     * @return cursor.
     */
    public Profile getPersonalInfo() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM personal_info", null);
        cursor.moveToFirst();
        Profile profile = new Profile(cursor.getString(0), cursor.getString(1).charAt(0), cursor.getString(2), cursor.getDouble(3), cursor.getDouble(4), cursor.getInt(5));

        return profile;
    }

    /**
     * Delete the user information.
     */
    public void deletePersonalInfo() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public void editPersonalInfo(String theType, String theValue) {
//        UPDATE Customers
//        SET ContactName='Alfred Schmidt', City='Frankfurt'
//        WHERE CustomerID=1;
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("UPDATE "+ TABLE_NAME+
                " SET " + theType + " = '" + theValue +
                "' WHERE 1");
    }

    public void editPersonalInfoNonString(String theType, String theValue) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("UPDATE "+ TABLE_NAME+
                " SET " + theType + " = " + theValue +
                " WHERE 1");
    }

    /**
     * Close datebase connection.
     */
    public void closeDB() {
        this.close();
    }
}
