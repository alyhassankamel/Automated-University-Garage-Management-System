package com.augms.entity;

import java.util.Date;

public class Invoice {
    private String invoiceID;
    private String requestID;
    private double amount;
    private Date date;
    private Date time;
    private InvoiceStatus status;
    // Association: Invoice 1..* → Payment
    private java.util.List<Payment> payments;
    
    public Invoice() {
        this.payments = new java.util.ArrayList<>();
    }
    
    public Invoice(String invoiceID, String requestID, double amount, 
                  Date date, Date time, InvoiceStatus status) {
        this.invoiceID = invoiceID;
        this.requestID = requestID;
        this.amount = amount;
        this.date = date;
        this.time = time;
        this.status = status;
        this.payments = new java.util.ArrayList<>();
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
    
    public InvoiceStatus getStatus() {
        return status;
    }
    
    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }
    
    public boolean updateStatus(InvoiceStatus status) {
        this.status = status;
        return true;
    }
    
    public java.util.List<Payment> getPayments() {
        return payments;
    }
    
    public void setPayments(java.util.List<Payment> payments) {
        this.payments = payments;
    }
    
    public void addPayment(Payment payment) {
        this.payments.add(payment);
    }
}

