package edu.uw.tcss450.nutrack.model;

/**
 * This model describes user profiles.
 */
public class Profile {

    /**
     * The name of the user.
     */
    private String mName;
    /**
     * The gender of the user.
     */
    private String mGender;
    /**
     * The date of birth of the user.
     */
    private String mDOB;
    /**
     * The height of the user.
     */
    private int mHeight;
    /**
     * The weight of the user.
     */
    private int mWeight;
    /**
     * The selected avatar ID of the user.
     */
    private int mAvatarId;

    /**
     * Constructs the users profile.
     *
     * @param name        the name of the user
     * @param gender      the gender of the user
     * @param dateOfBirth the date of birth of the user
     * @param height      the height of the user
     * @param weight      the weight of the user
     * @param theAvatarId the ID of the selected avatar of the user
     */
    public Profile(String name, String gender, String dateOfBirth, int height, int weight, int theAvatarId) {
        mName = name;
        mGender = gender;
        mDOB = dateOfBirth;
        mHeight = height;
        mWeight = weight;
        mAvatarId = theAvatarId;
    }

    /**
     * Sets the name of the user.
     *
     * @param theName the name of the user
     */
    public void setName(String theName) {
        mName = theName;
    }

    /**
     * Gets the name of the user.
     *
     * @return the name of the user
     */
    public String getName() {
        return mName;
    }

    /**
     * Sets the gender of the user.
     *
     * @param theGender the gender of the user
     */
    public void setGender(String theGender) {
        mGender = theGender;
    }

    /**
     * Gets the gender of the user.
     *
     * @return the gender of the user
     */
    public String getGender() {
        return mGender;
    }

    /**
     * Sets the date of birth of the user.
     *
     * @param theDOB the date of birth of the user
     */
    public void setDOB(String theDOB) {
        mDOB = theDOB;
    }

    /**
     * Gets the date of birth of the user.
     *
     * @return the date of birth of the user
     */
    public String getDOB() {
        return mDOB;
    }

    /**
     * Sets the height of the user.
     *
     * @param theHeight the height of the user
     */
    public void setHeight(int theHeight) {
        mHeight = theHeight;
    }

    /**
     * Gets the height of the user.
     *
     * @return the height of the user
     */
    public int getHeight() {
        return mHeight;
    }

    /**
     * Sets the weight of the user.
     *
     * @param theWeight the weight of the user
     */
    public void setWeight(int theWeight) {
        mWeight = theWeight;
    }

    /**
     * Gets the weight of the user.
     *
     * @return the weight of the user
     */
    public int getWeight() {
        return mWeight;
    }

    /**
     * Sets the avatar ID of the user.
     *
     * @param theAvatarId the avatar ID of the user
     */
    public void setAvatarId(int theAvatarId) {
        mAvatarId = theAvatarId;
    }

    /**
     * Gets the avatar ID of the user.
     *
     * @return the avatar ID of the user
     */
    public int getAvatarId() {
        return mAvatarId;
    }
}
