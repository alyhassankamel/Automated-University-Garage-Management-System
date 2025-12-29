package controller;

import service.GarageSystem;

public class VehicleRegistrationController {

    private GarageSystem garageSystem;

    public VehicleRegistrationController() {
        this.garageSystem = new GarageSystem();
    }

    public void handleRegistrationRequest(String licensePlate, String type) {
        String result = garageSystem.registerVehicle(licensePlate, type);
        System.out.println(result);
    }
}
