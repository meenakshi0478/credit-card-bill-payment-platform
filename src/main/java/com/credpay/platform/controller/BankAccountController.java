package com.credpay.platform.controller;

import com.credpay.platform.dto.*;
import com.credpay.platform.model.BankAccount;
import com.credpay.platform.repository.BankAccountRepository;
import com.credpay.platform.service.BankAccountService;
import com.credpay.platform.shared.ApiResponse;
import com.credpay.platform.shared.CustomCredPayHttpStatus;
import io.sentry.Sentry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/bank")
public class BankAccountController {

    @Autowired
    BankAccountRepository bankAccountRepository;

    @Autowired
    BankAccountService bankAccountService;

    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == principal.userId")
    @GetMapping("{userId}/get/{id}")
    public ResponseEntity<ApiResponse> getBankById(@PathVariable Long id , @PathVariable String userId) {
        Map data = new HashMap();
        try {
            Optional<BankAccount> bankDetails = bankAccountService.getBankDetailsById(id);
            data.put("data", bankDetails);
            return ResponseEntity.ok(new ApiResponse(data, "success", CustomCredPayHttpStatus.SUCCESS.ordinal()));
        }catch (Exception ex) {
            Sentry.captureException(ex);
            data.put("message", ex.getClass().getName() + ": " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(data, "failed", CustomCredPayHttpStatus.FAILED.ordinal()));
        }

    }

    @PreAuthorize(" #userId == principal.userId ")
    @PostMapping("/add/{userId}/bankDetails")
    public ResponseEntity<ApiResponse> addBankDetails(@PathVariable("userId") String userId,
                                                           @RequestBody BankAccountDto bankAccountDto) {
        Map data = new HashMap();
        try {
            BankAccountDto savedBankAccount = bankAccountService.addBankDetails(userId, bankAccountDto);
            data.put("data", savedBankAccount);
            return ResponseEntity.ok(new ApiResponse(data, "success", CustomCredPayHttpStatus.SUCCESS.ordinal()));
        }catch (Exception ex) {
            Sentry.captureException(ex);
            data.put("message", ex.getClass().getName() + ": " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(data, "failed", CustomCredPayHttpStatus.FAILED.ordinal()));
        }

    }


    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == principal.userId")
    @PutMapping("/{userId}/update/{id}")
    public ResponseEntity<ApiResponse> updateAccount(@PathVariable("userId") String userId,
                                                          @PathVariable("id") Long id,
                                                          @RequestBody UpdateBankAccountDto updateBankAccountDto) {
        Map data = new HashMap();
        try {
            UpdateBankAccountDto accountDetails = bankAccountService.updateBankAccount(userId, id, updateBankAccountDto);
            data.put("data", accountDetails);
            return ResponseEntity.ok(new ApiResponse(data, "success", CustomCredPayHttpStatus.SUCCESS.ordinal()));
        }catch (Exception ex) {
            Sentry.captureException(ex);
            data.put("message", ex.getClass().getName() + ": " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(data, "failed", CustomCredPayHttpStatus.FAILED.ordinal()));
        }
    }


    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == principal.userId")
    @DeleteMapping("/{userId}/delete/{id}")
    public ResponseEntity<String> removeBankAccount(@PathVariable("userId") String userId,
                                                    @PathVariable("id") Long id) {
        bankAccountService.deleteByUserIdAndId( userId, id );
        return ResponseEntity.ok("Bank Account Removed Successfully");
    }

}
