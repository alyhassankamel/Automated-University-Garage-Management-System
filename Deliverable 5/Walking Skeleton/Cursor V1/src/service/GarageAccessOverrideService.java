package com.augms.service;

import com.augms.dao.UniversityManagerDAO;

public class GarageAccessOverrideService {
    private UniversityManagerDAO universityManagerDAO;
    private LoggingService loggingService;
    
    public GarageAccessOverrideService(UniversityManagerDAO universityManagerDAO, LoggingService loggingService) {
        this.universityManagerDAO = universityManagerDAO;
        this.loggingService = loggingService;
    }
    
    public boolean setGlobalOverride(String mode, String reason) {
        // Set global override
        logOverrideChange(mode, reason);
        return true;
    }
    
    public boolean resetGlobalOverride() {
        // Reset global override
        logOverrideChange("RESET", "Global override reset");
        return true;
    }
    
    public String getCurrentOverrideStatus() {
        // Get current override status
        return null;
    }
    
    public boolean logOverrideChange(String mode, String reason) {
        // Log override change
        return loggingService.recordAccessChange(null);
    }
}
