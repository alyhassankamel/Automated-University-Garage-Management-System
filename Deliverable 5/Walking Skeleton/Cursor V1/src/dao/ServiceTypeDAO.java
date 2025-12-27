package com.augms.dao;

import com.augms.entity.ServiceType;
import java.util.List;

public interface ServiceTypeDAO {
    ServiceType findServiceType(String serviceID);
    List<ServiceType> getAllServiceTypes();
    List<ServiceType> getAvailableServicesForSpot(String spotID);
}

