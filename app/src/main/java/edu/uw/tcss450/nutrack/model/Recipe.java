package edu.uw.tcss450.nutrack.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Ming on 3/4/2017.
 */

public class Recipe implements Parcelable {
    /**
     * The ID of the recipe item.
     */
    private int mId;
    /**
     * The name of the recipe item.
     */
    private String mName;

    /**
     * The calorie value of the recipe item.
     */
    private ArrayList<Double> mCalorie;

    /**
     * The fat value of the recipe item.
     */
    private ArrayList<Double> mFat;

    /**
     * The carbs value of the recipe item.
     */
    private ArrayList<Double> mCarbs;

    /**
     * The protein value of the recipe item.
     */
    private ArrayList<Double> mProtein;

    /**
     * Constructs the recipe item.
     *
     * @param theId   the ID of the recipe item
     * @param theName the name of the recipe item
     */
    public Recipe(int theId, String theName) {
        mId = theId;
        mName = theName;
        mCalorie = new ArrayList<>();
        mFat = new ArrayList<>();
        mCarbs = new ArrayList<>();
        mProtein = new ArrayList<>();
    }

    /**
     * Constructs an empty recipe item.
     */
    public Recipe() {
        mId = 0;
        mName = null;
        mCalorie = new ArrayList<>();
        mFat = new ArrayList<>();
        mCarbs = new ArrayList<>();
        mProtein = new ArrayList<>();
    }

    protected Recipe(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    /**
     * Sets the ID of the recipe item.
     *
     * @param theId the ID of the recipe item
     */
    public void setId(int theId) {
        mId = theId;
    }

    /**
     * Gets the ID of the recipe item.
     *
     * @return the ID of the recipe item
     */
    public int getId() {
        return mId;
    }

    /**
     * Sets the name of the recipe item.
     *
     * @param theName the name of the recipe item
     */
    public void setName(String theName) {
        mName = theName;
    }

    /**
     * Gets the name of the recipe item.
     *
     * @return the name of the recipe item
     */
    public String getName() {
        return mName;
    }

    /**
     * Sets the caloric value of the recipe item.
     *
     * @param theCalorie the caloric value of the recipe item
     */
    public void setCalorie(ArrayList<Double> theCalorie) {
        mCalorie = theCalorie;
    }

    /**
     * Gets the caloric value of the recipe item.
     *
     * @return the caloric value of the recipe item
     */
    public ArrayList<Double> getCalorie() {
        return mCalorie;
    }

    public void setFat(ArrayList<Double> theFat) {
        mFat = theFat;
    }

    public ArrayList<Double> getFat() {
        return mFat;
    }

    public void setCarbs(ArrayList<Double> theCarbs) {
        mCarbs = theCarbs;
    }

    public ArrayList<Double> getCarbs() {
        return mCarbs;
    }

    public void setProtein(ArrayList<Double> theProtein) {
        mProtein = theProtein;
    }

    public ArrayList<Double> getProtein() {
        return mProtein;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeInt(mId);
        dest.writeList(mCalorie);
        dest.writeList(mFat);
        dest.writeList(mCarbs);
        dest.writeList(mProtein);
    }
}
