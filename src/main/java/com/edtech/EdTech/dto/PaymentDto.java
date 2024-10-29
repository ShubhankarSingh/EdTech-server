package com.edtech.EdTech.dto;

import lombok.Data;

@Data
public class PaymentDto {

    private Long courseId;
    private String userEmail;
    private double amount;
}
