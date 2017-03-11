package edu.uw.tcss450.nutrack.fragment;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.uw.tcss450.nutrack.R;
import edu.uw.tcss450.nutrack.activity.MainActivity;
import edu.uw.tcss450.nutrack.database.DBNutrientRecord;
import edu.uw.tcss450.nutrack.database.DBWeight;
import im.dacer.androidcharts.LineView;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.ChartData;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

import static java.text.DateFormat.getDateTimeInstance;

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

    private View mView;

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
        mView = inflater.inflate(R.layout.fragment_monthly_weight_overview, container, false);
        //ArrayList<Integer> weights = new ArrayList<>(); //Should go away after we have real code here.
        initializeMonthlyWeightGraph(mView);

        Button submitButton = (Button) mView.findViewById(R.id.ov_weight_save);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logWeight();

                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mView.getApplicationWindowToken(), 0);
            }
        });

        return mView;
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

    public void initializeMonthlyWeightGraph(View view) {
        EditText editText = (EditText) mView.findViewById(R.id.overview_editText_weightInput);

        DBWeight dbWeight = new DBWeight(getContext());
        Cursor cursor = dbWeight.getTodayWeight();
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            editText.setHint(String.valueOf(cursor.getInt(1)) + " lbs");
        } else {
            editText.setHint("lbs");
        }


        LineChartView weightChart = (LineChartView) mView.findViewById(R.id.weight_chart);
        weightChart.setInteractive(false);
        weightChart.setZoomEnabled(false);
        weightChart.setScrollContainer(false);

        LineChartData chartData = weightChart.getLineChartData();

         //dbWeight.insertWeight("2017-03-07", 112);
        dbWeight.insertWeight("2017-03-06", 122);
        dbWeight.insertWeight("2017-03-05", 121);
        dbWeight.insertWeight("2017-03-04", 120);
        dbWeight.insertWeight("2017-03-03", 121);
        dbWeight.insertWeight("2017-03-02", 145);
        dbWeight.insertWeight("2017-03-01", 999);
        //TEST CODE E
        //Comment next line so I can test my code
        ArrayList<Integer> weightIntegers = dbWeight.getWeight(new Date());

        //Plug Weights into values
        String[] labels = new String[7];
        List<PointValue> values = new ArrayList<PointValue>();
        DBNutrientRecord db = new DBNutrientRecord(getContext());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat letterMonthFormat = new SimpleDateFormat("yyyy-MMM-dd");
        Date date = new Date();
        Date dateBefore = new Date(date.getTime() - (6) * 24 * 3600 * 1000l);

        for (int i = 6; i >= 0; i--) {
            values.add(new PointValue(i,weightIntegers.remove(0)));
            labels[6 - i] = letterMonthFormat.format(dateBefore).substring(5, 8) + "-" + letterMonthFormat.format(dateBefore).substring(9, 11);
            dateBefore = new Date(date.getTime() - (i - 1) * 24 * 3600 * 1000l);
        }
        //Setup X axis
        ArrayList<AxisValue> dayList = new ArrayList<>();


        for(int i = 0; i < 7; i++){
            dayList.add(new AxisValue(i).setLabel(labels[i]));
        }
        Axis axisX = new Axis(dayList);

        chartData.setBaseValue(10);
        chartData.setAxisXBottom(axisX);


        /*
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        Date dateBefore = date;
        String dateString = dateFormat.format(date);
        ArrayList<Integer> weightList = dbWeight.getWeight(date);
        String[] dateSplit = dateString.split("-");
        String result = dateSplit[2];
        int today = Integer.parseInt(result);

        for (int i = 6; i >=0; i--) {
            String tempDate = dateFormat.format(dateFormat).substring(6, 11).replace('-', '/');
            values.add(new PointValue(i, (float) weightList));
            values.ad
            dateBefore = new Date(date.getTime() - i * 24 * 3600 * 1000l);

        }
        */

        Line line = new Line(values).setColor(getResources().getColor(R.color.colorPrimary)).setCubic(false);
        line.setStrokeWidth(2);
        line.setHasLabels(true);
        List<Line> lines = new ArrayList<Line>();
        lines.add(line);

        chartData.setLines(lines);

        weightChart.setLineChartData(chartData);
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

    public void refresh(){
        initializeMonthlyWeightGraph(mView);
    }
    public void logWeight(){
        EditText weightEditText = (EditText) mView.findViewById(R.id.overview_editText_weightInput);
        if(!weightEditText.getText().toString().equals("")) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            DBWeight dbWeight = new DBWeight(getContext());
            String currentDate = dateFormat.format(new Date());
            dbWeight.insertWeight(currentDate, Integer.valueOf(weightEditText.getText().toString()));
            weightEditText.setText("");
            refresh();
        } else {
            weightEditText.setError("Weight cannot be empty.");
        }


    }
}
