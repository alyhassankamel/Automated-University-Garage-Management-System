package com.augms.entity;

import java.util.Date;
import java.util.List;

public class UsageReport {
    private String reportID;
    private Date startDate;
    private Date endDate;
    private List<EntryExitLog> entryExitLogs;
    private List<ServiceLog> serviceLogs;
    private String format; // PDF, Excel, HTML
    
    public UsageReport() {}
    
    public UsageReport(String reportID, Date startDate, Date endDate, 
                      List<EntryExitLog> entryExitLogs, List<ServiceLog> serviceLogs, String format) {
        this.reportID = reportID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.entryExitLogs = entryExitLogs;
        this.serviceLogs = serviceLogs;
        this.format = format;
    }
    
    public String getReportID() {
        return reportID;
    }
    
    public void setReportID(String reportID) {
        this.reportID = reportID;
    }
    
    public Date getStartDate() {
        return startDate;
    }
    
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
    public Date getEndDate() {
        return endDate;
    }
    
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    public List<EntryExitLog> getEntryExitLogs() {
        return entryExitLogs;
    }
    
    public void setEntryExitLogs(List<EntryExitLog> entryExitLogs) {
        this.entryExitLogs = entryExitLogs;
    }
    
    public List<ServiceLog> getServiceLogs() {
        return serviceLogs;
    }
    
    public void setServiceLogs(List<ServiceLog> serviceLogs) {
        this.serviceLogs = serviceLogs;
    }
    
    public String getFormat() {
        return format;
    }
    
    public void setFormat(String format) {
        this.format = format;
    }
    
    public Object generateReport() {
        // Generate report document
        return null;
    }
}

