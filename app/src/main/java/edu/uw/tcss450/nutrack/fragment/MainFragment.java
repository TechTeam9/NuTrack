package edu.uw.tcss450.nutrack.fragment;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.animation.ValueAnimatorCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.util.ArrayList;
import java.util.List;

import edu.uw.tcss450.nutrack.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private int graphHeight;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
    // TODO: Rename and change types and number of parameters
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


        //Line Chart for weight
        final LineChart weightChart = (LineChart) view.findViewById(R.id.main_weight_chart);
        weightChart.setLogEnabled(false);
        weightChart.setDescription(null);
        weightChart.setBorderColor(Color.BLACK);
        weightChart.setBorderWidth(1);
        weightChart.setDrawBorders(true);

        Legend weightChartLegend = weightChart.getLegend();
        weightChartLegend.setEnabled(false);
        YAxis yAxisLeft = weightChart.getAxisLeft();
        yAxisLeft.setEnabled(false);
        YAxis yAxisRight = weightChart.getAxisRight();
        yAxisRight.setEnabled(false);
        XAxis xAxis = weightChart.getXAxis();
        xAxis.setEnabled(false);

        List<Point> weightData = new ArrayList<Point>();
        weightData.add(new Point(1, 2));
        weightData.add(new Point(2, 4));
        weightData.add(new Point(3, 10));
        weightData.add(new Point(4, 8));
        weightData.add(new Point(5, 5));
        weightData.add(new Point(6, 1));
        weightData.add(new Point(7, 3));


        List<Entry> entries = new ArrayList<Entry>();

        for (Point tempPoint : weightData) {
            entries.add(new Entry(tempPoint.x, tempPoint.y));
        }

        LineDataSet dataSet = new LineDataSet(entries, "");
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.BLACK);

        LineData lineData = new LineData(dataSet);
        weightChart.setData(lineData);
        weightChart.animateY(1000);


        Switch weightChartSwitch = (Switch) view.findViewById(R.id.main_weightChart_switch);
        weightChartSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                final LinearLayout weightChartFrame = (LinearLayout) view.findViewById(R.id.main_weightChart_frame);
                LinearLayout weightChartTitle = (LinearLayout) view.findViewById(R.id.main_weightChart_title);
                ValueAnimator slideAnimator;
                AnimatorSet set = new AnimatorSet();

                if (isChecked) {
                    slideAnimator = ValueAnimator.ofInt(weightChartFrame.getHeight(), graphHeight).setDuration(500);

                } else {
                    //********************Need to Fix***********************
                    graphHeight = weightChartFrame.getHeight();
                    //**************************************************
                    weightChart.animate().scaleY(0).setStartDelay(0);
                    slideAnimator = ValueAnimator.ofInt(weightChartFrame.getHeight(), weightChartTitle.getHeight()).setDuration(500);
                }

                slideAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        // get the value the interpolator is at
                        Integer value = (Integer) animation.getAnimatedValue();
                        // I'm going to set the layout's height 1:1 to the tick
                        weightChartFrame.getLayoutParams().height = value.intValue();
                        // force all layouts to see which ones are affected by
                        // this layouts height change
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
}
