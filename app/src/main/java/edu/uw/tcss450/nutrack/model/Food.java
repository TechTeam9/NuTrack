package edu.uw.tcss450.nutrack.model;

/**
 * Created by Ming on 1/26/2017.
 */

public class Food {

    private int myId;

    private String myName;

    private double myCalorie;

    public Food(int theId, String theName, double theCalorie) {
        myId = theId;
        myName = theName;
        myCalorie = theCalorie;
    }

    public Food() {
        myId = 0;
        myName = null;
        myCalorie = 0;
    }

    public void setId(int theId) {
        myId = theId;
    }

    public int getId() {
        return myId;
    }

    public void setName(String theName) {
        myName = theName;
    }

    public String getName() {
        return myName;
    }

    public void setCalorie(double theCalorie) {
        myCalorie = theCalorie;
    }

    public double getCalorie() {
        return myCalorie;
    }
}
