package edu.uw.tcss450.nutrack.helper;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import edu.uw.tcss450.nutrack.database.DBFoodRecentSearch;
import edu.uw.tcss450.nutrack.database.DBRecipeRecentSearch;
import edu.uw.tcss450.nutrack.model.Food;
import edu.uw.tcss450.nutrack.model.Recipe;

/**
 * Created by Ming on 3/4/2017.
 */

public class RecentSearchHelper {

    private DBFoodRecentSearch mFoodTable;

    private DBRecipeRecentSearch mRecipeTable;

    public RecentSearchHelper(Context theContext) {
            mFoodTable = new DBFoodRecentSearch(theContext);
            mRecipeTable = new DBRecipeRecentSearch(theContext);
    }

    public Cursor getFoodRecentSearch() {
        Cursor cursor = mFoodTable.getAllFood();
        return cursor;
    }

    public Cursor getRecipeRecentSearch() {
        Cursor cursor = mRecipeTable.getAllRecipe();
        return cursor;
    }

    public void addFoodRecentSearch(String theFood) {
        mFoodTable.insertFood(theFood);
    }

    public void addRecipeRecentSearch(String theRecipe) {
        mRecipeTable.insertRecipe(theRecipe);
    }

    public void clearFoodRecentSearch() {
        mFoodTable.deleteAll();
    }

    public void clearRecipeRecentSearch() {
        mRecipeTable.deleteAll();
    }

    public void close() {
        mFoodTable.close();
        mRecipeTable.close();
    }
}
