package com.augms.dao;

import com.augms.entity.PaymentMethod;

public interface PaymentMethodDAO {
    PaymentMethod findPaymentMethod(String methodID);
    boolean processPayment(String methodID, double amount);
    boolean validatePaymentMethod(String methodID);
}

