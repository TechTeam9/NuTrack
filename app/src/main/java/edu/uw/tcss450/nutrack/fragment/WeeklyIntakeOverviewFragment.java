package edu.uw.tcss450.nutrack.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.uw.tcss450.nutrack.R;
import edu.uw.tcss450.nutrack.database.DBDailyLog;
import edu.uw.tcss450.nutrack.database.DBNutrientRecord;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WeeklyIntakeOverviewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WeeklyIntakeOverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeeklyIntakeOverviewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * height of the graph
     */
    private int mGraphHeight;

    private OnFragmentInteractionListener mListener;

    public WeeklyIntakeOverviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WeeklyIntakeOverviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WeeklyIntakeOverviewFragment newInstance(String param1, String param2) {
        WeeklyIntakeOverviewFragment fragment = new WeeklyIntakeOverviewFragment();
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
        final View view = inflater.inflate(R.layout.fragment_weekly_intake_overview, container, false);
        initializeWeeklyCalorieChart(view);
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

    /**
     * Initialize the calories chart.
     *
     * @param view the view
     */
    private void initializeWeeklyCalorieChart(final View view) {
        final ColumnChartView weightChart = (ColumnChartView) view.findViewById(R.id.weekly_intake_chart);
        weightChart.setInteractive(false);
        weightChart.setZoomEnabled(false);
        weightChart.setClickable(false);

        ColumnChartData weightChartData;

        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        String[] labels = new String[7];
        //Open Daily Log Table to grab calories value
        DBNutrientRecord db = new DBNutrientRecord(getContext());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat letterMonthFormat = new SimpleDateFormat("yyyy-MMM-dd");
        Date date = new Date();
        Date dateBefore = new Date(date.getTime() - (6) * 24 * 3600 * 1000l);

        for (int i = 6; i >= 0; i--) {

            labels[6- i] = letterMonthFormat.format(dateBefore).substring(5, 8) + " / " + letterMonthFormat.format(dateBefore).substring(9, 11);
            double calories = db.getCaloriesByDate(dateFormat.format(dateBefore));

            values = new ArrayList<>();
            values.add(new SubcolumnValue((float) calories, getResources().getColor(R.color.colorPrimary)));

            Column column = new Column(values);
            column.setHasLabels(true);
            columns.add(column);

            dateBefore = new Date(date.getTime() - (i - 1) * 24 * 3600 * 1000l);
        }


        weightChartData = new ColumnChartData(columns);

        //Set Axis
        //String[] labels = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        List<AxisValue> axisXValueList = new ArrayList<AxisValue>();
        Axis axisX = new Axis();

        for (int i = 0; i < 7; i++) {
            axisXValueList.add(new AxisValue(i).setLabel(labels[i]));
        }
        axisX.setValues(axisXValueList);

        weightChartData.setAxisXBottom(axisX);

        weightChart.setColumnChartData(weightChartData);

        final View graphView = view.findViewById(R.id.weekly_intake_chart);
        //********************Need to Fix***********************
        mGraphHeight = graphView.getHeight();
        //**************************************************
    }
}
