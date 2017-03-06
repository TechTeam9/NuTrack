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

import edu.uw.tcss450.nutrack.R;
import edu.uw.tcss450.nutrack.api.FatSecretHelper;
import edu.uw.tcss450.nutrack.helper.RecentSearchHelper;
import edu.uw.tcss450.nutrack.model.Recipe;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchRecipeTabFragment extends Fragment {
    /**
     * The chosen recipe.
     */
    private String mRecipeName;

    /**
     * The current context.
     */
    private Context mContext;

    private ListView mListView;

    private ArrayList<Integer> mRecipeId;

    private View mView;

    /**
     * TheLookUpFoodFragment interaction listener.
     */
    private OnFragmentInteractionListener mListener;

    public SearchRecipeTabFragment() {
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
                searchRecipe(searchInput);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SearchFoodTabFragment.OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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
        Cursor recipes = helper.getRecipeRecentSearch();

        if (recipes.getCount() != 0) {
            ArrayList<HashMap<String, String>> recipeList = new ArrayList<>();

            int i = 0;
            recipes.moveToLast();
            while (!recipes.isFirst()) {
                Date currentTime = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date searchTime = null;
                try {
                    searchTime = dateFormat.parse(recipes.getString(1));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String timeDifference = compareTime(currentTime, searchTime);
                HashMap<String, String> tempFood = new HashMap<>();
                tempFood.put("recipe", recipes.getString(0));
                tempFood.put("time", timeDifference);
                recipeList.add(tempFood);
                recipes.moveToPrevious();
            }

            if (recipes.isFirst()) {
                Date currentTime = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date searchTime = null;
                try {
                    searchTime = dateFormat.parse(recipes.getString(1));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String timeDifference = compareTime(currentTime, searchTime);
                HashMap<String, String> tempFood = new HashMap<>();
                tempFood.put("recipe", recipes.getString(0));
                tempFood.put("time", timeDifference);
                recipeList.add(tempFood);
                recipes.moveToPrevious();
            }

            //foodList.add(foods.getString(0));
            String formArray[] = {"recipe", "time"};
            int to[] = {R.id.recentSearch_textView_food, R.id.recentSearch_textView_time};
            SimpleAdapter adapter = new SimpleAdapter(getContext(), recipeList, R.layout.list_view_recent_search_items, formArray, to);
            mListView = (ListView) theView.findViewById(R.id.lookUp_listView);
            mListView.setAdapter(adapter);

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, final View view,
                                        int position, long id) {
                    HashMap<String, Object> item = (HashMap<String, Object>) parent.getItemAtPosition(position);
                    final String recipe = (String) item.get("recipe");
                    searchRecipe(recipe);
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


    private void searchRecipe(final String theRecipe) {
        RecentSearchHelper helper = new RecentSearchHelper(getContext());
        helper.addRecipeRecentSearch(theRecipe);
        helper.close();
        mRecipeName = theRecipe;
        // FOR API USES!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        final APIRecipeSearch mFatSecret = new APIRecipeSearch();
        mFatSecret.execute(theRecipe);

    }


    private void goToResult(String theRecipeName, ArrayList<String> theFoodList) {
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

                final APIRecipeGet mFatSecret = new APIRecipeGet();
                mFatSecret.execute(String.valueOf(mRecipeId.get(position)));

            }
        });

    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Recipe theRecipe);
    }

    private class APIRecipeSearch extends AsyncTask<String, Void, String> {

        private final static String METHOD = "GET";


        @Override
        protected String doInBackground(String... strings) {
            List<String> params = new ArrayList<>(Arrays.asList(FatSecretHelper.generateOauthParams()));
            String[] template = new String[1];
            String response = "";
            params.add("method=recipes.search");
            params.add("search_expression=" + Uri.encode(strings[0]));
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
            final ArrayList<String> recipesList = new ArrayList<>();
            mRecipeId = new ArrayList<>();
            JSONObject jsonObject;
            JSONArray FOODS_ARRAY;
            try {
                if (result != null) {
                    jsonObject = new JSONObject(result).getJSONObject("recipes");
                    if (jsonObject.getInt("total_results") != 0) {
                        FOODS_ARRAY = jsonObject.getJSONArray("recipe");
                        if (FOODS_ARRAY != null) {
                            for (int i = 0; i < FOODS_ARRAY.length(); i++) {
                                JSONObject recipe_items = FOODS_ARRAY.optJSONObject(i);

                                recipesList.add(recipe_items.getString("recipe_name"));
                                mRecipeId.add(recipe_items.getInt("recipe_id"));
                            }
                        }
                        goToResult(mRecipeName, recipesList);
                        ((TextView) mView.findViewById(R.id.foodTab_textView)).setText("Recipe Results");
                    } else {
                        Toast.makeText(getContext(), "No result Founded.", Toast.LENGTH_SHORT).show();
                    }
                }

            } catch (JSONException exception) {
                Log.e("API Error!", exception.getMessage());
            }
        }
    }

    private class APIRecipeGet extends AsyncTask<String, Void, String> {

        private final static String METHOD = "GET";

        Recipe mRecipe = new Recipe();

        @Override
        protected String doInBackground(String... strings) {
            List<String> params = new ArrayList<>(Arrays.asList(FatSecretHelper.generateOauthParams()));
            String[] template = new String[1];
            String response = "";
            params.add("method=recipe.get");
            params.add("recipe_id=" + Uri.encode(strings[0]));

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
            System.out.println(result);
            JSONObject jsonObject;

            ArrayList<Double> calorieList = new ArrayList<>();
            ArrayList<Double> fatList = new ArrayList<>();
            ArrayList<Double> carbsList = new ArrayList<>();
            ArrayList<Double> proteinList = new ArrayList<>();

            try {
                if (result != null) {
                    mRecipe.setId(new JSONObject(result).getJSONObject("recipe").getInt("recipe_id"));
                    mRecipe.setName(new JSONObject(result).getJSONObject("recipe").getString("recipe_name"));
                    JSONObject imageJSONObject = new JSONObject(result).getJSONObject("recipe").optJSONObject("recipe_images");
                    if (imageJSONObject != null) {
                        mRecipe.setImageURL(imageJSONObject.getString("recipe_image"));
                    }

//                    if (imageJSONObject.has("recipe_images")) {
//                        mRecipe.setImageURL(imageJSONObject.getJSONObject("recipe_images").getString("recipe_image"));
//                    }

                    jsonObject = new JSONObject(result).getJSONObject("recipe").getJSONObject("serving_sizes");
                    JSONObject servingObject = jsonObject.optJSONObject("serving");

                    calorieList.add(servingObject.getDouble("calories"));
                    fatList.add(servingObject.getDouble("fat"));
                    carbsList.add(servingObject.getDouble("carbohydrate"));
                    proteinList.add(servingObject.getDouble("protein"));


                    mRecipe.setCalorie(calorieList);
                    mRecipe.setFat(fatList);
                    mRecipe.setCarbs(carbsList);
                    mRecipe.setProtein(proteinList);
                    mListener.onFragmentInteraction(mRecipe);

                }

            } catch (JSONException exception) {
                Toast.makeText(getContext(), "Error fetching food info", Toast.LENGTH_LONG);
                Log.e("API Error!", exception.getMessage());
            }
        }
    }
}
