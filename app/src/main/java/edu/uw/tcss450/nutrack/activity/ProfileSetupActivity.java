package edu.uw.tcss450.nutrack.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.ArrayList;

import edu.uw.tcss450.nutrack.fragment.AvatarSelectorFragment;
import edu.uw.tcss450.nutrack.helper.ProfileHelper;
import edu.uw.tcss450.nutrack.R;
import edu.uw.tcss450.nutrack.model.Profile;

import static android.media.CamcorderProfile.get;

/**
 * Profile Setup activity for first time user login to provide their information.
 */
public class ProfileSetupActivity extends AppCompatActivity implements AvatarSelectorFragment.OnFragmentInteractionListener, ProfileHelper.InsertProfileCompleted {
    /**
     * The user's chosen gender.
     */
    private String mGenderChosen;

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

        mGenderChosen = "Male";
        maleIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAvatarSelectorFragment.changeAvatarGender(AvatarSelectorFragment.MALE, femaleIcon);
                maleIcon.setClickable(false);
                mGenderChosen = "Male";
            }
        });
        maleIcon.setClickable(false);

        femaleIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAvatarSelectorFragment.changeAvatarGender(AvatarSelectorFragment.FEMALE, maleIcon);
                femaleIcon.setClickable(false);
                mGenderChosen = "Female";
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
        Context context = getApplicationContext();
        TextInputEditText fieldName = (TextInputEditText) findViewById(R.id.profileSetup_editText_name);
        TextInputEditText fieldHeight = (TextInputEditText) findViewById(R.id.profileSetup_editText_height);
        TextInputEditText fieldWeight = (TextInputEditText) findViewById(R.id.profileSetup_editText_weight);
        DatePicker datePicker = (DatePicker) findViewById(R.id.profileSetup_datePicker);
        ArrayList<TextInputEditText> fields = new ArrayList<>();
        String[] fieldsName = {"Name", "Height", "Weight"};
        fields.add(fieldName);
        fields.add(fieldHeight);
        fields.add(fieldWeight);
        String hint = "";
        int emptyCount = 0;
        for (int i = 0; i < fields.size(); i++) {
            if (fields.get(i).getText().toString().isEmpty()) {
                hint = hint + fieldsName[i] + ", ";
                emptyCount++;
            }
        }

        if (emptyCount > 0) {
            hint = hint.substring(0, hint.length() - 2);
            if (emptyCount == 1) {
                hint = hint + " is ";
            } else {
                hint = hint + " are ";
            }
            Toast.makeText(context, hint + "empty!", Toast.LENGTH_SHORT).show();
        } else {
            int avatarIconId = mAvatarSelectorFragment.getChosen();
            String dateOfBirth = datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth();

            Profile profile = new Profile(fieldName.getText().toString(), mGenderChosen
                    , dateOfBirth
                    , Integer.parseInt(fieldHeight.getText().toString())
                    , Integer.parseInt(fieldWeight.getText().toString())
                    , avatarIconId);


            SharedPreferences sharedPrefProfile = this.getSharedPreferences(getString(R.string.preference_profile), Context.MODE_PRIVATE);
            sharedPrefProfile.edit().putString("name", fieldName.getText().toString()).commit();
            sharedPrefProfile.edit().putString("gender", mGenderChosen).commit();
            sharedPrefProfile.edit().putString("dob", dateOfBirth).commit();
            sharedPrefProfile.edit().putInt("height", Integer.parseInt(fieldHeight.getText().toString())).commit();
            sharedPrefProfile.edit().putInt("weight", Integer.parseInt(fieldWeight.getText().toString())).commit();
            sharedPrefProfile.edit().putInt("avatar_id", avatarIconId).commit();

            ProfileHelper.insertProfile(this, mEmail, profile);
        }
    }

    @Override
    public void onFragmentInteraction(Uri theUri) {

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Sorry, you have to finish your profile", Toast.LENGTH_LONG).show();
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
