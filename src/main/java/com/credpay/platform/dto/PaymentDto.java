package com.credpay.platform.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {
    private BigDecimal paymentAmount;
    private LocalDate paymentDate;
    private Long creditCardId;
    private Long billId;
    private String paymentMethod;
    private String paymentReference;
    private String status;
}
