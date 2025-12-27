package com.augms.presentation;

import com.augms.controller.GarageGlobalAccessController;
import com.augms.entity.ParkingGarage;

public class GarageGlobalAccessView {
    private GarageGlobalAccessController controller;
    
    public GarageGlobalAccessView(GarageGlobalAccessController controller) {
        this.controller = controller;
    }
    
    public void displayCurrentStatus(ParkingGarage parkingGarage) {
        // Display current status
    }
    
    public void showOverrideConfirmation(String accessMode) {
        // Show override confirmation
    }
    
    public void displaySuccess() {
        // Display success message
    }
    
    public void displayError() {
        // Display error message
    }
}
