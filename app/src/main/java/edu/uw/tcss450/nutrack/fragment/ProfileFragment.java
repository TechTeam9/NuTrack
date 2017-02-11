package edu.uw.tcss450.nutrack.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import edu.uw.tcss450.nutrack.ProfileHelper;
import edu.uw.tcss450.nutrack.R;
import edu.uw.tcss450.nutrack.model.Profile;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);

        final LinearLayout nameFrame = (LinearLayout) view.findViewById(R.id.profile_frame_name);

        /*
        nameFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activateEditMode(v);
            }
        });
        */

        initializePersonalInfo(view);

        return view;
    }

    /**
     *
     * @param theView
     */
    public void initializePersonalInfo(View theView) {
        Profile profile = ProfileHelper.getPersonalInfo(getContext());

        TextView viewName = (TextView) theView.findViewById(R.id.profile_textView_valueName);
        TextView viewGender = (TextView) theView.findViewById(R.id.profile_textView_valueGender);
        TextView viewDOB = (TextView) theView.findViewById(R.id.profile_textView_valueDoB);
        TextView viewHeight = (TextView) theView.findViewById(R.id.profile_textView_valueHeight);
        TextView viewWeight = (TextView) theView.findViewById(R.id.profile_textView_valueWeight);

        viewName.setText(profile.getName());
        if (profile.getGender() == 'm') {
            viewGender.setText("Male");
        } else {
            viewGender.setText("Female");
        }
        viewDOB.setText(profile.getDOB());
        viewHeight.setText(String.valueOf(profile.getHeight()));
        viewWeight.setText(String.valueOf(profile.getWeight()));
    }

    /*
    private void activateEditMode(View view) {
        TextView nameValue = (TextView) view.findViewById(R.id.profile_textView_valueName);
        EditText nameEdit = (EditText) view.findViewById(R.id.profile_editText_name);
        nameEdit.setText(nameValue.getText().toString());
        nameValue.setVisibility(View.GONE);
        nameEdit.setVisibility(View.VISIBLE);

        TextView heightValue = (TextView) view.findViewById(R.id.profile_textView_valueHeight);
        EditText heightEdit = (EditText) view.findViewById(R.id.profile_editText_height);
        heightEdit.setText(heightValue.getText().toString());
        heightValue.setVisibility(View.GONE);
        heightEdit.setVisibility(View.VISIBLE);

        TextView weightValue = (TextView) view.findViewById(R.id.profile_textView_valueWeight);
        EditText weightEdit = (EditText) view.findViewById(R.id.profile_editText_weight);
        weightEdit.setText(weightValue.getText().toString());
        weightValue.setVisibility(View.GONE);
        weightEdit.setVisibility(View.VISIBLE);

        TextView genderValue = (TextView) view.findViewById(R.id.profile_textView_valueGender);
        EditText genderEdit = (EditText) view.findViewById(R.id.profile_editText_gender);
        //genderEdit.setText(genderValue.getText().toString());
        genderValue.setVisibility(View.GONE);
        genderEdit.setVisibility(View.VISIBLE);

        TextView DoBValue = (TextView) view.findViewById(R.id.profile_textView_valueDoB);
        EditText DoBEdit = (EditText) view.findViewById(R.id.profile_editText_DoB);
        //DoBEdit.setText(DoBValue.getText().toString());
        DoBValue.setVisibility(View.GONE);
        DoBEdit.setVisibility(View.VISIBLE);

    }
    */


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
