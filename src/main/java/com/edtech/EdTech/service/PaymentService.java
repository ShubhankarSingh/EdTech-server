package com.edtech.EdTech.service;

import com.stripe.exception.StripeException;

public interface PaymentService {

    String createPaymentIntent(Long courseId, String email, double amount) throws StripeException;
}
