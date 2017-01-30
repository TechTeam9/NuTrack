package edu.uw.tcss450.nutrack;

import android.content.Context;
import android.database.Cursor;

import edu.uw.tcss450.nutrack.DBHelper.DBMemberTableHelper;
import edu.uw.tcss450.nutrack.model.Account;

public class LoginHelper {

    public static final int ERROR = 101;

    public static final int CORRECT_LOGIN_INFO = 0;

    public static final int NO_USERNAME_FOUND = 1;

    public static final int INCORRECT_PASSWORD = 2;

    public static final int CORRECT_AUTO_LOGIN_INFO = 10;

    public static final int NO_ACCOUNT_FOUND = 11;

    public static final int ACCOUNT_FOUND_BUT_LOGIN_ERROR = 12;


    public static int autoVerifyAccountExistance(Context theContext) {
        int resultCode = ERROR;
        DBMemberTableHelper memberTable = new DBMemberTableHelper(theContext);
        //NEED TO CHANGE COMPARE TO == AFTER COMPLETING LOGIN PAGE
        if (memberTable.getMemberSize() >= 1) {
            Cursor accountInfo = memberTable.getData();
            accountInfo.moveToFirst();
            Account account = new Account(accountInfo.getString(0), accountInfo.getString(1));
            resultCode = autoVerifyAccount(account);

            if (resultCode == ACCOUNT_FOUND_BUT_LOGIN_ERROR) {
                memberTable.deleteData();
            }

        } else {
            resultCode = NO_ACCOUNT_FOUND;
        }

        memberTable.close();
        return resultCode;
    }

    private static int autoVerifyAccount(Account theAccount) {
        //Need to connect to the external DB

        return CORRECT_AUTO_LOGIN_INFO;
    }

    public static int verifyAccount(Account theAccount) {

        return CORRECT_LOGIN_INFO;
    }
}
