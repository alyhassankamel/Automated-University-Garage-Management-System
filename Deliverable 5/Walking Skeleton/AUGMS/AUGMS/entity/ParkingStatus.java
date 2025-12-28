package AUGMS.entity;

import java.io.*;
import java.util.*;

/**
 * 
 */
public class ParkingStatus {

    /**
     * Default constructor
     */
    public ParkingStatus() {
    }

    /**
     * 
     */
    private String garageID;

    /**
     * 
     */
    private int availableSpots;

    /**
     * 
     */
    private int occupiedSpots;

    /**
     * 
     */
    private int totalSpots;

    /**
     * 
     */
    private Date timestamp;


    // Constructors
    public ParkingStatus(String garageID, int availableSpots, int occupiedSpots, int totalSpots) {
        this.garageID = garageID;
        this.availableSpots = availableSpots;
        this.occupiedSpots = occupiedSpots;
        this.totalSpots = totalSpots;
        this.timestamp = new Date();
    }

    // Getters and Setters
    public String getGarageID() {
        return garageID;
    }

    public void setGarageID(String garageID) {
        this.garageID = garageID;
    }

    /**
     * @return
     */
    public int getAvailableSpots() {
        return availableSpots;
    }

    public void setAvailableSpots(int availableSpots) {
        this.availableSpots = availableSpots;
    }

    /**
     * @return
     */
    public int getOccupiedSpots() {
        return occupiedSpots;
    }

    public void setOccupiedSpots(int occupiedSpots) {
        this.occupiedSpots = occupiedSpots;
    }

    /**
     * @return
     */
    public int getTotalSpots() {
        return totalSpots;
    }

    public void setTotalSpots(int totalSpots) {
        this.totalSpots = totalSpots;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

}
