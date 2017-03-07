package edu.uw.tcss450.nutrack.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;

import static android.media.tv.TvContract.Channels.COLUMN_TYPE;
import static edu.uw.tcss450.nutrack.R.string.carbs;
import static edu.uw.tcss450.nutrack.R.string.fat;
import static edu.uw.tcss450.nutrack.R.string.protein;

/**
 * Created by Ming on 3/7/2017.
 */

public class DBCalories extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "nutrack.db";

    private static final String TABLE_NAME = "calories_log";

    private static final String COLUMN_CALORIES = "calories";

    private static final String COLUMN_DATE = "date";

    /**
     * Constructor.
     *
     * @param context
     */
    public DBCalories(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS calories_log(date TEXT PRIMARY KEY, calories INTEGER)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS calories_log(date TEXT PRIMARY KEY, calories INTEGER)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void getCalories(String theDate, int theCalories) {
        SQLiteDatabase dbRead = this.getReadableDatabase();
        SQLiteDatabase dbWrite = this.getWritableDatabase();

        Cursor cursor = dbRead.rawQuery("SELECT * FROM nutrient_record WHERE date='" + theDate + "'", null);

        if (cursor.getCount() == 0) {
            ContentValues nutrient = new ContentValues();
            nutrient.put("date", theDate);
            nutrient.put("calories", theCalories);

            dbWrite.insert(TABLE_NAME, null, nutrient);
        } else {
            cursor.moveToFirst();
            double calories = cursor.getDouble(1) + theCalories;

            ContentValues nutrient = new ContentValues();
            nutrient.put("calories", calories);

            dbWrite.update(TABLE_NAME, nutrient, "date=?", new String[]{theDate});
        }

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
