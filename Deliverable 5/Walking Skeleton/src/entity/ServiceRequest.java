package com.augms.entity;

import java.util.Date;

public class ServiceRequest {
    private String requestID;
    private String serviceID;
    private String spotID;
    private String userID;
    private String vehicleID;
    private RequestStatus status;
    private Date date;
    private Date time;
    // Associations
    private ServiceType serviceType;  // ServiceRequest 1 → ServiceType
    private ParkingSpot parkingSpot;  // ServiceRequest 1 → ParkingSpot
    private ParkingUser parkingUser;   // ServiceRequest 1 → ParkingUser
    private Vehicle vehicle;           // ServiceRequest 1 → Vehicle
    private Invoice invoice;           // ServiceRequest 0..1 → Invoice
    
    public ServiceRequest() {}
    
    public ServiceRequest(String requestID, String serviceID, String spotID, 
                         String userID, String vehicleID, RequestStatus status, 
                         Date date, Date time) {
        this.requestID = requestID;
        this.serviceID = serviceID;
        this.spotID = spotID;
        this.userID = userID;
        this.vehicleID = vehicleID;
        this.status = status;
        this.date = date;
        this.time = time;
    }
    
    public String getRequestID() {
        return requestID;
    }
    
    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }
    
    public String getServiceID() {
        return serviceID;
    }
    
    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }
    
    public String getSpotID() {
        return spotID;
    }
    
    public void setSpotID(String spotID) {
        this.spotID = spotID;
    }
    
    public String getUserID() {
        return userID;
    }
    
    public void setUserID(String userID) {
        this.userID = userID;
    }
    
    public String getVehicleID() {
        return vehicleID;
    }
    
    public void setVehicleID(String vehicleID) {
        this.vehicleID = vehicleID;
    }
    
    public RequestStatus getStatus() {
        return status;
    }
    
    public void setStatus(RequestStatus status) {
        this.status = status;
    }
    
    public Date getDate() {
        return date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
    
    public Date getTime() {
        return time;
    }
    
    public void setTime(Date time) {
        this.time = time;
    }
    
    public boolean updateStatus(RequestStatus status) {
        this.status = status;
        return true;
    }
    
    public boolean associateRequester(String userID) {
        this.userID = userID;
        return true;
    }
    
    public boolean associateVehicle(String vehicleID) {
        this.vehicleID = vehicleID;
        return true;
    }
    
    // Association getters and setters
    public ServiceType getServiceType() {
        return serviceType;
    }
    
    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }
    
    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }
    
    public void setParkingSpot(ParkingSpot parkingSpot) {
        this.parkingSpot = parkingSpot;
    }
    
    public ParkingUser getParkingUser() {
        return parkingUser;
    }
    
    public void setParkingUser(ParkingUser parkingUser) {
        this.parkingUser = parkingUser;
    }
    
    public Vehicle getVehicle() {
        return vehicle;
    }
    
    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
    
    public Invoice getInvoice() {
        return invoice;
    }
    
    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }
}

