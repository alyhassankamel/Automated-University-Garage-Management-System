package com.augms.entity;

import java.util.Date;

public class SensorAlert {
    private String alertID;
    private String sensorID;
    private String errorType;
    private Date timestamp;
    private String severity;
    private String details;
    
    public SensorAlert() {
        this.timestamp = new Date();
    }
    
    public SensorAlert(String alertID, String sensorID, String errorType, 
                      Date timestamp, String severity, String details) {
        this.alertID = alertID;
        this.sensorID = sensorID;
        this.errorType = errorType;
        this.timestamp = timestamp != null ? timestamp : new Date();
        this.severity = severity;
        this.details = details;
    }
    
    public String getAlertID() {
        return alertID;
    }
    
    public void setAlertID(String alertID) {
        this.alertID = alertID;
    }
    
    public String getSensorID() {
        return sensorID;
    }
    
    public void setSensorID(String sensorID) {
        this.sensorID = sensorID;
    }
    
    public String getErrorType() {
        return errorType;
    }
    
    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }
    
    public Date getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getSeverity() {
        return severity;
    }
    
    public void setSeverity(String severity) {
        this.severity = severity;
    }
    
    public String getDetails() {
        return details;
    }
    
    public void setDetails(String details) {
        this.details = details;
    }
}

