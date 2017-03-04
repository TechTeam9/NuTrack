package edu.uw.tcss450.nutrack.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

import edu.uw.tcss450.nutrack.R;
import edu.uw.tcss450.nutrack.TabAdapter;
import edu.uw.tcss450.nutrack.fragment.LookUpFoodFragment;


public class NutrientActivity extends AppCompatActivity implements LookUpFoodFragment.OnFragmentInteractionListener{

    private Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrient);

        View customTitle = getLayoutInflater().inflate(R.layout.toolbar, null);
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mToolBar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);


        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle(null);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Food"));
        tabLayout.addTab(tabLayout.newTab().setText("Recipe"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final TabAdapter adapter = new TabAdapter(getSupportFragmentManager());

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        TabLayout tabs = (TabLayout) findViewById(R.id.tab_layout);
        //viewPager.setVisibility(View.GONE);
        //tabs.setVisibility(View.GONE);

        /*
        Class fragmentClass = LookUpFoodFragment.class;
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentTransaction fragmentTracs = getSupportFragmentManager().beginTransaction();
        fragmentTracs.add(R.id.nutrient_fragment_content, fragment).commit();
        */
    }



    @Override
    public void onFragmentInteraction(String theFoodName, ArrayList<String> theFoodList) {

    }
}
