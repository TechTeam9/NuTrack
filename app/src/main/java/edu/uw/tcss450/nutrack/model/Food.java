package edu.uw.tcss450.nutrack.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * This model describes food items.
 * Created by Ming on 1/26/2017.
 */
public class Food implements Parcelable {
    /**
     * The ID of the food item.
     */
    private int mId;
    /**
     * The name of the food item.
     */
    private String mName;

    /**
     * The calorie value of the food item.
     */
    private ArrayList<Double> mCalorie;

    /**
     * The fat value of the food item.
     */
    private ArrayList<Double> mFat;

    /**
     * The carbs value of the food item.
     */
    private ArrayList<Double> mCarbs;

    /**
     * The protein value of the food item.
     */
    private ArrayList<Double> mProtein;

    /**
     * The more details information link.
     */
    private ArrayList<String> mURL;

    /**
     * The serving size.
     */
    private ArrayList<String> mServing;

    /**
     * Constructs the food item.
     *
     * @param theId   the ID of the food item
     * @param theName the name of the food item
     */
    public Food(int theId, String theName) {
        mId = theId;
        mName = theName;
        mCalorie = new ArrayList<>();
        mFat = new ArrayList<>();
        mCarbs = new ArrayList<>();
        mProtein = new ArrayList<>();
        mURL = new ArrayList<>();
        mServing = new ArrayList<>();
    }

    /**
     * Constructs an empty food item.
     */
    public Food() {
        mId = 0;
        mName = null;
        mCalorie = new ArrayList<>();
        mFat = new ArrayList<>();
        mCarbs = new ArrayList<>();
        mProtein = new ArrayList<>();
        mURL = new ArrayList<>();
        mServing = new ArrayList<>();
    }

    protected Food(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
    }

    public static final Creator<Food> CREATOR = new Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };

    /**
     * Sets the ID of the food item.
     *
     * @param theId the ID of the food item
     */
    public void setId(int theId) {
        mId = theId;
    }

    /**
     * Gets the ID of the food item.
     *
     * @return the ID of the food item
     */
    public int getId() {
        return mId;
    }

    /**
     * Sets the name of the food item.
     *
     * @param theName the name of the food item
     */
    public void setName(String theName) {
        mName = theName;
    }

    /**
     * Gets the name of the food item.
     *
     * @return the name of the food item
     */
    public String getName() {
        return mName;
    }

    /**
     * Sets the caloric value of the food item.
     *
     * @param theCalorie the caloric value of the food item
     */
    public void setCalorie(ArrayList<Double> theCalorie) {
        mCalorie = theCalorie;
    }

    /**
     * Gets the caloric value of the food item.
     *
     * @return the caloric value of the food item
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

    public ArrayList<String> getmURL() {
        return mURL;
    }

    public void setmURL(ArrayList<String> mURL) {
        this.mURL = mURL;
    }

    public ArrayList<String> getmServing() {
        return mServing;
    }

    public void setmServing(ArrayList<String> mServing) {
        this.mServing = mServing;
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
        dest.writeList(mURL);
        dest.writeList(mServing);
    }

}
