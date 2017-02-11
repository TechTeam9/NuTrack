package edu.uw.tcss450.nutrack.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

import edu.uw.tcss450.nutrack.DBHelper.DBMemberInfoHelper;
import edu.uw.tcss450.nutrack.API.getAccountInfo;
import edu.uw.tcss450.nutrack.Helper.LoginHelper;
import edu.uw.tcss450.nutrack.API.AddAccountInfo;
import edu.uw.tcss450.nutrack.Helper.ProfileHelper;
import edu.uw.tcss450.nutrack.R;
import edu.uw.tcss450.nutrack.model.Account;
import edu.uw.tcss450.nutrack.model.Profile;

/**
 * Provide a login activity when user open the app.
 *
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

    /**
     * Builds and sets up LoginActivity.
     * @param savedInstanceState the saved instance state.
     */
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
                EditText fieldEmail = (EditText) findViewById(R.id.login_editText_email);
                EditText fieldPassword = (EditText) findViewById(R.id.login_editText_password);

                if (fieldEmail.getText().toString().isEmpty()) {
                    showError("Email field cannot be empty.");
                } else if (fieldPassword.getText().toString().isEmpty()) {
                    showError("Password field cannot be empty.");
                } else {
                    Account account = new Account(mFieldEmail.getText().toString(), mFieldPassword.getText().toString());
                    loginAccount(account);
                }
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
     * Pop out a toast to show user errors.
     */
    private void showError(String theMessage) {
        Toast.makeText(this, theMessage, Toast.LENGTH_SHORT).show();

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
        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);

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
                    if (textViewError != null) {
                        textViewError.setText(R.string.all_fields_must_fill_in_error, null);
                        textViewError.setVisibility(View.VISIBLE);
                    }
                } else if (!password.equals(confirmPassword)) {
                    TextView textViewError = (TextView) mRegistrationDialog.findViewById(R.id.registration_textView_error);
                    if (textViewError != null) {
                        textViewError.setText(R.string.passwords_not_the_same_error);
                        textViewError.setVisibility(View.VISIBLE);
                    }
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
        DBMemberInfoHelper memberTable = new DBMemberInfoHelper(this);

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
            if (textViewError != null) {
                textViewError.setText(R.string.email_already_exist, null);
                textViewError.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * Verifies login info. Upon success starts the ProfileSetupActivity, upon failure
     * informs the user of error.
     *
     * @param theResultCode the backend result indication
     */
    @Override
    public void onLoginCompleted(int theResultCode, String theEmail, String thePassword) {
        mEmail = theEmail;
        if (theResultCode == LoginHelper.CORRECT_LOGIN_INFO) {
            DBMemberInfoHelper memberTable = new DBMemberInfoHelper(this);

            if (memberTable.getMemberSize() == 0) {
                memberTable.insertMember(theEmail, thePassword);
            }

            ProfileHelper.checkProfileExistence(this, theEmail);

        } else if (theResultCode == LoginHelper.ACCOUNT_FOUND_BUT_LOGIN_ERROR) {
            DBMemberInfoHelper memberTable = new DBMemberInfoHelper(this);
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
            JSONArray jsonArray = new JSONArray(theResult);
            int resultCode = jsonArray.getJSONObject(0).getInt("result_code");

            if (resultCode == ProfileHelper.PROFILE_FOUND) {
                if (!ProfileHelper.hasProfile(this)) {
                    Profile profile = new Profile(jsonArray.getJSONObject(1).getString("name")
                            , jsonArray.getJSONObject(1).getString("gender").charAt(0)
                            , jsonArray.getJSONObject(1).getString("date_of_birth")
                            , jsonArray.getJSONObject(1).getDouble("height")
                            , jsonArray.getJSONObject(1).getDouble("weight")
                            , jsonArray.getJSONObject(1).getInt("avatar_id"));
                    ProfileHelper.insertLocalProfile(this, profile);
                }
                startMainActivity();
            } else if (resultCode == ProfileHelper.NO_PROFILE_FOUND) {
                startProfileSetupActivity();
            } else {
                Toast.makeText(this, "Oops, there is an unknown error", Toast.LENGTH_LONG).show();
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