package edu.uw.tcss450.nutrack.API;

import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class FatSecretAPI {

    private final static String METHOD = "GET";

    private final static String CONSUMER_KEY = "aceffd6069f24bde9e9710dbcef45ea9";

    private final static String SECRET_KEY = "cc2b625f2d0a49509c0745e8e434b008";

    private final static String URL = "http://platform.fatsecret.com/rest/server.api";

    private final static String SIGNATURE_METHOD = "HMAC-SHA1";

    private final static String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    public JSONObject searchFood(String searchFood) {
        List<String> params = new ArrayList<>(Arrays.asList(generateOauthParams()));
        String[] template = new String[1];
        params.add("method=foods.search");
        params.add("search_expression=" + Uri.encode(searchFood));
        params.add("oauth_signature=" + sign(METHOD, URL, params.toArray(template)));

        JSONObject foods = null;
        try {
            URL url = new URL(URL + "?" + paramify(params.toArray(template)));
            URLConnection api = url.openConnection();
            String line;
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(api.getInputStream()));
            while ((line = reader.readLine()) != null) builder.append(line);
            JSONObject food = new JSONObject(builder.toString());
            foods = food.getJSONObject("foods");
        } catch (Exception exception) {
            Log.e("FatSecret Error", exception.toString());
            exception.printStackTrace();
        }
        return foods;
    }

    public String generateRandomNonce() {
        Random r = new Random();
        StringBuffer n = new StringBuffer();
        for (int i = 0; i < r.nextInt(8) + 2; i++) {
            n.append(r.nextInt(26) + 'a');
        }
        return n.toString();
    }

    private String sign(String method, String uri, String[] params) {
        String[] p = {method, Uri.encode(uri), Uri.encode(paramify(params))};
        String s = join(p, "&");
        String tempSK = SECRET_KEY + "&";
        SecretKey sk = new SecretKeySpec(tempSK.getBytes(), HMAC_SHA1_ALGORITHM);
        try {
            Mac m = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            m.init(sk);
            return Uri.encode(new String(Base64.encode(m.doFinal(s.getBytes()), Base64.DEFAULT)).trim());
        } catch (java.security.NoSuchAlgorithmException e) {
            Log.w("FatSecret_TEST FAIL", e.getMessage());
            return null;
        } catch (java.security.InvalidKeyException e) {
            Log.w("FatSecret_TEST FAIL", e.getMessage());
            return null;
        }
    }

    public String[] generateOauthParams() {
        String[] baseString = {
                "oauth_consumer_key=" + CONSUMER_KEY,
                "oauth_signature_method=" + SIGNATURE_METHOD,
                "oauth_timestamp=" + new Long(System.currentTimeMillis() / 1000).toString(),
                "oauth_nonce=" + generateRandomNonce(),
                "oauth_version=1.0",
                "format=json"
        };

        return baseString;
    }

    private String paramify(String[] theParam) {
        String[] param = Arrays.copyOf(theParam, theParam.length);
        Arrays.sort(param);
        return join(param, "&");
    }

    private String join(String[] theParam, String separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < theParam.length; i++) {
            if (i > 0)
                sb.append(separator);
            sb.append(theParam[i]);
        }
        return sb.toString();
    }
}
