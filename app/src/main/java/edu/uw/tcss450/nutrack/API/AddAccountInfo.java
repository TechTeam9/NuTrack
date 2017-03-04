package edu.uw.tcss450.nutrack.API;

import android.content.Context;
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

import edu.uw.tcss450.nutrack.Helper.LoginHelper;

/**
 * Add account information into database.
 */
public class AddAccountInfo extends AsyncTask<String, Void, String> {
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
    public interface RegistrationCompleted {
        public void onRegistrationCompleted(int resultCode);
    }
}