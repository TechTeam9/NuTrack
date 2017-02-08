package edu.uw.tcss450.nutrack.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
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
import android.view.MenuItem;
import android.widget.ImageView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.util.ArrayList;

import edu.uw.tcss450.nutrack.DBHelper.DBMemberTableHelper;
import edu.uw.tcss450.nutrack.R;
import edu.uw.tcss450.nutrack.fragment.LookUpFoodFragment;
import edu.uw.tcss450.nutrack.fragment.MainFragment;
import edu.uw.tcss450.nutrack.fragment.ProfileFragment;
import edu.uw.tcss450.nutrack.fragment.SearchResultFragment;
import edu.uw.tcss450.nutrack.fragment.SettingFragment;

public class MainActivity extends AppCompatActivity implements ProfileFragment.OnFragmentInteractionListener, LookUpFoodFragment.OnFragmentInteractionListener, MainFragment.OnFragmentInteractionListener, SettingFragment.OnFragmentInteractionListener, SearchResultFragment.OnFragmentInteractionListener{

    private DrawerLayout mDrawer;

    private Toolbar mToolbar;

    private NavigationView mNaviDrawer;

    private ActionBarDrawerToggle mDrawerToggle;

    private SQLiteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle("Home");

        mNaviDrawer = (NavigationView) findViewById(R.id.naviView);
        mDrawer = (DrawerLayout) findViewById(R.id.main_frame);

        setSupportActionBar(mToolbar);

        mDrawerToggle = setupDrawerToggle();

        mDrawer.addDrawerListener(mDrawerToggle);

        initializeDrawerContent();

        Class fragmentClass = MainFragment.class;
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        FragmentTransaction fragmentTracs = getSupportFragmentManager().beginTransaction();
        fragmentTracs.add(R.id.flContent, fragment).commit();

        initializeFloatingActionButton();

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

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

    public void initializeDrawerContent() {
        mNaviDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectDrawerItem(item);
                return true;
            }
        });
    }

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

    public void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment = null;

        Class fragmentClass = null;
        switch(menuItem.getItemId()) {
            case R.id.nav_profile:
                fragmentClass = ProfileFragment.class;
                break;
            case R.id.nav_home:
                fragmentClass = MainFragment.class;
                break;
            case R.id.nav_settings:
                Intent intent = new Intent(this, ProfileSetupActivity.class);
                startActivity(intent);
                //fragmentClass = SettingFragment.class;
                break;
            case R.id.nav_sign_out:
                userSignOut();
                break;

            case R.id.nav_calories_calculator:
                fragmentClass = LookUpFoodFragment.class;
                break;
            default:
                fragmentClass = MainFragment.class;
        }

        if (menuItem.getItemId() != R.id.nav_sign_out && menuItem.getItemId() != R.id.nav_settings) {
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        }
        menuItem.setChecked(true);
        mToolbar.setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();

    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.drawer_open, R.string.drawer_close);
    }

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

    private void userSignOut() {
        DBMemberTableHelper dbMemberTableHelper = new DBMemberTableHelper(this);
        dbMemberTableHelper.deleteData();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

