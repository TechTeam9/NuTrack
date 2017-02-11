package edu.uw.tcss450.nutrack.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Retrieving user searching record in database.
 */
public class DBRecentSearchTableHelper extends SQLiteOpenHelper {

    /**
     * Database name.
     */
    private static final String DATABASE_NAME = "nutrack.db";

    /**
     * Table name.
     */
    private static final String TABLE_NAME = "recent_search";

    /**
     * Food name column.
     */
    private static final String COLUMN_FOOD_NAME = "food_name";

    /**
     * Time column.
     */
    private static final String COLUMN_TIME = "search_time";

    /**
     * Constructor.
     *
     * @param context
     */
    public DBRecentSearchTableHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS recent_search(food_name TEXT, search_time NUMERIC)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS recent_search(food_name TEXT, search_time NUMERIC)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * Insert food.
     *
     * @param food name.
     * @return true if add successfully, otherwise return false.
     */
    public boolean insertFood(String food) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues foodName = new ContentValues();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        foodName.put(COLUMN_FOOD_NAME, food);
        foodName.put(COLUMN_TIME, dateFormat.format(date));

        db.insert(TABLE_NAME, null, foodName);

        return true;
    }

    /**
     * Delete food.
     *
     * @param food name.
     * @return true if delete successfully, otherwise return false.
     */
    public boolean deleteFood(String food) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] foodArr = {food};
        db.delete(TABLE_NAME, "food_name", foodArr);

        return true;
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
    public Cursor getAllFood() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM recent_search", null);

    }

    /**
     * Close database connection.
     */
    public void closeDB() {
        this.close();
    }
}


