package edu.uw.tcss450.nutrack;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    private ImageView mainLogo;

    private EditText editTextEmail;

    private EditText editTextPassword;

    private EditText editTextComfirmPassword;

    private Button btnSubmit;

    private TextView textViewRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mainLogo = (ImageView) findViewById(R.id.NutrackLogo);
        editTextEmail = (EditText) findViewById(R.id.login_editText_email);
        editTextPassword = (EditText) findViewById(R.id.login_editText_password);
        btnSubmit = (Button) findViewById(R.id.login_button_login);
        textViewRegister = (TextView) findViewById(R.id.login_textView_register);

        editTextEmail.setVisibility(View.GONE);
        editTextPassword.setVisibility(View.GONE);
        btnSubmit.setVisibility(View.GONE);
        textViewRegister.setVisibility(View.GONE);

        textViewRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                initializeRegistrationDialog();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyLogin();
            }
        });

        if (verifyAccountExistance()) {
            verifyLogin();
        } else {
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


    }

    public void verifyLogin() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    private boolean verifyAccountExistance() {
        DBMemberTableHelper memberTable = new DBMemberTableHelper(this);
        //NEED TO CHANGE COMPARE TO == AFTER COMPLETING LOGIN PAGE
        if (memberTable.getMemberSize() >= 1) {
            memberTable.close();
            return true;
        } else {
            return false;
        }
    }

    // Codes for building a dialog.
    private void initializeRegistrationDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder((LoginActivity.this));
        View mView = getLayoutInflater().inflate(R.layout.dialog_registration, null);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.setCanceledOnTouchOutside(false);

        dialog.show();

        Button btnRegister = (Button) dialog.findViewById(R.id.registration_button_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email =  ((EditText) dialog.findViewById(R.id.registration_editText_email))
                        .getText()
                        .toString();
                String password = ((EditText) dialog.findViewById(R.id.registration_editText_password))
                        .getText()
                        .toString();
                String confirmPassword = ((EditText) dialog.findViewById(R.id.registration_editText_comfirmPassword))
                        .getText()
                        .toString();

                if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    TextView textViewError = (TextView) dialog.findViewById(R.id.registration_textView_error);
                    textViewError.setText("All Fields must be filled in.");
                    textViewError.setVisibility(View.VISIBLE);
                } else if (!password.equals(confirmPassword)){
                    TextView textViewError = (TextView) dialog.findViewById(R.id.registration_textView_error);
                    textViewError.setText("Confirm password has to be the same as password.");
                    textViewError.setVisibility(View.VISIBLE);
                } else {
                    if (insertNewMemberData(email, password)) {
                        dialog.dismiss();
                        verifyLogin();
                    }
                }
            }
        });
    }

    private boolean insertNewMemberData(String theEmail, String thePassword) {
        DBMemberTableHelper memberTable = new DBMemberTableHelper(this);

        if (memberTable.insertMember(theEmail, thePassword)) {
            memberTable.close();
            return true;
        } else {
            return false;
        }
    }


    private class LoginAnimationListener implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            editTextEmail.setAlpha(0f);
            editTextEmail.setVisibility(View.VISIBLE);

            editTextPassword.setAlpha(0f);
            editTextPassword.setVisibility(View.VISIBLE);

            btnSubmit.setAlpha(0f);
            btnSubmit.setVisibility(View.VISIBLE);

            textViewRegister.setAlpha(0f);
            textViewRegister.setVisibility(View.VISIBLE);

            int transitionRate = getResources().getInteger(android.R.integer.config_mediumAnimTime);

            editTextEmail.animate()
                    .alpha(1f)
                    .setDuration(transitionRate)
                    .setListener(null);

            editTextPassword.animate()
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
