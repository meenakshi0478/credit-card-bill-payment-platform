package com.credpay.platform.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_amount")
    private double transactionAmount;

    @Column(name = "transaction_date")
    private LocalDate transactionDate;

    private String transactionType;

    @ManyToOne
    @JoinColumn(name = "credit_card_id")
    private CreditCard creditCard;

    @ManyToOne
    @JoinColumn(name = "bill_id")
    private Bill bill;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    private String status;
    // getters and setters
}


