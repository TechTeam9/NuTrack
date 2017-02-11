package edu.uw.tcss450.nutrack.model;


import java.util.Date;

/**
 * Profile model class.
 */
public class Profile {

    private String mName;

    private char mGender;

    private String mDOB;

    private double mHeight;

    private double mWeight;

    private int mAvatarId;

    public Profile(String name, char gender, String dateOfBirth, double height, double weight, int theAvatarId) {
        mName = name;
        mGender = gender;
        mDOB = dateOfBirth;
        mHeight = height;
        mWeight = weight;
        mAvatarId = theAvatarId;
    }

    public void setName(String theName) {
        mName = theName;
    }

    public String getName() {
        return mName;
    }

    public void setGender(char theGender) {
        mGender = theGender;
    }

    public char getGender() {
        return mGender;
    }

    public void setDOB(String theDOB) {
        mDOB = theDOB;
    }

    public String getDOB() {
        return mDOB;
    }

    public void setHeight(double theHeight) {
        mHeight = theHeight;
    }

    public double getHeight() {
        return mHeight;
    }

    public void setWeight(double theWeight) {
        mWeight = theWeight;
    }

    public double getWeight() {
        return mWeight;
    }

    public void setAvatarId(int theAvatarId) {
        mAvatarId = theAvatarId;
    }

    public int getAvatarId() {
        return mAvatarId;
    }
}
