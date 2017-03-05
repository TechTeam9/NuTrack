package edu.uw.tcss450.nutrack.api;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Retrieving account information from database.
 */
public class getAccountInfo extends AsyncTask<String, Void, String> {
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

    /**
     * Interface to be implemented by the parent activity.
     */
    public interface LoginCompleted {
        public void onLoginCompleted(int resultCode, String theEmail, String thePassword);
    }
}
