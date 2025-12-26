package com.augms.view;

import com.augms.controller.VehicleEntryController;

public class VehicleEntryView {
    private VehicleEntryController controller;
    
    public VehicleEntryView(VehicleEntryController controller) {
        this.controller = controller;
    }
    
    public void displayEntryStatus(String status) {
        // Display entry status
    }
    
    public void displayGarageFull() {
        // Display garage full message
    }
    
    public void displayInvalidVehicle() {
        // Display invalid vehicle message
    }
    
    public void displayEntryApproved() {
        // Display entry approved message
    }
}

