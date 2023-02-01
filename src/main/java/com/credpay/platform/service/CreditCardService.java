package com.credpay.platform.service;

import com.credpay.platform.dto.CreditCardDto;
import com.credpay.platform.exceptions.ErrorMessages;
import com.credpay.platform.exceptions.UserServiceException;
import com.credpay.platform.model.CreditCard;
import com.credpay.platform.model.User;
import com.credpay.platform.repository.CreditCardRepository;
import com.credpay.platform.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreditCardService {

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    EmailSenderService emailSenderService;

    public CreditCardDto addCreditCard(String userId, CreditCardDto creditCardDto) {
        if (creditCardDto.getCardNumber() == null) {
            throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        }
        CreditCard existingCreditCard = creditCardRepository.findByUserIdAndId(userId, creditCardDto.getCardNumber());
        if (existingCreditCard != null) {
            throw new UserServiceException(ErrorMessages.CARD_ALREADY_EXISTS.getErrorMessage());
        }

        CreditCard creditCard = new CreditCard();
        BeanUtils.copyProperties(creditCardDto, creditCard);
        creditCard.setUserId(userId);
        creditCard.setCurrentDebt(creditCardDto.getTotalLimit().subtract(creditCardDto.getAvailableCardLimit()));
        CreditCard storedCreditCard = creditCardRepository.save(creditCard);
        User user = userRepository.findByUserId(userId);
        emailSenderService.sendSimpleEmail(user.getEmail()," CreditCard Added Successfully",
                "Hi " + user.getFirstName() + " You have successfully added your CreditCard with Number " + creditCard.getCardNumber()+
                        " To your CredPay Account " +
                        " Reagards CredPay Team!");
        CreditCardDto returnValue = new CreditCardDto();
        BeanUtils.copyProperties(storedCreditCard, returnValue);
        return returnValue;
    }

    public CreditCardDto updateCreditCard(String userId, Long id, CreditCardDto creditCardDto) {
        CreditCardDto returnValue = new CreditCardDto();
        CreditCard creditCard = creditCardRepository.findByUserIdAndId(userId, id);
        if (creditCard == null) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        creditCard.setCardNumber(creditCardDto.getCardNumber());
        creditCard.setCardholderName(creditCardDto.getCardholderName());
        creditCard.setCvv(creditCardDto.getCvv());
        creditCard.setExpirationDate(creditCardDto.getExpirationDate());
        creditCard.setTotalLimit(creditCardDto.getTotalLimit());
        creditCard.setAvailableCardLimit(creditCardDto.getAvailableCardLimit());
        CreditCard updatedCardDetails = creditCardRepository.save(creditCard);
        BeanUtils.copyProperties(updatedCardDetails, returnValue);
        return returnValue;


    }

    public void removeCreditCard(String userId, Long creditCardId) {
        CreditCard creditCard = creditCardRepository.findByUserIdAndId(userId, creditCardId);
        if (creditCard == null) { throw new RuntimeException("Credit card not found"); }
        creditCardRepository.delete(creditCard);
    }

    public List<CreditCard> findAllCreditCards() {
        List<CreditCard> creditCardList = creditCardRepository.findAllActiveCreditCardList();
        return creditCardList;
    }

    public void deleteUserIdAndId(String userId, Long id) {
        CreditCard creditCard = creditCardRepository.findByUserIdAndId(userId, id);
        if (creditCard == null) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        creditCardRepository.delete(creditCard);
        User user = userRepository.findByUserId(userId);
        emailSenderService.sendSimpleEmail(user.getEmail()," CreditCard Deactivated Successfully ",
                "Hi " + user.getFirstName() + " You have successfully added your CreditCard with Number " + creditCard.getCardNumber()+
                        " To your CredPay Account " +
                        " Reagards CredPay Team!");
    }
}

