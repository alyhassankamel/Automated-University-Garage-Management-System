package com.augms.controller;

import com.augms.service.VehicleUpdateService;
import java.util.Map;

public class VehicleUpdateController {
    private VehicleUpdateService vehicleUpdateService;
    
    public VehicleUpdateController(VehicleUpdateService vehicleUpdateService) {
        this.vehicleUpdateService = vehicleUpdateService;
    }
    
    public void handleVehicleUpdateRequest(String vehicleID, Map<String, Object> updatedDetails) {
        vehicleUpdateService.updateVehicleDetails(vehicleID, updatedDetails);
    }
    
    public void handleUpdateConfirmation(boolean success) {
        // Handle update confirmation
    }
}

