package com.credpay.platform.model.payload.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditCardDetailsRequestModel {
    private Long id;
    private String userId;
    private Long cardNumber;
    private String cardholderName;
    private Long cvv;
    private LocalDate expirationDate;
    private BigDecimal totalLimit;
    private BigDecimal availableCardLimit;

}