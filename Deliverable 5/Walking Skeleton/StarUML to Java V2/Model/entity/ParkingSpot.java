package entity;

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




    /**
     * @return
     */
    public String getSpotID() {
        // TODO implement here
        return "";
    }

    /**
     * @return
     */
    public SpotStatus getOccupancyStatus() {
        // TODO implement here
        return null;
    }

    /**
     * @param newStatus 
     * @return
     */
    public void updateStatus(SpotStatus newStatus) {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public OccupancySensor getOccupancySensor() {
        // TODO implement here
        return null;
    }

}