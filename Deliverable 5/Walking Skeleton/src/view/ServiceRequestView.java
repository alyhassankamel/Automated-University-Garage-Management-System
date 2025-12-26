package com.augms.view;

import com.augms.controller.ServiceRequestController;
import com.augms.entity.ServiceType;
import com.augms.entity.Invoice;
import java.util.List;

public class ServiceRequestView {
    private ServiceRequestController controller;
    
    public ServiceRequestView(ServiceRequestController controller) {
        this.controller = controller;
    }
    
    public void initiateServiceRequest(String spotID) {
        controller.handleServiceRequest(spotID, null, null);
    }
    
    public void displayAvailableServices(List<ServiceType> services) {
        // Display available services
    }
    
    public void displayInvoice(Invoice invoice) {
        // Display invoice
    }
    
    public void displayPaymentOptions() {
        // Display payment options
    }
    
    public void displayResult(boolean requestRecorded, String paymentStatus) {
        // Display result
    }
}

