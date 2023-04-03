package com.credpay.platform.service;

import com.credpay.platform.dto.BillDto;
import com.credpay.platform.model.Bill;
import com.credpay.platform.model.CreditCard;
import com.credpay.platform.model.Transaction;
import com.credpay.platform.repository.BillRepository;
import com.credpay.platform.repository.CreditCardRepository;
import com.credpay.platform.repository.TransactionRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;



@Service
public class BillService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private CreditCardRepository creditCardRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }


    public BillDto createBill(Long creditCardId) {
        List<Transaction>transactions = transactionRepository.findAllByCreditcardId(creditCardId);

        Bill bill = new Bill();
        int expenses = transactions.stream()
                .mapToInt(obj -> (int) obj.getAmount())
                .sum();
        expenses=Math.abs(expenses);
        bill.setBillAmount(BigDecimal.valueOf(expenses));
        CreditCard creditCard = creditCardRepository.findById(creditCardId).get();
        BigDecimal debt = creditCard.getCurrentDebt();
        creditCard.setAvailableCardLimit(creditCard.getTotalLimit().subtract(BigDecimal.valueOf(expenses)));
        creditCard.setCurrentDebt(debt.add(BigDecimal.valueOf(expenses)));
        creditCardRepository.save(creditCard);
        bill.setUserId(creditCard.getUserId());
        bill.setCreditCardId(creditCardId);
        bill.setCardholderName(creditCard.getCardholderName());
        bill.setBillDate(LocalDate.now());
        bill.setDueDate(LocalDate.now().plusDays(30));
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

