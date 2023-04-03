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
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_amount")
    private BigDecimal paymentAmount;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "credit_card_id")
    private Long creditCardId;

    @Column(name = "bill_id")
    private Long billId;

    private String paymentMethod;
    private String paymentReference;
    private String status = "Due";

}


