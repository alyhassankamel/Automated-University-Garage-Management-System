package com.augms.entity;

public class AccessChangeResult {
    private boolean success;
    private String message;
    
    public AccessChangeResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public String getMessage() {
        return message;
    }
}

