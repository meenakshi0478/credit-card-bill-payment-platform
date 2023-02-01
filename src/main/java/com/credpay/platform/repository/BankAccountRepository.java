package com.credpay.platform.repository;

import com.credpay.platform.dto.BankAccountDto;
import com.credpay.platform.model.BankAccountModel;
import com.credpay.platform.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository  extends JpaRepository<BankAccountModel,Long> {
    BankAccountModel findByUserIdAndId(String userId, Long id);

    BankAccountModel findByAccountNo(Long accountNo);

    BankAccountModel findByUserId(String userId);

}
