package com.credpay.platform.controller;

import com.credpay.platform.dto.*;
import com.credpay.platform.model.BankAccountModel;
import com.credpay.platform.repository.BankAccountRepository;
import com.credpay.platform.service.BankAccountService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/bank")
public class BankController {

    private final BankAccountRepository bankAccountRepository;
    private final BankAccountService bankAccountService;

    public BankController(BankAccountRepository bankAccountRepository, BankAccountService bankAccountService) {
        this.bankAccountRepository = bankAccountRepository;
        this.bankAccountService = bankAccountService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == principal.userId")
    @GetMapping("{userId}/get/{id}")
    public Optional<BankAccountModel> getBankById(@PathVariable Long id , @PathVariable String userId) {

        Optional<BankAccountModel> returnValue = bankAccountService.getBankDetailsById(id);;
        return returnValue;

    }


    @PreAuthorize(" #userId == principal.userId")
    @PostMapping("/add/{userId}/bankDetails")
    public ResponseEntity<BankAccountModel> addBankDetails(@PathVariable("userId") String userId,
                                                          @RequestBody BankAccountDto bankAccountDetails) {
        BankAccountDto bankAccountDto = new BankAccountDto();
        BeanUtils.copyProperties(bankAccountDetails, bankAccountDto);

        BankAccountDto savedBankAccount = bankAccountService.addBankDetails(userId, bankAccountDto);
        BankAccountModel returnValue = new BankAccountModel();
        BeanUtils.copyProperties(savedBankAccount, returnValue);
        return ResponseEntity.ok(returnValue);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == principal.userId")
    @PutMapping("/{userId}/update/{id}")
    public ResponseEntity<BankAccountModel> updateCreditCard(@PathVariable("userId") String userId,
                                                                @PathVariable("id") Long id,
                                                                @RequestBody UpdateBankAccountDto updateBankAccountDto) {
        BankAccountModel bankAccountModel = new BankAccountModel();
        BankAccountDto bankAccountDto = new BankAccountDto();
        BeanUtils.copyProperties(updateBankAccountDto, bankAccountDto);
        BankAccountDto updatedBankAccount = bankAccountService.updateBankAccount(userId, id, bankAccountDto);
        BankAccountModel returnvalue = new BankAccountModel();
        BeanUtils.copyProperties(updatedBankAccount, returnvalue);
        return ResponseEntity.ok(returnvalue);

    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == principal.userId")
    @DeleteMapping("/{userId}/delete/{id}")
    public ResponseEntity<String> removeBankAccount(@PathVariable("userId") String userId,
                                                   @PathVariable("id") Long id) {
        bankAccountService.deleteByUserIdAndId( userId, id );
        return ResponseEntity.ok("Bank Removed Successfully");
    }

}
