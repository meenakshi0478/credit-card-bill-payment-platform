package com.credpay.platform.controller;

import com.credpay.platform.dto.BillDto;
import com.credpay.platform.dto.PaymentDto;
import com.credpay.platform.model.Bill;
import com.credpay.platform.model.CreditCard;
import com.credpay.platform.model.Payment;
import com.credpay.platform.model.User;
import com.credpay.platform.model.payload.BillDetailsRequestModel;
import com.credpay.platform.repository.CreditCardRepository;
import com.credpay.platform.repository.UserRepository;
import com.credpay.platform.service.BillService;
import com.credpay.platform.service.EmailSenderService;
import com.credpay.platform.service.PaymentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    @Autowired
    private BillService billService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CreditCardRepository creditCardRepository;
    @Autowired
    EmailSenderService emailSenderService;


    @GetMapping
    public ResponseEntity<List<Bill>> getBills() {
        List<Bill> bills = billService.getAllBills();
        return new ResponseEntity<>(bills, HttpStatus.OK);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/generate/{creditCardId}")
    public ResponseEntity<BillDto> generateBill(@PathVariable Long creditCardId,
                                                @RequestBody BillDetailsRequestModel billDetails) {
        BillDto billDto = new BillDto();
        BeanUtils.copyProperties(billDetails, billDto);

        BillDto createdBill = billService.createBill(creditCardId, billDto);
        BillDto returnValue = new BillDto();

        CreditCard creditCard = creditCardRepository.findByUserIdAndId(createdBill.getUserId(),creditCardId);

        User user = userRepository.findByUserId(createdBill.getUserId());
        emailSenderService.sendSimpleEmail(user.getEmail()," CreditCard Bill",
                "Hi " + user.getFirstName() + " The CreditCard Bill of your CredirCard " + creditCard.getCardNumber()+
                        " Is Rs "+createdBill.getBillAmount()+
                        " Please close the due before  "+ createdBill.getDueDate()+
                        " Regards CredPay Team!" );
        BeanUtils.copyProperties(createdBill, returnValue);
        return ResponseEntity.ok(returnValue);
    }


    @PreAuthorize("#userId == principal.userId")
    @PostMapping("/pay/{id}/{userId}")
    public ResponseEntity<PaymentDto> payBill(@PathVariable Long id, @PathVariable String userId,
                                              @RequestBody PaymentDto paymentDto) {
        PaymentDto processedPayment = paymentService.processPayment(id, userId,paymentDto);
        return new ResponseEntity<>(processedPayment, HttpStatus.CREATED);
    }
}

