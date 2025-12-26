package com.augms.controller;

import com.augms.service.VehicleEntryService;
import com.augms.entity.EntryValidationResult;

public class VehicleEntryController {
    private VehicleEntryService entryService;
    
    public VehicleEntryController(VehicleEntryService entryService) {
        this.entryService = entryService;
    }
    
    public void handleLicensePlateScan(String licensePlate) {
        // Handle license plate scan
        entryService.validateRegistration(licensePlate);
    }
    
    public void handleEntryValidation(EntryValidationResult result) {
        // Handle entry validation result
    }
}

