package com.credpay.platform.service;

import com.credpay.platform.dto.BankAccountDto;
import com.credpay.platform.dto.BillDto;
import com.credpay.platform.dto.PaymentDto;
import com.credpay.platform.exceptions.ErrorMessages;
import com.credpay.platform.model.*;
import com.credpay.platform.repository.*;
import com.credpay.platform.shared.Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    private Utils utils;

    public PaymentDto processPayment(Long id, String userId,PaymentDto paymentDto) {
        PaymentDto returnValue = new PaymentDto();
        Payment payment = new Payment();
        BeanUtils.copyProperties(paymentDto,payment);
        Bill bill = billRepository.findByUserIdAndId(userId,id);
        CreditCard creditCard = creditCardRepository.findByUserIdAndId(userId,paymentDto.getCreditCardId());
        String publicReferenceId = utils.generateUserId(30);
        BankAccountModel bankAccountModel = bankAccountRepository.findByUserId(userId);

        if (bill.isPaid()==false && bill.getBillAmount().compareTo(bankAccountModel.getAccountBalance())==-1 ) {
            payment.setPaymentAmount(bill.getBillAmount());
            payment.setPaymentReference(publicReferenceId);
            payment.setBillId(id);
            payment.setStatus("Paid");
            Payment updatedPaymentDetails = paymentRepository.save(payment);
            bill.setPaid(Boolean.TRUE);
            Bill updateBill = billRepository.save(bill);
            creditCard.setAvailableCardLimit(creditCard.getTotalLimit());
            creditCard.setCurrentDebt(BigDecimal.valueOf(0));
            CreditCard updateCrediCard = creditCardRepository.save(creditCard);
            bankAccountModel.setAccountBalance(bankAccountModel.getAccountBalance().subtract(bill.getBillAmount()));
            BankAccountModel updateBankBalance = bankAccountRepository.save(bankAccountModel);

            User user = userRepository.findByUserId(userId);
            emailSenderService.sendSimpleEmail(user.getEmail()," CreditCard Due Payment Completed ",
                    "Hi " + user.getFirstName() + " You have successfully closed your due amount Rs "+ bill.getBillAmount()+" of your CredirCard" + creditCard.getCardNumber()+
                            " Regards CredPay Team!");
            BeanUtils.copyProperties(updatedPaymentDetails, returnValue);
            return returnValue;
        }
        else throw new RuntimeException("Could not Process Payment .....");
    }

}

