package edu.uw.tcss450.nutrack.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import edu.uw.tcss450.nutrack.helper.ProfileHelper;
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
public class ProfileFragment extends Fragment implements EditProfileDialogFragment.OnFragmentInteractionListener, View.OnClickListener{
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

    private static View mView;

    /**
     * Fragment interaction listener.
     */
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
        mView = inflater.inflate(R.layout.fragment_profile, container, false);

        LinearLayout layout = (LinearLayout) mView.findViewById(R.id.profile_frame_name);
        layout.setOnClickListener(this);
        layout = (LinearLayout) mView.findViewById(R.id.profile_frame_gender);
        layout.setOnClickListener(this);
        layout = (LinearLayout) mView.findViewById(R.id.profile_frame_dob);
        layout.setOnClickListener(this);
        layout = (LinearLayout) mView.findViewById(R.id.profile_frame_height);
        layout.setOnClickListener(this);
        layout = (LinearLayout) mView.findViewById(R.id.profile_frame_weight);
        layout.setOnClickListener(this);

        initializePersonalInfo(mView);
        return mView;
    }

    @Override
    public void onClick(View v) {

        FragmentManager fragmentManger = getActivity().getSupportFragmentManager();
        DialogFragment editProfileDialog = new EditProfileDialogFragment();
        Bundle bundle = new Bundle();

        if (mListener != null) {
            Log.d("type", String.valueOf(v.getId()));
            switch (v.getId()) {
                case R.id.profile_frame_name:
                    bundle.putInt("type", EditProfileDialogFragment.NAME_TYPE);
                    break;
                case R.id.profile_frame_gender:
                    bundle.putInt("type", EditProfileDialogFragment.GENDER_TYPE);
                    Log.d("type", "Gender");
                    break;
                case R.id.profile_frame_dob:
                    bundle.putInt("type", EditProfileDialogFragment.DOB_TYPE);
                    break;
                case R.id.profile_frame_height:
                    bundle.putInt("type", EditProfileDialogFragment.HEIGHT_TYPE);
                    break;
                case R.id.profile_frame_weight:
                    bundle.putInt("type", EditProfileDialogFragment.WEIGHT_TYPE);
                    break;
            }
        }


        editProfileDialog.setArguments(bundle);
        editProfileDialog.show(fragmentManger, "Edit Profile Dialog Fragment");
    }

    @Override
    public void onFragmentInteraction() {

    }

    /**
     * Get personal info from database and put them in the textview.
     * @param theView
     */
    public void initializePersonalInfo(View theView) {
        Profile profile = ProfileHelper.getPersonalInfo(getContext());

        TextView textView = (TextView) theView.findViewById(R.id.profile_textView_valueName);
        textView.setText(profile.getName());
        textView = (TextView) theView.findViewById(R.id.profile_textView_valueGender);

        ImageView imageAvatar = (ImageView) theView.findViewById(R.id.profile_imageView_avatar);


        if (profile.getGender() == 'm') {
            textView.setText("Male");
        } else {
            textView.setText("Female");
        }

        textView = (TextView) theView.findViewById(R.id.profile_textView_valueDoB);
        textView.setText(profile.getDOB());
        textView = (TextView) theView.findViewById(R.id.profile_textView_valueHeight);
        textView.setText(String.valueOf(profile.getHeight()));
        textView = (TextView) theView.findViewById(R.id.profile_textView_valueWeight);
        textView.setText(String.valueOf(profile.getWeight()));

        imageAvatar.setImageResource(profile.getAvatarId());
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
