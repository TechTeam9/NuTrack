package edu.uw.tcss450.nutrack.helper;

import android.content.Context;
import android.database.Cursor;

import edu.uw.tcss450.nutrack.api.AddAccountInfo;
import edu.uw.tcss450.nutrack.database.DBMemberInfo;
import edu.uw.tcss450.nutrack.api.getAccountInfo;
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
     * Static method for auto verifying account existance.
     * @param theContext context of the activity
     * @return result code
     */
    public static int autoVerifyAccountExistance(Context theContext) {
        int resultCode = ERROR;
        DBMemberInfo dbHelper = new DBMemberInfo(theContext);
        //NEED TO CHANGE COMPARE TO == AFTER COMPLETING LOGIN PAGE
        if (dbHelper.getMemberSize() >= 1) {
            Cursor accountInfo = dbHelper.getData();
            accountInfo.moveToFirst();
            Account account = new Account(accountInfo.getString(0), accountInfo.getString(1));

            autoVerifyAccount(account, theContext);
        } else {
            resultCode = NO_ACCOUNT_FOUND;
        }

        dbHelper.close();
        return resultCode;
    }

    /**
     * Auto verify account for current user.
     * @param theAccount account model
     * @param theContext context
     */
    private static void autoVerifyAccount(Account theAccount, Context theContext) {
        final String baseUrl = "http://cssgate.insttech.washington.edu/~mhl325/";

        getAccountInfo task = new getAccountInfo(theContext);
        task.execute(baseUrl, theAccount.getUsername(), theAccount.getPassword(), "0");
    }

    /**
     * Verify account for first time user.
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
    /**
     *
     * @param theContext context
     */
    public static Account getAccountInfo(Context theContext) {
        DBMemberInfo dbHelper = new DBMemberInfo(theContext);
        return dbHelper.getAccountData();
    }
}
