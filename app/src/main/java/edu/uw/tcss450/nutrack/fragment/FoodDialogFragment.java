package edu.uw.tcss450.nutrack.fragment;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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

import edu.uw.tcss450.nutrack.API.FatSecretHelper;
import edu.uw.tcss450.nutrack.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FoodDialogFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FoodDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FoodDialogFragment extends DialogFragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    /**
     * First parameter string.
     */
    private static final String ARG_PARAM1 = "param1";

    /**
     * Second parameter string.
     */
    private static final String ARG_PARAM2 = "param2";
    /**
     * FoodDialogFragment view.
     */
    public View mView;
    /**
     * First Parameter string.
     */
    private String mParam1;
    /**
     * Second Parameter string.
     */
    private String mParam2;
    /**
     * Fragment interaction listener.
     */
    private OnFragmentInteractionListener mListener;

    /**
     * FoodDialogFragment constructor.
     */
    public FoodDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FoodDialogFragment.
     */
    public static FoodDialogFragment newInstance() {
        FoodDialogFragment fragment = new FoodDialogFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            FatSecretAPI api = new FatSecretAPI();
            api.execute(String.valueOf(getArguments().getInt("food_id")));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View mView = inflater.inflate(R.layout.fragment_food_dialog, container, false);
//        View foodDetailsTV = mView.findViewById(R.id.food_dialog_details);

        // Changing food nutrient information
//        ((TextView) foodDetailsTV).setText("More Coming Soon.....");

        //try to do it in onCreateView but still crash
//        if (getArguments() != null) {
//            FatSecretAPI api = new FatSecretAPI();
//            api.execute(String.valueOf(getArguments().getInt("food_id")));
//        }
        // Buttons action
        Button cancelButton = (Button) mView.findViewById(R.id.dialog_cancel_button);
        Button addButton = (Button) mView.findViewById(R.id.dialog_add_button);

        //Remove cancel button for now and will remove this line of code in the next version.
        cancelButton.setVisibility(mView.GONE);
        addButton.setVisibility(mView.GONE);

        return mView;
    }


    /**
     * Action when button pressed.
     *
     * @param uri uri.
     */
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction();
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

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
        void onFragmentInteraction();
    }

    private class FatSecretAPI extends AsyncTask<String, Void, String> {

        private final static String METHOD = "GET";

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

            try {
                if (result != null) {
                    jsonObject = new JSONObject(result).getJSONObject("food").getJSONObject("servings");
                    jsonArray = jsonObject.getJSONArray("serving");
                    if (jsonArray != null) {
                        //for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject servingObject = jsonArray.getJSONObject(0);
//                        System.out.println(servingObject.getInt("calories"));
                        int cal = servingObject.getInt("calories");
                        int fat = servingObject.getInt("fat");
                        int carbs = servingObject.getInt("carbohydrate");
                        int protein = servingObject.getInt("protein");
                        //}
                        // TODO it crashes here........for using the view, it said null object
                        TextView calTV = (TextView) mView.findViewById(R.id.cal_food_result);
//                        TextView fatTV = (TextView) mView.findViewById(R.id.fat_food_result);
//                        TextView carbsTV = (TextView) mView.findViewById(R.id.carbs_food_result);
//                        TextView proteinTV = (TextView) mView.findViewById(R.id.protein_food_result);
                        calTV.setText(cal);
//                        fatTV.setText(fat);
//                        carbsTV.setText(carbs);
//                        proteinTV.setText(protein);
                        System.out.println("cal: " + cal + " fat: " + fat + " carbs: " + carbs + " protein: " + protein);
                    }

                }


            } catch (JSONException exception) {
                Log.e("API Error!", exception.getMessage());
            }
        }
    }
}
