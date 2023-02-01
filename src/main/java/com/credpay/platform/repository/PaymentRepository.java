package com.credpay.platform.repository;

import com.credpay.platform.model.Bill;
import com.credpay.platform.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository  extends JpaRepository<Payment,Long> {
}
