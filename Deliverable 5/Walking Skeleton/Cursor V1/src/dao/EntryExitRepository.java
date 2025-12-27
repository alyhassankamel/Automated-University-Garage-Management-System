package com.augms.dao;

import com.augms.entity.EntryExitLog;
import java.util.Date;
import java.util.List;

public interface EntryExitRepository {
    List<EntryExitLog> getEntryExitLogs(Date startDate, Date endDate);
    boolean createEntryLog(EntryExitLog log);
    boolean createExitLog(EntryExitLog log);
}

