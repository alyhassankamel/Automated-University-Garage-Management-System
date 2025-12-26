package com.augms.view;

import com.augms.controller.VehicleUpdateController;
import com.augms.entity.Vehicle;
import java.util.Map;

public class VehicleUpdateView {
    private VehicleUpdateController controller;
    
    public VehicleUpdateView(VehicleUpdateController controller) {
        this.controller = controller;
    }
    
    public void displayVehicleDetails(Vehicle vehicle) {
        // Display vehicle details
    }
    
    public void submitVehicleUpdate(String vehicleID, Map<String, Object> updatedDetails) {
        controller.handleVehicleUpdateRequest(vehicleID, updatedDetails);
    }
    
    public void displayUpdateConfirmation(boolean success) {
        // Display update confirmation
    }
}

