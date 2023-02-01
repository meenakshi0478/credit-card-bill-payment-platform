package com.credpay.platform.service;

import com.credpay.platform.dto.BankAccountDto;
import com.credpay.platform.exceptions.ErrorMessages;
import com.credpay.platform.exceptions.UserServiceException;
import com.credpay.platform.model.BankAccountModel;
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

        BankAccountModel bankAccountModel = new BankAccountModel();
        BeanUtils.copyProperties(bankAccountDto, bankAccountModel );
        bankAccountModel.setUserId(userId);
        BankAccountModel storedBankDetails = bankAccountRepository.save(bankAccountModel);
        BankAccountDto returnValue = new BankAccountDto();
        BeanUtils.copyProperties(storedBankDetails, returnValue);
        return returnValue;
    }

    public BankAccountDto updateBankAccount(String userId, Long id, BankAccountDto bankAccountDto) {
        BankAccountDto returnValue = new BankAccountDto();
        BankAccountModel bankAccount = bankAccountRepository.findByUserIdAndId(userId, id);
        if (bankAccount == null) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        bankAccount.setAccountBalance(bankAccountDto.getAccountBalance());
        bankAccount.setAccountNo(bankAccountDto.getAccountNo());
        bankAccount.setAccountHolderName(bankAccountDto.getAccountHolderName());
        bankAccount.setIfsc(bankAccountDto.getIfsc());
        bankAccount.setAccountBalance(bankAccountDto.getAccountBalance());
        BankAccountModel updatedBankDetails = bankAccountRepository.save(bankAccount);
        BeanUtils.copyProperties(updatedBankDetails, returnValue);
        return returnValue;
    }


    public void deleteByUserIdAndId(String userId, Long id) {
        BankAccountModel bankAccountModel = bankAccountRepository.findByUserIdAndId(userId, id);
        if (bankAccountModel == null) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        bankAccountRepository.delete(bankAccountModel);
    }

    public Optional<BankAccountModel> getBankDetailsById(Long id) {
        Optional<BankAccountModel> bankAccountModel= bankAccountRepository.findById(id);
        if(bankAccountModel== null) throw new RuntimeException("Bank Details Not Found");
        return bankAccountModel;
    }
}
