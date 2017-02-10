package edu.uw.tcss450.nutrack;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import edu.uw.tcss450.nutrack.model.Profile;

/**
 * @Author
 * @version
 * @since
 */
public class ProfileHelper {

    /** The URL of our backend. */
    private static final String BASE_URL = "http://cssgate.insttech.washington.edu/~mhl325/";

    /** No profile found result indicator. */
    public static final int NO_PROFILE_FOUND = 14;

    /** Profile found result indicator. */
    public static final int PROFILE_FOUND = 13;

    /** Insert success result indicator. */
    public static final int INSERT_SUCCESS = 15;

    /** Insert failed result indicator. */
    public static final int INSERT_FAILURE = 16;

    /**
     * Checks to see if the requested profile exists.
     * @param theContext
     * @param theEmail
     */
    public static void checkProfileExistence(Context theContext, String theEmail) {
        GetPersonalInfo getInfo = new GetPersonalInfo(theContext);
        getInfo.execute(theEmail);
    }

    /**
     * Inserts the profile into the database.
     * @param theContext
     * @param email
     * @param theProfile
     */
    public static void insertProfile(Context theContext, String email, Profile theProfile) {
        PostPersonalInfo postInfo = new PostPersonalInfo(theContext);
        postInfo.execute(email, theProfile);
    }

    /**
     *
     */
    private static class GetPersonalInfo extends AsyncTask<String, Void, String> {

        private final CheckProfileCompleted mCallback;

        /**
         *
         * @param theContext
         */
        public GetPersonalInfo(Context theContext) {
            mCallback = (CheckProfileCompleted) theContext;
        }

        /**
         * Fetches the user's personal info from our web service.
         * @param strings the email to pass to the backend
         * @return the personal info from the back end or an error message
         */
        @Override
        protected String doInBackground(String... strings) {
            String response = "";

            String serviceURL = "personal_info_get.php";

            HttpURLConnection urlConnection = null;
            String args = "?email=" + strings[0];
            try {
                URL urlObject = new URL(BASE_URL + serviceURL + args);
                urlConnection = (HttpURLConnection) urlObject.openConnection();
                InputStream content = urlConnection.getInputStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s;
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
            System.out.println(result);
            mCallback.onCheckProfileCompleted(result);
        }

    }

    private static class PostPersonalInfo extends  AsyncTask<Object, Void, String> {

        private final InsertProfileCompleted mCallback;

        public PostPersonalInfo(Context theContext) {
            mCallback = (InsertProfileCompleted) theContext;
        }

        @Override
        protected String doInBackground(Object... objects) {
            String response = "";
            String serviceURL = "personal_info_post.php";

            Profile profile = (Profile) objects[1];
            HttpURLConnection urlConnection = null;
            try {
                URL urlObject = new URL(BASE_URL + serviceURL);
                urlConnection = (HttpURLConnection) urlObject.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode((String) objects[0], "UTF-8")
                        + "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(profile.getName(), "UTF-8")
                        + "&" + URLEncoder.encode("gender", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(profile.getGender()), "UTF-8")
                        + "&" + URLEncoder.encode("date_of_birth", "UTF-8") + "=" + URLEncoder.encode(profile.getDOB(), "UTF-8")
                        + "&" + URLEncoder.encode("height", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(profile.getHeight()), "UTF-8")
                        + "&" + URLEncoder.encode("weight", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(profile.getWeight()), "UTF-8")
                        + "&" + URLEncoder.encode("avatar_id", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(profile.getAvatarId()), "UTF-8");

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

        @Override
        protected void onPostExecute(String result) {
            mCallback.onInsertProfileCompleted(result);
        }
    }

    public interface CheckProfileCompleted {
        public void onCheckProfileCompleted(String result);
    }


    public interface InsertProfileCompleted {
        public void onInsertProfileCompleted(String result);
    }
}
