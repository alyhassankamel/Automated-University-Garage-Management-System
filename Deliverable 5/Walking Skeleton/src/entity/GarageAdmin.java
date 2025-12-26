package com.augms.entity;

public class GarageAdmin {
    private String uniID;
    private String name;
    private String phone;
    private String adminID;
    
    public GarageAdmin() {}
    
    public GarageAdmin(String uniID, String name, String phone, String adminID) {
        this.uniID = uniID;
        this.name = name;
        this.phone = phone;
        this.adminID = adminID;
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
    
    public String getAdminID() {
        return adminID;
    }
    
    public void setAdminID(String adminID) {
        this.adminID = adminID;
    }
    
    public boolean receiveSensorErrorAlert(SensorAlert alert) {
        // Receive and process sensor error alert
        return true;
    }
    
    public ImpactAssessment assessErrorImpact(SensorAlert alert) {
        // Assess impact of error
        return new ImpactAssessment("MEDIUM", "Moderate impact");
    }
    
    public boolean initiateResponseActions(SensorAlert alert) {
        // Initiate response actions
        return true;
    }
    
    public boolean notifyUniversityManager(SensorAlert alert) {
        // Notify university manager
        return true;
    }
}

