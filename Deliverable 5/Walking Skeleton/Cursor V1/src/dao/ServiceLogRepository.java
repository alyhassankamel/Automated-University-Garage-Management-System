package com.augms.dao;

import com.augms.entity.ServiceLog;
import java.util.Date;
import java.util.List;

public interface ServiceLogRepository {
    List<ServiceLog> getServiceLogs(Date startDate, Date endDate);
    boolean createServiceLog(ServiceLog log);
}

