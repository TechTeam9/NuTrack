package edu.uw.tcss450.nutrack.model;


import java.util.Date;

public class Profile {

    private String mName;

    private char mGender;

    private Date mDOB;

    private double mHeight;

    private double mWeight;

    public Profile(String name, char gender, Date dateOfBirth, double height, double weight) {
        mName = name;
        mGender = gender;
        mDOB = dateOfBirth;
        mHeight = height;
        mWeight = weight;
    }

    public String getName() {
        return mName;
    }

    public char getGender() {
        return mGender;
    }

    public Date getDOB() {
        return mDOB;
    }

    public double getHeight() {
        return mHeight;
    }

    public double getWeight() {
        return mWeight;
    }
}
