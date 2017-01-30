package edu.uw.tcss450.nutrack.model;

import edu.uw.tcss450.nutrack.activity.LoginActivity;

public class Account {
    private String myUsername;

    private String myPassword;

    public Account(String theUsername, String thePassword) {
        myUsername = theUsername;
        myPassword = thePassword;
    }

    public void setUsername(String theUsername) {
        myUsername = theUsername;
    }

    public String getMyUsername() {
        return myUsername;
    }

    public void setPassword(String thePassword) {
        myPassword = thePassword;
    }

    public String getPassword() {
        return myPassword;
    }

}
