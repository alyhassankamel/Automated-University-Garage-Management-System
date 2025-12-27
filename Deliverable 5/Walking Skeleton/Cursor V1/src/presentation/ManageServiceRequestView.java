package com.augms.presentation;

import com.augms.controller.ManageServiceRequestController;
import com.augms.entity.ServiceRequest;
import java.util.List;

public class ManageServiceRequestView {
    private ManageServiceRequestController controller;
    
    public ManageServiceRequestView(ManageServiceRequestController controller) {
        this.controller = controller;
    }
    
    public void displayServiceRequests(List<ServiceRequest> requests) {
        // Display service requests
    }
    
    public void submitRequestUpdate(String requestID, String status) {
        controller.handleServiceRequestUpdate(requestID, status);
    }
    
    public void displayUpdateConfirmation(boolean success, String message) {
        // Display update confirmation
    }
}
