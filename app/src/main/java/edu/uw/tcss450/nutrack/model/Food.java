package edu.uw.tcss450.nutrack.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Food model class.
 */

public class Food{

    private int mId;

    private String mName;

    private double mCalorie;

    public Food(int theId, String theName, double theCalorie) {
        mId = theId;
        mName = theName;
        mCalorie = theCalorie;
    }

    public Food() {
        mId = 0;
        mName = null;
        mCalorie = 0;
    }

    public void setId(int theId) {
        mId = theId;
    }

    public int getId() {
        return mId;
    }

    public void setName(String theName) {
        mName = theName;
    }

    public String getName() {
        return mName;
    }

    public void setCalorie(double theCalorie) {
        mCalorie = theCalorie;
    }

    public double getCalorie() {
        return mCalorie;
    }
}
