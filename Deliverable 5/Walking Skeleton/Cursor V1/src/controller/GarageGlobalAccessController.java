package com.augms.controller;

import com.augms.service.GarageAccessOverrideService;

public class GarageGlobalAccessController {
    private GarageAccessOverrideService garageAccessOverrideService;
    
    public GarageGlobalAccessController(GarageAccessOverrideService garageAccessOverrideService) {
        this.garageAccessOverrideService = garageAccessOverrideService;
    }
    
    public void handleOverrideRequest(String mode, String reason) {
        garageAccessOverrideService.setGlobalOverride(mode, reason);
    }
    
    public void handleResetRequest() {
        garageAccessOverrideService.resetGlobalOverride();
    }
}
