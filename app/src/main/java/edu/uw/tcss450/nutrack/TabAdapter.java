package edu.uw.tcss450.nutrack;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import edu.uw.tcss450.nutrack.fragment.SearchFoodTabFragment;
import edu.uw.tcss450.nutrack.fragment.SearchRecipeTabFragment;

/**
 * Created by Ming on 2/25/2017.
 */

public class TabAdapter  extends FragmentStatePagerAdapter{
    private int mNumTabs;

    public TabAdapter(FragmentManager fm) {
        super(fm);
        mNumTabs = 2;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                SearchFoodTabFragment tabFood = new SearchFoodTabFragment();
                return tabFood;
            case 1:
                SearchRecipeTabFragment tabRecipe = new SearchRecipeTabFragment();
                return tabRecipe;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumTabs;
    }
}
