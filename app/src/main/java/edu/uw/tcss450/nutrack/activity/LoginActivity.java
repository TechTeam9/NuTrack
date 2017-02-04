package edu.uw.tcss450.nutrack.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import edu.uw.tcss450.nutrack.R;
import edu.uw.tcss450.nutrack.model.Account;

public class LoginActivity extends AppCompatActivity implements PostWebServiceTask.RegistrationCompleted, GetWebServiceTask.LoginCompleted{
    private ImageView mainLogo;

    private EditText editTextEmail;

    private EditText editTextPassword;

    private EditText editTextComfirmPassword;

    private Button btnSubmit;

    private TextView textViewRegister;

    private AlertDialog myRegistrationDialog;
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
                Account account = new Account(editTextEmail.getText().toString(), editTextPassword.getText().toString());
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
                mainLogo.startAnimation(moveMainLogoAnimation);
            }
        }, 1000);
    }

    // Codes for building a dialog.
    private void initializeRegistrationDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder((LoginActivity.this));
        View mView = getLayoutInflater().inflate(R.layout.dialog_registration, null);

        mBuilder.setView(mView);
        myRegistrationDialog = mBuilder.create();
        myRegistrationDialog.setCanceledOnTouchOutside(false);

        myRegistrationDialog.show();

        Button btnRegister = (Button) myRegistrationDialog.findViewById(R.id.registration_button_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String email = ((EditText) myRegistrationDialog.findViewById(R.id.registration_editText_email))
                    .getText()
                    .toString();
            String password = ((EditText) myRegistrationDialog.findViewById(R.id.registration_editText_password))
                    .getText()
                    .toString();
            String confirmPassword = ((EditText) myRegistrationDialog.findViewById(R.id.registration_editText_comfirmPassword))
                    .getText()
                    .toString();

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                TextView textViewError = (TextView) myRegistrationDialog.findViewById(R.id.registration_textView_error);
                textViewError.setText(R.string.all_fields_must_fill_in_error, null);
                textViewError.setVisibility(View.VISIBLE);
            } else if (!password.equals(confirmPassword)) {
                TextView textViewError = (TextView) myRegistrationDialog.findViewById(R.id.registration_textView_error);
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
            String email = ((EditText) myRegistrationDialog.findViewById(R.id.registration_editText_email))
                    .getText()
                    .toString();
            String password = ((EditText) myRegistrationDialog.findViewById(R.id.registration_editText_password))
                    .getText()
                    .toString();

            if (insertNewMemberData(email, password)) {
                myRegistrationDialog.dismiss();

                startMainActivity();
            }

        } else if (resultCode == LoginHelper.EMAIL_ALREADY_EXIST){
            TextView textViewError = (TextView) myRegistrationDialog.findViewById(R.id.registration_textView_error);
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
