package com.credpay.platform.repository;

import com.credpay.platform.model.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {

    CreditCard findByUserIdAndId(String userId, Long creditCardId);

    @Query(nativeQuery = true, value ="Select * from credit_card cc where cc.is_active ='false'")
    List<CreditCard> findAllActiveCreditCardList();

    @Query(nativeQuery = true, value ="Select user_id from credit_card cc where cc.id =:creditCardId")
    String findByUserByCardId(Long creditCardId);

    @Query(nativeQuery = true, value ="Select cardholder_name from credit_card cc where cc.id =:creditCardId")
    String findNameByCardId(Long creditCardId);



//    boolean findByCreditCardNum(Long creditCardId);
}
