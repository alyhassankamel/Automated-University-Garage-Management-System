package com.augms.controller;

import com.augms.service.VehicleExitService;
import java.util.Date;

public class VehicleExitController {
    private VehicleExitService exitService;
    
    public VehicleExitController(VehicleExitService exitService) {
        this.exitService = exitService;
    }
    
    public void handleVehicleExit(String licensePlate, Date timestamp, String exitGateID) {
        exitService.processExit(licensePlate, timestamp);
    }
    
    public void handleExitConfirmation() {
        // Handle exit confirmation
    }
}

