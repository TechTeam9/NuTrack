package edu.uw.tcss450.nutrack.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.uw.tcss450.nutrack.api.FatSecretHelper;
import edu.uw.tcss450.nutrack.R;
import edu.uw.tcss450.nutrack.TabAdapter;
import edu.uw.tcss450.nutrack.fragment.FoodDialogFragment;
import edu.uw.tcss450.nutrack.fragment.RecipeDialogFragment;
import edu.uw.tcss450.nutrack.fragment.SearchFoodTabFragment;
import edu.uw.tcss450.nutrack.fragment.SearchRecipeTabFragment;
import edu.uw.tcss450.nutrack.model.Food;
import edu.uw.tcss450.nutrack.model.Recipe;


public class NutrientActivity extends AppCompatActivity implements SearchFoodTabFragment.OnFragmentInteractionListener
        , SearchRecipeTabFragment.OnFragmentInteractionListener
        , FoodDialogFragment.OnFragmentInteractionListener
        , RecipeDialogFragment.OnFragmentInteractionListener {

    private static final int REQUEST_PERMISSIONS = 20;

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

        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("back clicked ");
                finish();
            }
        });

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

        FloatingActionButton barcodeScanner = (FloatingActionButton) findViewById(R.id.barcode_scanner);
        barcodeScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBarcodeScanner();
            }
        });
    }

    private void openBarcodeScanner() {
        if (ContextCompat.checkSelfPermission(NutrientActivity.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(NutrientActivity.this, Manifest.permission.CAMERA)) {
                /**
                 * This part need fix
                 */
            } else {
                ActivityCompat.requestPermissions(NutrientActivity.this,
                        new String[]{Manifest.permission
                                .CAMERA},
                        REQUEST_PERMISSIONS);

                /*
                if (ContextCompat.checkSelfPermission(NutrientActivity.this,
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    IntentIntegrator integrator = new IntentIntegrator(this);
                    integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                    integrator.setPrompt("Scan a barcode");
                    integrator.setCameraId(0);  // Use a specific camera of the device
                    integrator.setBeepEnabled(false);
                    integrator.initiateScan();
                }
                */
            }
        } else {

            Intent intent = new Intent(this, BarcodeScannerActivity.class);
            startActivityForResult(intent, 10);
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (10): {
                if (resultCode == Activity.RESULT_OK) {
                    String returnValue = (String) data.getStringExtra("barcode");

                    APIBarcodeSearch barcodeSearch = new APIBarcodeSearch();
                    barcodeSearch.execute(returnValue);
                }
                break;
            }
        }
    }

    @Override
    public void onFragmentInteraction(Food theFood, String theType) {
        Bundle bundle = new Bundle();
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        DialogFragment dialogFragment = new FoodDialogFragment();
        bundle.putParcelable("food_info", theFood);
        bundle.putString("type", theType);

        dialogFragment.setArguments(bundle);
        dialogFragment.show(fragmentManager, "Info Dialog");
    }

    @Override
    public void onFragmentInteraction(Recipe theRecipe, String theType) {
        Bundle bundle = new Bundle();
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        DialogFragment dialogFragment = new RecipeDialogFragment();
        bundle.putParcelable("recipe_info", theRecipe);
        bundle.putString("type", theType);

        dialogFragment.setArguments(bundle);
        dialogFragment.show(fragmentManager, "Info Dialog");
    }

    private class APIBarcodeSearch extends AsyncTask<String, Void, String> {

        private final static String METHOD = "GET";


        @Override
        protected String doInBackground(String... strings) {
            List<String> params = new ArrayList<>(Arrays.asList(FatSecretHelper.generateOauthParams()));
            String[] template = new String[1];
            String response = "";
            params.add("method=food.find_id_for_barcode");
            params.add("barcode=" + Uri.encode(strings[0]));
            params.add("oauth_signature=" + FatSecretHelper.sign(METHOD, FatSecretHelper.URL, params.toArray(template)));

            try {
                java.net.URL url = new URL(FatSecretHelper.URL + "?" + FatSecretHelper.paramify(params.toArray(template)));
                URLConnection api = url.openConnection();
                String line;
                StringBuilder builder = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(api.getInputStream()));
                while ((line = reader.readLine()) != null) builder.append(line);
                response = builder.toString();
            } catch (Exception exception) {
                Log.e("FatSecret Error", exception.toString());
                exception.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            JSONObject jsonObject;
            int foodId;
            try {
                if (result != null) {
                    jsonObject = new JSONObject(result).getJSONObject("food_id");

                    foodId = jsonObject.getInt("value");
                    APIBarcodeGet barcodeGet = new APIBarcodeGet();
                    barcodeGet.execute(String.valueOf(foodId));
                }

            } catch (JSONException exception) {
                Log.e("API Error!", exception.getMessage());
            }
        }
    }

    private class APIBarcodeGet extends AsyncTask<String, Void, String> {

        private final static String METHOD = "GET";

        Food mFood = new Food();

        @Override
        protected String doInBackground(String... strings) {
            List<String> params = new ArrayList<>(Arrays.asList(FatSecretHelper.generateOauthParams()));
            String[] template = new String[1];
            String response = "";
            params.add("method=food.get");
            params.add("food_id=" + Uri.encode(strings[0]));

            params.add("oauth_signature=" + FatSecretHelper.sign(METHOD, FatSecretHelper.URL, params.toArray(template)));

            try {
                java.net.URL url = new URL(FatSecretHelper.URL + "?" + FatSecretHelper.paramify(params.toArray(template)));
                URLConnection api = url.openConnection();
                String line;
                StringBuilder builder = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(api.getInputStream()));
                while ((line = reader.readLine()) != null) builder.append(line);
                response = builder.toString();
            } catch (Exception exception) {
                Log.e("FatSecret Error", exception.toString());
                exception.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            JSONObject jsonObject;
            JSONArray jsonArray;

            ArrayList<Double> calorieList = new ArrayList<>();
            ArrayList<Double> fatList = new ArrayList<>();
            ArrayList<Double> carbsList = new ArrayList<>();
            ArrayList<Double> proteinList = new ArrayList<>();
            ArrayList<String> urlList = new ArrayList<>();
            ArrayList<String> servingList = new ArrayList<>();
            ArrayList<Integer> servingIdList = new ArrayList<>();

            try {
                if (result != null) {
                    mFood.setId(new JSONObject(result).getJSONObject("food").getInt("food_id"));
                    mFood.setName(new JSONObject(result).getJSONObject("food").getString("food_name"));

                    jsonObject = new JSONObject(result).getJSONObject("food").getJSONObject("servings");
                    JSONObject servingObject = jsonObject.optJSONObject("serving");
                    if (servingObject != null) {
                        calorieList.add(servingObject.getDouble("calories"));
                        fatList.add(servingObject.getDouble("fat"));
                        carbsList.add(servingObject.getDouble("carbohydrate"));
                        proteinList.add(servingObject.getDouble("protein"));
                        if (servingObject.has("serving_url")) {
                            urlList.add(servingObject.getString("serving_url"));
                        }
                        if (servingObject.has("metric_serving_amount")) {
                            servingList.add(servingObject.getDouble("metric_serving_amount")
                                    + servingObject.getString("metric_serving_unit"));
                        }
                        if (servingObject.has("serving_id")) {
                            servingIdList.add(servingObject.getInt("serving_id"));
                        }
                    } else {
                        jsonArray = jsonObject.getJSONArray("serving");
                        if (jsonArray != null) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject serving = jsonArray.getJSONObject(i);
                                calorieList.add(serving.getDouble("calories"));
                                fatList.add(serving.getDouble("fat"));
                                carbsList.add(serving.getDouble("carbohydrate"));
                                proteinList.add(serving.getDouble("protein"));
                                if (serving.has("serving_url")) {
                                    urlList.add(serving.getString("serving_url"));
                                }
                                if (serving.has("metric_serving_amount")) {
                                    servingList.add(serving.getDouble("metric_serving_amount")
                                            + serving.getString("metric_serving_unit"));
                                }
                                if (serving.has("serving_id")) {
                                    servingIdList.add(serving.getInt("serving_id"));
                                }
                            }
                        }
                    }

                    mFood.setCalorie(calorieList);
                    mFood.setFat(fatList);
                    mFood.setCarbs(carbsList);
                    mFood.setProtein(proteinList);
                    mFood.setmURL(urlList);
                    mFood.setmServing(servingList);
                    mFood.setServingId(servingIdList);
                    onFragmentInteraction(mFood, "write");
                }

            } catch (JSONException exception) {
                Log.e("API Error!", exception.getMessage());
            }
        }
    }
}
