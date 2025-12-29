package controller;

import service.GarageSystem;
import java.sql.Timestamp;

public class VehicleExitController {

    private GarageSystem garageSystem;

    public VehicleExitController() {
        this.garageSystem = new GarageSystem();
    }

    public void handleExitRequest(String licensePlate, int gateId) {
        System.out.println("Processing exit for vehicle: " + licensePlate);
        String result = garageSystem.processExit(licensePlate, new Timestamp(System.currentTimeMillis()), gateId);
        System.out.println(result);
    }
}
