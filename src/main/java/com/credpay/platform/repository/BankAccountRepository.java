package com.credpay.platform.repository;

import com.credpay.platform.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository  extends JpaRepository<BankAccount,Long> {
    BankAccount findByUserIdAndId(String userId, Long id);

    BankAccount findByAccountNo(Long accountNo);

    BankAccount findByUserId(String userId);

}
