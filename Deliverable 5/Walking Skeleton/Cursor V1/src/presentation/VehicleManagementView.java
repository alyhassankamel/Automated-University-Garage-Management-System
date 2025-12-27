package com.augms.presentation;

import com.augms.controller.VehicleManagementController;
import com.augms.entity.ParkingUser;
import java.util.List;

public class VehicleManagementView {
    private VehicleManagementController controller;
    
    public VehicleManagementView(VehicleManagementController controller) {
        this.controller = controller;
    }
    
    public void displayPendingRegistrations(List<ParkingUser> users) {
        // Display pending registrations
    }
    
    public void displayUserDetails(ParkingUser user) {
        // Display user details
    }
    
    public void displayConfirmation(boolean success, String message) {
        // Display confirmation
    }
}

