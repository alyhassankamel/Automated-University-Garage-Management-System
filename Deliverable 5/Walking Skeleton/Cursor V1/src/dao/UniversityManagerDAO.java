package com.augms.dao;

import com.augms.entity.UniversityManager;

public interface UniversityManagerDAO {
    UniversityManager findManager(String managerID);
    UniversityManager findManagerByUniID(String uniID);
    boolean updateManager(UniversityManager manager);
}

