package edu.uw.tcss450.nutrack.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Database helper class for connecting to member table in the database
 */
public class DBMemberInfoHelper extends SQLiteOpenHelper {

    /**
     * Join date column.
     */
    public static final String COLUMN_JOINDATE = "join_date";

    /**
     * Database name.
     */
    private static final String DATABASE_NAME = "nutrack.db";

    /**
     * Table name.
     */
    private static final String TABLE_NAME = "account_info";

    /**
     * Email column.
     */
    private static final String COLUMN_EMAIL = "email";

    /**
     * Password column.
     */
    private static final String COLUMN_PASSWORD = "password";

    /**
     * Constructor.
     *
     * @param context context
     */
    public DBMemberInfoHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS account_info(" +
                "email TEXT PRIMARY KEY ASC," +
                "password TEXT," +
                "join_date NUMERIC" +
                ")";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS account_info(" +
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

    /**
     * Insert member info
     *
     * @param theEmail    email.
     * @param thePassword password
     * @return ture if added successfully, otherwise return false.
     */
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

    /**
     * Get member info
     *
     * @return member info cursor
     */
    public Cursor getData() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM account_info", null);
    }

    /**
     * Get member info table size
     *
     * @return size
     */
    public int getMemberSize() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM account_info", null).getCount();
    }

    /**
     * Delete member info table data.
     *
     * @return true
     */
    public boolean deleteData() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, "1", null);
        db.close();
        return true;
    }

}
