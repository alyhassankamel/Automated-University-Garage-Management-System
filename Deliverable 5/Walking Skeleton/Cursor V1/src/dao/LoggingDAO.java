package com.augms.dao;

import com.augms.entity.Log;
import com.augms.entity.ErrorLog;
import java.util.Date;
import java.util.List;

public interface LoggingDAO {
    boolean createLog(Log log);
    Log findLog(String logID);
    List<Log> getLogsByType(String logType);
    List<Log> getLogsByDateRange(Date startDate, Date endDate);
    List<ErrorLog> getErrorLogs();
}

