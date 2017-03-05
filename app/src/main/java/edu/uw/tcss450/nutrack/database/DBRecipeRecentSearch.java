package edu.uw.tcss450.nutrack.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ming on 3/4/2017.
 */

public class DBRecipeRecentSearch extends SQLiteOpenHelper {

    /**
     * Database name.
     */
    private static final String DATABASE_NAME = "nutrack.db";

    /**
     * Table name.
     */
    private static final String TABLE_NAME = "recipe_recent_search";

    /**
     * Food name column.
     */
    private static final String COLUMN_FOOD_NAME = "recipe_name";

    /**
     * Time column.
     */
    private static final String COLUMN_TIME = "search_time";

    /**
     * Constructor.
     *
     * @param context
     */
    public DBRecipeRecentSearch(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS recipe_recent_search(recipe_name TEXT, search_time NUMERIC)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS recipe_recent_search(recipe_name TEXT, search_time NUMERIC)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertRecipe(String theRecipe) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues recipe = new ContentValues();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        recipe.put(COLUMN_FOOD_NAME, theRecipe);
        recipe.put(COLUMN_TIME, dateFormat.format(date));

        db.insert(TABLE_NAME, null, recipe);

        return true;
    }


    public boolean deleteRecipe(String theRecipe) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] recipeArr = {theRecipe};
        db.delete(TABLE_NAME, "recipe_name", recipeArr);

        return true;
    }

    public boolean deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        return true;
    }


    public Cursor getAllRecipe() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM recipe_recent_search", null);
    }

}


