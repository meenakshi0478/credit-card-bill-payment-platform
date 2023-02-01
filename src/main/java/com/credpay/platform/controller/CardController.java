package com.credpay.platform.controller;

import com.credpay.platform.dto.CreditCardDto;
import com.credpay.platform.dto.UpdateCardDto;
import com.credpay.platform.dto.UserDto;
import com.credpay.platform.model.CreditCard;
import com.credpay.platform.model.payload.CreditCardDetailsRequest;
import com.credpay.platform.model.payload.CreditCardRestModel;
import com.credpay.platform.model.payload.UserRestModel;
import com.credpay.platform.repository.CreditCardRepository;
import com.credpay.platform.service.CreditCardService;
import com.lowagie.text.DocumentException;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
    public ResponseEntity<CreditCardRestModel> addCreditCard(@PathVariable("userId") String userId,
                                                             @RequestBody CreditCardDetailsRequest creditCardDetails) {
        CreditCardDto creditCardDto = new CreditCardDto();
        BeanUtils.copyProperties(creditCardDetails, creditCardDto);

        CreditCardDto savedCreditCard = creditCardService.addCreditCard(userId, creditCardDto);
        CreditCardRestModel returnvalue = new CreditCardRestModel();
        BeanUtils.copyProperties(savedCreditCard, returnvalue);
        return ResponseEntity.ok(returnvalue);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == principal.userId")
    @PutMapping("/{userId}/update/{id}")
    public ResponseEntity<CreditCardRestModel> updateCreditCard(@PathVariable("userId") String userId,
                                                       @PathVariable("id") Long id,
                                                       @RequestBody UpdateCardDto updateCardDto) {
        CreditCardRestModel creditCardRestModel = new CreditCardRestModel();
        CreditCardDto creditCardDto = new CreditCardDto();
        BeanUtils.copyProperties(updateCardDto, creditCardDto);
        CreditCardDto updatedCreditCard = creditCardService.updateCreditCard(userId, id, creditCardDto);
        CreditCardRestModel returnvalue = new CreditCardRestModel();
        BeanUtils.copyProperties(updatedCreditCard, returnvalue);
        return ResponseEntity.ok(returnvalue);

    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == principal.userId")
    @DeleteMapping("/{userId}/delete/{id}")
    public ResponseEntity<String> removeCreditCard(@PathVariable("userId") String userId,
                                   @PathVariable("id") Long id) {
        creditCardService.deleteUserIdAndId( userId, id );
        return ResponseEntity.ok("Card Removed Successfully");
    }

}
