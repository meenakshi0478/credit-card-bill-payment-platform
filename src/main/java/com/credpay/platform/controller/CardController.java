package com.credpay.platform.controller;

import com.credpay.platform.dto.CreditCardDto;
import com.credpay.platform.dto.UpdateCardDto;
import com.credpay.platform.model.CreditCard;
import com.credpay.platform.model.payload.Request.CreditCardDetailsRequestModel;
import com.credpay.platform.repository.CreditCardRepository;
import com.credpay.platform.service.CreditCardService;
import com.credpay.platform.shared.ApiResponse;
import com.credpay.platform.shared.CustomCredPayHttpStatus;
import io.sentry.Sentry;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/card")

public class CardController {
    private final CreditCardRepository creditCardRepository;
    private final CreditCardService creditCardService;

    public CardController(CreditCardRepository creditCardRepository, CreditCardService creditCardService) {
        this.creditCardRepository = creditCardRepository;
        this.creditCardService = creditCardService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/get/all")
    public ResponseEntity<List<CreditCard>> findAllActiveCreditCards(){
        List<CreditCard> creditCards = creditCardService.findAllCreditCards();
        return ResponseEntity.ok(creditCards);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == principal.userId")
    @PostMapping("/add/{userId}/creditcard")
    public ResponseEntity<ApiResponse> addCreditCard(@PathVariable("userId") String userId,
                                                     @RequestBody CreditCardDetailsRequestModel creditCardDetails) {
        Map data = new HashMap();
        try {
            CreditCardDto creditCardDto = new CreditCardDto();
            BeanUtils.copyProperties(creditCardDetails, creditCardDto);
            CreditCardDto savedCreditCard = creditCardService.addCreditCard(userId, creditCardDto);
            data.put("data", savedCreditCard);
            return ResponseEntity.ok(new ApiResponse(data, "success", CustomCredPayHttpStatus.SUCCESS.ordinal()));
        }catch (Exception ex) {
            Sentry.captureException(ex);
            data.put("message", ex.getClass().getName() + ": " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(data, "failed", CustomCredPayHttpStatus.FAILED.ordinal()));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == principal.userId")
    @PutMapping("/{userId}/update/{id}")
    public ResponseEntity<CreditCardDto> updateCreditCard(@PathVariable("userId") String userId,
                                                                @PathVariable("id") Long id,
                                                                @RequestBody UpdateCardDto updateCardDto) {
        Map data = new HashMap();
        try {
        CreditCardDto creditCardDto = new CreditCardDto();
        BeanUtils.copyProperties(updateCardDto, creditCardDto);
        CreditCardDto updatedCreditCard = creditCardService.updateCreditCard(userId, id, creditCardDto);
        data.put("data", updatedCreditCard);
            return ResponseEntity.status(HttpStatus.OK).body(updatedCreditCard);
        }catch (Exception ex) {
            Sentry.captureException(ex);
            data.put("message", ex.getClass().getName() + ": " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == principal.userId")
    @DeleteMapping("/{userId}/delete/{id}")
    public ResponseEntity<String> removeCreditCard(@PathVariable("userId") String userId,
                                                   @PathVariable("id") Long id) {
        creditCardService.deleteUserIdAndId( userId, id );
        return ResponseEntity.ok("Card Removed Successfully");
    }

}
