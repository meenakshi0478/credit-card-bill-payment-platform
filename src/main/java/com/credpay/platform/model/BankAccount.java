package com.credpay.platform.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Long accountNo;

    @Column(nullable = false, length = 25)
    private String accountHolderName;

    @Column(nullable = false, length = 25)
    private String ifsc;

    @Column(nullable = false, length = 25)
    private BigDecimal accountBalance;

    @Column(unique = true)
    private String userId;
}

