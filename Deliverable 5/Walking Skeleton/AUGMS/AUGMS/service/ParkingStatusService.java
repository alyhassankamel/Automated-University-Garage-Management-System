package AUGMS.service;

import AUGMS.dao.ParkingGarageDAO;
import AUGMS.dao.ParkingSpotDAO;
import AUGMS.dao.OccupancySensorDAO;
import AUGMS.dao.ParkingStatusDAO;
import java.io.*;
import java.util.*;

/**
 * ...
 */
public class ParkingStatusService {

    /**
     * Default constructor
     */
    public ParkingStatusService() {
    }

    /**
     * 
     */
    private final ParkingGarageDAO parkingGarageDAO;

    /**
     * 
     */
    private final ParkingSpotDAO parkingSpotDAO;

    /**
     * 
     */
    private final OccupancySensorDAO occupancySensorDAO;

    /**
     * 
     */
    private final ParkingStatusDAO parkingStatusDAO;







    /**
     * @return
     */
    public void checkCurrentStatus() {
        // TODO implement here
        return null;
    }

    /**
     * ...
     * @return
     */
    public void aggregateData() {
        // TODO implement here
        return null;
    }

    /**
     * ...
     * @return
     */
    public void getAvailableSpots() {
        // TODO implement here
    }

    /**
     * @return
     */
    public void getOccupiedSpots() {
        // TODO implement here
    }

    /**
     * @return
     */
    public int countAvailableSlots() {
        // TODO implement here
        return 0;
    }

    /**
     * @return
     */
    public int countOccupiedSlots() {
        // TODO implement here
        return 0;
    }

    /**
     * @return
     */
    public void getParkingStatus() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public boolean isGarageAccessible() {
        // TODO implement here
        return false;
    }

    /**
     * @return
     */
    public void checkAccessPermissions() {
        // TODO implement here
        return null;
    }

}
