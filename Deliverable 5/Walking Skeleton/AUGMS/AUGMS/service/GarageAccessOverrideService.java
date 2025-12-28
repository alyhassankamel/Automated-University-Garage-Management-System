package AUGMS.service;

// import AUGMS.dao.UniversityManagerDAO;
import AUGMS.dao.ParkingGarageDAO;
// import dao.LoggingDAO; // Optional - not critical for core functionality
// import AUGMS.entity.UniversityManager;
import AUGMS.entity.ParkingGarage;
import AUGMS.entity.GarageAccess;
import java.io.*;
import java.util.*;

/**
 * Service for managing garage access overrides
 */
public class GarageAccessOverrideService {

    /**
     * 
     */
    // private final UniversityManagerDAO universityManagerDAO;
    private final ParkingGarageDAO parkingGarageDAO;
    // private final LoggingDAO loggingDAO; // Optional
    private Map<String, GarageAccess> overrideStatus; // garageID -> access mode
    private Map<String, String> overrideReasons; // garageID -> reason

    /**
     * Default constructor
    */
    public GarageAccessOverrideService() {
        // this.universityManagerDAO = new UniversityManagerDAO();
        this.parkingGarageDAO = new ParkingGarageDAO();
        // this.loggingDAO = new LoggingDAO(); // Optional
        this.overrideStatus = new HashMap<>();
        this.overrideReasons = new HashMap<>();
    }

    /**
     * @param garageID
     * @param accessMode
     * @param reason
     * @return
     */
    public void setGlobalOverride(String garageID, GarageAccess accessMode, String reason) {
        ParkingGarage garage = parkingGarageDAO.findGarage(garageID);
        if (garage != null) {
            overrideStatus.put(garageID, accessMode);
            overrideReasons.put(garageID, reason);
            garage.setGarageAccess(accessMode);
            parkingGarageDAO.updateGarage(garage);
            logOverrideChange(garageID, "SET", accessMode.toString(), reason);
        }
    }

    /**
     * @param garageID
     * @return
     */
    public void resetGlobalOverride(String garageID) {
        ParkingGarage garage = parkingGarageDAO.findGarage(garageID);
        if (garage != null) {
            overrideStatus.remove(garageID);
            overrideReasons.remove(garageID);
            garage.setGarageAccess(GarageAccess.NORMAL);
            parkingGarageDAO.updateGarage(garage);
            logOverrideChange(garageID, "RESET", "NORMAL", "Override reset");
        }
    }

    /**
     * @param garageID
     * @return
     */
    public GarageAccess getCurrentOverrideStatus(String garageID) {
        ParkingGarage garage = parkingGarageDAO.findGarage(garageID);
        if (garage != null) {
            return garage.getGarageAccess();
        }
        return GarageAccess.NORMAL;
    }

    /**
     * @param garageID
     * @param action
     * @param accessMode
     * @param reason
     * @return
     */
    public void logOverrideChange(String garageID, String action, String accessMode, String reason) {
        String logMessage = String.format("[%s] Garage: %s, Action: %s, Mode: %s, Reason: %s",
                new Date(), garageID, action, accessMode, reason);
        System.out.println("OVERRIDE LOG: " + logMessage);
        // In a real implementation, this would use loggingDAO
    }

    // public UniversityManagerDAO getUniversityManagerDAO() {
    //     return universityManagerDAO;
    // }

    public ParkingGarageDAO getParkingGarageDAO() {
        return parkingGarageDAO;
    }
}
