package com.augms.entity;

public class ServiceType {
    private String serviceID;
    private String serviceName;
    private String description;
    private double price;
    
    public ServiceType() {}
    
    public ServiceType(String serviceID, String serviceName, String description, double price) {
        this.serviceID = serviceID;
        this.serviceName = serviceName;
        this.description = description;
        this.price = price;
    }
    
    public String getServiceID() {
        return serviceID;
    }
    
    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }
    
    public String getServiceName() {
        return serviceName;
    }
    
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
}

