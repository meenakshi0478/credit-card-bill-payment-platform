package com.credpay.platform.service;

import com.credpay.platform.dto.BankAccountDto;
import com.credpay.platform.dto.UpdateBankAccountDto;
import com.credpay.platform.exceptions.ErrorMessages;
import com.credpay.platform.exceptions.UserServiceException;
import com.credpay.platform.model.BankAccount;
import com.credpay.platform.repository.BankAccountRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BankAccountService {

    @Autowired
    private BankAccountRepository bankAccountRepository;
    public BankAccountDto addBankDetails(String userId, BankAccountDto bankAccountDto)  {

        if (bankAccountRepository.findByAccountNo(bankAccountDto.getAccountNo())!=null){
            throw new UserServiceException(ErrorMessages.RECORD_ALREADY_EXISTS.getErrorMessage());
        }

        BankAccount bankAccount = new BankAccount();
        BeanUtils.copyProperties(bankAccountDto, bankAccount);
        bankAccount.setUserId(userId);
        BankAccount storedBankDetails = bankAccountRepository.save(bankAccount);
        BankAccountDto returnValue = new BankAccountDto();
        BeanUtils.copyProperties(storedBankDetails, returnValue);
        return returnValue;
    }

    public UpdateBankAccountDto updateBankAccount(String userId, Long id, UpdateBankAccountDto bankAccountDto) {
        UpdateBankAccountDto returnValue = new UpdateBankAccountDto();
        BankAccount bankAccount = bankAccountRepository.findByUserIdAndId(userId, id);
        if (bankAccount == null) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        bankAccount.setAccountBalance(bankAccountDto.getAccountBalance());
        bankAccount.setAccountHolderName(bankAccountDto.getAccountHolderName());
        bankAccount.setIfsc(bankAccountDto.getIfsc());
        BankAccount updatedBankDetails = bankAccountRepository.save(bankAccount);
        BeanUtils.copyProperties(updatedBankDetails, returnValue);
        return returnValue;
    }


    public void deleteByUserIdAndId(String userId, Long id) {
        BankAccount bankAccount = bankAccountRepository.findByUserIdAndId(userId, id);
        if (bankAccount == null) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        bankAccountRepository.delete(bankAccount);
    }

    public Optional<BankAccount> getBankDetailsById(Long id) {
        Optional<BankAccount> bankAccountModel= bankAccountRepository.findById(id);
        if(bankAccountModel.isEmpty()) throw new RuntimeException("Bank Details Not Found");
        return bankAccountModel;
    }
}
