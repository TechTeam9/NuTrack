package edu.uw.tcss450.nutrack.activity;

import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import edu.uw.tcss450.nutrack.R;

public class ProfileSetupActivity extends AppCompatActivity {

    private String genderChosen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);

        genderChosen = "male";
        ImageView maleIcon = (ImageView) findViewById(R.id.profileSetup_imageView_male);
        maleIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchGender("male");
            }
        });

        ImageView femaleIcon = (ImageView) findViewById(R.id.profileSetup_imageView_female);
        femaleIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchGender("female");
            }
        });

        //Button btnSubmit = (Button) findViewById(R.id.profileSetup_button_submit)

    }

    private void insertProfile() {

    }

    private void switchGender(String theGender) {
        ImageView maleIcon = (ImageView) findViewById(R.id.profileSetup_imageView_male);
        ImageView femaleIcon = (ImageView) findViewById(R.id.profileSetup_imageView_female);

        if (theGender.equals("male")) {
            if (!genderChosen.equals("male")) {
                maleIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.male_100, null));
                femaleIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_launcher, null));
                genderChosen = "male";
            }
        } else {
            if (!genderChosen.equals("female")) {
                femaleIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.female_100, null));
                maleIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_launcher, null));
                genderChosen = "female";
            }
        }
    }

}
