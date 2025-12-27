package com.augms.presentation;

import com.augms.controller.RegistrationController;
import com.augms.entity.ParkingUser;
import com.augms.entity.Vehicle;

public class RegistrationView {
    private RegistrationController controller;
    
    public RegistrationView(RegistrationController controller) {
        this.controller = controller;
    }
    
    public void submitRegistrationDetails(ParkingUser parkingUser, Vehicle vehicle) {
        controller.handleRegistrationRequest(parkingUser, vehicle);
    }
    
    public void displayConfirmation(boolean success, String message) {
        // Display confirmation to user
    }
    
    public void displayError(String error) {
        // Display error to user
    }
}

