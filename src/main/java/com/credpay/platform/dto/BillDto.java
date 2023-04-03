package com.credpay.platform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillDto {
    private Long id;
    private BigDecimal billAmount;
    private LocalDate billDate;
    private LocalDate dueDate;
    private String userId;
    private String CardholderName;
    private Long creditCardId;
    private boolean paid;


}
