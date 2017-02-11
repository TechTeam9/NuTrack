package edu.uw.tcss450.nutrack.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.nutrack.AvatarSelectorFragment;
import edu.uw.tcss450.nutrack.ProfileHelper;
import edu.uw.tcss450.nutrack.R;
import edu.uw.tcss450.nutrack.model.Profile;

/**
 * Profile Setup activity for first time user login to provide their information.
 */
public class ProfileSetupActivity extends AppCompatActivity implements AvatarSelectorFragment.OnFragmentInteractionListener, ProfileHelper.InsertProfileCompleted {
    /**
     * The user's chosen gender.
     */
    private char mGenderChosen;

    /**
     * The user's email.
     */
    private String mEmail;

    /**
     * The avatar selector fragment.
     */
    private AvatarSelectorFragment mAvatarSelectorFragment;

    /**
     * Initializes the Activity and AvatarSelectorFragment.
     *
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);

        Intent intent = getIntent();

        mEmail = intent.getStringExtra("email");

        mAvatarSelectorFragment = new AvatarSelectorFragment();
        final Button maleIcon = (Button) findViewById(R.id.profileSetup_button_male);
        final Button femaleIcon = (Button) findViewById(R.id.profileSetup_button_female);

        mGenderChosen = 'm';
        maleIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAvatarSelectorFragment.changeAvatarGender(AvatarSelectorFragment.MALE, femaleIcon);
                maleIcon.setClickable(false);
                mGenderChosen = 'm';
            }
        });
        maleIcon.setClickable(false);

        femaleIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAvatarSelectorFragment.changeAvatarGender(AvatarSelectorFragment.FEMALE, maleIcon);
                femaleIcon.setClickable(false);
                mGenderChosen = 'f';
            }
        });

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.avatar_frame, mAvatarSelectorFragment, "Avatar");
        fragmentTransaction.commit();

        Button btnSubmit = (Button) findViewById(R.id.profileSetup_button_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitProfile();
            }
        });

    }

    /**
     * Starts the MainActivity.
     */
    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Submits the user's profile.
     */
    private void submitProfile() {
        TextInputEditText fieldName = (TextInputEditText) findViewById(R.id.profileSetup_editText_name);
        TextInputEditText fieldHeight = (TextInputEditText) findViewById(R.id.profileSetup_editText_height);
        TextInputEditText fieldWeight = (TextInputEditText) findViewById(R.id.profileSetup_editText_weight);
        DatePicker datePicker = (DatePicker) findViewById(R.id.profileSetup_datePicker);

        int avatarIconId = mAvatarSelectorFragment.getChosen();
        String dateOfBirth = datePicker.getYear() + "-" + datePicker.getMonth() + "-" + datePicker.getDayOfMonth();

        Profile profile = new Profile(fieldName.getText().toString(), mGenderChosen
                , dateOfBirth
                , Double.parseDouble(fieldHeight.getText().toString())
                , Double.parseDouble(fieldWeight.getText().toString())
                , avatarIconId);

        System.out.println(mEmail);

        System.out.println(fieldName.getText().toString());
        System.out.println(mGenderChosen);
        System.out.println(dateOfBirth);
        System.out.println(Double.parseDouble(fieldHeight.getText().toString()));
        System.out.println(Double.parseDouble(fieldWeight.getText().toString()));
        System.out.println(avatarIconId);


        ProfileHelper.insertProfile(this, mEmail, profile);
    }

    @Override
    public void onFragmentInteraction(Uri theUri) {

    }

    /**
     * Starts the main activity after verifying the user account insert completed successfully.
     *
     * @param theResult the backend result indication
     */
    @Override
    public void onInsertProfileCompleted(String theResult) {
        try {
            JSONObject jsonObject = new JSONObject(theResult);
            int resultCode = jsonObject.getInt("result_code");
            if (resultCode == ProfileHelper.INSERT_SUCCESS) {
                startMainActivity();
            } else if (resultCode == ProfileHelper.INSERT_FAILURE) {

            } else {
                Toast.makeText(this, "Oops, there is an unexpected error.", Toast.LENGTH_LONG);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
