package com.edtech.EdTech.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class PaymentDto {

    private Long courseId;
    private String userEmail;
    private double amount;
}
