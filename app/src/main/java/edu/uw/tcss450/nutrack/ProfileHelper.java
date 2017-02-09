package edu.uw.tcss450.nutrack;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.uw.tcss450.nutrack.model.Profile;


public class ProfileHelper {

    private static final String BASE_URL = "http://cssgate.insttech.washington.edu/~mhl325/";

    public static final int NO_PROFILE_FOUND = 14;

    public static final int PROFILE_FOUND = 13;

    public static void checkProfileExistance(String email) {

    }


    private void cloneProfile(String email) {

    }


    private class GetPersonalInfo extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String response = "";

            String serviceURL = "getPersonalInfo_GET.php";

            HttpURLConnection urlConnection = null;
            String args = "?email=" + strings[0]
                    + "&name=" + strings[1]
                    + "&gender=" + strings[2]
                    + "&date_of_birth=" + strings[3]
                    + "&height=" + strings[4]
                    + "&weight=" + strings[5];
            try {
                URL urlObject = new URL(BASE_URL + serviceURL + args);
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

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                //mCallback.onLoginCompleted(jsonObject.getInt("result_code"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }

    private class AddPersonalInfo extends  AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            return null;
        }
    }

    public interface CheckProfileCompleted {
        public void onCheckProfileCompleted(int theResultCode, Profile theProfile);
    }
}
