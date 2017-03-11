package edu.uw.tcss450.nutrack.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.uw.tcss450.nutrack.database.DBDailyLog;
import edu.uw.tcss450.nutrack.database.DBFoodRecentSearch;
import edu.uw.tcss450.nutrack.database.DBNutrientRecord;
import edu.uw.tcss450.nutrack.database.DBRecipeRecentSearch;
import edu.uw.tcss450.nutrack.database.DBWeight;
import edu.uw.tcss450.nutrack.fragment.DailyIntakeOverviewFragment;
import edu.uw.tcss450.nutrack.fragment.FoodDialogFragment;
import edu.uw.tcss450.nutrack.fragment.WeeklyWeightOverviewFragment;
import edu.uw.tcss450.nutrack.fragment.OverviewFragment;
import edu.uw.tcss450.nutrack.fragment.RecipeDialogFragment;
import edu.uw.tcss450.nutrack.fragment.WeeklyCaloriesOverviewFragment;
import edu.uw.tcss450.nutrack.helper.ProfileHelper;
import edu.uw.tcss450.nutrack.R;
import edu.uw.tcss450.nutrack.fragment.DailyLogFragment;
import edu.uw.tcss450.nutrack.fragment.EditProfileDialogFragment;
import edu.uw.tcss450.nutrack.fragment.ProfileFragment;
import edu.uw.tcss450.nutrack.fragment.SearchResultFragment;
import edu.uw.tcss450.nutrack.fragment.SettingFragment;
import edu.uw.tcss450.nutrack.model.Food;
import edu.uw.tcss450.nutrack.model.Profile;
import edu.uw.tcss450.nutrack.model.Recipe;

import static edu.uw.tcss450.nutrack.R.id.naviView;

/**
 * Main container holding all fragment activity.
 * ProfileFragment, LookUpFoodFragment, SearchResultFragment, and SettingFragment.
 */
public class MainActivity extends AppCompatActivity implements ProfileFragment.OnFragmentInteractionListener, OverviewFragment.OnFragmentInteractionListener,
        SettingFragment.OnFragmentInteractionListener, SearchResultFragment.OnFragmentInteractionListener,
        EditProfileDialogFragment.OnFragmentInteractionListener, DailyIntakeOverviewFragment.OnFragmentInteractionListener,
        WeeklyCaloriesOverviewFragment.OnFragmentInteractionListener, WeeklyWeightOverviewFragment.OnFragmentInteractionListener,
        DailyLogFragment.OnFragmentInteractionListener, FoodDialogFragment.OnFragmentInteractionListener, RecipeDialogFragment.OnFragmentInteractionListener {

    private static final int NUTRIENT_ACTIVITY_CODE = 1101;


    /**
     * The layout that hold the navigation drawer.
     */
    private DrawerLayout mDrawer;

    /**
     * The top tool bar.
     */
    private Toolbar mToolbar;

    /**
     * The navigation drawer.
     */
    private NavigationView mNaviDrawer;

    private Fragment mFragment;

    private String mCurrentFragment;

    /**
     * The toggle for expanding and collapsing the drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;

    /**
     * Builds and sets up MainActivity.
     *
     * @param savedInstanceState the saved instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Overview");
        mToolbar.setTitleTextColor(Color.WHITE);


        mNaviDrawer = (NavigationView) findViewById(naviView);
        mDrawer = (DrawerLayout) findViewById(R.id.main_frame);

        setSupportActionBar(mToolbar);

        mDrawerToggle = setupDrawerToggle();

        mDrawer.addDrawerListener(mDrawerToggle);

        initializeDrawerContent();
        initializeDrawerHeaderContent();

        if (savedInstanceState == null) {
            Class fragmentClass = OverviewFragment.class;
            try {
                mFragment = (Fragment) fragmentClass.newInstance();
                mCurrentFragment = "overview";
            } catch (Exception e) {
                e.printStackTrace();
            }

            FragmentTransaction fragmentTracs = getSupportFragmentManager().beginTransaction();
            fragmentTracs.add(R.id.flContent, mFragment);

            fragmentTracs.commit();
        }
        //initializeFloatingActionButton();
    }

    /**
     * Sets the initial state of the drawer.
     *
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    /**
     * Toggles the state of the drawer.
     *
     * @param newConfig configuration
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Initializes the Drawer content.
     */
    public void initializeDrawerContent() {
        mNaviDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectDrawerItem(item);
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(mCurrentFragment.equals("overview")) {
            finish();

            android.os.Process.killProcess(android.os.Process.myPid());
            super.onDestroy();
        } else {
            mFragment = new OverviewFragment();
            mCurrentFragment = "overview";
            mToolbar.setTitle("Overview");

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, mFragment).commit();

        }



    }

    /* SAVE THIS PART OF THE CODE FOR NEXT VERSION. NOT USING IT IN CURRENT VERSION
    public void initializeFloatingActionButton() {
    //Floating Menu Icon
    ImageView floatingMenuIcon = new ImageView(this);
    floatingMenuIcon.setImageResource(R.mipmap.ic_launcher);

    final FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
            .setContentView(floatingMenuIcon)
            .build();


    //Variable naming in here is not final yet, will rename after decide what button to add.
    SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
    ImageView itemIcon1 = new ImageView(this);
    itemIcon1.setImageResource(R.mipmap.ic_launcher);

    ImageView itemIcon2 = new ImageView(this);
    itemIcon2.setImageResource(R.mipmap.ic_launcher);

    ImageView itemIcon3 = new ImageView(this);
    itemIcon3.setImageResource(R.mipmap.ic_launcher);

    ImageView itemIcon4 = new ImageView(this);
    itemIcon4.setImageResource(R.mipmap.ic_launcher);

    SubActionButton button1 = itemBuilder.setContentView(itemIcon1).build();
    SubActionButton button2 = itemBuilder.setContentView(itemIcon2).build();
    SubActionButton button3 = itemBuilder.setContentView(itemIcon3).build();
    SubActionButton button4 = itemBuilder.setContentView(itemIcon4).build();


    final FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
            .addSubActionView(button1)
            .addSubActionView(button2)
            .addSubActionView(button3)
            .addSubActionView(button4)
            .attachTo(actionButton)
            .build();

    }
    */

    /**
     * Allows the user to select a menu item from the slide out drawer.
     *
     * @param theMenuItem the menu item selected by the user
     */
    public void selectDrawerItem(MenuItem theMenuItem) {
        mFragment = null;

        Class fragmentClass = null;
        switch (theMenuItem.getItemId()) {
            case R.id.nav_profile:
                fragmentClass = ProfileFragment.class;
                mCurrentFragment = "profile";
                mToolbar.setTitle("Profile");
                break;
            case R.id.nav_overview:
                fragmentClass = OverviewFragment.class;
                mCurrentFragment = "overview";
                mToolbar.setTitle("Overview");
                break;
            case R.id.nav_Daily_log:
                fragmentClass = DailyLogFragment.class;
                mCurrentFragment = "dailyLog";
                mToolbar.setTitle("Daily Log");
                break;
            case R.id.nav_settings:
                fragmentClass = SettingFragment.class;
                mCurrentFragment = "settings";
                mToolbar.setTitle("Settings");
                break;
            case R.id.nav_sign_out:
                userSignOut();
                break;
            case R.id.nav_add_food:
                Intent intent = new Intent(this, NutrientActivity.class);
                startActivity(intent);
                break;
            default:
                fragmentClass = OverviewFragment.class;
        }

        if (theMenuItem.getItemId() != R.id.nav_sign_out && theMenuItem.getItemId() != R.id.nav_add_food) {
            try {
                mFragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, mFragment).commit();
        }
        theMenuItem.setChecked(true);
        mDrawer.closeDrawers();

    }

    /**
     * Constructs a Drawer toggle to open and close the navigation drawer.
     *
     * @return the created ActionBarDrawerToggle
     */
    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.drawer_open, R.string.drawer_close);
    }

    /**
     * Handles interactions from LookUpFoodFragment and passes along the food and food list to a
     * new instance of SearchResultFragment.
     *
     * @param theFoodName the name of the food item selected
     * @param theFoodList the list of searched food items
     */
    @Override
    public void onFragmentInteraction(String theFoodName, ArrayList<String> theFoodList) {
        SearchResultFragment fragment = new SearchResultFragment();
        Bundle args = new Bundle();
        args.putString("food_name", theFoodName);
        args.putStringArrayList("food_list", theFoodList);
        fragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flContent, fragment);
        // Commit the transaction
        transaction.commit();
    }

    /**
     * Cleans up after the user logs out.
     */
    private void userSignOut() {
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_account), Context.MODE_PRIVATE);

        sharedPref.edit().remove("email").commit();
        sharedPref.edit().remove("password").commit();

        SharedPreferences sharedPrefProfile = this.getSharedPreferences(getString(R.string.preference_profile), Context.MODE_PRIVATE);
        sharedPrefProfile.edit().clear().commit();

        DBWeight dbWeight = new DBWeight(this);
        dbWeight.deleteAll();
        dbWeight.close();

        DBDailyLog dbDailyLog = new DBDailyLog(this);
        dbDailyLog.deleteAll();
        dbDailyLog.close();

        DBNutrientRecord dbNutrientRecord = new DBNutrientRecord(this);
        dbNutrientRecord.deleteAll();
        dbNutrientRecord.close();

        DBFoodRecentSearch dbFoodRecentSearch = new DBFoodRecentSearch(this);
        dbFoodRecentSearch.deleteAll();
        dbFoodRecentSearch.close();

        DBRecipeRecentSearch dbRecipeRecentSearch = new DBRecipeRecentSearch(this);
        dbRecipeRecentSearch.deleteAll();
        dbRecipeRecentSearch.close();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mFragment != null) {
            switch (mCurrentFragment) {
                case "overview":
                    mFragment = new OverviewFragment();
                    break;
                case "profile":
                    mFragment = new ProfileFragment();
                    break;
                case "dailyLog":
                    mFragment = new DailyLogFragment();
                    break;
                case "settings":
                    mFragment = new SettingFragment();
                    break;
                default:
                    mFragment = new OverviewFragment();
                    break;
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, mFragment).commit();
        }

    }



    @Override
    public void onFragmentInteraction() {

        switch (mCurrentFragment) {
            case "overview":
                mFragment = new OverviewFragment();
                break;
            case "profile":
                mFragment = new ProfileFragment();
                break;
            case "dailyLog":
                mFragment = new DailyLogFragment();
                break;
            case "settings":
                mFragment = new SettingFragment();
                break;
            default:
                mFragment = new OverviewFragment();
                break;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, mFragment).commit();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteraction(Food theFood, String theType) {
        Bundle bundle = new Bundle();
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        DialogFragment dialogFragment = null;
        bundle.putParcelable("food_info", theFood);
        bundle.putString("type", theType);
        dialogFragment = new FoodDialogFragment();


        dialogFragment.setArguments(bundle);
        dialogFragment.show(fragmentManager, "Info Dialog");
    }

    @Override
    public void onFragmentInteraction(Recipe theRecipe, String theType) {
        Bundle bundle = new Bundle();
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        DialogFragment dialogFragment = null;
        bundle.putParcelable("recipe_info", theRecipe);
        bundle.putString("type", theType);
        dialogFragment = new RecipeDialogFragment();

        dialogFragment.setArguments(bundle);
        dialogFragment.show(fragmentManager, "Info Dialog");
    }

    @Override
    public void onFragmentInteraction(String theMessage) {
        if (theMessage.equals("nutrient")) {
            Intent intent = new Intent(this, NutrientActivity.class);
            startActivityForResult(intent, NUTRIENT_ACTIVITY_CODE);
        }
    }

    /**
     * Switches out the current fragment for the profile fragment.
     * Adrian
     * @param view The current view.
     */
    public void goToProfile(View view){
        Fragment fragment = null;
        Class fragmentClass = ProfileFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
    mToolbar.setTitle("Profile");
    mDrawer.closeDrawers();
    }

    /**
     * Initializes the user's image in the nav drawer.
     * Trying to get this working and failing.
     */
    public void initializeDrawerHeaderContent() {
        Context context = this;
        View headerView = mNaviDrawer.getHeaderView(0);
        Profile profile = ProfileHelper.getPersonalInfo(context);
        //Account account = AccountHelper.getAccountInfo(context);

        ImageView imageAvatar = (ImageView) headerView.findViewById(R.id.nav_avatar_image);
        TextView name = (TextView) headerView.findViewById(R.id.name);
        TextView email = (TextView) headerView.findViewById(R.id.email);

        Log.i("WHAT", "avatar " + profile.getAvatarId());

        imageAvatar.setImageResource(profile.getAvatarId());
        name.setText(profile.getName());
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_account), Context.MODE_PRIVATE);
        String userEmail = sharedPref.getString("email", "null");
        if (userEmail.equals("null")) {
            email.setText("");
        } else {
            email.setText(userEmail);
        }
        //LAP TEST
    }
}