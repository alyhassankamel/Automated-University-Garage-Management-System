package com.augms.entity;

public class PaymentResult {
    private boolean success;
    private String message;
    private String reason;
    
    public PaymentResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public PaymentResult(boolean success, String message, String reason) {
        this.success = success;
        this.message = message;
        this.reason = reason;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public String getReason() {
        return reason;
    }
}

