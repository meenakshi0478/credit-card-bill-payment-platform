package com.credpay.platform.controller;

import com.credpay.platform.dto.BillDto;
import com.credpay.platform.dto.PaymentDto;
import com.credpay.platform.model.*;
import com.credpay.platform.repository.CreditCardRepository;
import com.credpay.platform.repository.TransactionRepository;
import com.credpay.platform.repository.UserRepository;
import com.credpay.platform.service.BillService;
import com.credpay.platform.service.EmailSenderService;
import com.credpay.platform.service.PDFGeneratorService;
import com.credpay.platform.service.PaymentService;
import com.credpay.platform.shared.ApiResponse;
import com.credpay.platform.shared.CustomCredPayHttpStatus;
import com.itextpdf.text.DocumentException;
import io.sentry.Sentry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    PDFGeneratorService pdfGeneratorService;

    @Autowired
    private TransactionRepository transactionRepository;


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{userId}/all")
    public ResponseEntity<List<Bill>> getBillsByUserId(@PathVariable String userId) {
        List<Bill> bills = billService.getAllBillsById(userId);
        return new ResponseEntity<>(bills, HttpStatus.OK);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/generate/{creditCardId}")
    public ResponseEntity<ApiResponse> generateBill(@PathVariable Long creditCardId,
                                                    @RequestBody BillDto billDto,HttpServletResponse response) throws IOException, MessagingException, DocumentException {
        Map data = new HashMap();
        try {
        BillDto createdBill = billService.createBill(creditCardId);
        CreditCard creditCard = creditCardRepository.findByUserIdAndId(createdBill.getUserId(),creditCardId);
        List<Transaction> transactions = transactionRepository.findAllByCreditcardId(creditCardId);
        User user = userRepository.findByUserId(createdBill.getUserId());
        emailSenderService.sendPdfTableViaEmail(creditCard,createdBill);
        data.put("data", createdBill);
        return ResponseEntity.ok(new ApiResponse(data, "success", CustomCredPayHttpStatus.SUCCESS.ordinal()));
        }catch (Exception ex) {
            Sentry.captureException(ex);
            data.put("message", ex.getClass().getName() + ": " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(data, "failed", CustomCredPayHttpStatus.FAILED.ordinal()));
        }
    }


    @PreAuthorize("#userId == principal.userId")
    @PostMapping("/pay/{id}/{userId}")
    public ResponseEntity<ApiResponse> payBill(@PathVariable Long id, @PathVariable String userId,
                                              @RequestBody PaymentDto paymentDto) throws MessagingException {
        Map data = new HashMap();
        try {
            PaymentDto processedPayment = paymentService.processPayment(id, userId, paymentDto);
            data.put("data", processedPayment);
            return ResponseEntity.ok(new ApiResponse(data, "success", CustomCredPayHttpStatus.SUCCESS.ordinal()));
        }catch (Exception ex) {
            Sentry.captureException(ex);
            data.put("message", ex.getClass().getName() + ": " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(data, "failed", CustomCredPayHttpStatus.FAILED.ordinal()));
        }
    }


}

