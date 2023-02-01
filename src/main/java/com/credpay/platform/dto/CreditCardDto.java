package com.credpay.platform.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CreditCardDto {
    private Long id;
    private String userId;
    private Long cardNumber;
    private String cardholderName;
    private Long cvv;
    private LocalDate expirationDate;
    private BigDecimal totalLimit;
    private BigDecimal availableCardLimit;
    private BigDecimal currentDebt;
    private BigDecimal minimumPaymentAmount;
    private LocalDate cutoffDate;
    private LocalDate dueDate;
    private LocalDate cancelDate;
}
