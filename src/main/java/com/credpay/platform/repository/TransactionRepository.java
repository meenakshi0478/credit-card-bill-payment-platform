package com.credpay.platform.repository;

import com.credpay.platform.model.Bill;
import com.credpay.platform.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query(nativeQuery = true, value ="Select * from transaction t where t.credit_card_id =:creditCardId")
    List<Transaction>findAllByCreditcardId(Long creditCardId);


    @Query(nativeQuery = true, value ="Select * from transaction t where t.credit_card_id =:creditCardId and t.user_id =:userId ")
    List<Transaction> findAllByCreditcardIdAndUserId(Long creditCardId, String userId);
}