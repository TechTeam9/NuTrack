package edu.uw.tcss450.nutrack.fragment;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarListener;
import edu.uw.tcss450.nutrack.R;
import edu.uw.tcss450.nutrack.database.DBDailyLog;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DailyLogFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DailyLogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DailyLogFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Date mSelectedDate;

    private ArrayList<DailyLogFood> mBreakfastList;

    private ArrayList<DailyLogFood> mLunchList;

    private ArrayList<DailyLogFood> mDinnerList;

    private ArrayList<DailyLogFood> mSnackList;

    private View mView;

    public DailyLogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DailyLogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DailyLogFragment newInstance(String param1, String param2) {
        DailyLogFragment fragment = new DailyLogFragment();
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
        mView = inflater.inflate(R.layout.fragment_daily_log, container, false);



        //Beware of API < 24
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(mView, R.id.calendarView)
                .startDate(startDate.getTime())
                .endDate(endDate.getTime())
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Date date, int position) {
                mSelectedDate = date;
                intializeListView();
            }
        });

        //get list of food from database
        mSelectedDate = new Date();
        intializeListView();

        return mView;
    }

    private void intializeListView() {
        final ListView breakfastListView = (ListView) mView.findViewById(R.id.breakfast_listView);
        final ListView lunchListView = (ListView) mView.findViewById(R.id.lunch_listView);
        final ListView dinnerListView = (ListView) mView.findViewById(R.id.dinner_listView);
        final ListView snackListView = (ListView) mView.findViewById(R.id.snack_listView);


        ArrayList<HashMap<String, String>> foodList = new ArrayList<>();

        DBDailyLog db = new DBDailyLog(getContext());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        ArrayList<HashMap<String, String>> breakfastContentList = new ArrayList<>();
        ArrayList<HashMap<String, String>> lunchContentList = new ArrayList<>();
        ArrayList<HashMap<String, String>> dinnerContentList = new ArrayList<>();
        ArrayList<HashMap<String, String>> snackContentList = new ArrayList<>();

        mBreakfastList = new ArrayList<>();
        mLunchList = new ArrayList<>();
        mDinnerList = new ArrayList<>();
        mSnackList = new ArrayList<>();

        try {
            Cursor cursor = db.getFoodByDate(dateFormat.format(mSelectedDate).toString());
            if (cursor.getCount() != 0) {
                cursor.moveToFirst();

                System.out.println(cursor.getCount());
                for (int i = 0; i < cursor.getCount(); i++) {
                    System.out.println(mBreakfastList.size());

                    DailyLogFood dailyLogFood = new DailyLogFood(cursor.getString(1), cursor.getString(3), cursor.getInt(0), cursor.getInt(2));
                    HashMap<String, String> tempFood = new HashMap<>();
                    tempFood.put("name", cursor.getString(1));
                    tempFood.put("type", cursor.getString(3));

                    switch (cursor.getString(4)) {
                        case "breakfast":
                            mBreakfastList.add(dailyLogFood);
                            breakfastContentList.add(tempFood);
                            break;
                        case "lunch":
                            mLunchList.add(dailyLogFood);
                            lunchContentList.add(tempFood);
                            break;
                        case "dinner":
                            mDinnerList.add(dailyLogFood);
                            dinnerContentList.add(tempFood);
                            break;
                        case "snack":
                            mSnackList.add(dailyLogFood);
                            snackContentList.add(tempFood);
                            break;
                    }
                    cursor.moveToNext();
                }

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        SimpleAdapter adapter = new SimpleAdapter(getContext(), breakfastContentList, android.R.layout.two_line_list_item, new String[]{"name", "type"}, new int[]{android.R.id.text1, android.R.id.text2});
        breakfastListView.setAdapter(adapter);
        calculateListViewHeight(breakfastListView);

        adapter = new SimpleAdapter(getContext(), lunchContentList, android.R.layout.two_line_list_item, new String[]{"name", "type"}, new int[]{android.R.id.text1, android.R.id.text2});
        lunchListView.setAdapter(adapter);
        calculateListViewHeight(lunchListView);

        adapter = new SimpleAdapter(getContext(), dinnerContentList, android.R.layout.two_line_list_item, new String[]{"name", "type"}, new int[]{android.R.id.text1, android.R.id.text2});
        dinnerListView.setAdapter(adapter);
        calculateListViewHeight(dinnerListView);

        adapter = new SimpleAdapter(getContext(), snackContentList, android.R.layout.two_line_list_item, new String[]{"name", "type"}, new int[]{android.R.id.text1, android.R.id.text2});
        snackListView.setAdapter(adapter);
        calculateListViewHeight(snackListView);
    }

    private void calculateListViewHeight(ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        if (adapter == null) {
            return;
        }

        ViewGroup viewGroup = listView;
        int finalHeight = 0;

        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, viewGroup);
            listItem.measure(0, 0);
            finalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = finalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
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

    private class DailyLogFood {
        private String mName;

        private String mType;

        private int mDailyLogId;

        private int mId;

        public DailyLogFood(String theName, String theType, int theDailyLogId, int theId) {
            mName = theName;
            mType = theType;
            mDailyLogId = theDailyLogId;
            mId = theId;
        }

        public String getName() {
            return mName;
        }

        public String getType() {
            return mType;
        }

        public int getDailyLogId() {
            return mDailyLogId;
        }

        public int getId() {
            return mId;
        }
    }
}
