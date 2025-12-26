package com.augms.entity;

import java.util.Map;

public class PaymentMethod {
    private String methodID;
    private String methodType;
    private Map<String, String> details;
    
    public PaymentMethod() {}
    
    public PaymentMethod(String methodID, String methodType, Map<String, String> details) {
        this.methodID = methodID;
        this.methodType = methodType;
        this.details = details;
    }
    
    public String getMethodID() {
        return methodID;
    }
    
    public void setMethodID(String methodID) {
        this.methodID = methodID;
    }
    
    public String getMethodType() {
        return methodType;
    }
    
    public void setMethodType(String methodType) {
        this.methodType = methodType;
    }
    
    public Map<String, String> getDetails() {
        return details;
    }
    
    public void setDetails(Map<String, String> details) {
        this.details = details;
    }
    
    public PaymentResult processPayment(double amount) {
        // Process payment using this method
        return new PaymentResult(true, "Payment successful");
    }
    
    public boolean validateMethod() {
        // Validate payment method
        return true;
    }
}

