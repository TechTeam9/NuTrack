package edu.uw.tcss450.nutrack.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import edu.uw.tcss450.nutrack.model.Account;

public class LoginHelper {

    /**
     * General error code.
     */
    public static final int ERROR = 101;

    /**
     * Correct login info code.
     */
    public static final int CORRECT_LOGIN_INFO = 0;

    /**
     * No username error code.
     */
    public static final int NO_USERNAME_FOUND = 1;

    /**
     * Incorrect password error code.
     */
    public static final int INCORRECT_PASSWORD = 2;

    /**
     * No account found error code.
     */
    public static final int NO_ACCOUNT_FOUND = 11;

    /**
     * Account found but connection error code.
     */
    public static final int ACCOUNT_FOUND_BUT_LOGIN_ERROR = 12;

    /**
     * Registration success code.
     */
    public static final int REGISTRATION_SUCCESS = 20;

    /**
     * Email already exist code
     */
    public static final int EMAIL_ALREADY_EXIST = 21;


    /**
     * Auto verify account for current user.
     *
     * @param theAccount account model
     * @param theContext context
     */
    public static void autoVerifyAccount(Account theAccount, Context theContext) {
        final String baseUrl = "http://cssgate.insttech.washington.edu/~mhl325/";

        getAccountInfo task = new getAccountInfo(theContext);
        task.execute(baseUrl, theAccount.getUsername(), theAccount.getPassword(), "0");
    }

    /**
     * Verify account for first time user.
     *
     * @param theAccount account model
     * @param theContext context
     */
    public static void verifyAccount(Account theAccount, Context theContext) {
        final String baseUrl = "http://cssgate.insttech.washington.edu/~mhl325/";

        getAccountInfo task = new getAccountInfo(theContext);
        task.execute(baseUrl, theAccount.getUsername(), theAccount.getPassword(), "1");
    }

    /**
     * Add a new account.
     *
     * @param theAccount account model
     * @param theContext context
     */
    public static void addNewAccount(Account theAccount, Context theContext) {
        final String baseUrl = "http://cssgate.insttech.washington.edu/~mhl325/";

        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //Date date = new Date();

        AddAccountInfo task = new AddAccountInfo(theContext);
        task.execute(baseUrl, theAccount.getUsername(), theAccount.getPassword());
    }

    public static class AddAccountInfo extends AsyncTask<String, Void, String> {
        /**
         * The script to run on the backend.
         */
        private final String SERVICE_URL = "account_info_post.php";
        /**
         * The current context.
         */
        private Context mContext;
        /**
         * The registration completed indication value.
         */
        private RegistrationCompleted mCallback;

        /**
         * Constructs an instance of AddAccountInfo.
         * @param context the current context
         */
        public AddAccountInfo(Context context) {
            mContext = context;
            mCallback = (RegistrationCompleted) context;
        }

        /**
         * Posts account info to backend webservice.
         *
         * @param strings the user's account info
         * @return success or failure response
         */
        @Override
        protected String doInBackground(String... strings) {
            String response = "";
            HttpURLConnection urlConnection = null;
            String url = strings[0];
            try {
                URL urlObject = new URL(url + SERVICE_URL);
                urlConnection = (HttpURLConnection) urlObject.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                String data = URLEncoder.encode("email", "UTF-8")
                        + "=" + URLEncoder.encode(strings[1], "UTF-8")
                        + "&" + URLEncoder.encode("password", "UTF-8")
                        + "=" + URLEncoder.encode(strings[2], "UTF-8");
                wr.write(data);
                wr.flush();
                InputStream content = urlConnection.getInputStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s = "";
                while ((s = buffer.readLine()) != null) {
                    response += s;
                }
            } catch (Exception e) {
                response = "Unable to connect, Reason: "
                        + e.getMessage();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return response;
        }

        /**
         * Receives indication success or failure of add account operation.
         *
         * @param result the success or failure code
         */
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jObject = new JSONObject(result);

                if (Integer.parseInt(jObject.getString("result_code")) == -1) {
                    mCallback.onRegistrationCompleted(LoginHelper.EMAIL_ALREADY_EXIST);
                } else {
                    mCallback.onRegistrationCompleted(LoginHelper.REGISTRATION_SUCCESS);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        /**
         * Interface to be used by the parent activity.
         */

    }

    /**
     * Retrieving account information from database.
     */
    public static class getAccountInfo extends AsyncTask<String, Void, String> {
        /**
         * The script to run on the backend.
         */
        private final String SERVICE_URL = "account_info_get.php";
        /**
         * The login info.
         */
        private LoginCompleted mCallback;
        /**
         * The email of the user.
         */
        private String mEmail;
        /**
         * The password of the user.
         */
        private String mPassword;
        /**
         * Gets the user's account info.
         * @param theContext the current context
         */
        public getAccountInfo(Context theContext) {
            mCallback = (LoginCompleted) theContext;
        }

        /**
         * Gets account info from backend webservice.
         *
         * @param strings the user's account info
         * @return User account info or failure response
         */
        @Override
        protected String doInBackground(String... strings) {

            String response = "";
            HttpURLConnection urlConnection = null;
            String url = strings[0];
            mEmail = strings[1];
            mPassword = strings[2];
            String args = "?email=" + strings[1] + "&password=" + strings[2] + "&login_mode=" + strings[3];
            try {
                URL urlObject = new URL(url + SERVICE_URL + args);
                urlConnection = (HttpURLConnection) urlObject.openConnection();
                InputStream content = urlConnection.getInputStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s = "";
                while ((s = buffer.readLine()) != null) {
                    response += s;
                }
            } catch (Exception e) {
                response = "Unable to connect, Reason: "
                        + e.getMessage();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return response;
        }

        /**
         * Receives indication success or failure of get account operation.
         *
         * @param result the success or failure code
         */
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                mCallback.onLoginCompleted(jsonObject.getInt("result_code"), mEmail, mPassword);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }



    }

    /**
     * Interface to be implemented by the parent activity.
     */
    public interface LoginCompleted {
        public void onLoginCompleted(int resultCode, String theEmail, String thePassword);
    }

    public interface RegistrationCompleted {
        public void onRegistrationCompleted(int resultCode);
    }
}
