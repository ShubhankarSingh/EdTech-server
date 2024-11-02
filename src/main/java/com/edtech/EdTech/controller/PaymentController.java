package com.edtech.EdTech.controller;

import com.edtech.EdTech.dto.PaymentDto;
import com.edtech.EdTech.model.Payment;
import com.edtech.EdTech.service.PaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment/")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/payment-intent")
    public ResponseEntity<String> createPaymentIntent(@RequestBody PaymentDto paymentRequest) throws StripeException {

        try{
            String clientSecret =  paymentService.createPaymentIntent(paymentRequest.getCourseId(), paymentRequest.getUserEmail(),
                    paymentRequest.getAmount());
            return ResponseEntity.ok(clientSecret);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating payment intent: " + e.getMessage());
        }
    }
}
