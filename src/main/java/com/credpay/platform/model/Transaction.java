package com.credpay.platform.model;

import com.lowagie.text.pdf.PdfPCell;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount")
    private double amount;

    @Column
    private String userId;

    @Column(name = "date")
    private LocalDate date;

    private String description;

    @ManyToOne
    @JoinColumn(name = "credit_card_id",referencedColumnName = "id", nullable = false)
    private CreditCard creditCardId;
}