package com.credpay.platform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountDto {
    private Long id;
    private Long accountNo;
    private String accountHolderName;
    private String ifsc;
    private BigDecimal accountBalance;
    private String userId;

}
