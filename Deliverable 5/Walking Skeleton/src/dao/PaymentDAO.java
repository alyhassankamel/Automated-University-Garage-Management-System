package com.augms.dao;

import com.augms.entity.Payment;
import com.augms.entity.PaymentStatus;
import java.util.List;

public interface PaymentDAO {
    boolean createPayment(Payment payment);
    Payment findPayment(String paymentID);
    boolean updatePaymentStatus(String paymentID, PaymentStatus status);
    List<Payment> getPaymentsByRequest(String requestID);
}

