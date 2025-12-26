package com.augms.dto;

public class VehicleDetails {
    private String licensePlate;
    private String model;
    private String color;
    
    public VehicleDetails() {}
    
    public VehicleDetails(String licensePlate, String model, String color) {
        this.licensePlate = licensePlate;
        this.model = model;
        this.color = color;
    }
    
    // Getters and setters
    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
}

