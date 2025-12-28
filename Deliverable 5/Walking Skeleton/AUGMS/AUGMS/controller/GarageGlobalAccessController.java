package controller;

import AUGMS.service.GarageAccessOverrideService;
import AUGMS.entity.ParkingGarage;
import AUGMS.entity.GarageAccess;
import AUGMS.view.GarageGlobalAccessView;
import java.io.*;
import java.util.*;

/**
 * Controller for Garage Global Access management
 */
public class GarageGlobalAccessController {

    private GarageAccessOverrideService overrideService;
    private GarageGlobalAccessView view;

    /**
     * Default constructor
     */
    public GarageGlobalAccessController() {
        this.overrideService = new GarageAccessOverrideService();
        this.view = new GarageGlobalAccessView(this);
    }

    public GarageGlobalAccessController(GarageGlobalAccessView view) {
        this.overrideService = new GarageAccessOverrideService();
        this.view = view;
    }

    /**
     * @param mode 
     * @param reason 
     * @return
     */
    public void handleOverrideRequest(String garageID, String mode, String reason) {
        try {
            GarageAccess accessMode = GarageAccess.valueOf(mode.toUpperCase());
            overrideService.setGlobalOverride(garageID, accessMode, reason);
            
            ParkingGarage garage = overrideService.getParkingGarageDAO().findGarage(garageID);
            if (garage != null) {
                view.displaySuccess();
                view.displayCurrentStatus(garage);
            } else {
                view.displayError("Garage not found: " + garageID);
            }
        } catch (IllegalArgumentException e) {
            view.displayError("Invalid access mode: " + mode);
        } catch (Exception e) {
            view.displayError("Error processing override request: " + e.getMessage());
        }
    }

    /**
     * @param garageID
     * @return
     */
    public void handleResetRequest(String garageID) {
        try {
            overrideService.resetGlobalOverride(garageID);
            ParkingGarage garage = overrideService.getParkingGarageDAO().findGarage(garageID);
            if (garage != null) {
                view.displaySuccess();
                view.displayCurrentStatus(garage);
            } else {
                view.displayError("Garage not found: " + garageID);
            }
        } catch (Exception e) {
            view.displayError("Error resetting override: " + e.getMessage());
        }
    }

    public GarageAccessOverrideService getOverrideService() {
        return overrideService;
    }

    public List<ParkingGarage> getAllGarages() {
        return new ArrayList<>(overrideService.getParkingGarageDAO().getAllGarages());
    }

    public ParkingGarage getGarage(String garageID) {
        return overrideService.getParkingGarageDAO().findGarage(garageID);
    }
}
