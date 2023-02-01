package com.credpay.platform.repository;

import com.credpay.platform.dto.BillDto;
import com.credpay.platform.model.Bill;
import com.credpay.platform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BillRepository extends JpaRepository<Bill,Long> {
    Bill findByCreditCardIdAndId(Long creditCardId, Long id);

    List<Bill> findAll();

    Bill findByUserIdAndId(String userId, Long billId);
}
