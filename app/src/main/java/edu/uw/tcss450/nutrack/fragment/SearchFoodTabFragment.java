package edu.uw.tcss450.nutrack.fragment;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import edu.uw.tcss450.nutrack.api.FatSecretHelper;
import edu.uw.tcss450.nutrack.database.DBFoodRecentSearch;
import edu.uw.tcss450.nutrack.R;
import edu.uw.tcss450.nutrack.activity.NutrientActivity;
import edu.uw.tcss450.nutrack.database.DBRecipeRecentSearch;
import edu.uw.tcss450.nutrack.helper.RecentSearchHelper;
import edu.uw.tcss450.nutrack.model.Food;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static java.security.AccessController.getContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFoodTabFragment extends Fragment {
    /**
     * The chosen food.
     */
    private String mFood;

    /**
     * The current context.
     */
    private Context mContext;

    private ListView mListView;

    private ArrayList<Integer> mFoodId;

    /**
     * A view holding a whole fragment.
     */
    private View mView;

    /**
     * TheLookUpFoodFragment interaction listener.
     */
    private SearchFoodTabFragment.OnFragmentInteractionListener mListener;

    public SearchFoodTabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_search_food_tab, container, false);

        mContext = container.getContext();
        initializeSearchListener(mView);
        initializeRecentSearchList(mView);

        return mView;
    }

    /**
     * Start searching listener.
     *
     * @param theView showing view.
     */
    private void initializeSearchListener(View theView) {
        final SearchView searchView = (SearchView) theView.findViewById(R.id.lookUp_searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String searchInput = searchView.getQuery().toString();
                searchView.clearFocus();
                searchFood(searchInput);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });
        /*
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        */
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SearchFoodTabFragment.OnFragmentInteractionListener) {
            mListener = (SearchFoodTabFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    /**
     * Start the Recent search record list.
     *
     * @param theView showing view.
     */
    private void initializeRecentSearchList(View theView) {
        RecentSearchHelper helper = new RecentSearchHelper(getContext());
        Cursor foods = helper.getFoodRecentSearch();

        if (foods.getCount() != 0) {
            ArrayList<HashMap<String, String>> foodList = new ArrayList<>();

            int i = 0;
            foods.moveToLast();
            while (!foods.isFirst()) {
                Date currentTime = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date searchTime = null;
                try {
                    searchTime = dateFormat.parse(foods.getString(1));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String timeDifference = compareTime(currentTime, searchTime);
                HashMap<String, String> tempFood = new HashMap<>();
                tempFood.put("food", foods.getString(0));
                tempFood.put("time", timeDifference);
                foodList.add(tempFood);
                foods.moveToPrevious();
            }

            if (foods.isFirst()) {
                Date currentTime = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date searchTime = null;
                try {
                    searchTime = dateFormat.parse(foods.getString(1));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String timeDifference = compareTime(currentTime, searchTime);
                HashMap<String, String> tempFood = new HashMap<>();
                tempFood.put("food", foods.getString(0));
                tempFood.put("time", timeDifference);
                foodList.add(tempFood);
                foods.moveToPrevious();
            }

            //foodList.add(foods.getString(0));
            String formArray[] = {"food", "time"};
            int to[] = {R.id.recentSearch_textView_food, R.id.recentSearch_textView_time};
            SimpleAdapter adapter = new SimpleAdapter(getContext(), foodList, R.layout.list_view_recent_search_items, formArray, to);
            mListView = (ListView) theView.findViewById(R.id.lookUp_listView);
            mListView.setAdapter(adapter);

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, final View view,
                                        int position, long id) {
                    HashMap<String, Object> item = (HashMap<String, Object>) parent.getItemAtPosition(position);
                    final String food = (String) item.get("food");
                    searchFood(food);
                }
            });
        } else {
            mListView = (ListView) theView.findViewById(R.id.lookUp_listView);
        }

        helper.close();
    }

    /**
     * Compare current time and searching time
     *
     * @param theCurrentTime system current time
     * @param theSearchTime  time that doing search
     * @return a string that can tell how long ago did this search
     */
    private String compareTime(Date theCurrentTime, Date theSearchTime) {
        long difference = theCurrentTime.getTime() - theSearchTime.getTime();
        difference = difference / 1000;

        //NEED TO FIX ALL THE MAGIC NUMBERS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        if (difference < 60) {
            return difference + " seconds ago";
        } else if (difference >= 60 && difference < 3600) {
            difference = difference / 60;

            return difference + "  minutes ago";
        } else if (difference >= 3600 && difference < 86400) {
            difference = difference / 60 / 60;

            return difference + " hours ago";
        } else {
            return "Long times ago";
        }
    }

    /**
     * Searches for the entered food via the FatSecret API.
     *
     * @param theFood The searched food.
     */
    private void searchFood(final String theFood) {
        RecentSearchHelper helper = new RecentSearchHelper(getContext());
        helper.addFoodRecentSearch(theFood);
        mFood = theFood;
        // FOR API USES!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        final APIFoodSearch mFatSecret = new APIFoodSearch();
        mFatSecret.execute(theFood);

    }

    /**
     * Passes the food and food list back to the activity to be inorder to create a
     * new instance of SearchResultFragment.
     *
     * @param theFoodName The searched food.
     * @param theFoodList The food list.
     */
    private void goToResult(String theFoodName, ArrayList<String> theFoodList) {
        mListView.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, theFoodList));
//        listView.invalidate();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                mListView.setClickable(false);
//                listView.setVisibility(View.INVISIBLE);
                Bundle bundle = new Bundle();

                final APIFoodGet mFatSecret = new APIFoodGet();
                mFatSecret.execute(String.valueOf(mFoodId.get(position)));

            }
        });

    }


    private class APIFoodSearch extends AsyncTask<String, Void, String> {

        private final static String METHOD = "GET";


        @Override
        protected String doInBackground(String... strings) {
            List<String> params = new ArrayList<>(Arrays.asList(FatSecretHelper.generateOauthParams()));
            String[] template = new String[1];
            String response = "";
            params.add("method=foods.search");
            params.add("search_expression=" + Uri.encode(strings[0]));
            params.add("oauth_signature=" + FatSecretHelper.sign(METHOD, FatSecretHelper.URL, params.toArray(template)));

            JSONObject foods = null;
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
            final ArrayList<String> foodsList = new ArrayList<>();
            mFoodId = new ArrayList<>();
            JSONObject jsonObject;
            JSONArray FOODS_ARRAY;
            try {
                if (result != null) {
                    jsonObject = new JSONObject(result).getJSONObject("foods");
                    if (jsonObject.getInt("total_results") != 0) {
                        FOODS_ARRAY = jsonObject.getJSONArray("food");
                        if (FOODS_ARRAY != null) {
                            for (int i = 0; i < FOODS_ARRAY.length(); i++) {
                                JSONObject food_items = FOODS_ARRAY.optJSONObject(i);

                                foodsList.add(food_items.getString("food_name"));
                                mFoodId.add(food_items.getInt("food_id"));
                            }
                        }
                        goToResult(mFood, foodsList);
                        ((TextView) mView.findViewById(R.id.foodTab_textView)).setText("Food Results");
                    } else {
                        Toast.makeText(getContext(), "No result Founded.", Toast.LENGTH_SHORT).show();
                    }
                }

            } catch (JSONException exception) {
                Log.e("API Error!", exception.getMessage());
            }
        }
    }

    private class APIFoodGet extends AsyncTask<String, Void, String> {

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

            JSONObject foods = null;
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
                            }
                        }
                    }

                    mFood.setCalorie(calorieList);
                    mFood.setFat(fatList);
                    mFood.setCarbs(carbsList);
                    mFood.setProtein(proteinList);
                    mFood.setmURL(urlList);
                    mFood.setmServing(servingList);
                    mListener.onFragmentInteraction(mFood);
                }

            } catch (JSONException exception) {
                Toast.makeText(getContext(), "Error fetching food info", Toast.LENGTH_LONG);
                Log.e("API Error!", exception.getMessage());
            }
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Food theFood);
    }
}
