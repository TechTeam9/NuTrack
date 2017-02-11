package edu.uw.tcss450.nutrack.fragment;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private String mFood;

    private Context mContext;

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
        mContext = container.getContext();
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
        mFood = theFood;
        // FOR API USES!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        final FatSecretAPI mFatSecret = new FatSecretAPI();
        mFatSecret.execute(theFood);


    }

    private void goToResult(String theFoodName, ArrayList<String> theFoodList) {
        mListener.onFragmentInteraction(theFoodName, theFoodList);

    }


    @Override
    public void onAttach(Context theContext) {
        super.onAttach(theContext);
        mContext = theContext;
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
        void onFragmentInteraction(String theFoodName, ArrayList<String> theFoodList);
    }

    private class FatSecretAPI extends AsyncTask<String, Void, String> {

        private final static String METHOD = "GET";

        private final static String CONSUMER_KEY = "aceffd6069f24bde9e9710dbcef45ea9";

        private final static String SECRET_KEY = "cc2b625f2d0a49509c0745e8e434b008";

        private final static String URL = "http://platform.fatsecret.com/rest/server.api";

        private final static String SIGNATURE_METHOD = "HMAC-SHA1";

        private final static String HMAC_SHA1_ALGORITHM = "HmacSHA1";


        @Override
        protected String doInBackground(String... strings) {
            List<String> params = new ArrayList<>(Arrays.asList(generateOauthParams()));
            String[] template = new String[1];
            String response = "";
            params.add("method=foods.search");
            params.add("search_expression=" + Uri.encode(strings[0]));
            params.add("oauth_signature=" + sign(METHOD, URL, params.toArray(template)));

            JSONObject foods = null;
            try {
                java.net.URL url = new URL(URL + "?" + paramify(params.toArray(template)));
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
            JSONObject jsonObject;
            JSONArray FOODS_ARRAY;
            try {
                if (result != null) {
                    jsonObject = new JSONObject(result).getJSONObject("foods");
                    FOODS_ARRAY = jsonObject.getJSONArray("food");
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
                goToResult(mFood, foodsList);

            } catch (JSONException exception) {
                Log.e("API Error!", exception.getMessage());
            }
        }

        public JSONObject searchFood(String searchFood) {
            List<String> params = new ArrayList<>(Arrays.asList(generateOauthParams()));
            String[] template = new String[1];
            params.add("method=foods.search");
            params.add("search_expression=" + Uri.encode(searchFood));
            params.add("oauth_signature=" + sign(METHOD, URL, params.toArray(template)));

            JSONObject foods = null;
            try {
                URL url = new URL(URL + "?" + paramify(params.toArray(template)));
                URLConnection api = url.openConnection();
                String line;
                StringBuilder builder = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(api.getInputStream()));
                while ((line = reader.readLine()) != null) builder.append(line);
                JSONObject food = new JSONObject(builder.toString());
                foods = food.getJSONObject("foods");
            } catch (Exception exception) {
                Log.e("FatSecret Error", exception.toString());
                exception.printStackTrace();
            }
            return foods;
        }

        /**
         *
         * @return
         */
        public String generateRandomNonce() {
            Random r = new Random();
            StringBuffer n = new StringBuffer();
            for (int i = 0; i < r.nextInt(8) + 2; i++) {
                n.append(r.nextInt(26) + 'a');
            }
            return n.toString();
        }

        /**
         * Sign
         * @param method
         * @param uri
         * @param params
         * @return
         */
        private String sign(String method, String uri, String[] params) {
            String[] p = {method, Uri.encode(uri), Uri.encode(paramify(params))};
            String s = join(p, "&");
            String tempSK = SECRET_KEY + "&";
            SecretKey sk = new SecretKeySpec(tempSK.getBytes(), HMAC_SHA1_ALGORITHM);
            try {
                Mac m = Mac.getInstance(HMAC_SHA1_ALGORITHM);
                m.init(sk);
                return Uri.encode(new String(Base64.encode(m.doFinal(s.getBytes()), Base64.DEFAULT)).trim());
            } catch (java.security.NoSuchAlgorithmException e) {
                Log.w("FatSecret_TEST FAIL", e.getMessage());
                return null;
            } catch (java.security.InvalidKeyException e) {
                Log.w("FatSecret_TEST FAIL", e.getMessage());
                return null;
            }
        }

        /**
         *
         * @return
         */
        public String[] generateOauthParams() {
            String[] baseString = {
                    "oauth_consumer_key=" + CONSUMER_KEY,
                    "oauth_signature_method=" + SIGNATURE_METHOD,
                    "oauth_timestamp=" + new Long(System.currentTimeMillis() / 1000).toString(),
                    "oauth_nonce=" + generateRandomNonce(),
                    "oauth_version=1.0",
                    "format=json"
            };

            return baseString;
        }

        /**
         *
         * @param theParam
         * @return
         */
        private String paramify(String[] theParam) {
            String[] param = Arrays.copyOf(theParam, theParam.length);
            Arrays.sort(param);
            return join(param, "&");
        }

        /**
         *
         * @param theParam
         * @param separator
         * @return
         */
        private String join(String[] theParam, String separator) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < theParam.length; i++) {
                if (i > 0)
                    sb.append(separator);
                sb.append(theParam[i]);
            }
            return sb.toString();
        }

    }
}
