package edu.uw.tcss450.nutrack.fragment;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import az.plainpie.PieView;
import edu.uw.tcss450.nutrack.R;
import edu.uw.tcss450.nutrack.database.DBNutrientRecord;

import static android.R.attr.x;
import static android.R.attr.y;
import static android.graphics.Color.rgb;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DailyIntakeOverviewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DailyIntakeOverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DailyIntakeOverviewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public DailyIntakeOverviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DailyIntakeOverviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DailyIntakeOverviewFragment newInstance(String param1, String param2) {
        DailyIntakeOverviewFragment fragment = new DailyIntakeOverviewFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_daily_intake_overview, container, false);

        TextView fatTextView = (TextView) view.findViewById(R.id.overview_textView_dailyIntakeFat);
        TextView carbsTextView = (TextView) view.findViewById(R.id.overview_textView_dailyIntakeCarbs);
        TextView proteinTextView = (TextView) view.findViewById(R.id.overview_textView_dailyIntakeProtein);

        DBNutrientRecord dbNutrientRecord = new DBNutrientRecord(getContext());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            Cursor cursor = dbNutrientRecord.getNutrientByDate(dateFormat.format(date));
            if (cursor.getCount() == 0) {
                fatTextView.setText("0g");
                carbsTextView.setText("0g");
                proteinTextView.setText("0g");
                initializeDailyCalorieChart(view, 0);
            } else {
                cursor.moveToFirst();
                DecimalFormat df = new DecimalFormat("0.00");
                fatTextView.setText(String.valueOf(df.format(cursor.getDouble(2))) + "g");
                carbsTextView.setText(String.valueOf(df.format(cursor.getDouble(3))) + "g");
                proteinTextView.setText(String.valueOf(df.format(cursor.getDouble(4))) + "g");
                initializeDailyCalorieChart(view, (int) cursor.getDouble(1));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return view;
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

    public void initializeDailyCalorieChart(final View view, final int theInt) {
        PieView pieView = (PieView) view.findViewById(R.id.pieView);
        pieView.setPercentageBackgroundColor(getResources().getColor(R.color.colorPrimary));
        pieView.setPieInnerPadding(50);
        int result =(int) ((theInt / 2000.0) * 100);
        Log.d("Percentage", String.valueOf(result));
        pieView.setPercentage(90f);
        //pieView.setPercentage((float) result);
        pieView.setInnerText("Calories\n" + result + "%");
        pieView.setPercentageTextSize(35.0f);
//        pieView.setTextColor(getResources().getColor(R.color.calories));
        TextView caloriesProgress =(TextView) view.findViewById(R.id.overview_textView_caloriesProgress);
        caloriesProgress.setText(theInt + " / 2000");
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
