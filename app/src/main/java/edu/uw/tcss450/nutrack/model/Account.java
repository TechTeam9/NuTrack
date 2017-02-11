package edu.uw.tcss450.nutrack.model;

/**
 * This model describes user accounts.
 */
public class Account {
    /**
     * The user's username.
     */
    private String mUsername;
    /**
     * The user's password.
     */
    private String mPassword;

    /**
     * Constructs the account.
     *
     * @param theUsername the user's username
     * @param thePassword the user's password
     */
    public Account(String theUsername, String thePassword) {
        mUsername = theUsername;
        mPassword = thePassword;
    }

    /**
     * Sets the user's username.
     *
     * @param theUsername the user's username
     */
    public void setUsername(String theUsername) {
        mUsername = theUsername;
    }

    /**
     * Gets the user's username.
     *
     * @return the user's username
     */
    public String getUsername() {
        return mUsername;
    }

    /**
     * Sets the user's password.
     *
     * @param theUsername the user's password
     */
    public void setPassword(String thePassword) {
        mPassword = thePassword;
    }

    /**
     * Gets the user's username.
     *
     * @return the user's password
     */
    public String getPassword() {
        return mPassword;
    }

}
