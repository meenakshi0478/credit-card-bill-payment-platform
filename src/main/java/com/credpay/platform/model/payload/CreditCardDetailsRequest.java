package com.credpay.platform.model.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditCardDetailsRequest {
    private Long id;
    private String userId;
    private Long cardNumber;
    private String cardholderName;
    private Long cvv;
    private LocalDate expirationDate;
    private BigDecimal totalLimit;
    private BigDecimal availableCardLimit;

}