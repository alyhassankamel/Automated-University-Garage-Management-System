package com.augms.controller;

import com.augms.service.ParkingStatusService;
import com.augms.entity.ParkingStatus;

public class ParkingStatusController {
    private ParkingStatusService parkingStatusService;
    
    public ParkingStatusController(ParkingStatusService parkingStatusService) {
        this.parkingStatusService = parkingStatusService;
    }
    
    public void handleLiveOccupancyRequest() {
        ParkingStatus status = parkingStatusService.getParkingStatus();
        // Process and return to view
    }
    
    public void handleStatusUpdate(ParkingStatus status) {
        // Handle status updates
    }
}

