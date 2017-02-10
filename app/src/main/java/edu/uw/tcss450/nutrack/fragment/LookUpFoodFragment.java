package edu.uw.tcss450.nutrack.fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import edu.uw.tcss450.nutrack.API.FatSecretAPI;
import edu.uw.tcss450.nutrack.DBHelper.DBRecentSearchTableHelper;
import edu.uw.tcss450.nutrack.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LookUpFoodFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LookUpFoodFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LookUpFoodFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private OnFragmentInteractionListener mListener;

    public LookUpFoodFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CaloriesCalculator.
     */
    // TODO: Rename and change types and number of parameters
    public static LookUpFoodFragment newInstance(String param1, String param2) {
        LookUpFoodFragment fragment = new LookUpFoodFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_look_up_food, container, false);
        ;

        initializeSearchListener(view);
        initializeRecentSearchList(view);

        return view;
    }

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


            //foodList.add(foods.getString(0));
            String formArray[] = {"food", "time"};
            int to[] = {R.id.recentSearch_textView_food, R.id.recentSearch_textView_time};
            SimpleAdapter adapter = new SimpleAdapter(getContext(), foodList, R.layout.list_view_recent_search_items, formArray, to);
            ListView listView = (ListView) theView.findViewById(R.id.lookUp_listView);
            listView.setAdapter(adapter);
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

    private void searchFood(final String theFood) {
        DBRecentSearchTableHelper dbHelper = new DBRecentSearchTableHelper(getContext());
        dbHelper.insertFood(theFood);
        final ArrayList<String> foodsList = new ArrayList<>();

        // FOR API USES!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        final FatSecretAPI mFatSecret = new FatSecretAPI();
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... arg0) {
                System.out.println("Starting to SearchFood");

                JSONObject food = mFatSecret.searchFood(theFood);
                JSONArray FOODS_ARRAY;
                try {
                    if (food != null) {
                        FOODS_ARRAY = food.getJSONArray("food");
                        if (FOODS_ARRAY != null) {
                            for (int i = 0; i < FOODS_ARRAY.length(); i++) {
                                JSONObject food_items = FOODS_ARRAY.optJSONObject(i);

                                 /*
                                 String food_name = food_items.getString("food_name");
                                 String food_description = food_items.getString("food_description");
                                 String[] row = food_description.split("-");
                                 String id = food_items.getString("food_type");

                                 if (id.equals("Brand")) {
                                     brand = food_items.getString("brand_name");
                                 }
                                 if (id.equals("Generic")) {
                                     brand = "Generic";
                                 }

                                 String food_id = food_items.getString("food_id");
                                // mItem.add(new Item(food_name, row[1].substring(1),
                                 //        "" + brand, food_id));
                                 */
                                //System.out.println(food_items.getString("food_name"));
                                //System.out.println(food_id);

                                foodsList.add(food_items.getString("food_name"));
                            }
                        }
                    }
                    goToResult(theFood, foodsList);

                } catch (JSONException exception) {
                    return "Error";
                }
                return "";
            }
        }.execute();


    }

    private void goToResult(String theFoodName, ArrayList<String> theFoodList) {
        System.out.println(theFoodList.get(0));
        mListener.onFragmentInteraction(theFoodName, theFoodList);

    }


    @Override
    public void onAttach(Context theContext) {
        super.onAttach(theContext);
        if (theContext instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) theContext;
        } else {
            throw new RuntimeException(theContext.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String theFoodName, ArrayList<String> theFoodList);
    }
}
