package edu.uw.tcss450.nutrack.API;

import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import java.util.Arrays;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Ming on 2/26/2017.
 */

public class FatSecretHelper {

    private final static String CONSUMER_KEY = "aceffd6069f24bde9e9710dbcef45ea9";

    private final static String SECRET_KEY = "cc2b625f2d0a49509c0745e8e434b008";

    public final static String URL = "http://platform.fatsecret.com/rest/server.api";

    private final static String SIGNATURE_METHOD = "HMAC-SHA1";

    private final static String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    /**
     * Sign.
     *
     * @param method The method of signing
     * @param uri    The FatSecret uniform resource identifier
     * @param params The necessary parameters
     * @return Encoded URI, null on fail
     */
    public static String sign(String method, String uri, String[] params) {
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

    /**
     * Generates Oauth parameters.
     *
     * @return Oauth parameters.
     */
    public static String[] generateOauthParams() {
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

    /**
     * Translates parameters.
     *
     * @param theParam the parameters to translate.
     * @return the parameters joined with &
     */
    public static String paramify(String[] theParam) {
        String[] param = Arrays.copyOf(theParam, theParam.length);
        Arrays.sort(param);
        return join(param, "&");
    }

    /**
     * A simple join function.
     *
     * @param theParam  the parameters to be join
     * @param separator the what to separate the parameters with
     * @return the joined parameters
     */
    public static String join(String[] theParam, String separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < theParam.length; i++) {
            if (i > 0)
                sb.append(separator);
            sb.append(theParam[i]);
        }
        return sb.toString();
    }


    /**
     * Generate a random number.
     *
     * @return The number.
     */
    public static String generateRandomNonce() {
        Random r = new Random();
        StringBuffer n = new StringBuffer();
        for (int i = 0; i < r.nextInt(8) + 2; i++) {
            n.append(r.nextInt(26) + 'a');
        }
        return n.toString();
    }
}
