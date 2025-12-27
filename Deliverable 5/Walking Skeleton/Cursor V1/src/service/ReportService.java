package com.augms.service;

import com.augms.dao.EntryExitRepository;
import com.augms.dao.ServiceLogRepository;
import com.augms.dao.LogsDAO;
import com.augms.entity.UsageReport;
import com.augms.entity.EntryExitLog;
import com.augms.entity.ServiceLog;
import java.util.Date;
import java.util.List;

public class ReportService {
    private EntryExitRepository entryExitRepository;
    private ServiceLogRepository serviceLogRepository;
    private ReportGenerator reportGenerator;
    private LogsDAO logsDAO;
    
    public ReportService(EntryExitRepository entryExitRepository,
                        ServiceLogRepository serviceLogRepository,
                        ReportGenerator reportGenerator,
                        LogsDAO logsDAO) {
        this.entryExitRepository = entryExitRepository;
        this.serviceLogRepository = serviceLogRepository;
        this.reportGenerator = reportGenerator;
        this.logsDAO = logsDAO;
    }
    
    public UsageReport compileUsageReport(Date startDate, Date endDate) {
        List<EntryExitLog> entryExitLogs = entryExitRepository.getEntryExitLogs(startDate, endDate);
        List<ServiceLog> serviceLogs = serviceLogRepository.getServiceLogs(startDate, endDate);
        return generateReport(entryExitLogs, serviceLogs);
    }
    
    public UsageReport generateReport(List<EntryExitLog> entryExitLogs, List<ServiceLog> serviceLogs) {
        return reportGenerator.generateReport(entryExitLogs, serviceLogs);
    }
    
    public String saveReport(UsageReport report) {
        return logsDAO.saveReport(report);
    }
}

