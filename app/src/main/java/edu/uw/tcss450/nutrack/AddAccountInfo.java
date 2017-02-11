package edu.uw.tcss450.nutrack;

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

/**
 * Add account information into database.
 */
public class AddAccountInfo extends AsyncTask<String, Void, String> {
    private final String SERVICE_URL = "account_info_post.php";

    private Context mContext;

    private RegistrationCompleted mCallback;

    public AddAccountInfo(Context context) {
        mContext = context;
        mCallback = (RegistrationCompleted) context;
    }

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

    public interface RegistrationCompleted {
        public void onRegistrationCompleted(int resultCode);
    }
}