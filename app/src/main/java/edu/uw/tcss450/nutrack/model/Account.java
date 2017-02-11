package edu.uw.tcss450.nutrack.model;

import edu.uw.tcss450.nutrack.activity.LoginActivity;

/**
 * Account model class.
 */
public class Account {
    private String mUsername;

    private String mPassword;

    public Account(String theUsername, String thePassword) {
        mUsername = theUsername;
        mPassword = thePassword;
    }

    public void setUsername(String theUsername) {
        mUsername = theUsername;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setPassword(String thePassword) {
        mPassword = thePassword;
    }

    public String getPassword() {
        return mPassword;
    }

}
