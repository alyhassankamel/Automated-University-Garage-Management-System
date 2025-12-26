package com.augms.dao;

import com.augms.entity.UsageReport;
import java.util.List;

public interface LogsDAO {
    String saveReport(UsageReport report);
    UsageReport findReport(String reportID);
    List<UsageReport> getAllReports();
}

