package com.augms.entity;

import java.util.Date;

public class Payment {
    private String paymentID;
    private String invoiceID;
    private String requestID;
    private double amount;
    private Date date;
    private Date time;
    private String status;
    private String methodID;
    // Association: Payment 1 → PaymentMethod
    private PaymentMethod paymentMethod;
    
    public Payment() {}
    
    public Payment(String paymentID, String invoiceID, String requestID, 
                  double amount, Date date, Date time, String status, String methodID) {
        this.paymentID = paymentID;
        this.invoiceID = invoiceID;
        this.requestID = requestID;
        this.amount = amount;
        this.date = date;
        this.time = time;
        this.status = status;
        this.methodID = methodID;
    }
    
    public String getPaymentID() {
        return paymentID;
    }
    
    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }
    
    public String getInvoiceID() {
        return invoiceID;
    }
    
    public void setInvoiceID(String invoiceID) {
        this.invoiceID = invoiceID;
    }
    
    public String getRequestID() {
        return requestID;
    }
    
    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    public Date getDate() {
        return date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
    
    public Date getTime() {
        return time;
    }
    
    public void setTime(Date time) {
        this.time = time;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getMethodID() {
        return methodID;
    }
    
    public void setMethodID(String methodID) {
        this.methodID = methodID;
    }
    
    public boolean processPayment() {
        // Process payment logic
        return true;
    }
    
    public boolean updateStatus(String status) {
        this.status = status;
        return true;
    }
    
    public boolean paymentSuccess() {
        this.status = "SUCCESS";
        return true;
    }
    
    public boolean paymentFailed(String reason) {
        this.status = "FAILED";
        return false;
    }
    
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}

