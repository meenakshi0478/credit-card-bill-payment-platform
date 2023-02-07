package com.credpay.platform.service;

import com.credpay.platform.dto.BillDto;
import com.credpay.platform.dto.CreditCardDto;
import com.credpay.platform.exceptions.ErrorMessages;
import com.credpay.platform.exceptions.UserServiceException;
import com.credpay.platform.model.Bill;
import com.credpay.platform.model.CreditCard;
import com.credpay.platform.repository.BillRepository;
import com.credpay.platform.repository.CreditCardRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BillService {

    @Autowired
    private BillRepository billRepository;
    @Autowired
    private CreditCardRepository creditCardRepository;

    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }


    public BillDto createBill(Long creditCardId, BillDto billDto) {
        Bill existingBill = billRepository.findByCreditCardIdAndId(creditCardId, billDto.getId());
        if (existingBill != null) {
            throw new UserServiceException(ErrorMessages.BILL_ALREADY_EXISTS.getErrorMessage());
        }

        Bill bill = new Bill();
        BeanUtils.copyProperties(billDto, bill);
        Optional<CreditCard> creditCard = creditCardRepository.findById(creditCardId);
        bill.setUserId(creditCard.get().getUserId());
        bill.setCreditCardId(creditCardId);
        bill.setCardholderName(creditCard.get().getCardholderName());
        bill.setBillAmount(creditCard.get().getTotalLimit().subtract(creditCard.get().getAvailableCardLimit()));
        Bill createdBill = billRepository.save(bill);
        BillDto returnValue = new BillDto();
        BeanUtils.copyProperties(createdBill, returnValue);
        return returnValue;
    }

    public List<Bill> getAllBillsById( String userId) {
       List<Bill>bills= billRepository.findByCreditCardIdAndUserId(userId);
        return bills;
    }

}

