package com.augms.dao;

import com.augms.entity.ServiceRequest;
import com.augms.entity.RequestStatus;
import java.util.List;

public interface ServiceRequestDAO {
    boolean createRequest(ServiceRequest request);
    ServiceRequest findRequest(String requestID);
    boolean updateRequestStatus(String requestID, RequestStatus status);
    List<ServiceRequest> getRequestsByUser(String userID);
    List<ServiceRequest> getRequestsBySpot(String spotID);
}

