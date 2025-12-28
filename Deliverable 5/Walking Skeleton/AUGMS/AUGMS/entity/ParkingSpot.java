package AUGMS.entity;

import java.io.*;
import java.util.*;

/**
 * 
 */
public class ParkingSpot {

    /**
     * Default constructor
     */
    public ParkingSpot() {
    }

    /**
     * 
     */
    private String spotID;


    /**
     * 
     */
    private SpotStatus status;




    // private OccupancySensor occupancySensor; // Optional dependency

    // Constructors
    public ParkingSpot(String spotID) {
        this.spotID = spotID;
        this.status = SpotStatus.AVAILABLE;
    }

    /**
     * @return
     */
    public String getSpotID() {
        return spotID;
    }

    public void setSpotID(String spotID) {
        this.spotID = spotID;
    }

    /**
     * @return
     */
    public SpotStatus getStatus() {
        return status;
    }

    /**
     * @return
     */
    public SpotStatus getOccupancyStatus() {
        return status;
    }

    /**
     * @param newStatus 
     * @return
     */
    public void updateStatus(SpotStatus newStatus) {
        this.status = newStatus;
    }

    /**
     * @return
     */
    // public OccupancySensor getOccupancySensor() {
    //     return occupancySensor;
    // }

    // public void setOccupancySensor(OccupancySensor occupancySensor) {
    //     this.occupancySensor = occupancySensor;
    // }

}
