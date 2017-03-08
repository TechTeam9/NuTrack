package edu.uw.tcss450.nutrack.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AlertDialogLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarListener;
import edu.uw.tcss450.nutrack.R;
import edu.uw.tcss450.nutrack.api.FatSecretHelper;
import edu.uw.tcss450.nutrack.database.DBDailyLog;
import edu.uw.tcss450.nutrack.database.DBNutrientRecord;
import edu.uw.tcss450.nutrack.model.Food;
import edu.uw.tcss450.nutrack.model.Recipe;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DailyLogFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DailyLogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DailyLogFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Date mSelectedDate;

    private ArrayList<DailyLogFood> mBreakfastList;

    private ArrayList<DailyLogFood> mLunchList;

    private ArrayList<DailyLogFood> mDinnerList;

    private ArrayList<DailyLogFood> mSnackList;

    private View mView;

    private Food mDeleteFood;

    public DailyLogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DailyLogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DailyLogFragment newInstance(String param1, String param2) {
        DailyLogFragment fragment = new DailyLogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_daily_log, container, false);


        //Beware of API < 24
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(mView, R.id.calendarView)
                .startDate(startDate.getTime())
                .endDate(endDate.getTime())
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Date date, int position) {
                mSelectedDate = date;
                intializeListView();
            }
        });

        //get list of food from database
        mSelectedDate = new Date();
        intializeListView();

        // Set the textview to open the Nutrient activity
        ((LinearLayout) mView.findViewById(R.id.dailyLog_addButton)).setOnClickListener(new AddListener());
        ((LinearLayout) mView.findViewById(R.id.dailyLog_addButton1)).setOnClickListener(new AddListener());
        ((LinearLayout) mView.findViewById(R.id.dailyLog_addButton2)).setOnClickListener(new AddListener());
        ((LinearLayout) mView.findViewById(R.id.dailyLog_addButton3)).setOnClickListener(new AddListener());

        return mView;
    }

    private class AddListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            mListener.onFragmentInteraction("nutrient");
        }
    }

    private void intializeListView() {
        final ListView breakfastListView = (ListView) mView.findViewById(R.id.breakfast_listView);
        final ListView lunchListView = (ListView) mView.findViewById(R.id.lunch_listView);
        final ListView dinnerListView = (ListView) mView.findViewById(R.id.dinner_listView);
        final ListView snackListView = (ListView) mView.findViewById(R.id.snack_listView);


        ArrayList<HashMap<String, String>> foodList = new ArrayList<>();

        DBDailyLog db = new DBDailyLog(getContext());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        ArrayList<HashMap<String, String>> breakfastContentList = new ArrayList<>();
        ArrayList<HashMap<String, String>> lunchContentList = new ArrayList<>();
        ArrayList<HashMap<String, String>> dinnerContentList = new ArrayList<>();
        ArrayList<HashMap<String, String>> snackContentList = new ArrayList<>();

        mBreakfastList = new ArrayList<>();
        mLunchList = new ArrayList<>();
        mDinnerList = new ArrayList<>();
        mSnackList = new ArrayList<>();

        try {
            Cursor cursor = db.getFoodByDate(dateFormat.format(mSelectedDate).toString());
            if (cursor.getCount() != 0) {
                cursor.moveToFirst();

                System.out.println(cursor.getCount());
                for (int i = 0; i < cursor.getCount(); i++) {
                    System.out.println(mBreakfastList.size());

                    DailyLogFood dailyLogFood = new DailyLogFood(cursor.getString(1), cursor.getString(3), cursor.getInt(0), cursor.getInt(2));
                    HashMap<String, String> tempFood = new HashMap<>();
                    tempFood.put("name", cursor.getString(1));
                    tempFood.put("type", cursor.getString(3));

                    switch (cursor.getString(4)) {
                        case "breakfast":
                            mBreakfastList.add(dailyLogFood);
                            breakfastContentList.add(tempFood);
                            break;
                        case "lunch":
                            mLunchList.add(dailyLogFood);
                            lunchContentList.add(tempFood);
                            break;
                        case "dinner":
                            mDinnerList.add(dailyLogFood);
                            dinnerContentList.add(tempFood);
                            break;
                        case "snack":
                            mSnackList.add(dailyLogFood);
                            snackContentList.add(tempFood);
                            break;
                    }
                    cursor.moveToNext();
                }

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Initialize Breakfast ListView
        SimpleAdapter adapter = new SimpleAdapter(getContext(), breakfastContentList, android.R.layout.two_line_list_item, new String[]{"name", "type"}, new int[]{android.R.id.text1, android.R.id.text2});
        breakfastListView.setAdapter(adapter);
        calculateListViewHeight(breakfastListView);

        initializeListViewEvent(breakfastListView, mBreakfastList);

        //Initialize Lunch ListView
        adapter = new SimpleAdapter(getContext(), lunchContentList, android.R.layout.two_line_list_item, new String[]{"name", "type"}, new int[]{android.R.id.text1, android.R.id.text2});
        lunchListView.setAdapter(adapter);
        calculateListViewHeight(lunchListView);

        initializeListViewEvent(lunchListView, mLunchList);


        //Initialize Dinner ListView
        adapter = new SimpleAdapter(getContext(), dinnerContentList, android.R.layout.two_line_list_item, new String[]{"name", "type"}, new int[]{android.R.id.text1, android.R.id.text2});
        dinnerListView.setAdapter(adapter);
        calculateListViewHeight(dinnerListView);

        initializeListViewEvent(dinnerListView, mDinnerList);


        //Initialize Snack ListView
        adapter = new SimpleAdapter(getContext(), snackContentList, android.R.layout.two_line_list_item, new String[]{"name", "type"}, new int[]{android.R.id.text1, android.R.id.text2});
        snackListView.setAdapter(adapter);
        calculateListViewHeight(snackListView);

        initializeListViewEvent(snackListView, mSnackList);

    }

    public void initializeListViewEvent(ListView theListView, final ArrayList<DailyLogFood> theMealList) {
        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (theMealList.get(position).getType().equals("food")) {
                    APIFoodGet api = new APIFoodGet();
                    api.execute(String.valueOf(theMealList.get(position).getId()));
                } else {
                    APIRecipeGet api = new APIRecipeGet();
                    api.execute(String.valueOf(theMealList.get(position).getId()), "get", "0", "0");
                }
            }
        });

        theListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                initializeDeleteDialog(theMealList.get(position));
                return true;
            }
        });
    }

    private void initializeDeleteDialog(final DailyLogFood theFood) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Delete Confirmation");
        alertDialog.setMessage("Are you sure you want to delete " + theFood.getName());


        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DBDailyLog dbDailyLog = new DBDailyLog(getContext());
                Cursor cursor = dbDailyLog.getFoodByDailyLogId(theFood.getDailyLogId());
                cursor.moveToFirst();

                if (theFood.getType().equals("food")) {
                    APIFoodGetWithServing api = new APIFoodGetWithServing();
                    api.execute(String.valueOf(cursor.getInt(2)), String.valueOf(cursor.getInt(5)), cursor.getString(6), String.valueOf(theFood.getDailyLogId()));
                } else {
                    APIRecipeGet api = new APIRecipeGet();
                    api.execute(String.valueOf(cursor.getInt(2)), "delete", cursor.getString(6), String.valueOf(theFood.getDailyLogId()));
                }

            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void calculateListViewHeight(ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        if (adapter == null) {
            return;
        }

        ViewGroup viewGroup = listView;
        int finalHeight = 0;

        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, viewGroup);
            listItem.measure(0, 0);
            finalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = finalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
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
        void onFragmentInteraction(Food theFood, String theType);

        void onFragmentInteraction(Recipe theRecipe, String theType);

        void onFragmentInteraction(String theMessage);
    }

    private class DailyLogFood {
        private String mName;

        private String mType;

        private int mDailyLogId;

        private int mId;

        public DailyLogFood(String theName, String theType, int theDailyLogId, int theId) {
            mName = theName;
            mType = theType;
            mDailyLogId = theDailyLogId;
            mId = theId;
        }

        public String getName() {
            return mName;
        }

        public String getType() {
            return mType;
        }

        public int getDailyLogId() {
            return mDailyLogId;
        }

        public int getId() {
            return mId;
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
            Log.d("API", "Fetched");
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
                    mListener.onFragmentInteraction(mFood, "read");
                }

            } catch (JSONException exception) {
                Toast.makeText(getContext(), "Error fetching food info", Toast.LENGTH_LONG);
                Log.e("API Error!", exception.getMessage());
            }
        }
    }

    private class APIRecipeGet extends AsyncTask<String, Void, String> {

        private final static String METHOD = "GET";

        Recipe mRecipe = new Recipe();

        private String mActionType;

        private String mDate;

        private int mDailyLogId;

        @Override
        protected String doInBackground(String... strings) {
            mActionType = strings[1];
            mDate = strings[2];
            mDailyLogId = Integer.valueOf(strings[3]);
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

                    if (mActionType.equals("get")) {
                        mListener.onFragmentInteraction(mRecipe, "read");
                    } else {
                        DBDailyLog dbDailyLog = new DBDailyLog(getContext());
                        DBNutrientRecord dbNutrient = new DBNutrientRecord(getContext());

                        dbNutrient.deleteNutrient(mDate, mRecipe.getCalorie().get(0),
                                mRecipe.getFat().get(0), mRecipe.getCarbs().get(0),
                                mRecipe.getProtein().get(0));
                        dbDailyLog.deleteFood(mDailyLogId);

                    }

                }

            } catch (JSONException exception) {
                Toast.makeText(getContext(), "Error fetching food info", Toast.LENGTH_LONG);
                Log.e("API Error!", exception.getMessage());
            }
        }
    }

    private class APIFoodGetWithServing extends AsyncTask<String, Void, String> {

        private final static String METHOD = "GET";

        Food mFood = new Food();

        private int mServingId;

        private String mDate;

        private int mDailyLogId;
        @Override
        protected String doInBackground(String... strings) {
            mServingId = Integer.parseInt(strings[1]);
            List<String> params = new ArrayList<>(Arrays.asList(FatSecretHelper.generateOauthParams()));
            String[] template = new String[1];
            String response = "";
            params.add("method=food.get");
            mDate = strings[2];
            mDailyLogId = Integer.parseInt(strings[3]);
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
            Log.d("API", "Fetched");
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            JSONObject jsonObject;
            JSONArray jsonArray;

            mDeleteFood = new Food();
            ArrayList<Double> calorieList = new ArrayList<>();
            ArrayList<Double> fatList = new ArrayList<>();
            ArrayList<Double> carbsList = new ArrayList<>();
            ArrayList<Double> proteinList = new ArrayList<>();

            try {
                if (result != null) {
                    mDeleteFood.setId(new JSONObject(result).getJSONObject("food").getInt("food_id"));
                    mDeleteFood.setName(new JSONObject(result).getJSONObject("food").getString("food_name"));

                    jsonObject = new JSONObject(result).getJSONObject("food").getJSONObject("servings");
                    JSONObject servingObject = jsonObject.optJSONObject("serving");
                    if (servingObject != null) {
                        if (servingObject.getInt("serving_id") == mServingId) {
                            calorieList.add(servingObject.getDouble("calories"));
                            fatList.add(servingObject.getDouble("fat"));
                            carbsList.add(servingObject.getDouble("carbohydrate"));
                            proteinList.add(servingObject.getDouble("protein"));
                        }

                    } else {
                        jsonArray = jsonObject.getJSONArray("serving");
                        if (jsonArray != null) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject serving = jsonArray.getJSONObject(i);
                                if (serving.getInt("serving_id") == mServingId) {
                                    calorieList.add(serving.getDouble("calories"));
                                    fatList.add(serving.getDouble("fat"));
                                    carbsList.add(serving.getDouble("carbohydrate"));
                                    proteinList.add(serving.getDouble("protein"));
                                }
                            }
                        }
                    }

                    mDeleteFood.setCalorie(calorieList);
                    mDeleteFood.setFat(fatList);
                    mDeleteFood.setCarbs(carbsList);
                    mDeleteFood.setProtein(proteinList);

                    DBNutrientRecord dbnutrient = new DBNutrientRecord(getContext());
                    dbnutrient.deleteNutrient(mDate,
                            mDeleteFood.getCalorie().get(0),
                            mDeleteFood.getFat().get(0),
                            mDeleteFood.getCarbs().get(0),
                            mDeleteFood.getProtein().get(0));
                    DBDailyLog dbDailyLog = new DBDailyLog(getContext());
                    dbDailyLog.deleteFood(mDailyLogId);
                }

            } catch (JSONException exception) {
                Toast.makeText(getContext(), "Error fetching food info", Toast.LENGTH_LONG);
                Log.e("API Error!", exception.getMessage());
            }
        }
    }

}
