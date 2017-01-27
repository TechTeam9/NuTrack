package edu.uw.tcss450.nutrack.API;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by Ming on 1/27/2017.
 */

public class FatSecretAPI {

    private final String CONSUMER_KEY;

    private final String SECRET_KEY;

    private final String URL = "http://platform.fatsecret.com/rest/server.api";

    private final String SIGNATURE_METHOD = "HMAC-SHA1";

    public FatSecretAPI(String theConsumerKey, String theSecretKey) {
        CONSUMER_KEY = theConsumerKey;
        SECRET_KEY = theSecretKey;
    }

    public String generateRandomNonce() {
        Random r = new Random();
        StringBuffer n = new StringBuffer();
        for (int i = 0; i < r.nextInt(8) + 2; i++) {
            n.append(r.nextInt(26) + 'a');
        }
        return n.toString();
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

    private String paraify(String[] theParam) {
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
