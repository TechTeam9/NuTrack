package edu.uw.tcss450.nutrack;

import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import edu.uw.tcss450.nutrack.fragment.CaloriesCalculator;

public class Main extends AppCompatActivity implements Profile.OnFragmentInteractionListener, CaloriesCalculator.OnFragmentInteractionListener{

    private DrawerLayout myDrawer;

    private Toolbar myToolbar;

    private NavigationView myNaviDrawer;

    private ActionBarDrawerToggle myDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        myToolbar.setTitleTextColor(Color.WHITE);
        myToolbar.setTitle("Home");

        myNaviDrawer = (NavigationView) findViewById(R.id.naviView);
        myDrawer = (DrawerLayout) findViewById(R.id.main_frame);

        setSupportActionBar(myToolbar);

        myDrawerToggle = setupDrawerToggle();

        myDrawer.addDrawerListener(myDrawerToggle);


        initializeDrawerContent();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        myDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        myDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                myDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void initializeDrawerContent() {
        myNaviDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectDrawerItem(item);
                return true;
            }
        });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment = null;

        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.nav_profile:
                fragmentClass = Profile.class;
                break;
            case R.id.nav_home:
                fragmentClass = Main.class;
                break;

            default:
                fragmentClass = CaloriesCalculator.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        menuItem.setChecked(true);
        myToolbar.setTitle(menuItem.getTitle());
        myDrawer.closeDrawers();
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, myDrawer, myToolbar, R.string.drawer_open, R.string.drawer_close);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

