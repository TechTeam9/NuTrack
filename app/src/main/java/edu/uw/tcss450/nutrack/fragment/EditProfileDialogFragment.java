package edu.uw.tcss450.nutrack.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import edu.uw.tcss450.nutrack.DBHelper.DBPersonalInfoTableHelper;
import edu.uw.tcss450.nutrack.Helper.ProfileHelper;
import edu.uw.tcss450.nutrack.R;
import edu.uw.tcss450.nutrack.model.Profile;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditProfileDialogFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditProfileDialogFragment} factory method to
 * create an instance of this fragment.
 */
public class EditProfileDialogFragment extends DialogFragment {

    public static final int NAME_TYPE = 0;

    public static final int GENDER_TYPE = 1;

    public static final int DOB_TYPE = 2;

    public static final int HEIGHT_TYPE = 3;

    public static final int WEIGHT_TYPE = 4;

    private int mType;

    private OnFragmentInteractionListener mListener;

    public EditProfileDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mType = getArguments().getInt("type");
            Log.d("Hello", String.valueOf(mType));
        } else {
            mType = -1;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_edit_profile_dialog, container, false);

        final TextView textView = (TextView) view.findViewById(R.id.dialog_editProfile_title);
        final EditText editText = (EditText) view.findViewById(R.id.dialog_editProfile_inputField);
        final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.dialog_radioGroup);


        Button button = (Button) view.findViewById(R.id.dialog_editProfile_submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBPersonalInfoTableHelper dbHelper = new DBPersonalInfoTableHelper(getContext());

                switch (mType) {
                    case NAME_TYPE:
                        if (editText.getText().length() < 1) {
                            editText.setError("Field cannot be left blank.");
                        }
                        dbHelper.editPersonalInfo(DBPersonalInfoTableHelper.COLUMN_NAME, editText.getText().toString());
                        break;
                    case GENDER_TYPE:
                        String value;
                        if (radioGroup.getCheckedRadioButtonId() == R.id.dialog_editProfile_radioMale) {
                            value = "m";
                        } else {
                            value = "f";
                        }
                        dbHelper.editPersonalInfo(DBPersonalInfoTableHelper.COLUMN_GENDER, value);
                        break;
                    case DOB_TYPE:
                        //-------------------------------------------------Need Add------------------------------------------------
                        break;
                    case HEIGHT_TYPE:
                        if (editText.getText().length() < 1) {
                            editText.setError("Field cannot be left blank.");
                        }
                        dbHelper.editPersonalInfoNonString(DBPersonalInfoTableHelper.COLUMN_HEIGHT, editText.getText().toString());
                        break;
                    case WEIGHT_TYPE:
                        if (editText.getText().length() < 1) {
                            editText.setError("Field cannot be left blank.");
                        }
                        dbHelper.editPersonalInfoNonString(DBPersonalInfoTableHelper.COLUMN_WEIGHT, editText.getText().toString());
                        break;
                    default:
                        textView.setText("Error");
                        break;
                }
                dismiss();
            }
        });

        button = (Button) view.findViewById(R.id.dialog_editProfile_cancel);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        Profile profile = ProfileHelper.getPersonalInfo(getContext());
        if (mType != -1) {
            switch (mType) {
                case NAME_TYPE:
                    textView.setText("Name");
                    editText.setHint(profile.getName());
                    editText.setVisibility(View.VISIBLE);
                    DBPersonalInfoTableHelper dbHelper = new DBPersonalInfoTableHelper(getContext());
                    break;
                case GENDER_TYPE:
                    textView.setText("Gender");
                    radioGroup.setVisibility(View.VISIBLE);
                    RadioButton radioButton;
                    if (profile.getGender() == 'm') {
                        radioButton = (RadioButton) view.findViewById(R.id.dialog_editProfile_radioMale);
                    } else {
                        radioButton = (RadioButton) view.findViewById(R.id.dialog_editProfile_radioFemale);
                    }

                    radioButton.setChecked(true);
                    break;
                case DOB_TYPE:
                    textView.setText("Date of Birth");
                    break;
                case HEIGHT_TYPE:
                    textView.setText("Height");
                    editText.setHint(String.valueOf(profile.getHeight()));
                    editText.setVisibility(View.VISIBLE);
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    break;
                case WEIGHT_TYPE:
                    textView.setText("Weight");
                    editText.setHint(String.valueOf(profile.getWeight()));
                    editText.setVisibility(View.VISIBLE);
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);

                    break;
                default:
                    textView.setText("Error");
                    break;
            }
        }


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction();
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
        void onFragmentInteraction();
    }
}
