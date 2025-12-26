package com.augms.controller;

import com.augms.service.ReportService;
import com.augms.entity.UsageReport;
import java.util.Date;

public class ReportController {
    private ReportService reportService;
    
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }
    
    public void handleUsageReportRequest(Date startDate, Date endDate) {
        reportService.compileUsageReport(startDate, endDate);
    }
    
    public void handleReportGeneration(UsageReport report) {
        reportService.saveReport(report);
    }
}

