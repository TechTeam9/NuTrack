package edu.uw.tcss450.nutrack.fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import edu.uw.tcss450.nutrack.R;
import im.dacer.androidcharts.LineView;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.ChartData;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MonthlyWeightOverviewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MonthlyWeightOverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MonthlyWeightOverviewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MonthlyWeightOverviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MonthlyWeightOverviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MonthlyWeightOverviewFragment newInstance(String param1, String param2) {
        MonthlyWeightOverviewFragment fragment = new MonthlyWeightOverviewFragment();
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
        View view = inflater.inflate(R.layout.fragment_monthly_weight_overview, container, false);
        //ArrayList<Integer> weights = new ArrayList<>(); //Should go away after we have real code here.
        //initializeMonthlyWeightGraph(view, weights);
        LineChartView weightChart = (LineChartView) view.findViewById(R.id.weight_chart);
        weightChart.setInteractive(false);
        weightChart.setZoomEnabled(false);
        weightChart.setScrollContainer(false);
        LineChartData chartData = weightChart.getLineChartData();

        ArrayList<AxisValue> dayList = new ArrayList<>();

        for(int i=1; i < 31; i++){
            dayList.add(new AxisValue(i));
        }
        Axis axisX = new Axis(dayList);

        chartData.setBaseValue(10);
        chartData.setAxisXBottom(axisX);

        List<PointValue> values = new ArrayList<PointValue>();
        values.add(new PointValue(1, 250));
        values.add(new PointValue(3, 251));
        values.add(new PointValue(4, 248));
        values.add(new PointValue(5, 247));
        values.add(new PointValue(7, 251));
        values.add(new PointValue(9, 248));
        values.add(new PointValue(10, 247));
        values.add(new PointValue(15, 190));

        Line line = new Line(values).setColor(getResources().getColor(R.color.colorPrimaryDark)).setCubic(true);
        line.setStrokeWidth(2);
        line.setHasLabels(true);
        List<Line> lines = new ArrayList<Line>();
        lines.add(line);

        chartData.setLines(lines);

        weightChart.setLineChartData(chartData);

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

//    public void initializeMonthlyWeightGraph(View view, ArrayList<Integer> weights) {
//
//        ArrayList<ArrayList<Integer>> dataLists = new ArrayList<>();
//
//        weights.add(0, 250);
//        weights.add(0);
//        weights.add(251);
//        weights.add(200);
//        weights.add(20);
//        dataLists.add(weights);
//        ArrayList<String> strList = new ArrayList<>();
//        strList.add("JAN 1");
//        strList.add("JAN 2");
//        strList.add("JAN 3");
//        strList.add("JAN 4");
//        strList.add("JAN 5");
//        strList.add("JAN 6");
//        LineView lineView = (LineView) view.findViewById(R.id.weight_chart);
//        lineView.setDrawDotLine(true); //optional
//        lineView.setShowPopup(LineView.SHOW_POPUPS_MAXMIN_ONLY); //optional
//        lineView.setBottomTextList(strList);
//        lineView.setColorArray(new int[]{Color.BLACK,Color.GREEN,Color.GRAY,Color.CYAN});
//        lineView.setDataList(dataLists); //or lineView.setFloatDataList(floatDataLists)
//        lineView.setBottom(50);
//    }
}
