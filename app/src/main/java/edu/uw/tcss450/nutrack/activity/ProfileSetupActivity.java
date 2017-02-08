package edu.uw.tcss450.nutrack.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.app.Fragment;
import android.net.Uri;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import edu.uw.tcss450.nutrack.AvatorSelectorFragment;
import edu.uw.tcss450.nutrack.R;

public class ProfileSetupActivity extends AppCompatActivity implements AvatorSelectorFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);

        final AvatorSelectorFragment avatorSelectorFragment = new AvatorSelectorFragment();
        final Button maleIcon = (Button) findViewById(R.id.profileSetup_button_male);
        final Button femaleIcon = (Button) findViewById(R.id.profileSetup_button_female);

        maleIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avatorSelectorFragment.changeAvatarGender(AvatorSelectorFragment.MALE, femaleIcon);
                maleIcon.setClickable(false);
            }
        });
        maleIcon.setClickable(false);

        femaleIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avatorSelectorFragment.changeAvatarGender(AvatorSelectorFragment.FEMALE, maleIcon);
                femaleIcon.setClickable(false);
            }
        });

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.avatar_frame, avatorSelectorFragment, "Avator");
        fragmentTransaction.commit();

    }



    private void insertProfile() {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
