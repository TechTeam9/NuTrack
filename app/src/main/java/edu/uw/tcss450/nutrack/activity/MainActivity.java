package edu.uw.tcss450.nutrack.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
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
import android.widget.Toast;

import java.util.ArrayList;

import edu.uw.tcss450.nutrack.database.DBMemberInfo;
import edu.uw.tcss450.nutrack.database.DBPersonalInfo;
import edu.uw.tcss450.nutrack.fragment.DailyIntakeOverviewFragment;
import edu.uw.tcss450.nutrack.fragment.LookUpFoodFragment;
import edu.uw.tcss450.nutrack.fragment.MonthlyWeightOverviewFragment;
import edu.uw.tcss450.nutrack.fragment.OverviewFragment;
import edu.uw.tcss450.nutrack.fragment.WeeklyIntakeOverviewFragment;
import edu.uw.tcss450.nutrack.helper.ProfileHelper;
import edu.uw.tcss450.nutrack.R;
import edu.uw.tcss450.nutrack.fragment.DailyLogFragment;
import edu.uw.tcss450.nutrack.fragment.EditProfileDialogFragment;
import edu.uw.tcss450.nutrack.fragment.ProfileFragment;
import edu.uw.tcss450.nutrack.fragment.SearchResultFragment;
import edu.uw.tcss450.nutrack.fragment.SettingFragment;
import edu.uw.tcss450.nutrack.model.Food;
import edu.uw.tcss450.nutrack.model.Profile;

import static edu.uw.tcss450.nutrack.R.id.naviView;

/**
 * Main container holding all fragment activity.
 * ProfileFragment, LookUpFoodFragment, SearchResultFragment, and SettingFragment.
 */
public class MainActivity extends AppCompatActivity implements ProfileFragment.OnFragmentInteractionListener,
        LookUpFoodFragment.OnFragmentInteractionListener, OverviewFragment.OnFragmentInteractionListener,
        SettingFragment.OnFragmentInteractionListener, SearchResultFragment.OnFragmentInteractionListener,
        EditProfileDialogFragment.OnFragmentInteractionListener, DailyIntakeOverviewFragment.OnFragmentInteractionListener,
        WeeklyIntakeOverviewFragment.OnFragmentInteractionListener,MonthlyWeightOverviewFragment.OnFragmentInteractionListener, DailyLogFragment.OnFragmentInteractionListener {
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
            Fragment fragment = null;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            FragmentTransaction fragmentTracs = getSupportFragmentManager().beginTransaction();
            fragmentTracs.add(R.id.flContent, fragment);

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
        Toast.makeText(this, "Please use the sign out button to sign out. Thank you.", Toast.LENGTH_LONG).show();
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
                break;
            case R.id.nav_overview:
                fragmentClass = OverviewFragment.class;
                break;
            case R.id.nav_Daily_log:
                fragmentClass = DailyLogFragment.class;
                break;
            case R.id.nav_settings:
                fragmentClass = SettingFragment.class;
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
        DBMemberInfo DBMemberInfo = new DBMemberInfo(this);
        DBMemberInfo.deleteData();

        DBPersonalInfo dbPersonalInfo = new DBPersonalInfo(this);
        dbPersonalInfo.deletePersonalInfo();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onFragmentInteraction() {
        mFragment = new ProfileFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, mFragment).commit();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteraction(Food food) {

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
        TextView email = (TextView) headerView.findViewById(R.id.name);

        Log.i("WHAT", "avatar " + profile.getAvatarId());

        imageAvatar.setImageResource(profile.getAvatarId());
        name.setText(profile.getName());
        //email.setText(account.getEmail());
        //LAP TEST
    }
}