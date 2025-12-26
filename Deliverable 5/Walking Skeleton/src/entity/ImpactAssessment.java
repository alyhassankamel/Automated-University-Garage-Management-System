package com.augms.entity;

public class ImpactAssessment {
    private String severity;
    private String description;
    
    public ImpactAssessment(String severity, String description) {
        this.severity = severity;
        this.description = description;
    }
    
    public String getSeverity() {
        return severity;
    }
    
    public String getDescription() {
        return description;
    }
}

