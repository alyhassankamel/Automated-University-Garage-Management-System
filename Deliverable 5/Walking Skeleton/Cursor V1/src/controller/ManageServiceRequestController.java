package com.augms.controller;

import com.augms.service.ManageServiceRequestService;
import com.augms.entity.ServiceRequest;
import java.util.List;

public class ManageServiceRequestController {
    private ManageServiceRequestService manageServiceRequestService;
    
    public ManageServiceRequestController(ManageServiceRequestService manageServiceRequestService) {
        this.manageServiceRequestService = manageServiceRequestService;
    }
    
    public void handleServiceRequestUpdate(String requestID, String status) {
        manageServiceRequestService.updateServiceRequestStatus(requestID, status);
    }
    
    public void handleGetAllRequests() {
        manageServiceRequestService.getAllServiceRequests();
    }
}
