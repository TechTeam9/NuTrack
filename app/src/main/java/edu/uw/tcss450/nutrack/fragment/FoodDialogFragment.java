package edu.uw.tcss450.nutrack.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.uw.tcss450.nutrack.R;
import edu.uw.tcss450.nutrack.database.DBDailyLog;
import edu.uw.tcss450.nutrack.model.Food;

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

        initializeSpinner();
        initializeFoodInfoPanel(0);

        // Buttons action
        final Button addButton = (Button) mView.findViewById(R.id.dialog_add_button);
        Button cancelButton = (Button) mView.findViewById(R.id.dialog_cancel_button);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout foodInfoPanel = (LinearLayout) mView.findViewById(R.id.foodDialog_foodInfo);
                LinearLayout addInfoPanel = (LinearLayout) mView.findViewById(R.id.foodDialog_addInfo);

                //If add button is add function, switch to add info view, else get data from add info view.
                if (addButton.getText().equals("ADD")) {
                    foodInfoPanel.setVisibility(View.GONE);
                    addInfoPanel.setVisibility(View.VISIBLE);
                    addButton.setText("SUBMIT");
                } else if (addButton.getText().equals("SUBMIT")) {
                    DatePicker datePicker = (DatePicker) mView.findViewById(R.id.foodDialog_datePicker);
                    String mealType = "";
                    RadioGroup mealTypeGroup = (RadioGroup) mView.findViewById(R.id.foodDialog_radioGroup);
                    switch (mealTypeGroup.getCheckedRadioButtonId()) {
                        case R.id.foodDialog_radioButton_breakfast:
                            mealType = "breakfast";
                            break;
                        case R.id.foodDialog_radioButton_lunch:
                            mealType = "lunch";
                            break;
                        case R.id.foodDialog_radioButton_dinner:
                            mealType = "dinner";
                            break;
                        case R.id.foodDialog_radioButton_snack:
                            mealType = "snack";
                            break;
                    }

                    DBDailyLog db = new DBDailyLog(getContext());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String date = dateFormat.format(new Date(datePicker.getYear() - 1900, datePicker.getMonth(), datePicker.getDayOfMonth()));
                    db.insertFood(mFood.getName(), mFood.getId(), "food", mealType, date);
                    System.out.println(mealType + ", " + date);
                    dismiss();
                }
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

    private void initializeSpinner() {
        Spinner servingSpinner = (Spinner) mView.findViewById(R.id.foodDialog_spinner);
        TextView servingLinearLayout = (TextView)  mView.findViewById(R.id.foodDialog_serving);
        ArrayList<String> servingArray = new ArrayList<>();

        if (mFood.getmServing().size() == 0) {
            servingSpinner.setVisibility(View.GONE);
            servingLinearLayout.setVisibility(View.GONE);
        } else {
            for (int i = 0; i < mFood.getmServing().size(); i++) {
                servingArray.add(mFood.getmServing().get(i));
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, servingArray);

            servingSpinner.setAdapter(adapter);

            servingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    initializeFoodInfoPanel(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    private void initializeFoodInfoPanel(final int thePosition) {
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
        caloriesResult.setText(String.valueOf(mFood.getCalorie().get(thePosition)));
        fatResult.setText(String.valueOf(mFood.getFat().get(thePosition)) + "g");
        carbsResult.setText(String.valueOf(mFood.getCarbs().get(thePosition)) + "g");
        proteinResult.setText(String.valueOf(mFood.getProtein().get(thePosition)) + "g");
        urlResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUrl(mFood.getmURL().get(thePosition));
            }
        });

        mView.invalidate();
    }

    /**
     * Go to the URL.
     *
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
