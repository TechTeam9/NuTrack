package edu.uw.tcss450.nutrack.database;

import android.content.ContentValues;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.media.tv.TvContract.Channels.COLUMN_TYPE;
import static edu.uw.tcss450.nutrack.R.color.calories;
import static edu.uw.tcss450.nutrack.R.string.carbs;
import static edu.uw.tcss450.nutrack.R.string.fat;
import static edu.uw.tcss450.nutrack.R.string.protein;

/**
 * Created by kuenk on 3/7/2017.
 */

public class DBWeight extends SQLiteOpenHelper {
    /**
     * Database name.
     */
    private static final String DATABASE_NAME = "nutrack.db";

    private static final String TABLE_NAME = "weight_log";

    private static final String COLUMN_DATE = "date";

    private static final String COLUMN_WEIGHT = "weight";

    /**
     * Constructor.
     *
     * @param context
     */
    public DBWeight(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS weight_log(date TEXT PRIMARY KEY, weight INTEGER)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS weight_log(date TEXT PRIMARY KEY, weight INTEGER)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertWeight(String theDate, int theWeight) {
        SQLiteDatabase dbRead = this.getReadableDatabase();
        SQLiteDatabase dbWrite = this.getWritableDatabase();

        Cursor cursor = dbRead.rawQuery("SELECT * FROM weight_log WHERE date='" + theDate + "'", null);

        if (cursor.getCount() == 0) {
            ContentValues nutrient = new ContentValues();
            nutrient.put(COLUMN_DATE, theDate);
            nutrient.put(COLUMN_WEIGHT, theWeight);

            dbWrite.insert(TABLE_NAME, null, nutrient);
        } else {
            cursor.moveToFirst();
            ContentValues nutrient = new ContentValues();
            nutrient.put("weight", theWeight);

            dbWrite.update(TABLE_NAME, nutrient, "date=?", new String[]{theDate});
        }
    }

    public Cursor getTodayWeight() {
        SQLiteDatabase dbRead = this.getReadableDatabase();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        return dbRead.rawQuery("SELECT * FROM weight_log WHERE date='" + dateFormat.format(date) + "'", null);
    }

    public ArrayList<Integer> getWeight(Date theDate) {
        SQLiteDatabase dbRead = this.getReadableDatabase();

        ArrayList<Integer> weightList = new ArrayList<>();
        ArrayList<String> dateList = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date = theDate;
        Date dateBefore = date;

//  Cursor cursor = dbRead.rawQuery("SELECT * FROM weight_log WHERE date BETWEEN "2011-01-11" AND "2011-8-11", null);
        for (int i = 1; i < 8; i++) {
            System.out.println(dateFormat.format(dateBefore));
            Cursor cursor = dbRead.rawQuery("SELECT * FROM weight_log WHERE date='" + dateFormat.format(dateBefore) + "'", null);
            int tempWeight;
            int d;

            dateList.add(dateFormat.format(dateBefore));
            if (cursor.getCount() == 0) {
                tempWeight = 0;
            } else {
                cursor.moveToFirst();
                tempWeight = cursor.getInt(1);
            }
            weightList.add(tempWeight);

            dateBefore = new Date(date.getTime() - i * 24 * 3600 * 1000l);
        }

        return weightList;
    }


}
