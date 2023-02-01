package com.credpay.platform.model;

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
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bill_amount")
    private BigDecimal billAmount;

    @Column(name = "bill_date")
    private LocalDate billDate;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "credit_card_id")
    private Long creditCardId;

    @Column
    private String cardholderName;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "biller_id")
    private Long biller;

    private String description;

    private boolean paid = Boolean.FALSE;


}


