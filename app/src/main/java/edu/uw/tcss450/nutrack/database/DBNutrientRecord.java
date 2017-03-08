package edu.uw.tcss450.nutrack.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;

/**
 * Created by Ming on 3/7/2017.
 */

public class DBNutrientRecord extends SQLiteOpenHelper{
    /**
     * Database name.
     */
    private static final String DATABASE_NAME = "nutrack.db";

    private static final String TABLE_NAME = "nutrient_record";

    private static final String COLUMN_DATE = "date";

    private static final String COLUMN_CALORIES = "calories";

    private static final String COLUMN_FAT = "fat";

    private static final String COLUMN_CARBS = "carbs";

    private static final String COLUMN_PROTEIN = "protein";

    /**
     * Constructor.
     *
     * @param context
     */
    public DBNutrientRecord(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS nutrient_record(date TEXT PRIMARY KEY, calories REAL, fat REAL, carbs REAL, protein REAL)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS nutrient_record(date TEXT PRIMARY KEY, calories REAL, fat REAL, carbs REAL, protein REAL)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertNutrient(String theDate, double theCalories, double theFat, double theCarbs, double theProtein) {
        SQLiteDatabase dbRead = this.getReadableDatabase();
        SQLiteDatabase dbWrite = this.getWritableDatabase();



        Cursor cursor = dbRead.rawQuery("SELECT * FROM nutrient_record WHERE date='" + theDate + "'", null);

        if (cursor.getCount() == 0) {
            ContentValues nutrient = new ContentValues();
            nutrient.put("date", theDate);
            nutrient.put("calories", theCalories);
            nutrient.put("fat", theFat);
            nutrient.put("carbs", theCarbs);
            nutrient.put("protein", theProtein);

            dbWrite.insert(TABLE_NAME, null, nutrient);
        } else {
            cursor.moveToFirst();
            double calories = cursor.getDouble(1) + theCalories;
            double fat = cursor.getDouble(2) + theFat;
            double carbs = cursor.getDouble(3) + theCarbs;
            double protein = cursor.getDouble(4) + theProtein;

            ContentValues nutrient = new ContentValues();
            nutrient.put("calories", calories);
            nutrient.put("fat", fat);
            nutrient.put("carbs", carbs);
            nutrient.put("protein", protein);

            dbWrite.update(TABLE_NAME, nutrient, "date=?", new String[]{theDate});
        }

    }

    public void deleteNutrient(String theDate, double theCalories, double theFat, double theCarbs, double theProtein) {
        SQLiteDatabase dbRead = this.getReadableDatabase();
        SQLiteDatabase dbWrite = this.getWritableDatabase();

        Cursor cursor = dbRead.rawQuery("SELECT * FROM nutrient_record WHERE date='" + theDate + "'", null);

        cursor.moveToFirst();
        double calories = cursor.getDouble(1) - theCalories;
        double fat = cursor.getDouble(2) - theFat;
        double carbs = cursor.getDouble(3) - theCarbs;
        double protein = cursor.getDouble(4) - theProtein;

        ContentValues nutrient = new ContentValues();
        nutrient.put("calories", calories);
        nutrient.put("fat", fat);
        nutrient.put("carbs", carbs);
        nutrient.put("protein", protein);

        dbWrite.update(TABLE_NAME, nutrient, "date=?", new String[]{theDate});
    }

    /**
     * Delete all nutrient record.
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
    public Cursor getNutrientByDate(String theDate) throws ParseException {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery("SELECT * FROM nutrient_record WHERE date=?", new String[]{theDate});
    }

    public double getCaloriesByDate(String theDate) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT calories FROM nutrient_record WHERE date='" + theDate + "'", null);
        double calories;
        if (cursor.getCount() == 0) {
            calories = 0;
        } else {
            cursor.moveToFirst();
            calories = cursor.getDouble(0);
        }
        return calories;

    }

}
