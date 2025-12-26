package com.augms.dao;

import com.augms.entity.PaymentMethod;
import com.augms.entity.PaymentResult;

public interface PaymentMethodDAO {
    PaymentMethod findPaymentMethod(String methodID);
    PaymentResult processPayment(String methodID, double amount);
    boolean validatePaymentMethod(String methodID);
}

