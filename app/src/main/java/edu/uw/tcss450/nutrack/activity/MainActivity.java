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

import edu.uw.tcss450.nutrack.DBHelper.DBMemberTableHelper;
import edu.uw.tcss450.nutrack.R;
import edu.uw.tcss450.nutrack.fragment.LookUpFoodFragment;
import edu.uw.tcss450.nutrack.fragment.MainFragment;
import edu.uw.tcss450.nutrack.fragment.ProfileFragment;
import edu.uw.tcss450.nutrack.fragment.SettingFragment;

public class MainActivity extends AppCompatActivity implements ProfileFragment.OnFragmentInteractionListener, LookUpFoodFragment.OnFragmentInteractionListener, MainFragment.OnFragmentInteractionListener, SettingFragment.OnFragmentInteractionListener{

    private DrawerLayout myDrawer;

    private Toolbar myToolbar;

    private NavigationView myNaviDrawer;

    private ActionBarDrawerToggle myDrawerToggle;

    private SQLiteDatabase myDatabase;

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
                fragmentClass = SettingFragment.class;
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

        if (menuItem.getItemId() != R.id.nav_sign_out) {
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        }
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

    private void userSignOut() {
        DBMemberTableHelper dbMemberTableHelper = new DBMemberTableHelper(this);
        dbMemberTableHelper.deleteData();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}

