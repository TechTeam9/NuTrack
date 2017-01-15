package edu.uw.tcss450.nutrack;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Login extends AppCompatActivity {
    private ImageView mainLogo;

    private EditText textEmail;

    private EditText textPassword;

    private EditText textComfirmPassword;

    private Button btnSubmit;

    private TextView textViewRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mainLogo = (ImageView) findViewById(R.id.NutrackLogo);
        textEmail = (EditText) findViewById(R.id.textEmail);
        textPassword = (EditText) findViewById(R.id.textPassword);
        textComfirmPassword = (EditText) findViewById(R.id.textComfirmPassword);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        textViewRegister = (TextView) findViewById(R.id.textViewRegister);

        textEmail.setVisibility(View.GONE);
        textPassword.setVisibility(View.GONE);
        textComfirmPassword.setVisibility(View.GONE);
        btnSubmit.setVisibility(View.GONE);
        textViewRegister.setVisibility(View.GONE);

        final Animation moveMainLogoAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_logo);
        moveMainLogoAnimation.setFillAfter(true);
        moveMainLogoAnimation.setAnimationListener(new LoginAnimationListener());

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                mainLogo.startAnimation(moveMainLogoAnimation);
            }
        }, 1000);
    }

    public void initializeRegistrationForm(View view) {
        int transitionRate = getResources().getInteger(android.R.integer.config_mediumAnimTime);

        textViewRegister.animate()
                .alpha(0f)
                .setDuration(transitionRate);

        btnSubmit.animate()
                .translationYBy(140f)
                .setDuration(transitionRate);

        textComfirmPassword.setAlpha(0f);
        textComfirmPassword.setVisibility(View.VISIBLE);

        textComfirmPassword.animate()
                .alpha(1f)
                .setDuration(transitionRate);
    }

    public void verifyLoginValues(View view) {

    }

    public void verifyRegistrationValues(View view) {

    }

    public void verifyUser() {

    }

    private class LoginAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            textEmail.setAlpha(0f);
            textEmail.setVisibility(View.VISIBLE);

            textPassword.setAlpha(0f);
            textPassword.setVisibility(View.VISIBLE);

            btnSubmit.setAlpha(0f);
            btnSubmit.setVisibility(View.VISIBLE);

            textViewRegister.setAlpha(0f);
            textViewRegister.setVisibility(View.VISIBLE);

            int transitionRate = getResources().getInteger(android.R.integer.config_mediumAnimTime);

            textEmail.animate()
                    .alpha(1f)
                    .setDuration(transitionRate)
                    .setListener(null);

            textPassword.animate()
                    .alpha(1f)
                    .setDuration(transitionRate)
                    .setListener(null);

            btnSubmit.animate()
                    .alpha(1f)
                    .setDuration(transitionRate)
                    .setListener(null);

            textViewRegister.animate()
                    .alpha(1f)
                    .setDuration(transitionRate)
                    .setListener(null);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
