package com.edtech.EdTech.service;

import com.edtech.EdTech.model.Payment;
import com.edtech.EdTech.repository.PaymentRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private PaymentRepository paymentRepository;

    public String createPaymentIntent(Long courseId, String email, double amount) throws StripeException{
        // Create PaymentIntent parameters
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount((long) (amount*100))
                .setCurrency("inr")
                .setReceiptEmail(email)
                .putMetadata("courseId", courseId.toString())
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams
                                .AutomaticPaymentMethods
                                .builder()
                                .setEnabled(true)
                                .build()
                )
                .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);

        Payment payment = new Payment();
        payment.setUserEmail(email);
        payment.setAmount(amount);
        payment.setCourseId(courseId);
        payment.setPaymentDate(LocalDateTime.now());

        // Save to repository (not yet confirmed payment)
        paymentRepository.save(payment);

        return paymentIntent.getClientSecret(); // Send client secret to the client to complete the payment
    }
}
