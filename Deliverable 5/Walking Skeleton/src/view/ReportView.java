package com.augms.view;

import com.augms.controller.ReportController;
import com.augms.entity.UsageReport;
import java.util.Date;

public class ReportView {
    private ReportController controller;
    
    public ReportView(ReportController controller) {
        this.controller = controller;
    }
    
    public void requestUsageReport(Date startDate, Date endDate) {
        controller.handleUsageReportRequest(startDate, endDate);
    }
    
    public void displayReport(UsageReport report) {
        // Display report
    }
    
    public void downloadReport(String reportId) {
        // Download report
    }
}

