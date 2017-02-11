package edu.uw.tcss450.nutrack.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

/**
 * Retrieving User personal data in database.
 */
public class DBPersonalInfoTableHelper extends SQLiteOpenHelper {

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
    private static final String COLUMN_NAME = "name";

    /**
     * Column Height.
     */
    private static final String COLUMN_HEIGHT = "height";

    /**
     * Column Weight.
     */
    private static final String COLUMN_WEIGHT = "weight";

    /**
     * Column Data of Birth.
     */
    private static final String COLUMN_DOB = "date_of_birth";
    /**
     * Column gender.
     */
    private static final String COLUMN_GENDER = "gender";

    /**
     * Avatar id.
     */
    private static final String COLUMN_AVATAR_ID = "avatar_id";

    /**
     * Tables helper.
     *
     * @param context context
     */
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
    public Cursor getPersonalInfo() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM personal_info", null);
    }

    /**
     * Delete the user information.
     */
    public void deletePersonalInfo() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    /**
     * Close datebase connection.
     */
    public void closeDB() {
        this.close();
    }
}
