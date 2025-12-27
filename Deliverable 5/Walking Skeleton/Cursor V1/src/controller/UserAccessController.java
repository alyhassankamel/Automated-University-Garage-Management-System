package com.augms.controller;

import com.augms.service.UserAccessService;

public class UserAccessController {
    private UserAccessService userAccessService;
    
    public UserAccessController(UserAccessService userAccessService) {
        this.userAccessService = userAccessService;
    }
    
    public void handleAccessChangeRequest(int userID, boolean status) {
        userAccessService.updateAccess(userID, status);
    }
    
    public void handleAccessChangeConfirmation(boolean success, String message) {
        // Handle access change confirmation
    }
}

