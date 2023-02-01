package com.credpay.platform.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;


import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class CreditCard {
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

    @Column
    private Long cvv;

    private String cardType;

    private String cardIssuer;

    @Column(nullable = false)
    private BigDecimal totalLimit;

    @Column
    private LocalDate dueDate;

    @Column(nullable = false)
    private BigDecimal availableCardLimit;


    @Column
    private BigDecimal currentDebt;

    @Column(nullable = false)
    private boolean isActive = true;



    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isActive() {return isActive;}

    public void setActive(boolean active) {isActive = active;}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(Long cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardholderName() {
        return cardholderName;
    }

    public void setCardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Long getCvv() {
        return cvv;
    }

    public void setCvv(Long cvv) {
        this.cvv = cvv;
    }


    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardIssuer() {
        return cardIssuer;
    }

    public void setCardIssuer(String cardIssuer) {
        this.cardIssuer = cardIssuer;
    }


    public BigDecimal getTotalLimit() {
        return totalLimit;
    }

    public void setTotalLimit(BigDecimal totalLimit) {
        this.totalLimit = totalLimit;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public BigDecimal getAvailableCardLimit() {
        return availableCardLimit;
    }

    public void setAvailableCardLimit(BigDecimal availableCardLimit) {
        this.availableCardLimit = availableCardLimit;
    }

    public BigDecimal getCurrentDebt() {
        return currentDebt;
    }

    public void setCurrentDebt(BigDecimal currentDebt) {
        this.currentDebt = currentDebt;
    }
}
