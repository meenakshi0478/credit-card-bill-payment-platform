package com.credpay.platform.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CreditCard extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false, unique = true)
    private Long cardNumber;

    @Column(nullable = false)
    private String cardholderName;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(nullable = false)
    private Long cvv;

    @Column(nullable = false)
    private BigDecimal totalLimit;

    @Column(nullable = false)
    private BigDecimal availableCardLimit;

    @Column
    private BigDecimal currentDebt;

    @Column(nullable = false)
    private boolean isActive = true;


}