package edu.uw.tcss450.nutrack.fragment;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;

import edu.uw.tcss450.nutrack.R;
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
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {
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
     * First Parameter string.
     */
    private String mParam1;
    /**
     * Second Parameter string.
     */
    private String mParam2;

    /**
     * height of the graph
     */
    private int mGraphHeight;

    /**
     * Fragment interaction listener.
     */
    private OnFragmentInteractionListener mListener;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
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
        final View view = inflater.inflate(R.layout.fragment_main, container, false);

        initializeWeightChart(view);
        initializeCaloriesChart(view);
        return view;
    }

    /**
     * Initializes the Column Chart for weight.
     * @param view the view
     */
    private void initializeWeightChart(final View view) {
        final ColumnChartView weightChart = (ColumnChartView) view.findViewById(R.id.main_weight_chart);
        weightChart.setInteractive(false);
        weightChart.setZoomEnabled(false);
        weightChart.setClickable(false);

        ColumnChartData weightChartData;
        int numSubcolumns = 1;
        int numColumns = 7;

        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numColumns; ++i) {

            values = new ArrayList<SubcolumnValue>();
            for (int j = 0; j < numSubcolumns; ++j) {
                values.add(new SubcolumnValue((float) Math.random() * 50f + 5, ChartUtils.pickColor()));
            }

            Column column = new Column(values);
            column.setHasLabels(true);
            columns.add(column);
        }


        weightChartData = new ColumnChartData(columns);

        //Set Axis
        String[] labels = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        List<AxisValue> axisXValueList = new ArrayList<AxisValue>();
        Axis axisX = new Axis();

        for (int i = 0; i < 7; i++) {
            axisXValueList.add(new AxisValue(i).setLabel(labels[i]));
        }
        axisX.setValues(axisXValueList);

        weightChartData.setAxisXBottom(axisX);

        weightChart.setColumnChartData(weightChartData);



        Switch weightChartSwitch = (Switch) view.findViewById(R.id.main_weightChart_switch);
        weightChartSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                final LinearLayout weightChartFrame = (LinearLayout) view.findViewById(R.id.main_weightChart_frame);
                LinearLayout weightChartTitle = (LinearLayout) view.findViewById(R.id.main_weightChart_title);
                ValueAnimator slideAnimator;
                AnimatorSet set = new AnimatorSet();

                if (isChecked) {
                    slideAnimator = ValueAnimator.ofInt(weightChartFrame.getHeight(), mGraphHeight).setDuration(500);

                } else {
                    //********************Need to Fix***********************
                    mGraphHeight = weightChartFrame.getHeight();
                    //**************************************************
                    weightChart.animate().scaleY(0).setStartDelay(0);
                    slideAnimator = ValueAnimator.ofInt(weightChartFrame.getHeight(), weightChartTitle.getHeight()).setDuration(500);
                }

                slideAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        Integer value = (Integer) animation.getAnimatedValue();
                        weightChartFrame.getLayoutParams().height = value.intValue();
                        weightChartFrame.requestLayout();
                    }
                });

                set.play(slideAnimator);
                set.setInterpolator(new AccelerateDecelerateInterpolator());
                set.start();

                if (isChecked) {
                    weightChart.animate().scaleY(1).setStartDelay(200);
                }
            }
        });
    }

    /**
     * Initialize the calories chart.
     * @param view the view
     */
    private void initializeCaloriesChart(final View view) {
        final ColumnChartView weightChart = (ColumnChartView) view.findViewById(R.id.main_calorie_chart);
        weightChart.setInteractive(false);
        weightChart.setZoomEnabled(false);
        weightChart.setClickable(false);

        ColumnChartData weightChartData;
        int numSubcolumns = 1;
        int numColumns = 7;

        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numColumns; ++i) {

            values = new ArrayList<SubcolumnValue>();
            for (int j = 0; j < numSubcolumns; ++j) {
                values.add(new SubcolumnValue((float) Math.random() * 50f + 5, ChartUtils.pickColor()));
            }

            Column column = new Column(values);
            column.setHasLabels(true);
            columns.add(column);
        }


        weightChartData = new ColumnChartData(columns);

        //Set Axis
        String[] labels = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        List<AxisValue> axisXValueList = new ArrayList<AxisValue>();
        Axis axisX = new Axis();

        for (int i = 0; i < 7; i++) {
            axisXValueList.add(new AxisValue(i).setLabel(labels[i]));
        }
        axisX.setValues(axisXValueList);

        weightChartData.setAxisXBottom(axisX);

        weightChart.setColumnChartData(weightChartData);

        Switch weightChartSwitch = (Switch) view.findViewById(R.id.main_calorieChart_switch);
        weightChartSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                final LinearLayout weightChartFrame = (LinearLayout) view.findViewById(R.id.main_calorieChart_frame);
                LinearLayout weightChartTitle = (LinearLayout) view.findViewById(R.id.main_calorieChart_title);
                ValueAnimator slideAnimator;
                AnimatorSet set = new AnimatorSet();

                if (isChecked) {
                    slideAnimator = ValueAnimator.ofInt(weightChartFrame.getHeight(), mGraphHeight).setDuration(500);

                } else {
                    //********************Need to Fix***********************
                    mGraphHeight = weightChartFrame.getHeight();
                    //**************************************************
                    weightChart.animate().scaleY(0).setStartDelay(0);
                    slideAnimator = ValueAnimator.ofInt(weightChartFrame.getHeight(), weightChartTitle.getHeight()).setDuration(500);
                }

                slideAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        Integer value = (Integer) animation.getAnimatedValue();
                        weightChartFrame.getLayoutParams().height = value.intValue();
                        weightChartFrame.requestLayout();
                    }
                });

                set.play(slideAnimator);
                set.setInterpolator(new AccelerateDecelerateInterpolator());
                set.start();

                if (isChecked) {
                    weightChart.animate().scaleY(1).setStartDelay(200);
                }
            }
        });
    }


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
        void onFragmentInteraction(Uri uri);
    }
}
