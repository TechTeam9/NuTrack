package edu.uw.tcss450.nutrack.model;

/**
 * This model describes food items.
 * Created by Ming on 1/26/2017.
 */
public class Food {
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
    private double mCalorie;

    /**
     * The fat value of the food item.
     */
    private double mFat;

    /**
     * The carbs value of the food item.
     */
    private double mCarbs;

    /**
     * The protein value of the food item.
     */
    private double mProtein;

    /**
     * Constructs the food item.
     *
     * @param theId      the ID of the food item
     * @param theName    the name of the food item
     * @param theCalorie the caloric value of the food item
     */
    public Food(int theId, String theName, double theCalorie) {
        mId = theId;
        mName = theName;
        mCalorie = theCalorie;
    }

    /**
     * Constructs an empty food item.
     */
    public Food() {
        mId = 0;
        mName = null;
        mCalorie = 0;
        mFat = 0;
        mCarbs = 0;
        mProtein = 0;
    }

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
    public void setCalorie(double theCalorie) {
        mCalorie = theCalorie;
    }

    /**
     * Gets the caloric value of the food item.
     *
     * @return the caloric value of the food item
     */
    public double getCalorie() {
        return mCalorie;
    }

}
