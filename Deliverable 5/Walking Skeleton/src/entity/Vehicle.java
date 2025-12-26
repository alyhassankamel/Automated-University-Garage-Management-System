package com.augms.entity;

import java.util.Map;

public class Vehicle {
    private String vehicleID;
    private String licensePlate;
    private String model;
    private String color;
    private boolean accessStatus;
    private String userID;
    
    public Vehicle() {}
    
    public Vehicle(String vehicleID, String licensePlate, String model, 
                  String color, boolean accessStatus, String userID) {
        this.vehicleID = vehicleID;
        this.licensePlate = licensePlate;
        this.model = model;
        this.color = color;
        this.accessStatus = accessStatus;
        this.userID = userID;
    }
    
    public String getVehicleID() {
        return vehicleID;
    }
    
    public void setVehicleID(String vehicleID) {
        this.vehicleID = vehicleID;
    }
    
    public String getLicensePlate() {
        return licensePlate;
    }
    
    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }
    
    public String getModel() {
        return model;
    }
    
    public void setModel(String model) {
        this.model = model;
    }
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
    
    public boolean isAccessStatus() {
        return accessStatus;
    }
    
    public void setAccessStatus(boolean accessStatus) {
        this.accessStatus = accessStatus;
    }
    
    public String getUserID() {
        return userID;
    }
    
    public void setUserID(String userID) {
        this.userID = userID;
    }
    
    public boolean updateDetails(Map<String, Object> details) {
        // Update vehicle details
        return true;
    }
    
    public boolean associateVehicle() {
        return true;
    }
}

