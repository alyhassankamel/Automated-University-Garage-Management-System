package com.augms.entity;

import java.util.List;
import java.util.ArrayList;

public class ParkingUser {
    private String userID;
    private String uniID;
    private String name;
    private String email;
    private String phone;
    private String role; // Student or Faculty
    private boolean accessStatus;
    private String registrationStatus;
    private boolean isUrgentMember;
    // Association: ParkingUser 1..* → Vehicle
    private List<Vehicle> vehicles;
    
    public ParkingUser() {
        this.vehicles = new ArrayList<>();
    }
    
    public ParkingUser(String userID, String uniID, String name, String email, 
                      String phone, boolean accessStatus, String registrationStatus, 
                      boolean isUrgentMember) {
        this.userID = userID;
        this.uniID = uniID;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.accessStatus = accessStatus;
        this.registrationStatus = registrationStatus;
        this.isUrgentMember = isUrgentMember;
        this.vehicles = new ArrayList<>();
    }
    
    public String getUserID() {
        return userID;
    }
    
    public void setUserID(String userID) {
        this.userID = userID;
    }
    
    public String getUniID() {
        return uniID;
    }
    
    public void setUniID(String uniID) {
        this.uniID = uniID;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public boolean isAccessStatus() {
        return accessStatus;
    }
    
    public void setAccessStatus(boolean accessStatus) {
        this.accessStatus = accessStatus;
    }
    
    public String getRegistrationStatus() {
        return registrationStatus;
    }
    
    public void setRegistrationStatus(String registrationStatus) {
        this.registrationStatus = registrationStatus;
    }
    
    public boolean isUrgentMember() {
        return isUrgentMember;
    }
    
    public void setUrgentMember(boolean urgentMember) {
        isUrgentMember = urgentMember;
    }
    
    public boolean updateAccess(boolean status) {
        this.accessStatus = status;
        return true;
    }
    
    public boolean confirmUpdate() {
        return true;
    }
    
    public List<Vehicle> getVehicles() {
        return vehicles;
    }
    
    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }
    
    public void addVehicle(Vehicle vehicle) {
        this.vehicles.add(vehicle);
    }
}

