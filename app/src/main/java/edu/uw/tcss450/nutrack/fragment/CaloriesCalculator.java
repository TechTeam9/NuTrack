package edu.uw.tcss450.nutrack.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.nutrack.API.FatSecretAPI;
import edu.uw.tcss450.nutrack.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CaloriesCalculator.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CaloriesCalculator#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CaloriesCalculator extends Fragment {



    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CaloriesCalculator() {
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
    public static CaloriesCalculator newInstance(String param1, String param2) {
        CaloriesCalculator fragment = new CaloriesCalculator();
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
        View view = inflater.inflate(R.layout.fragment_calories_calculator, container, false);;

        searchFood("apple");

        return view;
    }

    private void searchFood(final String theFood) {
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
                                 String food_name = food_items.getString("food_name");
                                 String food_description = food_items.getString("food_description");
                                 String[] row = food_description.split("-");
                                 String id = food_items.getString("food_type");
                                 /*
                                 if (id.equals("Brand")) {
                                     brand = food_items.getString("brand_name");
                                 }
                                 if (id.equals("Generic")) {
                                     brand = "Generic";
                                 }
                                 */
                                 String food_id = food_items.getString("food_id");
                                // mItem.add(new Item(food_name, row[1].substring(1),
                                 //        "" + brand, food_id));
                                 System.out.println(food_name);
                                 System.out.println(food_id);
                             }
                         }
                     }
                 } catch (JSONException exception) {
                     return "Error";
                 }
                 return "";
             }
         }.execute();
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
