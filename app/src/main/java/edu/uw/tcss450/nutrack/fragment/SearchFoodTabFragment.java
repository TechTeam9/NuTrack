package edu.uw.tcss450.nutrack.fragment;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;

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
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import edu.uw.tcss450.nutrack.API.FatSecretHelper;
import edu.uw.tcss450.nutrack.DBHelper.DBRecentSearchTableHelper;
import edu.uw.tcss450.nutrack.R;
import edu.uw.tcss450.nutrack.fragment.FoodDialogFragment;
import edu.uw.tcss450.nutrack.fragment.LookUpFoodFragment;


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
     * TheLookUpFoodFragment interaction listener.
     */
    private LookUpFoodFragment.OnFragmentInteractionListener mListener;

    public SearchFoodTabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_look_up_food, container, false);

        mContext = container.getContext();
        initializeSearchListener(view);
        initializeRecentSearchList(view);

        return view;
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

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /**
     * Start the Recent search record list.
     *
     * @param theView showing view.
     */
    private void initializeRecentSearchList(View theView) {
        DBRecentSearchTableHelper dbHelper = new DBRecentSearchTableHelper(getContext());

        Cursor foods = dbHelper.getAllFood();
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
        DBRecentSearchTableHelper dbHelper = new DBRecentSearchTableHelper(getContext());
        dbHelper.insertFood(theFood);
        mFood = theFood;
        // FOR API USES!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        final FatSecretAPI mFatSecret = new FatSecretAPI();
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
                bundle.putInt("food_id", mFoodId.get(position));
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                DialogFragment foodInfoDialog = new FoodDialogFragment();
                foodInfoDialog.setArguments(bundle);
                foodInfoDialog.show(fragmentManager, "food info dialog");
                //onFragmentInteraction();

            }
        });

    }


    private class FatSecretAPI extends AsyncTask<String, Void, String> {

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
                    FOODS_ARRAY = jsonObject.getJSONArray("food");
                    if (FOODS_ARRAY != null) {
                        for (int i = 0; i < FOODS_ARRAY.length(); i++) {
                            JSONObject food_items = FOODS_ARRAY.optJSONObject(i);

                            foodsList.add(food_items.getString("food_name"));
                            mFoodId.add(food_items.getInt("food_id"));
                        }
                    }
                }
                goToResult(mFood, foodsList);

            } catch (JSONException exception) {
                Log.e("API Error!", exception.getMessage());
            }
        }
    }
}
