package controller;

import service.GarageSystem;
import java.sql.Timestamp;

/**
 * Controller for Vehicle Entry Use Case.
 */
public class VehicleEntryController {

    private GarageSystem garageSystem;

    public VehicleEntryController() {
        this.garageSystem = new GarageSystem();
    }

    /**
     * Handles the request for a vehicle to enter.
     * 
     * @param licensePlate The license plate of the vehicle.
     * @param gateId       The ID of the entry gate.
     */
    public void handleEntryRequest(String licensePlate, int gateId) {
        System.out.println("Processing entry for vehicle: " + licensePlate);
        Timestamp now = new Timestamp(System.currentTimeMillis());

        String result = garageSystem.processEntry(licensePlate, now, gateId);
        System.out.println(result);
    }

    /**
     * Handles entry with manual spot selection.
     * 
     * @param licensePlate The license plate.
     * @param gateId       The gate ID.
     * @param spotNumber   The requested spot number.
     */
    public void handleManualEntryRequest(String licensePlate, int gateId, String spotNumber) {
        System.out.println("Processing entry for vehicle: " + licensePlate + " into spot: " + spotNumber);
        Timestamp now = new Timestamp(System.currentTimeMillis());

        String result = garageSystem.processEntry(licensePlate, now, gateId, spotNumber);
        System.out.println(result);
    }
}
