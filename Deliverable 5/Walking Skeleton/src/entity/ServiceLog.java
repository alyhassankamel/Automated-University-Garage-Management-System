package com.augms.entity;

import java.util.Date;

public class ServiceLog {
    private String logID;
    private String requestID;
    private String serviceID;
    private Date timestamp;
    private String status;
    
    public ServiceLog() {}
    
    public ServiceLog(String logID, String requestID, String serviceID, Date timestamp, String status) {
        this.logID = logID;
        this.requestID = requestID;
        this.serviceID = serviceID;
        this.timestamp = timestamp;
        this.status = status;
    }
    
    public String getLogID() {
        return logID;
    }
    
    public void setLogID(String logID) {
        this.logID = logID;
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
    
    public Date getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
}

