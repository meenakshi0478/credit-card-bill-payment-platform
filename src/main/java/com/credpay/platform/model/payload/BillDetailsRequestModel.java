package com.credpay.platform.model.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillDetailsRequestModel {
    private Long id;

    private BigDecimal billAmount;

    private LocalDate billDate;

    private LocalDate dueDate;

    private String userId;

    private String CardholderName;

    private Long creditCardId;

    private String description;

    private boolean paid;
}
