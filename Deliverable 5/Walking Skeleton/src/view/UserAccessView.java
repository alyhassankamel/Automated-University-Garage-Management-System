package com.augms.view;

import com.augms.controller.UserAccessController;

public class UserAccessView {
    private UserAccessController controller;
    
    public UserAccessView(UserAccessController controller) {
        this.controller = controller;
    }
    
    public void submitAccessChange(int userID, boolean status) {
        controller.handleAccessChangeRequest(userID, status);
    }
    
    public void displayConfirmation(boolean success, String message) {
        // Display confirmation
    }
}

