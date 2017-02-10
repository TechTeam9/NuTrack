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

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.nutrack.DBHelper.DBMemberTableHelper;
import edu.uw.tcss450.nutrack.getAccountInfo;
import edu.uw.tcss450.nutrack.LoginHelper;
import edu.uw.tcss450.nutrack.AddAccountInfo;
import edu.uw.tcss450.nutrack.ProfileHelper;
import edu.uw.tcss450.nutrack.R;
import edu.uw.tcss450.nutrack.model.Account;

/**
 * @Author
 */
public class LoginActivity extends AppCompatActivity implements AddAccountInfo.RegistrationCompleted, getAccountInfo.LoginCompleted, ProfileHelper.CheckProfileCompleted {

    /**
     * The ImageView for the main logo of the application.
     */
    private ImageView mMainLogo;

    /**
     * The EditText for the email input.
     */
    private EditText mFieldEmail;

    /**
     * The EditText for the password input.
     */
    private EditText mFieldPassword;

    /**
     * The Button for login.
     */
    private Button mBtnSubmit;

    /**
     * The TextView for register option.
     */
    private TextView mViewRegister;

    /**
     * The registration dialog.
     */
    private AlertDialog mRegistrationDialog;

    /**
     * The email input.
     */
    private String mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mMainLogo = (ImageView) findViewById(R.id.NutrackLogo);
        mFieldEmail = (EditText) findViewById(R.id.login_editText_email);
        mFieldPassword = (EditText) findViewById(R.id.login_editText_password);
        mBtnSubmit = (Button) findViewById(R.id.login_button_login);
        mViewRegister = (TextView) findViewById(R.id.login_textView_register);

        mFieldEmail.setVisibility(View.GONE);
        mFieldPassword.setVisibility(View.GONE);
        mBtnSubmit.setVisibility(View.GONE);
        mViewRegister.setVisibility(View.GONE);

        mViewRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View theView) {
                initializeRegistrationDialog();
            }
        });

        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View theView) {
                Account account = new Account(mFieldEmail.getText().toString(), mFieldPassword.getText().toString());
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

    /**
     * Starts the Login Form animation.
     */
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

    /**
     * Initializes and shows the registration dialog when called.
     */
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

    /**
     * Finishes LoginActivity and starts MainActivity.
     */
    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        finish();
        startActivity(intent);
    }

    /**
     * Finishes LoginActivity and starts ProfileSetupActivity.
     */
    private void startProfileSetupActivity() {
        Intent intent = new Intent(this, ProfileSetupActivity.class);
        intent.putExtra("email", mEmail);
        finish();
        startActivity(intent);
    }

    /**
     * Calls LoginHelper to create a new account.
     *
     * @param theEmail    the entered user email
     * @param thePassword the entered user password
     */
    private void createAccount(String theEmail, String thePassword) {
        LoginHelper.addNewAccount(new Account(theEmail, thePassword), this);
    }

    /**
     * Call LoginHelper to verify account and take appropriate actions according to the email
     * and password.
     *
     * @param theAccount the user's account
     */
    private void loginAccount(Account theAccount) {
        LoginHelper.verifyAccount(theAccount, this);
    }

    /**
     * Inserts a new account into application's local storage.
     *
     * @param theEmail    the user's email
     * @param thePassword the user's password
     * @return a pass or fail indicator
     */
    private boolean insertNewMemberData(String theEmail, String thePassword) {
        DBMemberTableHelper memberTable = new DBMemberTableHelper(this);

        if (memberTable.insertMember(theEmail, thePassword)) {
            memberTable.close();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Verifies registration info. Upon success starts the ProfileSetupActivity, upon failure
     * informs the user of error.
     *
     * @param theResultCode the backend result indication
     */
    @Override
    public void onRegistrationCompleted(int theResultCode) {
        if (theResultCode == LoginHelper.REGISTRATION_SUCCESS) {
            String email = ((EditText) mRegistrationDialog.findViewById(R.id.registration_editText_email))
                    .getText()
                    .toString();
            String password = ((EditText) mRegistrationDialog.findViewById(R.id.registration_editText_password))
                    .getText()
                    .toString();

            if (insertNewMemberData(email, password)) {
                mRegistrationDialog.dismiss();

                mEmail = email;
                startProfileSetupActivity();
            }

        } else if (theResultCode == LoginHelper.EMAIL_ALREADY_EXIST) {
            TextView textViewError = (TextView) mRegistrationDialog.findViewById(R.id.registration_textView_error);
            textViewError.setText(R.string.email_already_exist, null);
            textViewError.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Verifies login info. Upon success starts the ProfileSetupActivity, upon failure
     * informs the user of error.
     *
     * @param theResultCode the backend result indication
     */
    @Override
    public void onLoginCompleted(int theResultCode, String theEmail) {
        mEmail = theEmail;
        if (theResultCode == LoginHelper.CORRECT_LOGIN_INFO) {
            DBMemberTableHelper memberTable = new DBMemberTableHelper(this);
            String email = ((EditText) findViewById(R.id.login_editText_email))
                    .getText()
                    .toString();
            String password = ((EditText) findViewById(R.id.login_editText_password))
                    .getText()
                    .toString();

            if (memberTable.insertMember(email, password)) {
                ProfileHelper.checkProfileExistence(this, email);
            }
        } else if (theResultCode == LoginHelper.ACCOUNT_FOUND_BUT_LOGIN_ERROR) {
            DBMemberTableHelper memberTable = new DBMemberTableHelper(this);
            memberTable.deleteData();
            memberTable.close();

            initializeLoginForm();
            Toast.makeText(this, "Auto Login Error. Please Login Again.", Toast.LENGTH_SHORT).show();
        } else if (theResultCode == LoginHelper.INCORRECT_PASSWORD) {
            Toast.makeText(this, "Incorrect Password", Toast.LENGTH_SHORT).show();
        } else if (theResultCode == LoginHelper.NO_USERNAME_FOUND) {
            Toast.makeText(this, "No Username Found", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Checks to see that a profile has been setup for the user.
     *
     * @param theResult the backend result indication
     */
    @Override
    public void onCheckProfileCompleted(String theResult) {
        try {
            JSONObject jsonObject = new JSONObject(theResult);
            int resultCode = jsonObject.getInt("result_code");
            if (resultCode == ProfileHelper.PROFILE_FOUND) {
                startMainActivity();
            } else if (resultCode == ProfileHelper.NO_PROFILE_FOUND) {
                EditText fieldEmail = (EditText) findViewById(R.id.login_editText_email);
                startProfileSetupActivity();
            } else {
                Toast.makeText(this, "Oops, there is an unknown error", Toast.LENGTH_LONG);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    /**
     * Inner class that contains the login animation.
     */
    private class LoginAnimationListener implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation theAnimation) {
        }

        @Override
        public void onAnimationEnd(Animation theAnimation) {
            mFieldEmail.setAlpha(0f);
            mFieldEmail.setVisibility(View.VISIBLE);

            mFieldPassword.setAlpha(0f);
            mFieldPassword.setVisibility(View.VISIBLE);

            mBtnSubmit.setAlpha(0f);
            mBtnSubmit.setVisibility(View.VISIBLE);

            mViewRegister.setAlpha(0f);
            mViewRegister.setVisibility(View.VISIBLE);

            int transitionRate = 500;

            mFieldEmail.animate()
                    .alpha(1f)
                    .setDuration(transitionRate)
                    .setListener(null);

            mFieldPassword.animate()
                    .alpha(1f)
                    .setDuration(transitionRate)
                    .setListener(null);

            mBtnSubmit.animate()
                    .alpha(1f)
                    .setDuration(transitionRate)
                    .setListener(null);

            mViewRegister.animate()
                    .alpha(1f)
                    .setDuration(transitionRate)
                    .setListener(null);
        }

        @Override
        public void onAnimationRepeat(Animation theAnimation) {
        }
    }
}
