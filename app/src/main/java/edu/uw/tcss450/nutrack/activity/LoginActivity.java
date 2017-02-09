package edu.uw.tcss450.nutrack.activity;

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
import android.widget.Toast;

import edu.uw.tcss450.nutrack.DBHelper.DBMemberTableHelper;
import edu.uw.tcss450.nutrack.GetWebServiceTask;
import edu.uw.tcss450.nutrack.LoginHelper;
import edu.uw.tcss450.nutrack.PostWebServiceTask;
import edu.uw.tcss450.nutrack.ProfileHelper;
import edu.uw.tcss450.nutrack.R;
import edu.uw.tcss450.nutrack.model.Account;
import edu.uw.tcss450.nutrack.model.Profile;

public class LoginActivity extends AppCompatActivity implements PostWebServiceTask.RegistrationCompleted, GetWebServiceTask.LoginCompleted, ProfileHelper.CheckProfileCompleted{
    private ImageView mMainLogo;

    private EditText mEditTextEmail;

    private EditText mEditTextPassword;

    private EditText mEditTextComfirmPassword;

    private Button mBtnSubmit;

    private TextView mTextViewRegister;

    private AlertDialog mRegistrationDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mMainLogo = (ImageView) findViewById(R.id.NutrackLogo);
        mEditTextEmail = (EditText) findViewById(R.id.login_editText_email);
        mEditTextPassword = (EditText) findViewById(R.id.login_editText_password);
        mBtnSubmit = (Button) findViewById(R.id.login_button_login);
        mTextViewRegister = (TextView) findViewById(R.id.login_textView_register);

        mEditTextEmail.setVisibility(View.GONE);
        mEditTextPassword.setVisibility(View.GONE);
        mBtnSubmit.setVisibility(View.GONE);
        mTextViewRegister.setVisibility(View.GONE);

        mTextViewRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                initializeRegistrationDialog();
            }
        });

        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Account account = new Account(mEditTextEmail.getText().toString(), mEditTextPassword.getText().toString());
                loginAccount(account);
            }
        });

        switch (LoginHelper.autoVerifyAccountExistance(this)) {
            case LoginHelper.NO_ACCOUNT_FOUND:
                initializeLoginForm();
                break;
            default:
                break;
        }
    }

    private void initializeLoginForm() {
        final Animation moveMainLogoAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_logo);
        moveMainLogoAnimation.setFillAfter(true);
        moveMainLogoAnimation.setAnimationListener(new LoginAnimationListener());

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                mMainLogo.startAnimation(moveMainLogoAnimation);
            }
        }, 1000);
    }

    // Codes for building a dialog.
    private void initializeRegistrationDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder((LoginActivity.this));
        View mView = getLayoutInflater().inflate(R.layout.dialog_registration, null);

        mBuilder.setView(mView);
        mRegistrationDialog = mBuilder.create();
        mRegistrationDialog.setCanceledOnTouchOutside(false);

        mRegistrationDialog.show();

        Button btnRegister = (Button) mRegistrationDialog.findViewById(R.id.registration_button_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String email = ((EditText) mRegistrationDialog.findViewById(R.id.registration_editText_email))
                    .getText()
                    .toString();
            String password = ((EditText) mRegistrationDialog.findViewById(R.id.registration_editText_password))
                    .getText()
                    .toString();
            String confirmPassword = ((EditText) mRegistrationDialog.findViewById(R.id.registration_editText_comfirmPassword))
                    .getText()
                    .toString();

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                TextView textViewError = (TextView) mRegistrationDialog.findViewById(R.id.registration_textView_error);
                textViewError.setText(R.string.all_fields_must_fill_in_error, null);
                textViewError.setVisibility(View.VISIBLE);
            } else if (!password.equals(confirmPassword)) {
                TextView textViewError = (TextView) mRegistrationDialog.findViewById(R.id.registration_textView_error);
                textViewError.setText(R.string.passwords_not_the_same_error);
                textViewError.setVisibility(View.VISIBLE);
            } else {
                    createAccount(email, password);

            }
            }
        });
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        finish();
        startActivity(intent);
    }

    private void startProfileSetupActivity(String theEmail) {

    }

    private void createAccount(String theEmail, String thePassword) {
        LoginHelper.addNewAccount(new Account(theEmail, thePassword), this);
    }

    private void loginAccount(Account theAccount) {
        LoginHelper.verifyAccount(theAccount, this);
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

    @Override
    public void onRegistrationCompleted(int resultCode) {
        if (resultCode == LoginHelper.REGISTRATION_SUCCESS) {
            String email = ((EditText) mRegistrationDialog.findViewById(R.id.registration_editText_email))
                    .getText()
                    .toString();
            String password = ((EditText) mRegistrationDialog.findViewById(R.id.registration_editText_password))
                    .getText()
                    .toString();

            if (insertNewMemberData(email, password)) {
                mRegistrationDialog.dismiss();

                startProfileSetupActivity(email);
            }

        } else if (resultCode == LoginHelper.EMAIL_ALREADY_EXIST){
            TextView textViewError = (TextView) mRegistrationDialog.findViewById(R.id.registration_textView_error);
            textViewError.setText(R.string.email_already_exist, null);
            textViewError.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoginCompleted(int resultCode) {
        if (resultCode == LoginHelper.CORRECT_LOGIN_INFO) {
            DBMemberTableHelper memberTable = new DBMemberTableHelper(this);
            String email = ((EditText) findViewById(R.id.login_editText_email))
                    .getText()
                    .toString();
            String password = ((EditText) findViewById(R.id.login_editText_password))
                    .getText()
                    .toString();

            if (memberTable.insertMember(email, password)) {
                startMainActivity();
            };
        } else if (resultCode == LoginHelper.ACCOUNT_FOUND_BUT_LOGIN_ERROR) {
            DBMemberTableHelper memberTable = new DBMemberTableHelper(this);
            memberTable.deleteData();
            memberTable.close();

            initializeLoginForm();
            Toast.makeText(this, "Auto Login Error. Please Login Again.", Toast.LENGTH_SHORT).show();
        } else if (resultCode == LoginHelper.INCORRECT_PASSWORD) {
            Toast.makeText(this, "Incorrect Password", Toast.LENGTH_SHORT).show();
        } else if (resultCode == LoginHelper.NO_USERNAME_FOUND) {
            Toast.makeText(this, "No Username Found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCheckProfileCompleted(int theResultCode, Profile theProfile) {
        switch (theResultCode) {
            case ProfileHelper.PROFILE_FOUND:




                break;
            case ProfileHelper.NO_PROFILE_FOUND:







                break;
        }
    }


    private class LoginAnimationListener implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            mEditTextEmail.setAlpha(0f);
            mEditTextEmail.setVisibility(View.VISIBLE);

            mEditTextPassword.setAlpha(0f);
            mEditTextPassword.setVisibility(View.VISIBLE);

            mBtnSubmit.setAlpha(0f);
            mBtnSubmit.setVisibility(View.VISIBLE);

            mTextViewRegister.setAlpha(0f);
            mTextViewRegister.setVisibility(View.VISIBLE);

            int transitionRate = getResources().getInteger(android.R.integer.config_mediumAnimTime);

            mEditTextEmail.animate()
                    .alpha(1f)
                    .setDuration(transitionRate)
                    .setListener(null);

            mEditTextPassword.animate()
                    .alpha(1f)
                    .setDuration(transitionRate)
                    .setListener(null);

            mBtnSubmit.animate()
                    .alpha(1f)
                    .setDuration(transitionRate)
                    .setListener(null);

            mTextViewRegister.animate()
                    .alpha(1f)
                    .setDuration(transitionRate)
                    .setListener(null);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
