package com.augms.view;

import com.augms.controller.ParkingStatusController;
import com.augms.entity.OccupancyData;
import com.augms.entity.ParkingStatus;

public class ParkingStatusView {
    private ParkingStatusController controller;
    
    public ParkingStatusView(ParkingStatusController controller) {
        this.controller = controller;
    }
    
    public void requestLiveOccupancy() {
        controller.handleLiveOccupancyRequest();
    }
    
    public void displayLiveOccupancy(OccupancyData occupancyData) {
        // Display live occupancy data
    }
    
    public void displayParkingStatus(ParkingStatus status) {
        // Display parking status
    }
}

