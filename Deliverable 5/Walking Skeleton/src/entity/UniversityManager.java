package com.augms.entity;

import java.util.List;

public class UniversityManager {
    private String uniID;
    private String name;
    private String phone;
    private String managerID;
    
    public UniversityManager() {}
    
    public UniversityManager(String uniID, String name, String phone, String managerID) {
        this.uniID = uniID;
        this.name = name;
        this.phone = phone;
        this.managerID = managerID;
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
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getManagerID() {
        return managerID;
    }
    
    public void setManagerID(String managerID) {
        this.managerID = managerID;
    }
    
    public boolean receiveSystemWideAlert(SensorAlert alert) {
        // Receive system-wide alert
        return true;
    }
    
    public boolean manageGarageAccess(String garageID, boolean access) {
        // Manage garage access
        return true;
    }
    
    public List<AlertPattern> reviewAlertPatterns() {
        // Review alert patterns
        return null;
    }
    
    public boolean authorizeEscalationActions(String action) {
        // Authorize escalation actions
        return true;
    }
}

