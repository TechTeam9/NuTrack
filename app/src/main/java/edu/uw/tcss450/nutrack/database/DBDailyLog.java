package edu.uw.tcss450.nutrack.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ming on 2/28/2017.
 */

public class DBDailyLog extends SQLiteOpenHelper {
    /**
     * Database name.
     */
    private static final String DATABASE_NAME = "nutrack.db";

    private static final String TABLE_NAME = "daily_log";

    private static final String COLUMN_LOG_ID = "log_id";

    private static final String COLUMN_NAME = "name";

    private static final String COLUMN_ID = "id";

    private static final String COLUMN_TYPE = "type";

    private static final String COLUMN_MEAL_TYPE = "meal_type";

    private static final String COLUMN_TIME = "eat_time";

    /**
     * Constructor.
     *
     * @param context
     */
    public DBDailyLog(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS daily_log(log_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, id INTEGER, type TEXT, meal_type TEXT, eat_time NUMERIC)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS daily_log(log_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, id INTEGER, type TEXT, meal_type TEXT, eat_time NUMERIC)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertFood(String theName, int theId, String theType, String theMealType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        value.put(COLUMN_NAME, theName);
        value.put(COLUMN_ID, theId);
        value.put(COLUMN_TYPE, theType);
        value.put(COLUMN_MEAL_TYPE, theMealType);
        value.put(COLUMN_TIME, dateFormat.format(date));

        db.insert(TABLE_NAME, null, value);

        return true;
    }


    public void deleteFood(int theLogId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] logId = {String.valueOf(theLogId)};
        db.delete(TABLE_NAME, "food_name", logId);

    }

    /**
     * Delete all food.
     *
     * @return turn if delete successfully,otherewise return false.
     */
    public boolean deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        return true;
    }

    /**
     * Get all food.
     *
     * @return cursor.
     */
    public Cursor getFoodByDate(String theDate) throws ParseException {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery("SELECT * FROM daily_log WHERE eat_time='" + theDate + "'", null);
    }

}
