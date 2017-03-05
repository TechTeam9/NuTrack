package edu.uw.tcss450.nutrack.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import edu.uw.tcss450.nutrack.R;
import edu.uw.tcss450.nutrack.database.DBDailyLog;
import edu.uw.tcss450.nutrack.model.Food;

import static edu.uw.tcss450.nutrack.R.id.textView;

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

    private Food mFood;

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
            mFood = getArguments().getParcelable("food_info");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_food_dialog, container, false);

        TextView dialogTitle = (TextView) mView.findViewById(R.id.dialog_food_title);
        TextView caloriesResult = (TextView) mView.findViewById(R.id.calories_food_result);
        TextView fatResult = (TextView) mView.findViewById(R.id.fat_food_result);
        TextView carbsResult = (TextView) mView.findViewById(R.id.carbs_food_result);
        TextView proteinResult = (TextView) mView.findViewById(R.id.protein_food_result);
        TextView urlResult = (TextView) mView.findViewById(R.id.url_link);
        if (mFood.getmURL().size() < 1) {
            urlResult.setVisibility(View.GONE);
        } else {
            urlResult.setPaintFlags(urlResult.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }

        dialogTitle.setText(mFood.getName());
        caloriesResult.setText(String.valueOf(mFood.getCalorie().get(0)));
        fatResult.setText(String.valueOf(mFood.getFat().get(0)));
        carbsResult.setText(String.valueOf(mFood.getCarbs().get(0)));
        proteinResult.setText(String.valueOf(mFood.getProtein().get(0)));
        urlResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUrl (mFood.getmURL().get(0));
            }
        });

        // Buttons action
        Button addButton = (Button) mView.findViewById(R.id.dialog_add_button);
        Button cancelButton = (Button) mView.findViewById(R.id.dialog_cancel_button);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBDailyLog db = new DBDailyLog(getContext());
                db.insertFood(mFood.getName(), mFood.getId(), "food", "breakfast");
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        //Remove cancel button for now and will remove this line of code in the next version.
//        cancelButton.setVisibility(mView.GONE);
//        addButton.setVisibility(mView.GONE);

        return mView;
    }

    /**
     * Go to the URL.
     * @param url address.
     */
    private void goToUrl(String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    /**
     * Action when button pressed.
     *
     * @param uri uri.
     */
    public void onButtonPressed(Uri uri) {
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
        void onFragmentInteraction(Food theFood);
    }




}
