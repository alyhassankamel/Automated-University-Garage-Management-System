package com.augms.dao;

import com.augms.entity.GarageAdmin;

public interface GarageAdminDAO {
    GarageAdmin findAdmin(String adminID);
    GarageAdmin findAdminByUniID(String uniID);
    boolean validateCredentials(String adminID, String password);
    boolean updateAdmin(GarageAdmin admin);
}

