package edu.uw.tcss450.nutrack;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.uw.tcss450.nutrack.DBHelper.DBMemberTableHelper;
import edu.uw.tcss450.nutrack.model.Account;

public class LoginHelper {

    public static final int ERROR = 101;

    public static final int CORRECT_LOGIN_INFO = 0;

    public static final int NO_USERNAME_FOUND = 1;

    public static final int INCORRECT_PASSWORD = 2;

    public static final int NO_ACCOUNT_FOUND = 11;

    public static final int ACCOUNT_FOUND_BUT_LOGIN_ERROR = 12;

    public static final int REGISTRATION_SUCCESS = 20;

    public static final int EMAIL_ALREADY_EXIST = 21;

    private View myView;

    public static int autoVerifyAccountExistance(Context theContext) {
        int resultCode = ERROR;
        DBMemberTableHelper memberTable = new DBMemberTableHelper(theContext);
        //NEED TO CHANGE COMPARE TO == AFTER COMPLETING LOGIN PAGE
        if (memberTable.getMemberSize() >= 1) {
            Cursor accountInfo = memberTable.getData();
            accountInfo.moveToFirst();
            Account account = new Account(accountInfo.getString(0), accountInfo.getString(1));

            autoVerifyAccount(account, theContext);
        } else {
            resultCode = NO_ACCOUNT_FOUND;
        }

        memberTable.close();
        return resultCode;
    }

    private static void autoVerifyAccount(Account theAccount, Context theContext) {
        final String baseUrl = "http://cssgate.insttech.washington.edu/~mhl325/login_verification";

        GetWebServiceTask task = new GetWebServiceTask(theContext);
        task.execute(baseUrl, theAccount.getMyUsername(), theAccount.getPassword(), "0");
    }

    public static void verifyAccount(Account theAccount, Context theContext) {
        final String baseUrl = "http://cssgate.insttech.washington.edu/~mhl325/login_verification";

        GetWebServiceTask task = new GetWebServiceTask(theContext);
        task.execute(baseUrl, theAccount.getMyUsername(), theAccount.getPassword(), "1");
    }

    public static void addNewAccount(Account theAccount, Context theContext) {
        final String baseUrl = "http://cssgate.insttech.washington.edu/~mhl325/registration_verification";

        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //Date date = new Date();

        PostWebServiceTask task = new PostWebServiceTask(theContext);
        task.execute(baseUrl, theAccount.getMyUsername(), theAccount.getPassword());
    }


}
